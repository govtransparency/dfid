import requests
import json
import csv

def donorFinancings(financings):
  totalSum = None
  for financing in financings:
    if totalSum is None:
      totalSum = financing.get("amountWithVat")
    else: 
      totalSum += financing.get("amountWithVat")
    return totalSum
    
def processFinancingSummaries(projectId, summaries):
  for summary in summaries:
    summaryRow = []
    summaryApprovalDate = summary.get("approvalDate") if summary.get("approvalDate") is not None else None
    summaryClosinglDate = summary.get("closingDate") if summary.get("closingDate") is not None else None
    summaryLoanNumber = summary.get("loanNumber") if summary.get("loanNumber") is not None else None
    summaryCancelledAmount = summary.get("cancelledAmount").get("amountWithVat") if summary.get("cancelledAmount") is not None else None
    summaryDisbursed = summary.get("disbursed").get("amountWithVat") if summary.get("disbursed") is not None else None
    summaryInterestChargesFees = summary.get("interestChargesFees").get("amountWithVat") if summary.get("interestChargesFees") is not None else None
    summaryPrincipal = summary.get("principal").get("amountWithVat") if summary.get("principal") is not None else None
    summaryRepaymens = summary.get("repayments").get("amountWithVat") if summary.get("repayments") is not None else None
    
    summaryRow.append(projectId);
    summaryRow.append(summaryApprovalDate);
    summaryRow.append(summaryClosinglDate);
    summaryRow.append(summaryLoanNumber);
    summaryRow.append(summaryCancelledAmount);
    summaryRow.append(summaryDisbursed);
    summaryRow.append(summaryInterestChargesFees);
    summaryRow.append(summaryPrincipal);
    summaryRow.append(summaryRepaymens);
    
    financingSummaries.append(summaryRow);

def processDocumentPublications(projectId, publications):
  for publication in publications:
    if not publication.get("isIncluded"):
      documentRow = []
      documentSourceType = publication.get("sourceFormType")
      documentType = None
      documentFound = False
      if documentSourceType is not None:
        if documentSourceType == "Loan Agreement" or documentSourceType == "Agreement" or documentSourceType == "Credit Agreement" or documentSourceType == "Project Agreement" or documentSourceType == "Grant or Trust Fund Agreement" or documentSourceType == "Financing Agreement" or documentSourceType == "Guarantee Agreement" or documentSourceType == "Trust Fund Administrative Agreement":
          documentType = "Loan agreement"
          documentFound = True
        elif documentSourceType == "Procurement Plan":   
          documentType = "Procurement plans"
          documentFound = True
          
      if documentFound:
        documentRow.append(projectId)
        documentPublication = publication.get("publicationDate")
        documentRow.append(documentSourceType)
        documentRow.append(documentType)
        documentRow.append(documentPublication)
        documents.append(documentRow)

rows=[]
financingSummaries=[]
documents=[]
evaluations={}
projectNames = {}
projectDescriptions = {}
page = 0
projectDetailHeader=["Project approval date","Project borrower name","Project closing date","Project country",
      "Project description","Donor financing","Environmental and social category","Final costs","Financing 1","Financing 2",
      "Financing 3","Financing rest","Financier 1","Financier 2","Financier 3","Financier rest","Grant amount",
      "Implementing agency","Lending instrument","Name","Product line","Project ID","Region","Status","Team leader",
      "Sectors","Themes","Number of CFT","Number of CA","Number of documents","Newest CFT date","Newest CA date","Newest document date",
      "Oldest CFT date","Oldest CA date","Oldest document date", "Evaluation fiscal year", "Evaluation data", "Evaluation type", 
      "Evaluation err ex ante", "Evaluation err ex post", "Evaluation project outcome", "Evaluation project impact", 
      "Evaluation sustainability rating", "Evaluation risk to development", "Evaluation icr quality", "Evaluation me quality",
      "Borrower overall performance","Borrower government performance" ,"Borrower implementing agency performance", 
      "Borrower quality ex ante", "Donor supervision quality", "Donor overall performance", "Donor quality ex ante"]
financingSummaryHeader=["Project ID","Approval date","Closing date","Loan number","Cancelled amount",
      "Disbursed","Interest charges fees","Principal","Repayments"]
documentPublicationsHeader=["Project ID", "Document source type", "Document type", "Publication date"]
rows.append(projectDetailHeader)
financingSummaries.append(financingSummaryHeader)
documents.append(documentPublicationsHeader)

url = "https://x.x.x.x:5567/login"
payload = {'username': 'xxx', 'password': 'yyy'}
response = requests.post(url, data=payload, verify=False)
result = json.loads(response.content)
token = result.get("auth_token")

while True:
  url = "https://x.x.x.x:5567/protected/clean_project/timestamp/2016-07-01T12:30:00.000/source/eu.dfid.worker.wb.clean.WBIEGProjectPerformanceRatingsSODACleaner/page/%s?auth_token=%s" % (page, token)
  
  response = requests.get(url, verify=False)
  
  # For successful API call, response code will be 200 (OK)
  if(response.ok):
    result_set = json.loads(response.content)
    for data in result_set:
      if data.get("projectId") is not None and data.get("evaluation") is not None:
        evaluation = {}
        evaluation["evaluationFiscalYear"] = data.get("evaluation").get("evaluationFiscalYear") if data.get("evaluation").get("evaluationFiscalYear") is not None else None
        evaluation["evaluationDate"] = data.get("evaluation").get("evaluationDate") if data.get("evaluation").get("evaluationDate") is not None else None
        evaluation["evaluationType"] = data.get("evaluation").get("evaluationType") if data.get("evaluation").get("evaluationType") is not None else None
        evaluation["projectErrExAnte"] = data.get("evaluation").get("projectErrExAnte") if data.get("evaluation").get("projectErrExAnte") is not None else None
        evaluation["projectErrExPost"] = data.get("evaluation").get("projectErrExPost") if data.get("evaluation").get("projectErrExPost") is not None else None
        evaluation["projectOutcome"] = data.get("evaluation").get("projectOutcome") if data.get("evaluation").get("projectOutcome") is not None else None
        evaluation["projectImpact"] = data.get("evaluation").get("projectImpact") if data.get("evaluation").get("projectImpact") is not None else None                                                                                         
        evaluation["sustainabilityRating"] = data.get("evaluation").get("sustainabilityRating") if data.get("evaluation").get("sustainabilityRating") is not None else None
        evaluation["riskToDevelopment"] = data.get("evaluation").get("riskToDevelopment") if data.get("evaluation").get("riskToDevelopment") is not None else None
        evaluation["icrQuality"] = data.get("evaluation").get("icrQuality") if data.get("evaluation").get("icrQuality") is not None else None
        evaluation["meQuality"] = data.get("evaluation").get("meQuality") if data.get("evaluation").get("meQuality") is not None else None
        
        evaluation["borrowerOverallPerformance"] = None
        evaluation["borrowerGovernmentPerformance"] = None
        evaluation["borrowerImplementingAgencyPerformance"] = None
        evaluation["borrowerQualityExAnte"] = None

        if data.get("borrower") is not None and data.get("borrower").get("bodyEvaluation") is not None:
          evaluation["borrowerOverallPerformance"] = data.get("borrower").get("bodyEvaluation").get("overallPerformance")
          evaluation["borrowerGovernmentPerformance"] = data.get("borrower").get("bodyEvaluation").get("governmentPerformance")
          evaluation["borrowerImplementingAgencyPerformance"] = data.get("borrower").get("bodyEvaluation").get("implementingAgencyPerformance")
          evaluation["borrowerQualityExAnte"] = data.get("borrower").get("bodyEvaluation").get("qualityExAnte")
          
        evaluation["donorSupervisionQuality"] = data.get("donorEvaluation").get("supervisionQuality") if data.get("donorEvaluation") is not None and data.get("donorEvaluation").get("supervisionQuality") is not None else None
        evaluation["donorOverallPerformance"] = data.get("donorEvaluation").get("overallPerformance") if data.get("donorEvaluation") is not None and data.get("donorEvaluation").get("overallPerformance") is not None else None
        evaluation["donorQualityExAnte"] = data.get("donorEvaluation").get("qualityExAnte") if data.get("donorEvaluation") is not None and data.get("donorEvaluation").get("qualityExAnte") is not None else None 
         
        evaluations[data.get("projectId")] = evaluation
        print(evaluation)
 
    page = page + 1

    if len(result_set) < 1000:
      # last page of migration
      break
  else:
    response.raise_for_status()
    break

while True:
  url = "https://x.x.x.x:5567/protected/clean_project/timestamp/2016-07-01T12:30:00.000/source/eu.dfid.worker.wb.clean.WBPOProjectCleaner/page/%s?auth_token=%s" % (page, token)
  
  response = requests.get(url, verify=False)
  
  # For successful API call, response code will be 200 (OK)
  if(response.ok):
    result_set = json.loads(response.content)
    for data in result_set:
      if data.get("projectId") is not None:
        projectNames[data.get("projectId")] = data.get("name")
        projectDescriptions[data.get("projectId")] = data.get("description")
        print("Project ID: "+data.get("projectId")+", Project name: " + data.get("name"))
 
    page = page + 1

    if len(result_set) < 1000:
      # last page of migration
      break
  else:
    response.raise_for_status()
    break
  
page = 0
while True:
  url = "https://x.x.x.x:5567/protected/clean_project/timestamp/2016-07-01T12:30:00.000/source/eu.dfid.worker.wb.clean.WBPOWebProjectCleaner/page/%s?auth_token=%s" % (page, token)
  
  response = requests.get(url, verify=False)
  
  # For successful API call, response code will be 200 (OK)
  if(response.ok):
    result_set = json.loads(response.content)
    for data in result_set:
      row=[]
      projectApprovalDate = data.get("approvalDate") if data.get("approvalDate") is not None else None
      projectBorrowerName = data.get("borrower").get("name") if data.get("borrower") is not None else None
      projectClosingDate = data.get("closingDate") if data.get("closingDate") is not None else None
      projectCountry = data.get("country") if data.get("country") is not None else None
      projectDonorFinancing = donorFinancings(data.get("donorFinancings")) if data.get("donorFinancings") is not None else None
      projectEnvironmentalAndSocialCategory = data.get("environmentalAndSocialCategory") if data.get("environmentalAndSocialCategory") is not None else None
      projectFinalCost = data.get("finalCost").get("amountWithVat") if data.get("finalCost") is not None else None
      if data.get("financingSummary") is not None: 
        processFinancingSummaries(data.get("projectId"), data.get("financingSummary"))
        
      if data.get("publications") is not None: 
        processDocumentPublications(data.get("projectId"), data.get("publications"))
        
      financingPlan = data.get("financingPlan")
      
      projectFinancingRest = None
      projectFinancing1 = None
      projectFinancing2 = None
      projectFinancing3 = None
      if financingPlan is not None and len(financingPlan) > 0:
        projectFinancing1 =  financingPlan[0].get("commitment").get("amountWithVat") if financingPlan[0].get("commitment") is not None else None
      if financingPlan is not None and len(financingPlan) > 1:
        projectFinancing2 =  financingPlan[1].get("commitment").get("amountWithVat") if financingPlan[1].get("commitment") is not None else None
      if financingPlan is not None and len(financingPlan) > 2:
        projectFinancing3 =  financingPlan[2].get("commitment").get("amountWithVat") if financingPlan[2].get("commitment") is not None else None
      if financingPlan is not None and len(financingPlan) > 3:
        index = 0
        for financing in financingPlan:
          index += 1
          if index > 3:
            if projectFinancingRest is None:
              projectFinancingRest = str(financing.get("commitment").get("amountWithVat"))
            else:
              projectFinancingRest = projectFinancingRest + ";" + str(financing.get("commitment").get("amountWithVat"))
              
      projectFinancierRest = None
      projectFinancier1 = None
      projectFinancier2 = None
      projectFinancier3 = None
      if financingPlan is not None and len(financingPlan) > 0:
        projectFinancier1 =  financingPlan[0].get("financier")
      if financingPlan is not None and len(financingPlan) > 1:
        projectFinancier2 =  financingPlan[1].get("financier")
      if financingPlan is not None and len(financingPlan) > 2:
        projectFinancier3 =  financingPlan[2].get("financier")
      if financingPlan is not None and len(financingPlan) > 3:
        index = 0
        for financing in financingPlan:
          index += 1
          if index > 3:
            if projectFinancierRest is None:
              projectFinancierRest = financing.get("financier")
            else:
              projectFinancierRest = projectFinancierRest + ";" + financing.get("financier")

      projectGrantAmount = data.get("grantAmount") if data.get("grantAmount") is not None else None
      projectImplementingAgency = data.get("implementingAgency").get("name") if data.get("implementingAgency") is not None else None
      projectLendingInstrument = data.get("lendingInstrument") if data.get("lendingInstrument") is not None else None
      projectName = projectNames[data.get("projectId")] if data.get("projectId") in projectNames else None
      projectDescription = projectDescriptions[data.get("projectId")] if data.get("projectId") in projectDescriptions else None
      projectProductLine = data.get("productLine") if data.get("productLine") is not None else None
      projectProjectId = data.get("projectId") if data.get("projectId") is not None else None
      projectRegion = data.get("region") if data.get("region") is not None else None
      projectStatus = data.get("status") if data.get("status") is not None else None
      projectTeamLeader = data.get("teamLeader") if data.get("teamLeader") is not None else None
      
      evaluationFiscalYear = None
      evaluationDate = None
      evaluationType = None
      projectErrExAnte = None
      projectErrExPost = None
      projectOutcome = None
      projectImpact = None                                                                                         
      sustainabilityRating = None
      riskToDevelopment = None
      icrQuality = None
      meQuality = None
      borrowerOverallPerformance = None
      borrowerGovernmentPerformance = None
      borrowerImplementingAgencyPerformance = None
      borrowerQualityExAnte = None
      donorSupervisionQuality = None
      donorOverallPerformance = None
      donorQualityExAnte = None

      evaluation = evaluations[data.get("projectId")] if data.get("projectId") in evaluations else None

      if evaluation is not None:
        evaluationFiscalYear = evaluation["evaluationFiscalYear"] if evaluation["evaluationFiscalYear"] is not None else None
        evaluationDate = evaluation["evaluationDate"] if evaluation["evaluationDate"] is not None else None
        evaluationType = evaluation["evaluationType"] if evaluation["evaluationType"] is not None else None
        projectErrExAnte = evaluation["projectErrExAnte"] if evaluation["projectErrExAnte"] is not None else None
        projectErrExPost = evaluation["projectErrExPost"] if evaluation["projectErrExPost"] is not None else None
        projectOutcome = evaluation["projectOutcome"] if evaluation["projectOutcome"] is not None else None
        projectImpact = evaluation["projectImpact"] if evaluation["projectImpact"] is not None else None                                                                                         
        sustainabilityRating = evaluation["sustainabilityRating"] if evaluation["sustainabilityRating"] is not None else None
        riskToDevelopment = evaluation["riskToDevelopment"] if evaluation["riskToDevelopment"] is not None else None
        icrQuality = evaluation["icrQuality"] if evaluation["icrQuality"] is not None else None
        meQuality = evaluation["meQuality"] if evaluation["meQuality"] is not None else None
        borrowerOverallPerformance = evaluation["borrowerOverallPerformance"] if evaluation["borrowerOverallPerformance"] is not None else None
        borrowerGovernmentPerformance = evaluation["borrowerGovernmentPerformance"] if evaluation["borrowerGovernmentPerformance"] is not None else None
        borrowerImplementingAgencyPerformance = evaluation["borrowerImplementingAgencyPerformance"] if evaluation["borrowerImplementingAgencyPerformance"] is not None else None
        borrowerQualityExAnte = evaluation["borrowerQualityExAnte"] if evaluation["borrowerQualityExAnte"] is not None else None
        donorSupervisionQuality = evaluation["donorSupervisionQuality"] if evaluation["donorSupervisionQuality"] is not None else None
        donorOverallPerformance = evaluation["donorOverallPerformance"] if evaluation["donorOverallPerformance"] is not None else None
        donorQualityExAnte = evaluation["donorQualityExAnte"] if evaluation["donorQualityExAnte"] is not None else None
     
      projectSectors = None
      sectors = data.get("sectors")
      if sectors is not None and len(sectors) > 0: 
        for sector in sectors:
          sectorName = sector.get("name") if sector.get("name") is not None else ""
          sectorWeight = sector.get("weight") if sector.get("weight") is not None else "" 
          if projectSectors is None:
            projectSectors = sectorName + "," + str(sectorWeight) 
          else:
            projectSectors = projectSectors + ";" + sectorName + "," + str(sectorWeight)
           
      projectThemes = None
      themes = data.get("themes")
      if themes is not None and len(themes) > 0: 
        for theme in themes: 
          themeName = theme.get("name") if theme.get("name") is not None else ""
          themeWeight = theme.get("weight") if theme.get("weight") is not None else "" 
          if projectThemes is None:
            projectThemes = themeName + "," + str(themeWeight) 
          else:
            projectThemes = projectThemes + ";" + themeName + "," + str(themeWeight)

      publications = data.get("publications")
      projectCftNumber = None
      projectCaNumber = None
      projectDocumentsNumber = None
      projectNewestCftDate = None
      projectNewestCaDate = None
      projectNewestDocumentDate = None
      projectOldestCftDate = None
      projectOldestCaDate = None
      projectOldestDocumentDate = None
      if publications is not None and len(publications) > 0: 
        for publication in publications:
          # contract notices
          if publication is not None and publication.get("formType") == "CONTRACT_NOTICE":
            if projectCftNumber is None:
              projectCftNumber = 1
            else:
              projectCftNumber += 1
          
            if projectNewestCftDate is None:
              projectNewestCftDate = publication.get("publicationDate")
            elif publication.get("publicationDate") is not None and projectNewestCftDate < publication.get("publicationDate"):
              projectNewestCftDate = publication.get("publicationDate")
             
            if projectOldestCftDate is None:
              projectOldestCftDate = publication.get("publicationDate")
            elif publication.get("publicationDate") is not None and projectOldestCftDate > publication.get("publicationDate"):
              projectOldestCftDate = publication.get("publicationDate")
        
          # contract awards 
          if publication is not None and publication.get("formType") == "CONTRACT_AWARD":
            if projectCaNumber is None:
              projectCaNumber = 1
            else:
              projectCaNumber += 1

            if projectNewestCaDate is None:
              projectNewestCaDate = publication.get("publicationDate")
            elif publication.get("publicationDate") is not None and projectNewestCaDate < publication.get("publicationDate"):
              projectNewestCaDate = publication.get("publicationDate")
              
            if projectOldestCaDate is None:
              projectOldestCaDate = publication.get("publicationDate")
            elif publication.get("publicationDate") is not None and projectOldestCaDate > publication.get("publicationDate"):
              projectOldestCaDate = publication.get("publicationDate")
          
          # coduments
          if publication is not None and publication.get("formType") is None:
            if projectDocumentsNumber is None:
              projectDocumentsNumber = 1
            else:
              projectDocumentsNumber += 1
      
            if projectNewestDocumentDate is None:
              projectNewestDocumentDate = publication.get("publicationDate")
            elif publication.get("publicationDate") is not None and projectNewestDocumentDate < publication.get("publicationDate"):
              projectNewestDocumentDate = publication.get("publicationDate")
              
            if projectOldestDocumentDate is None:
              projectOldestDocumentDate = publication.get("publicationDate")
            elif publication.get("publicationDate") is not None and projectOldestDocumentDate > publication.get("publicationDate"):
              projectOldestDocumentDate = publication.get("publicationDate")
      row.append(projectApprovalDate);
      row.append(projectBorrowerName);
      row.append(projectClosingDate);
      row.append(projectCountry);
      row.append(projectDescription);
      row.append(projectDonorFinancing);
      row.append(projectEnvironmentalAndSocialCategory);
      row.append(projectFinalCost);
      row.append(projectFinancing1);
      row.append(projectFinancing2);
      row.append(projectFinancing3);
      row.append(projectFinancingRest);
      row.append(projectFinancier1);
      row.append(projectFinancier2);
      row.append(projectFinancier3);
      row.append(projectFinancierRest);
      row.append(projectGrantAmount);
      row.append(projectImplementingAgency);
      row.append(projectLendingInstrument);
      row.append(projectName);
      row.append(projectProductLine);
      row.append(projectProjectId);
      row.append(projectRegion);
      row.append(projectStatus);
      row.append(projectTeamLeader);
      row.append(projectSectors);
      row.append(projectThemes);
      row.append(projectCftNumber);
      row.append(projectCaNumber);
      row.append(projectDocumentsNumber);
      row.append(projectNewestCftDate);
      row.append(projectNewestCaDate);
      row.append(projectNewestDocumentDate);
      row.append(projectOldestCftDate);
      row.append(projectOldestCaDate);
      row.append(projectOldestDocumentDate);
      row.append(evaluationFiscalYear);
      row.append(evaluationDate);
      row.append(evaluationType);
      row.append(projectErrExAnte);
      row.append(projectErrExPost);
      row.append(projectOutcome);
      row.append(projectImpact);
      row.append(sustainabilityRating);
      row.append(riskToDevelopment);
      row.append(icrQuality);
      row.append(meQuality);   
      row.append(borrowerOverallPerformance);   
      row.append(borrowerGovernmentPerformance);   
      row.append(borrowerImplementingAgencyPerformance);   
      row.append(borrowerQualityExAnte);   
      row.append(donorSupervisionQuality);   
      row.append(donorOverallPerformance);   
      row.append(donorQualityExAnte);
      
      print(row)
      
      rows.append(row)
 
    page = page + 1

    if len(result_set) < 1000:
      # last page of migration
      break
  else:
    response.raise_for_status()
    break
    
with open('c:\\tmp\\wb_project_details.csv', 'w', newline='', encoding='utf-8') as f:
  writer = csv.writer(f)
  writer.writerows(rows)

with open('c:\\tmp\\wb_project_financing_summaries.csv', 'w', newline='', encoding='utf-8') as f:
  writer = csv.writer(f)
  writer.writerows(financingSummaries)
  
with open('c:\\tmp\\wb_project_documents.csv', 'w', newline='', encoding='utf-8') as f:
  writer = csv.writer(f)
  writer.writerows(documents)  

