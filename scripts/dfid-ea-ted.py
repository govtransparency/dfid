import requests
import json
import csv

rows=[]
page = 0
header=["Addres of implementation country", "Are variants accepted","Award decision date","Bid deadline","Buyer city","Buyer country",
      "Buyer address","CPVs","Description", "Requirements","Estimated duration in months","Estimated duration in days","Estimated invitation date","Estimated start date",
      "Estimated price","Final price","Fundings programme", "Fundings source", "Legal basis", "Procedure type", "Publication source ID", "Publication source tender ID",
      "Publication URL", "Publication publication date", "Publication dispatch date", "Publication source form type", "Publication form type", "Selection method",
      "Supply type", "Title", "Related publications", "Bidder name", "Bidder country", "Bidder address","Lot bid count","Lot title"]
rows.append(header)

url = "https://x.x.x.x:5567/login"
payload = {'username': 'xxx', 'password': 'yyy'}
response = requests.post(url, data=payload, verify=False)
result = json.loads(response.content)
token = result.get("auth_token")

while True:
  url = "https://x.x.x.x:5567/protected/clean_tender/timestamp/2016-01-01T00:00:00.000/source/eu.dfid.worker.ec.clean.EATedProcurementCleaner/page/%s?auth_token=%s" % (page, token)
  
  response = requests.get(url, verify=False)
  
  # For successful API call, response code will be 200 (OK)
  if(response.ok):
    result_set = json.loads(response.content)
    for data in result_set:
      row=[]
      
      tenderAddressOfImplementationCountry = data.get("addressOfImplementation").get("country") if data.get("addressOfImplementation") is not None else None
      tenderAreVariantsAccepted = data.get("areVariantsAccepted") if data.get("areVariantsAccepted") is not None else None
      tenderAwardDecisionDate = data.get("awardDecisionDate") if data.get("awardDecisionDate") is not None else None 
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
          buyerCity = buyer.get("address").get("city")
          buyerCountry = buyer.get("address").get("country")
          buyerAddress = buyer.get("address").get("rawAddress")
          
      cpvs = data.get("cpvs")
      cpvColumnValue = None
      if cpvs is not None:
        for cpv in cpvs:
          if cpvColumnValue is None:
            cpvColumnValue = cpv.get("code")
          else:
            cpvColumnValue = cpvColumnValue + ";" + cpv.get("code")
            
      tenderDescription = data.get("description") if data.get("description") is not None else None
      tenderEconomicRequirements = data.get("economicRequirements") if data.get("economicRequirements") is not None else None
      tenderEstimatedDurationInMonth = data.get("estimatedDurationInMonths") if data.get("estimatedDurationInMonths") is not None else None
      tenderEstimatedDurationInDays = data.get("estimatedDurationInDays") if data.get("estimatedDurationInDays") is not None else None
      tenderEstimatedInvitationDate = data.get("estimatedInvitationDate") if data.get("estimatedInvitationDate") is not None else None
      tenderEstimatedStartDate = data.get("estimatedStartDate") if data.get("estimatedStartDate") is not None else None
      tenderEstimatedPrice = data.get("estimatedPrice").get("netAmount") if data.get("estimatedPrice") is not None else None
      tenderFinalPrice = data.get("finalPrice").get("netAmount") if data.get("finalPrice") is not None else None

      fundings = data.get("fundings")
      fpColumnValue = None
      fsColumnValue = None
      if fundings is not None:
        for funding in fundings:
          if fpColumnValue is None:
            fpColumnValue = funding.get("programme")
          else:
            fpColumnValue = fpColumnValue + ";" + funding.get("code")
        for funding in fundings:
          if fsColumnValue is None:
            fsColumnValue = funding.get("source")
          else:
            fsColumnValue = fsColumnValue + ";" + funding.get("code")
            
      tenderLegalBasis = data.get("legalBasis") if data.get("legalBasis") is not None else None
      
      cleanLots = []       
      lots = data.get("lots")
      if lots is not None and len(lots) > 0:
        for lot in lots:
          bidderName = None
          bidderCountry = None
          bidderAddress = None
          bids = lot.get("bids")
          cleanLot = []
          if bids is not None and len(bids) > 0:
            bidders = bids[0].get("bidders")
            if bidders is not None and len(bidders) > 0:
              bidderName = bidders[0].get("name")
              if bidders[0].get("address"):
                bidderCountry = bidders[0].get("address").get("country")
                bidderAddress = bidders[0].get("address").get("rawAddress")
                
          cleanLot.append(bidderName)
          cleanLot.append(bidderCountry)
          cleanLot.append(bidderAddress)
          cleanLot.append(lot.get("bidsCount") if lot.get("bidsCount") is not None else None)
          cleanLot.append(lot.get("title") if lot.get("title") is not None else None)
          cleanLots.append(cleanLot)
      
      tenderPersonalRequirements = data.get("personalRequirements") if data.get("personalRequirements") is not None else None
      tenderProcedureType = data.get("procedureType") if data.get("procedureType") is not None else None

      publications = data.get("publications")
      publicationSourceId = None
      publicationSourceTenderId = None
      publicationHumanReadableUrl = None
      publicationPublicationDate = None
      publicationDispatchDate = None
      publicationSourceFormType = None
      publicationFormType = None
      relatedPublications = None
      if publications is not None and len(publications) > 0:
        publicationSourceId = publications[0].get("sourceId") if publications[0].get("sourceId") is not None else None
        publicationSourceTenderId = publications[0].get("sourceTenderId") if publications[0].get("sourceTenderId") is not None else None
        publicationHumanReadableUrl = publications[0].get("humanReadableUrl") if publications[0].get("humanReadableUrl") is not None else None
        publicationPublicationDate = publications[0].get("publicationDate") if publications[0].get("publicationDate") is not None else None
        publicationDispatchDate= publications[0].get("dispatchDate") if publications[0].get("dispatchDate") is not None else None
        publicationSourceFormType= publications[0].get("sourceFormType") if publications[0].get("sourceFormType") is not None else None
        publicationFormType= publications[0].get("formType") if publications[0].get("formType") is not None else None
        for publication in publications:
          if publication.get("isIncluded") is False:
            if relatedPublications is None:
              relatedPublications = publication.get("sourceId")
            else:
              relatedPublications = relatedPublications + ";" + publication.get("sourceId")
             
        
      tenderSelectionMethod = data.get("selectionMethod") if data.get("selectionMethod") is not None else None
#      tenderSpecificationsProvider = data.get("specificationsProvider") if data.get("specificationsProvider") is not None else None
      tenderSupplyType = data.get("supplyType") if data.get("supplyType") is not None else None
      tenderTechnicalRequirements = data.get("technicalRequirements") if data.get("technicalRequirements") is not None else None
      tenderTitle = data.get("title") if data.get("title") is not None else None
      
      row.append(tenderAddressOfImplementationCountry)
      row.append(tenderAreVariantsAccepted ) 
      row.append(tenderAwardDecisionDate)
      row.append(tenderBidDeadline)
#      row.append(tenderBidsRecipient)
      row.append(buyerCity)
      row.append(buyerCountry ) 
      row.append(buyerAddress)
      row.append(cpvColumnValue)      
      row.append(tenderDescription)
      row.append(tenderEconomicRequirements) 
      row.append(tenderEstimatedDurationInMonth)
      row.append(tenderEstimatedDurationInDays)
      row.append(tenderEstimatedInvitationDate)
      row.append(tenderEstimatedStartDate)
      row.append(tenderEstimatedPrice) 
      row.append(tenderFinalPrice)
      row.append(fpColumnValue)
      row.append(fsColumnValue) 
      row.append(tenderLegalBasis)
      #row.append(tenderPersonalRequirements)
      row.append(tenderProcedureType)
      row.append(publicationSourceId)
      row.append(publicationSourceTenderId)
      row.append(publicationHumanReadableUrl)
      row.append(publicationPublicationDate)
      row.append(publicationDispatchDate)
      row.append(publicationSourceFormType)
      row.append(publicationFormType)
      row.append(tenderSelectionMethod)
#      row.append(tenderSpecificationsProvider)
      row.append(tenderSupplyType)
      #row.append(tenderTechnicalRequirements)
      row.append(tenderTitle)
      row.append(relatedPublications)
      
      if len(cleanLots) > 0:
        for cleanLot in cleanLots:
          rows.append(row+cleanLot)
          print(row)
      else:
        rows.append(row)
  
    page = page + 1

    if len(result_set) < 1000:
      # last page of migration
      break

  else:
    response.raise_for_status()
    break

with open('c:\\tmp\\ec_ted_procurements.csv', 'w', newline='', encoding='utf-8') as f:
  writer = csv.writer(f)
  writer.writerows(rows)  