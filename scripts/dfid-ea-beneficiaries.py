import requests
import json
import csv

rows=[]
page = 0
header=["Title","Final price","Estimated duration in month","Purpose",
      "Supply type","Address of implementation - country","Address of implementation", "Bidder name",
      "Bidder country","Contract number", "Publication date"]
rows.append(header)

url = "https://x.x.x.x:5567/login"
payload = {'username': 'xxx', 'password': 'yyy'}
response = requests.post(url, data=payload, verify=False)
result = json.loads(response.content)
token = result.get("auth_token")

while True:
  url = "https://x.x.x.x:5567/protected/clean_tender/timestamp/2016-01-01T00:00:00.000/source/eu.dfid.worker.ec.clean.EAContractCleaner/page/%s?auth_token=%s" % (page, token)
  
  response = requests.get(url, verify=False)
  
  # For successful API call, response code will be 200 (OK)
  if(response.ok):
    result_set = json.loads(response.content)
    for data in result_set:
      row=[]
      
      tenderTitle = data.get("title") if data.get("title") is not None else None
      tenderFinalPrice = data.get("finalPrice").get("netAmount") if data.get("finalPrice") is not None else None
      tenderEstimatedDurationInMonth = data.get("estimatedDurationInMonths") if data.get("estimatedDurationInMonths") is not None else None
      tenderPurpose = data.get("purpose") if data.get("purpose") is not None else None
      tenderSupplyType = data.get("supplyType") if data.get("supplyType") is not None else None
      addressOfImplementationCountry = data.get("addressOfImplementation").get("country") if data.get("addressOfImplementation") is not None else None
      addressOfImplementation = data.get("addressOfImplementation").get("rawAddress") if data.get("addressOfImplementation") is not None else None
      
      winnerCountry = None
      winnerName = None
      tenderLots = data.get("lots")
      for tenderLot in tenderLots:
        bids = tenderLot.get("bids")
        for bid in bids:
          if bid.get("isWinning"):
            bidders = bid.get("bidders")
            for bidder in bidders:
              winnerName = bidder.get("name")
              winnerCountry = bidder.get("address").get("country") if bidder.get("address") is not None else None
      
      
      publications = data.get("publications")
      contractNo = None
      publicationDate = None
      
      if publications:
        for publication in publications:
          if publication.get("isIncluded"):
            contractNo = publication.get("sourceId")
            publicationDate = publication.get("publicationDate")
            
 
        
      row.append(tenderTitle)
      row.append(tenderFinalPrice)
      row.append(tenderEstimatedDurationInMonth)
      row.append(tenderPurpose)
      row.append(tenderSupplyType)
      row.append(addressOfImplementationCountry)
      row.append(addressOfImplementation)
      row.append(winnerName)
      row.append(winnerCountry)
      row.append(contractNo)
      row.append(publicationDate)

      print(row)
      
      rows.append(row)
  
    page = page + 1

    if len(result_set) < 1000:
      # last page of migration
      break

  else:
    response.raise_for_status()
    break

with open('c:\\tmp\\ec_beneficiaries.csv', 'w', newline='', encoding='utf-8') as f:
  writer = csv.writer(f)
  writer.writerows(rows)  