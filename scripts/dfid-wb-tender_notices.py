import requests
import json
import csv

rows=[]
projectNames = {}
page = 0
header=["Project borrower country","Project country","Project name","Project ID","Project region","Project product line",
"Bid deadline","Buyer assigned ID","Contract signature date","Title","Description","Final Price","Fiscal year",
"Objection date","Procurement type","Last update","Selection method","Supply type","Further information provider name","Further information provider email",
"Further information provider phone","Further information provider city","Further information provider street","Further information provider country",
"Bidder name","Bidder country","Contract number",
"Major sectors","Notice language","Notice status","Notice publication date","Notice type","Notice source ID"]
rows.append(header)

url = "https://x.x.x.x:5567/login"
payload = {'username': 'xxx', 'password': 'yyy'}
response = requests.post(url, data=payload, verify=False)
result = json.loads(response.content)
token = result.get("auth_token")

while True:
  url = "https://x.x.x.x:5567/protected/clean_tender/timestamp/2016-07-01T12:30:00.000/source/eu.dfid.worker.wb.clean.WBNoticeCleaner/page/%s?auth_token=%s" % (page, token)
  
  response = requests.get(url, verify=False)
  
  # For successful API call, response code will be 200 (OK)
  if(response.ok):
    result_set = json.loads(response.content)
    for data in result_set:
      row=[]
      projectBorrowerCountry = data.get("borrower").get("country") if data.get("borrower") is not None and data.get("borrower").get("address") is not None else None
      projectCountry = data.get("project").get("country") if data.get("project") is not None else None
      projectName = data.get("project").get("name") if data.get("project") is not None else None
      projectId = data.get("project").get("projectId") if data.get("project") is not None else None
      projectRegion = data.get("project").get("region") if data.get("project") is not None else None
      projectProductLine = data.get("productLine") if data.get("productLine") is not None else None
      
      bidDeadline = data.get("bidDeadline") if data.get("bidDeadline") is not None else None
      buyerAssignedId = data.get("buyerAssignedId") if data.get("buyerAssignedId") is not None else None
      contractSignatureDate = data.get("contractSignatureDate") if data.get("contractSignatureDate") is not None else None
      title = data.get("title") if data.get("title") is not None else None
      description = data.get("description") if data.get("description") is not None else None
      finalPrice = data.get("finalPrice").get("netAmount") if data.get("finalPrice") is not None else None
      fiscalYear = data.get("fiscalYear") if data.get("fiscalYear") is not None else None
      noObjectionDate = data.get("noObjectionDate") if data.get("noObjectionDate") is not None else None
      procurementType = data.get("procurementType") if data.get("procurementType") is not None else None
      lastUpdate = data.get("lastUpdate") if data.get("lastUpdate") is not None else None
      selectionMethod = data.get("selectionMethod") if data.get("selectionMethod") is not None else None
      supplyType = data.get("supplyType") if data.get("supplyType") is not None else None
                                                          
      furtherInformationProviderName = None
      furtherInformationProviderEmail = None
      furtherInformationProviderPhone = None
      furtherInformationProviderCity = None
      furtherInformationProviderStreet = None
      furtherInformationProviderCountry = None

      if data.get("furtherInformationProvider") is not None:
        furtherInformationProviderName = data.get("furtherInformationProvider").get("name")
        furtherInformationProviderEmail = data.get("furtherInformationProvider").get("email")
        furtherInformationProviderPhone = data.get("furtherInformationProvider").get("phone")
        if data.get("furtherInformationProvider").get("address") is not None:
          furtherInformationProviderCity = data.get("furtherInformationProvider").get("address").get("city")
          furtherInformationProviderStreet = data.get("furtherInformationProvider").get("address").get("street")
          furtherInformationProviderCountry = data.get("furtherInformationProvider").get("address").get("country")
      
      bidderName = None
      bidderCountry = None
      contractNumber = None
       
      lots = data.get("lots")
      if lots is not None and len(lots) > 0:
        bids = lots[0].get("bids")
        contractNumber = lots[0].get("contractNumber") 
        if bids is not None and len(bids) > 0:
          bidders = bids[0].get("bidders")
          if bidders is not None and len(bidders) > 0:
            bidderName = bidders[0].get("name")
            if bidders[0].get("address"):
              bidderCountry = bidders[0].get("address").get("country")
              
      majorSectors = data.get("majorSectors")
      msColumnValue = None
      if majorSectors is not None:
        for majorSector in majorSectors:
          if msColumnValue is None:
            msColumnValue = majorSector
          else:
            msColumnValue = msColumnValue + "#" + majorSector
            
      noticeLanguage = None    
      noticeStatus = None
      noticePublicationDate = None
      noticeType = None  
      noticeSourceId = None
      publications = data.get("publications")
      if publications is not None and len(publications) > 0:
        noticeLanguage = publications[0].get("language")
        noticeStatus = publications[0].get("noticeStatus")
        noticePublicationDate = publications[0].get("publicationDate")
        noticeType = publications[0].get("formType")
        noticeSourceId = publications[0].get("sourceId")      
      
      row.append(projectBorrowerCountry);
      row.append(projectCountry);
      row.append(projectName);
      row.append(projectId);
      row.append(projectRegion);
      row.append(projectProductLine);
      row.append(bidDeadline);
      row.append(buyerAssignedId);
      row.append(contractSignatureDate);
      row.append(title);
      row.append(description);
      row.append(finalPrice);
      row.append(fiscalYear);
      row.append(noObjectionDate);
      row.append(procurementType);
      row.append(lastUpdate);
      row.append(selectionMethod);
      row.append(supplyType);
      row.append(furtherInformationProviderName);
      row.append(furtherInformationProviderEmail);
      row.append(furtherInformationProviderPhone);
      row.append(furtherInformationProviderCity);
      row.append(furtherInformationProviderStreet);
      row.append(furtherInformationProviderCountry);
      row.append(bidderName);
      row.append(bidderCountry);
      row.append(contractNumber);
      row.append(msColumnValue);
      row.append(noticeLanguage);
      row.append(noticeStatus);
      row.append(noticePublicationDate);
      row.append(noticeType);
      row.append(noticeSourceId);
      
      print(row)
      
      rows.append(row)
 
    page = page + 1

    if len(result_set) < 1000:
      # last page of migration
      break

  else:
    response.raise_for_status()
    break
    
with open('c:\\tmp\\wb_tender_notices.csv', 'w', newline='', encoding='utf-8') as f:
  writer = csv.writer(f)
  writer.writerows(rows) 

