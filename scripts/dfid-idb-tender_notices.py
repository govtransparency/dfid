import requests
import json
import csv

def donorFinancings(financings):
  totalSum = None
  for financing in financings:
    if totalSum is None:
      totalSum = financing.get("netAmount")
    else: 
      totalSum += financing.get("netAmount")
    return totalSum
    
def processFinancingSummaries(projectId, summaries):
  for summary in summaries:
    summaryRow = []
    summaryDisbursed = summary.get("disbursed").get("netAmount") if summary.get("disbursed") is not None else None
    summaryRepaymens = summary.get("repayments").get("netAmount") if summary.get("repayments") is not None else None
    
    summaryRow.append(projectId)
    summaryRow.append(summaryDisbursed)
    summaryRow.append(summaryRepaymens)
    
    financingSummaries.append(summaryRow)

rows=[]
financingSummaries=[]
page = 0
header=["Project ID","Project approval date","Project borrower financing","Project country","Project description",
      "Project donor financing","Project final cost","Project signature dates","Major sectors","Sectors",
      "Tender project name","Tender project country","Tender project operations","Tender title","Tender donor financing",
      "Bidder name","Bidder city","Bidder country","Bidder address", 
      "Buyer assigned ID","Description","Final price","Fiscal year","Addres of implementation","Supply type","Contract signature date",
      "Estimated completion date","Project operations","Tender bid deadline","Tender procedure type","Tender procurement type",
      "Buyer name","Buyer city","Buyer country","Buyer address","Publication source id","Publication date",
      "Publication source form type","Publication form type"]
rows.append(header)
financingSummaryHeader=["Project ID","Disbursed","Repayments"]
financingSummaries.append(financingSummaryHeader)

url = "https://x.x.x.x:5567/login"
payload = {'username': 'xxx', 'password': 'yyy'}
response = requests.post(url, data=payload, verify=False)
result = json.loads(response.content)
token = result.get("auth_token")

while True:
  url = "https://x.x.x.x:5567/protected/clean_tender/timestamp/2016-01-01T00:00:00.000/source/eu.dfid.worker.idb.clean.IDBProcurementNoticesCleaner/page/%s?auth_token=%s" % (page, token)
  
  response = requests.get(url)
  
  # For successful API call, response code will be 200 (OK)
  if(response.ok):
    result_set = json.loads(response.content)
    for data in result_set:
      row=[]
      
      if data.get("financingSummary") is not None: 
        processFinancingSummaries(data.get("projectId"), data.get("financingSummary"))

      projectApprovalDate = data.get("approvalDate") if data.get("approvalDate") is not None else None
      projectBorrowerFinancing = addressOfImplementation = data.get("borrowerFinancing").get("netAmount") if data.get("borrowerFinancing") is not None else None
      projectCountry = data.get("country") if data.get("country") is not None else None
      projectDescription = data.get("description") if data.get("description") is not None else None
      projectDonorFinancing = donorFinancings(data.get("donorFinancings")) if data.get("donorFinancings") is not None else None
      projectFinalCost = data.get("finalCost").get("netAmount") if data.get("finalCost") is not None else None
      buyerAssignedId = data.get("buyerAssignedId") if data.get("buyerAssignedId") is not None else None
      description = data.get("description") if data.get("description") is not None else None
      finalPrice = data.get("finalPrice").get("netAmount") if data.get("finalPrice") is not None else None
      fiscalYear = data.get("fiscalYear") if data.get("fiscalYear") is not None else None
      addressOfImplementation = data.get("addressOfImplementation").get("country") if data.get("addressOfImplementation") is not None else None
      projectId = data.get("projectId") if data.get("projectId") is not None else None
      projectName = data.get("name") if data.get("name") is not None else None
      projectOperations = data.get("operations")
      poColumnValue = None
      if (projectOperations is not None):
        for operation in projectOperations:
          if poColumnValue is None:
            poColumnValue = operation.get("operationNumber")
          else:
            poColumnValue = poColumnValue + ";" + operation.get("operationNumber")
  
      tenderBidDeadline = data.get("bidDeadline") if data.get("bidDeadline") is not None else None
      buyers = data.get("buyers") if data.get("buyers") is not None else None
      buyerName = None
      buyerCity = None
      buyerCountry = None
      buyerAddress = None
  
      if (buyers):
        buyer = buyers[0]
        buyerName = buyer.get("name")
        if (buyer.get("address")):
          buyerCountry = buyer.get("address").get("city")
          buyerCountry = buyer.get("address").get("country")
          buyerAddress = buyer.get("address").get("rawAddress")
      
      bidderName = None
      bidderCity = None
      bidderCountry = None
      bidderAddress = None
       
      lots = data.get("lots")
      if lots is not None and len(lots) > 0:
        bids = lots[0].get("bids")
        if bids is not None and len(bids) > 0:
          bidders = bids[0].get("bidders")
          if bidders is not None and len(bidders) > 0:
            bidderName = bidders[0].get("name")
            if bidders[0].get("address"):
              bidderCity = bidders[0].get("address").get("city")
              bidderCountry = bidders[0].get("address").get("country")
              bidderAddress = bidders[0].get("address").get("rawAddress")
  
      tenderDonorFinancing = data.get("donorFinancing").get("netAmount") if data.get("donorFinancing") is not None else None
      supplyType = data.get("supplyType") if data.get("supplyType") is not None else None
      contractSignatureDate = data.get("contractSignatureDate") if data.get("contractSignatureDate") is not None else None
      estimatedCompletionDate = data.get("estimatedCompletionDate") if data.get("estimatedCompletionDate") is not None else None
      
      tenderMajorSectors = data.get("majorSectors")
      msColumnValue = None
      if tenderMajorSectors is not None:
        for majorSector in tenderMajorSectors:
          if msColumnValue is None:
            msColumnValue = majorSector
          else:
            msColumnValue = msColumnValue + "#" + majorSector
            
      tenderSectors = data.get("sectors")
      sColumnValue = None
      if tenderSectors is not None:
        for sector in tenderSectors:
          if sColumnValue is None:
            sColumnValue = sector
          else:
            sColumnValue = sColumnValue + ";" + sector
            
      signatureDates = data.get("signatureDates")
      sdColumnValue = None
      if signatureDates is not None:
        for singleSignatureDate in signatureDates:
          if sdColumnValue is None:
            sdColumnValue = singleSignatureDate
          else:
            sdColumnValue = sdColumnValue + ";" + singleSignatureDate
          
      tenderProcedureType = data.get("procedureType") if data.get("procedureType") is not None else None
      tenderProcurementType = data.get("procurementType") if data.get("procurementType") is not None else None
      tenderTitle = data.get("title") if data.get("title") is not None else None
      
      publications = data.get("publications")
      publicationSourceId = None
      publicationDate = None
      publicationSourceFormType = None
      publicationFormType = None
      if publications is not None and len(publications) > 0:
        publicationSourceId = publications[0].get("sourceId")
        publicationDate = publications[0].get("publicationDate")
        publicationSourceFormType = publications[0].get("sourceFormType")
        publicationFormType = publications[0].get("formType")
        
      tenderProject = data.get("project")
      tenderProjectCountry = None
      tpoColumnValue = None
      tenderProjectName = None
      tenderProjectId = None
      if tenderProject is not None:
        tenderProjectCountry = tenderProject.get("country")
        tenderProjectName = tenderProject.get("name")
        tenderProjectOperations = tenderProject.get("operations")
        tenderProjectId = tenderProject.get("projectId")
        if (tenderProjectOperations is not None):
          for tpOperation in tenderProjectOperations:
            if tpoColumnValue is None:
              tpoColumnValue = tpOperation.get("operationNumber")
            else:
              tpoColumnValue = tpoColumnValue + ";" + tpOperation.get("operationNumber")
        
      if projectId is not None:
        row.append(projectId)
      else:
        row.append(tenderProjectId)  
        
      row.append(projectApprovalDate)
      row.append(projectBorrowerFinancing)
      row.append(projectCountry)
      row.append(projectDescription)
      row.append(projectDonorFinancing)
      row.append(projectFinalCost)
      row.append(sdColumnValue)
      row.append(msColumnValue)
      row.append(sColumnValue)
      row.append(tenderProjectName)
      row.append(tenderProjectCountry)
      row.append(tpoColumnValue)
      row.append(tenderTitle)
      row.append(tenderDonorFinancing)
      row.append(bidderName)
      row.append(bidderCity)
      row.append(bidderCountry)
      row.append(bidderAddress)
      row.append(buyerAssignedId)
      row.append(description)
      row.append(finalPrice)
      row.append(fiscalYear)
      row.append(addressOfImplementation)
      row.append(supplyType)
      row.append(contractSignatureDate)
      row.append(estimatedCompletionDate)
      row.append(poColumnValue)
      row.append(tenderBidDeadline)
      row.append(tenderProcedureType)
      row.append(tenderProcurementType)
      row.append(buyerName)
      row.append(buyerCity)
      row.append(buyerCountry)
      row.append(buyerAddress)
      row.append(publicationSourceId)
      row.append(publicationDate)
      row.append(publicationSourceFormType)
      row.append(publicationFormType)

      print(row)
      
      rows.append(row)
  
    page = page + 1

    if len(result_set) < 1000:
      # last page of migration
      break

  else:
    response.raise_for_status()
    break

with open('c:\\tmp\\idb_procurement_notices.csv', 'w', newline='', encoding='utf-8') as f:
  writer = csv.writer(f)
  writer.writerows(rows)
  