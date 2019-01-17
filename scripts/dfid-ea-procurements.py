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
header=["URL", "Tender ID","Title","Status","Publication date","Bid deadline","Bids recepient","Address of implementation",
      "Project name","Estimated price","Currency", "Estimated price in EURs","Supply type","Firs CFT","Last CFT","CFT count",
      "First CA","Last CA","CA count","First CC","Last CC","CC count", "Last update"]
rows.append(header)  

url = "https://x.x.x.x:5567/login"
payload = {'username': 'xxx', 'password': 'yyy'}
response = requests.post(url, data=payload, verify=False)
result = json.loads(response.content)
token = result.get("auth_token")

while True:
  url = "https://x.x.x.x:5567/protected/clean_tender/timestamp/2016-01-01T00:00:00.000/source/eu.dfid.worker.ec.clean.EAProcurementCleaner/page/%s?auth_token=%s" % (page, token)
  
  response = requests.get(url, verify=False)
  
  # For successful API call, response code will be 200 (OK)
  if(response.ok):
    result_set = json.loads(response.content)
    for data in result_set:
      row=[]
      
      tenderBidDeadline = data.get("bidDeadline") if data.get("bidDeadline") is not None else None
      tenderBidsRecipient = data.get("bidsRecipient") if data.get("bidsRecipient") is not None else None
      tenderEstimatedPrice = data.get("estimatedPrice").get("netAmount") if data.get("estimatedPrice") is not None else None
      tenderEstimatedPriceCurrency = data.get("estimatedPrice").get("currency") if data.get("estimatedPrice") is not None else None
      tenderEstimatedPriceEur = data.get("estimatedPrice").get("netAmountEur") if data.get("estimatedPrice") is not None else None
      tenderSupplyType = data.get("supplyType") if data.get("supplyType") is not None else None
      tenderTitle = data.get("title") if data.get("title") is not None else None
      tenderLots = data.get("lots") if data.get("lots") is not None else None
      
      projectName = data.get("project").get("name") if data.get("project") is not None else None
      
      tenderStatus = None
      addressOfImplementation = None
      for tenderLot in tenderLots:
        tenderStatus = tenderLot.get("status")
        addressOfImplementation = tenderLot.get("addressOfImplementation").get("country") if tenderLot.get("addressOfImplementation") is not None else None
      
      
      publications = data.get("publications")
      sourceTenderId = None

      firstNoticeDate = None
      lastNoticeDate = None
      noticeCount = 0
      firstAwardDate = None
      lastAwardDate = None
      awardCount = 0
      firstCancellationDate = None
      lastCancellationDate = None
      cancellationCount = 0
      lastUpdate = None
      sourceUrl = None
      sourceTenderId = None
      publicationDate = None
      
      if publications:
        for publication in publications:
          if publication.get("isIncluded"):
            sourceTenderId = publication.get("sourceTenderId")
            lastUpdate = publication.get("lastUpdate")
            publicationDate = publication.get("publicationDate")
            sourceUrl = publication.get("humanReadableUrl") 
            sourceTenderId = publication.get("sourceTenderId") 
          if publication.get("sourceFormType") is not None and "contract notice" in publication.get("sourceFormType").lower():
            noticeCount = noticeCount + 1
            if publication.get("publicationDate") is not None:
              if firstNoticeDate is None or firstNoticeDate > publication.get("publicationDate"):
                firstNoticeDate = publication.get("publicationDate")
              if lastNoticeDate is None or lastNoticeDate < publication.get("publicationDate"):
                lastNoticeDate = publication.get("publicationDate")
          if publication.get("sourceFormType") is not None and "award" in publication.get("sourceFormType").lower():
            awardCount = awardCount + 1
            if publication.get("publicationDate") is not None:
              if firstAwardDate is None or firstAwardDate > publication.get("publicationDate"):
                firstAwardDate = publication.get("publicationDate")
              if lastAwardDate is None or lastAwardDate < publication.get("publicationDate"):
                lastAwardDate = publication.get("publicationDate")
          if publication.get("sourceFormType") is not None and "cancellation" in publication.get("sourceFormType").lower():
            cancellationCount = cancellationCount + 1
            if publication.get("publicationDate") is not None:
              if firstCancellationDate is None or firstCancellationDate > publication.get("publicationDate"):
                firstCancellationDate = publication.get("publicationDate")
              if lastCancellationDate is None or lastCancellationDate < publication.get("publicationDate"):
                lastCancellationDate = publication.get("publicationDate")
 
        
      row.append(sourceUrl)
      row.append(sourceTenderId ) 
      row.append(tenderTitle)
      row.append(tenderStatus)
      row.append(publicationDate)
      row.append(tenderBidDeadline)
      row.append(tenderBidsRecipient)
      row.append(addressOfImplementation)
      row.append(projectName)
      row.append(tenderEstimatedPrice)
      row.append(tenderEstimatedPriceCurrency)
      row.append(tenderEstimatedPriceEur)
      row.append(tenderSupplyType)
      row.append(firstNoticeDate)
      row.append(lastNoticeDate)
      row.append(noticeCount)
      row.append(firstAwardDate)
      row.append(lastAwardDate)
      row.append(awardCount)
      row.append(firstCancellationDate)
      row.append(lastCancellationDate)
      row.append(cancellationCount)
      row.append(lastUpdate)

      print(row)
      
      rows.append(row)
  
    page = page + 1

    if len(result_set) < 1000:
      # last page of migration
      break

  else:
    response.raise_for_status()
    break

with open('c:\\tmp\\ec_procurements.csv', 'w', newline='', encoding='utf-8') as f:
  writer = csv.writer(f)
  writer.writerows(rows)  