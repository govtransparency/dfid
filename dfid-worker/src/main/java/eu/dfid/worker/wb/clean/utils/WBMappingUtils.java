package eu.dfid.worker.wb.clean.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import eu.dfid.dataaccess.dto.codetables.DFIDRegion;
import eu.dfid.dataaccess.dto.codetables.LendingInstrument;
import eu.dfid.dataaccess.dto.codetables.LendingInstrumentType;
import eu.dfid.dataaccess.dto.codetables.ProjectStatus;
import eu.dl.dataaccess.dto.codetables.CountryCode;
import eu.dl.dataaccess.dto.codetables.PublicationFormType;
import eu.dl.dataaccess.dto.codetables.SelectionMethod;
import eu.dl.dataaccess.dto.codetables.TenderSupplyType;
import eu.dl.worker.clean.utils.CodeTableUtils;

/**
 * Provides common mappings for Worl Bank.
 *
 * @author Tomas Mrazek
 */
public final class WBMappingUtils {

    private static final String NULL_VALUE = "N/A";

    /**
     * Supress defaul constructor for noninstantiability.
     */
    private WBMappingUtils() {
    }

    /**
     * @return mapping for World Bank selection method
     */
    public static HashMap<Enum, List<String>> getSelectionMethodMapping() {
        HashMap<Enum, List<String>> mapping = new HashMap<>();

        mapping.put(SelectionMethod.COMMERCIAL_PRACTICES, Arrays.asList("Commercial Practices"));
        mapping.put(SelectionMethod.DIRECT_CONTRACTING, Arrays.asList("Direct Contracting"));
        mapping.put(SelectionMethod.FORCE_ACCOUNT, Arrays.asList("Force Account"));
        mapping.put(SelectionMethod.INDIVIDUAL, Arrays.asList("Individual"));
        mapping.put(SelectionMethod.INTERNATIONAL_COMPETITIVE_BIDDING,
            Arrays.asList("International Competitive Bidding"));
        mapping.put(SelectionMethod.INTERNATIONAL_SHOPPING, Arrays.asList("International Shopping"));
        mapping.put(SelectionMethod.LEAST_COST_SELECTION, Arrays.asList("Least Cost Selection"));
        mapping.put(SelectionMethod.LIMITED_INTERNATIONAL_BIDDING, Arrays.asList("Limited International Bidding"));
        mapping.put(SelectionMethod.MODIFIED_INTERNATIONAL_COMPETITIVE_BIDDING,
            Arrays.asList("Modified International Competitive Bidding"));
        mapping.put(SelectionMethod.NATIONAL_COMPETITIVE_BIDDING, Arrays.asList("National Competitive Bidding"));
        mapping.put(SelectionMethod.NATIONAL_SHOPPING, Arrays.asList("National Shopping"));
        mapping.put(SelectionMethod.QUALITY_AND_COST_BASED_SELECTION,
            Arrays.asList("Quality And Cost-Based Selection"));
        mapping.put(SelectionMethod.QUALITY_BASED_SELECTION, Arrays.asList("Quality Based Selection"));
        mapping.put(SelectionMethod.SELECTION_BASED_ON_CONSULTANTS_QUALIFICATION,
            Arrays.asList("Selection Based On Consultant's Qualification", "Selection Based On Consultant's Qualific"));
        mapping.put(SelectionMethod.SELECTION_BASED_ON_CONSULTANTS_QUALIFICATION_CQB,
            Arrays.asList("Selection Based On Consultant's Qualification-CQB"));
        mapping.put(SelectionMethod.SELECTION_UNDER_A_FIXED_BUDGET, Arrays.asList("Selection Under a Fixed Budget"));
        mapping.put(SelectionMethod.SERVICE_DELIVERY_CONTRACTS, Arrays.asList("Service Delivery Contracts"));
        mapping.put(SelectionMethod.SHOPPING, Arrays.asList("Shopping"));
        mapping.put(SelectionMethod.SINGLE_SOURCE_SELECTION, Arrays.asList("Single Source Selection"));

        mapping.put(null, Collections.singletonList(NULL_VALUE));
        
        return mapping;
    }

    /**
     * @return mapping for World Bank countries
     */
    public static HashMap<Enum, List<String>> getCountryMapping() {
        HashMap<Enum, List<String>> mapping = CodeTableUtils.enumToMapping(CountryCode.class);

        List<String> newMapping;
        //WB code for Western Africa
        newMapping = new ArrayList<>(mapping.get(CountryCode.WESTERN_AFRICA));
        newMapping.add("3W");
        mapping.put(CountryCode.WESTERN_AFRICA, newMapping);

        //WB code for Africa
        newMapping = new ArrayList<>(mapping.get(CountryCode.AFRICA));
        newMapping.add("3A");
        mapping.put(CountryCode.AFRICA, newMapping);

        //WB code for south-eastern Europe
        newMapping = new ArrayList<>(mapping.get(CountryCode.SOUTH_EASTERN_EUROPE_AND_BALKANS));
        newMapping.add("7B");
        mapping.put(CountryCode.SOUTH_EASTERN_EUROPE_AND_BALKANS, newMapping);

        mapping.put(null, Collections.singletonList(NULL_VALUE));

        return mapping;
    }

    /**
     * @return mapping for World Bank countries
     */
    //CHECKSTYLE.OFF: MethodLength
    public static HashMap<Enum, List<String>> getCountryFullNameMapping() {
    //CHECKSTYLE.ON: MethodLength
        HashMap<Enum, List<String>> mapping = new HashMap<>();

        mapping.put(CountryCode.AD, Collections.singletonList("Andorra"));
        mapping.put(CountryCode.AE, Collections.singletonList("United Arab Emirates"));
        mapping.put(CountryCode.AF, Collections.singletonList("Afghanistan"));
        mapping.put(CountryCode.AG, Collections.singletonList("Antigua and Barbuda"));
        mapping.put(CountryCode.AI, Collections.singletonList("Anguilla"));
        mapping.put(CountryCode.AL, Collections.singletonList("Albania"));
        mapping.put(CountryCode.AM, Collections.singletonList("Armenia"));
        mapping.put(CountryCode.AO, Collections.singletonList("Angola"));
        mapping.put(CountryCode.AQ, Collections.singletonList("Antarctica"));
        mapping.put(CountryCode.AR, Collections.singletonList("Argentina"));
        mapping.put(CountryCode.AS, Collections.singletonList("American Samoa"));
        mapping.put(CountryCode.AT, Collections.singletonList("Austria"));
        mapping.put(CountryCode.AU, Collections.singletonList("Australia"));
        mapping.put(CountryCode.AW, Collections.singletonList("Aruba"));
        mapping.put(CountryCode.AX, Collections.singletonList("Åland Islands"));
        mapping.put(CountryCode.AZ, Collections.singletonList("Azerbaijan"));
        mapping.put(CountryCode.BA, Collections.singletonList("Bosnia and Herzegovina"));
        mapping.put(CountryCode.BB, Collections.singletonList("Barbados"));
        mapping.put(CountryCode.BD, Collections.singletonList("Bangladesh"));
        mapping.put(CountryCode.BE, Collections.singletonList("Belgium"));
        mapping.put(CountryCode.BF, Collections.singletonList("Burkina Faso"));
        mapping.put(CountryCode.BG, Collections.singletonList("Bulgaria"));
        mapping.put(CountryCode.BH, Collections.singletonList("Bahrain"));
        mapping.put(CountryCode.BI, Collections.singletonList("Burundi"));
        mapping.put(CountryCode.BJ, Collections.singletonList("Benin"));
        mapping.put(CountryCode.BL, Collections.singletonList("Saint Barthélemy"));
        mapping.put(CountryCode.BM, Collections.singletonList("Bermuda"));
        mapping.put(CountryCode.BN, Collections.singletonList("Brunei Darussalam"));
        mapping.put(CountryCode.BO, Arrays.asList("Bolivia", "Bolivia, Plurinational State of"));
        mapping.put(CountryCode.BQ, Collections.singletonList("Bonaire, Sint Eustatius and Saba"));
        mapping.put(CountryCode.BR, Collections.singletonList("Brazil"));
        mapping.put(CountryCode.BS, Collections.singletonList("Bahamas"));
        mapping.put(CountryCode.BT, Collections.singletonList("Bhutan"));
        mapping.put(CountryCode.BV, Collections.singletonList("Bouvet Island"));
        mapping.put(CountryCode.BW, Collections.singletonList("Botswana"));
        mapping.put(CountryCode.BY, Collections.singletonList("Belarus"));        
        mapping.put(CountryCode.BZ, Collections.singletonList("Belize"));
        mapping.put(CountryCode.CA, Collections.singletonList("Canada"));
        mapping.put(CountryCode.CC, Collections.singletonList("Cocos (Keeling) Islands"));
        mapping.put(CountryCode.CD, Arrays.asList("Congo, Democratic Republic of", "Congo, Republic of"));
        mapping.put(CountryCode.CF, Collections.singletonList("Central African Republic"));
        mapping.put(CountryCode.CG, Collections.singletonList("Congo"));
        mapping.put(CountryCode.CH, Collections.singletonList("Switzerland"));
        mapping.put(CountryCode.CI, Collections.singletonList("Cote d'Ivoire"));
        mapping.put(CountryCode.CK, Collections.singletonList("Cook Islands"));
        mapping.put(CountryCode.CL, Collections.singletonList("Chile"));
        mapping.put(CountryCode.CM, Collections.singletonList("Cameroon"));
        mapping.put(CountryCode.CN, Collections.singletonList("China"));
        mapping.put(CountryCode.CO, Collections.singletonList("Colombia"));
        mapping.put(CountryCode.CR, Collections.singletonList("Costa Rica"));
        mapping.put(CountryCode.CU, Collections.singletonList("Cuba"));
        mapping.put(CountryCode.CV, Collections.singletonList("Cabo Verde"));
        mapping.put(CountryCode.CW, Collections.singletonList("Curaçao"));
        mapping.put(CountryCode.CX, Collections.singletonList("Christmas Island"));
        mapping.put(CountryCode.CY, Collections.singletonList("Cyprus"));
        mapping.put(CountryCode.CZ, Collections.singletonList("Czech Republic"));
        mapping.put(CountryCode.DE, Collections.singletonList("Germany"));
        mapping.put(CountryCode.DJ, Collections.singletonList("Djibouti"));
        mapping.put(CountryCode.DK, Collections.singletonList("Denmark"));
        mapping.put(CountryCode.DM, Collections.singletonList("Dominica"));
        mapping.put(CountryCode.DO, Collections.singletonList("Dominican Republic"));
        mapping.put(CountryCode.DZ, Collections.singletonList("Algeria"));
        mapping.put(CountryCode.EC, Collections.singletonList("Ecuador"));
        mapping.put(CountryCode.EE, Collections.singletonList("Estonia"));
        mapping.put(CountryCode.EG, Collections.singletonList("Egypt, Arab Republic of"));
        mapping.put(CountryCode.EH, Collections.singletonList("Western Sahara"));
        mapping.put(CountryCode.ER, Collections.singletonList("Eritrea"));
        mapping.put(CountryCode.ES, Collections.singletonList("Spain"));
        mapping.put(CountryCode.ET, Collections.singletonList("Ethiopia"));
        mapping.put(CountryCode.FI, Collections.singletonList("Finland"));
        mapping.put(CountryCode.FJ, Collections.singletonList("Fiji"));
        mapping.put(CountryCode.FK, Collections.singletonList("Falkland Islands (Malvinas)"));
        mapping.put(CountryCode.FM, Collections.singletonList("Micronesia, Federated States of"));
        mapping.put(CountryCode.FO, Collections.singletonList("Faroe Islands"));
        mapping.put(CountryCode.FR, Collections.singletonList("France"));
        mapping.put(CountryCode.GA, Collections.singletonList("Gabon"));
        mapping.put(CountryCode.GB, Collections.singletonList("United Kingdom of Great Britain and Northern Ireland"));
        mapping.put(CountryCode.GD, Collections.singletonList("Grenada"));
        mapping.put(CountryCode.GE, Collections.singletonList("Georgia"));
        mapping.put(CountryCode.GF, Collections.singletonList("French Guiana"));
        mapping.put(CountryCode.GG, Collections.singletonList("Guernsey"));
        mapping.put(CountryCode.GH, Collections.singletonList("Ghana"));
        mapping.put(CountryCode.GI, Collections.singletonList("Gibraltar"));
        mapping.put(CountryCode.GL, Collections.singletonList("Greenland"));
        mapping.put(CountryCode.GM, Arrays.asList("Gambia", "Gambia, The"));
        mapping.put(CountryCode.GN, Collections.singletonList("Guinea"));
        mapping.put(CountryCode.GP, Collections.singletonList("Guadeloupe"));
        mapping.put(CountryCode.GQ, Collections.singletonList("Equatorial Guinea"));
        mapping.put(CountryCode.GR, Collections.singletonList("Greece"));
        mapping.put(CountryCode.GS, Collections.singletonList("South Georgia and the South Sandwich Islands"));
        mapping.put(CountryCode.GT, Collections.singletonList("Guatemala"));
        mapping.put(CountryCode.GU, Collections.singletonList("Guam"));
        mapping.put(CountryCode.GW, Collections.singletonList("Guinea-Bissau"));
        mapping.put(CountryCode.GY, Collections.singletonList("Guyana"));
        mapping.put(CountryCode.HK, Collections.singletonList("Hong Kong"));
        mapping.put(CountryCode.HM, Collections.singletonList("Heard Island and McDonald Islands"));
        mapping.put(CountryCode.HN, Collections.singletonList("Honduras"));
        mapping.put(CountryCode.HR, Collections.singletonList("Croatia"));
        mapping.put(CountryCode.HT, Collections.singletonList("Haiti"));
        mapping.put(CountryCode.HU, Collections.singletonList("Hungary"));
        mapping.put(CountryCode.ID, Collections.singletonList("Indonesia"));
        mapping.put(CountryCode.IE, Collections.singletonList("Ireland"));
        mapping.put(CountryCode.IL, Collections.singletonList("Israel"));
        mapping.put(CountryCode.IM, Collections.singletonList("Isle of Man"));
        mapping.put(CountryCode.IN, Collections.singletonList("India"));
        mapping.put(CountryCode.IO, Collections.singletonList("British Indian Ocean Territory"));
        mapping.put(CountryCode.IQ, Collections.singletonList("Iraq"));
        mapping.put(CountryCode.IR, Collections.singletonList("Iran, Islamic Republic of"));
        mapping.put(CountryCode.IS, Collections.singletonList("Iceland"));
        mapping.put(CountryCode.IT, Collections.singletonList("Italy"));
        mapping.put(CountryCode.JE, Collections.singletonList("Jersey"));
        mapping.put(CountryCode.JM, Collections.singletonList("Jamaica"));
        mapping.put(CountryCode.JO, Collections.singletonList("Jordan"));
        mapping.put(CountryCode.JP, Collections.singletonList("Japan"));
        mapping.put(CountryCode.KE, Collections.singletonList("Kenya"));
        mapping.put(CountryCode.KG, Arrays.asList("Kyrgyzstan", "Kyrgyz Republic"));
        mapping.put(CountryCode.KH, Collections.singletonList("Cambodia"));
        mapping.put(CountryCode.KI, Collections.singletonList("Kiribati"));
        mapping.put(CountryCode.KM, Collections.singletonList("Comoros"));
        mapping.put(CountryCode.KN, Collections.singletonList("Saint Kitts and Nevis"));
        mapping.put(CountryCode.KP, Collections.singletonList("Korea, Democratic People's Republic of"));
        mapping.put(CountryCode.KR, Collections.singletonList("Korea, Republic of"));
        mapping.put(CountryCode.KW, Collections.singletonList("Kuwait"));
        mapping.put(CountryCode.KY, Collections.singletonList("Cayman Islands"));
        mapping.put(CountryCode.KZ, Collections.singletonList("Kazakhstan"));
        mapping.put(CountryCode.LA, Collections.singletonList("Lao People's Democratic Republic"));
        mapping.put(CountryCode.LB, Collections.singletonList("Lebanon"));
        mapping.put(CountryCode.LC, Arrays.asList("Saint Lucia", "St. Lucia"));
        mapping.put(CountryCode.LI, Collections.singletonList("Liechtenstein"));
        mapping.put(CountryCode.LK, Collections.singletonList("Sri Lanka"));
        mapping.put(CountryCode.LR, Collections.singletonList("Liberia"));
        mapping.put(CountryCode.LS, Collections.singletonList("Lesotho"));
        mapping.put(CountryCode.LT, Collections.singletonList("Lithuania"));
        mapping.put(CountryCode.LU, Collections.singletonList("Luxembourg"));
        mapping.put(CountryCode.LV, Collections.singletonList("Latvia"));
        mapping.put(CountryCode.LY, Collections.singletonList("Libya"));
        mapping.put(CountryCode.MA, Collections.singletonList("Morocco"));
        mapping.put(CountryCode.MC, Collections.singletonList("Monaco"));
        mapping.put(CountryCode.MD, Arrays.asList("Moldova, Republic of", "Moldova"));
        mapping.put(CountryCode.ME, Collections.singletonList("Montenegro"));
        mapping.put(CountryCode.MF, Collections.singletonList("Saint Martin (French part)"));
        mapping.put(CountryCode.MG, Collections.singletonList("Madagascar"));
        mapping.put(CountryCode.MH, Collections.singletonList("Marshall Islands"));
        mapping.put(CountryCode.MK, Collections.singletonList("Macedonia, the former Yugoslav Republic of"));
        mapping.put(CountryCode.ML, Collections.singletonList("Mali"));
        mapping.put(CountryCode.MM, Collections.singletonList("Myanmar"));
        mapping.put(CountryCode.MN, Collections.singletonList("Mongolia"));
        mapping.put(CountryCode.MO, Collections.singletonList("Macao"));
        mapping.put(CountryCode.MP, Collections.singletonList("Northern Mariana Islands"));
        mapping.put(CountryCode.MQ, Collections.singletonList("Martinique"));
        mapping.put(CountryCode.MR, Collections.singletonList("Mauritania"));
        mapping.put(CountryCode.MS, Collections.singletonList("Montserrat"));
        mapping.put(CountryCode.MT, Collections.singletonList("Malta"));
        mapping.put(CountryCode.MU, Collections.singletonList("Mauritius"));
        mapping.put(CountryCode.MV, Collections.singletonList("Maldives"));
        mapping.put(CountryCode.MW, Collections.singletonList("Malawi"));
        mapping.put(CountryCode.MX, Collections.singletonList("Mexico"));
        mapping.put(CountryCode.MY, Collections.singletonList("Malaysia"));
        mapping.put(CountryCode.MZ, Collections.singletonList("Mozambique"));
        mapping.put(CountryCode.NA, Collections.singletonList("Namibia"));
        mapping.put(CountryCode.NC, Collections.singletonList("New Caledonia"));
        mapping.put(CountryCode.NE, Collections.singletonList("Niger"));
        mapping.put(CountryCode.NF, Collections.singletonList("Norfolk Island"));
        mapping.put(CountryCode.NG, Collections.singletonList("Nigeria"));
        mapping.put(CountryCode.NI, Collections.singletonList("Nicaragua"));
        mapping.put(CountryCode.NL, Collections.singletonList("Netherlands"));
        mapping.put(CountryCode.NO, Collections.singletonList("Norway"));
        mapping.put(CountryCode.NP, Collections.singletonList("Nepal"));
        mapping.put(CountryCode.NR, Collections.singletonList("Nauru"));
        mapping.put(CountryCode.NU, Collections.singletonList("Niue"));
        mapping.put(CountryCode.NZ, Collections.singletonList("New Zealand"));
        mapping.put(CountryCode.OM, Collections.singletonList("Oman"));
        mapping.put(CountryCode.PA, Collections.singletonList("Panama"));
        mapping.put(CountryCode.PE, Collections.singletonList("Peru"));
        mapping.put(CountryCode.PF, Collections.singletonList("French Polynesia"));
        mapping.put(CountryCode.PG, Collections.singletonList("Papua New Guinea"));
        mapping.put(CountryCode.PH, Collections.singletonList("Philippines"));
        mapping.put(CountryCode.PK, Collections.singletonList("Pakistan"));
        mapping.put(CountryCode.PL, Collections.singletonList("Poland"));
        mapping.put(CountryCode.PM, Collections.singletonList("Saint Pierre and Miquelon"));
        mapping.put(CountryCode.PN, Collections.singletonList("Pitcairn"));
        mapping.put(CountryCode.PR, Collections.singletonList("Puerto Rico"));
        mapping.put(CountryCode.PS, Collections.singletonList("Palestine, State of"));
        mapping.put(CountryCode.PT, Collections.singletonList("Portugal"));
        mapping.put(CountryCode.PW, Collections.singletonList("Palau"));
        mapping.put(CountryCode.PY, Collections.singletonList("Paraguay"));
        mapping.put(CountryCode.QA, Collections.singletonList("Qatar"));
        mapping.put(CountryCode.RE, Collections.singletonList("Réunion"));
        mapping.put(CountryCode.RO, Collections.singletonList("Romania"));
        mapping.put(CountryCode.RS, Collections.singletonList("Serbia"));
        mapping.put(CountryCode.RU, Collections.singletonList("Russian Federation"));
        mapping.put(CountryCode.RW, Collections.singletonList("Rwanda"));
        mapping.put(CountryCode.SA, Collections.singletonList("Saudi Arabia"));
        mapping.put(CountryCode.SB, Collections.singletonList("Solomon Islands"));
        mapping.put(CountryCode.SC, Collections.singletonList("Seychelles"));
        mapping.put(CountryCode.SD, Collections.singletonList("Sudan"));
        mapping.put(CountryCode.SE, Collections.singletonList("Sweden"));
        mapping.put(CountryCode.SG, Collections.singletonList("Singapore"));
        mapping.put(CountryCode.SH, Collections.singletonList("Saint Helena, Ascension and Tristan da Cunha"));
        mapping.put(CountryCode.SI, Collections.singletonList("Slovenia"));
        mapping.put(CountryCode.SJ, Collections.singletonList("Svalbard and Jan Mayen"));
        mapping.put(CountryCode.SK, Collections.singletonList("Slovakia"));
        mapping.put(CountryCode.SL, Collections.singletonList("Sierra Leone"));
        mapping.put(CountryCode.SM, Collections.singletonList("San Marino"));
        mapping.put(CountryCode.SN, Collections.singletonList("Senegal"));
        mapping.put(CountryCode.SO, Collections.singletonList("Somalia"));
        mapping.put(CountryCode.SR, Collections.singletonList("Suriname"));
        mapping.put(CountryCode.SS, Collections.singletonList("South Sudan"));
        mapping.put(CountryCode.ST, Collections.singletonList("Sao Tome and Principe"));
        mapping.put(CountryCode.SV, Collections.singletonList("El Salvador"));
        mapping.put(CountryCode.SX, Collections.singletonList("Sint Maarten (Dutch part)"));
        mapping.put(CountryCode.SY, Collections.singletonList("Syrian Arab Republic"));
        mapping.put(CountryCode.SZ, Collections.singletonList("Swaziland"));
        mapping.put(CountryCode.TC, Collections.singletonList("Turks and Caicos Islands"));
        mapping.put(CountryCode.TD, Collections.singletonList("Chad"));
        mapping.put(CountryCode.TF, Collections.singletonList("French Southern Territories"));
        mapping.put(CountryCode.TG, Collections.singletonList("Togo"));
        mapping.put(CountryCode.TH, Collections.singletonList("Thailand"));
        mapping.put(CountryCode.TJ, Collections.singletonList("Tajikistan"));
        mapping.put(CountryCode.TK, Collections.singletonList("Tokelau"));
        mapping.put(CountryCode.TL, Collections.singletonList("Timor-Leste"));
        mapping.put(CountryCode.TM, Collections.singletonList("Turkmenistan"));
        mapping.put(CountryCode.TN, Collections.singletonList("Tunisia"));
        mapping.put(CountryCode.TO, Collections.singletonList("Tonga"));
        mapping.put(CountryCode.TR, Collections.singletonList("Turkey"));
        mapping.put(CountryCode.TT, Collections.singletonList("Trinidad and Tobago"));
        mapping.put(CountryCode.TV, Collections.singletonList("Tuvalu"));
        mapping.put(CountryCode.TW, Collections.singletonList("Taiwan, Province of China"));
        mapping.put(CountryCode.TZ, Collections.singletonList("Tanzania, United Republic of"));
        mapping.put(CountryCode.UA, Collections.singletonList("Ukraine"));
        mapping.put(CountryCode.UG, Collections.singletonList("Uganda"));
        mapping.put(CountryCode.UM, Collections.singletonList("United States Minor Outlying Islands"));
        mapping.put(CountryCode.US, Collections.singletonList("United States of America"));
        mapping.put(CountryCode.UY, Collections.singletonList("Uruguay"));
        mapping.put(CountryCode.UZ, Collections.singletonList("Uzbekistan"));
        mapping.put(CountryCode.VA, Collections.singletonList("Holy See"));
        mapping.put(CountryCode.VC, Collections.singletonList("Saint Vincent and the Grenadines"));
        mapping.put(CountryCode.VE, Collections.singletonList("Venezuela, Bolivarian Republic of"));
        mapping.put(CountryCode.VG, Collections.singletonList("Virgin Islands, British"));
        mapping.put(CountryCode.VI, Collections.singletonList("Virgin Islands, U.S."));
        mapping.put(CountryCode.VN, Collections.singletonList("Viet Nam"));
        mapping.put(CountryCode.VU, Collections.singletonList("Vanuatu"));
        mapping.put(CountryCode.WF, Collections.singletonList("Wallis and Futuna"));
        mapping.put(CountryCode.WS, Collections.singletonList("Samoa"));
        mapping.put(CountryCode.YE, Collections.singletonList("Yemen"));
        mapping.put(CountryCode.YT, Collections.singletonList("Mayotte"));
        mapping.put(CountryCode.ZA, Collections.singletonList("South Africa"));
        mapping.put(CountryCode.ZM, Collections.singletonList("Zambia"));
        mapping.put(CountryCode.ZW, Collections.singletonList("Zimbabwe"));
        mapping.put(CountryCode.XK, Collections.singletonList("Kosovo"));
        mapping.put(CountryCode.ZR, Collections.singletonList("Zaire"));

        mapping.put(CountryCode.WEST_BANK_AND_GAZA, Collections.singletonList("West Bank and Gaza"));
        mapping.put(CountryCode.AFRICA, Collections.singletonList("Africa"));
        mapping.put(CountryCode.WESTERN_AFRICA, Collections.singletonList("Western Africa"));
        mapping.put(CountryCode.OECS, Collections.singletonList("OECS Countries"));
        mapping.put(CountryCode.SOUTH_ASIA, Collections.singletonList("South Asia"));

        mapping.put(null, Collections.singletonList(NULL_VALUE));

        return mapping;
    }    

    /**
     * @return mapping for World Bank form types
     */
    public static HashMap<Enum, List<String>> getFormTypeMapping() {
        HashMap<Enum, List<String>> mapping = new HashMap<>();

        mapping.put(PublicationFormType.CONTRACT_AWARD, Arrays.asList("Contract Award", "Goods and Works Award",
            "Small Contracts Award", "Consultant Award"));
        mapping.put(PublicationFormType.CONTRACT_NOTICE, Arrays.asList("General Procurement Notice",
            "Invitation for Bids", "Invitation for Prequalification"));
        mapping.put(PublicationFormType.PRIOR_INFORMATION_NOTICE, Arrays.asList("Request for Expression of Interest"));

        mapping.put(null, Collections.singletonList(NULL_VALUE));

        return mapping;
    }

    /**
     * @return mapping for World Bank regions
     */
    public static HashMap<Enum, List<String>> getRegionMapping() {
        HashMap<Enum, List<String>> mapping =
            CodeTableUtils.enumToMapping(DFIDRegion.class, e -> e.name().replace("_", " "));

        mapping.put(null, Collections.singletonList(NULL_VALUE));
        
        return mapping;
    }

    /**
     * @return mapping for World Bank tender supply types
     */
    public static HashMap<Enum, List<String>> getSupplyTypeMapping() {
        List<String> types = Arrays.asList("Works, Infrastructure", "Works, Maintenance and Rehabilitation",
            "Works, Installation", "Equipment, Electrical", "Works, Building", "Management /Technical Advice",
            "Equipment, Transportation", "Equipment Information Technology", "Project Management",
            "Pharmaceuticals, Medical Products", "Construction Supervision", "Works, Industrial Plants",
            "Miscellaneous", "Equipment, Energy Exploration, Productio", "Equipment, Medical",
            "Equipment, Water Supply and Sewerage", "Equipment, Educational", "Health Services",
            "Equipment, Telecommunications", "Feasibility Studies", "Institution building", "Training Services",
            "Other Operational Services", "Raw Materials, Chemicals, Commodities", "Design Services",
            "Materials, Construction", "Equipment, Construction", "Procurement Technical Assistance",
            "Equipment, Mechanical", "Sector Studies", "Equipment, Industrial Plants", "Equipment, Agricultural",
            "Administrative Services", "Policy and Strategy", "Education Services", "ICT/Telecommunications Services",
            "Financial Services", "Agricultural Inputs", "Audit Services", "Research/Development Services",
            "Environmental Studies", "Equipment, Mining", "Data Processing Services", "Awareness Campaigns",
            "Hardware/Software Services", "Social Studies", "Services, Others", "Geological/Geophysical Services",
            "Services, Small Works", "Legal Advisory Services", "Statistical Services", "Survey Services",
            "Governance Services", "Safety Studies", "Services, Operational Admin", "Aerial Photography Services",
            "Satellite Imaging Services", "Not assigned", "Drilling Services", "Implementation Activity",
            "Consultant Services", "Civil Works", "Goods", "Non-consulting Services");

        HashMap<Enum, List<String>> mapping = new HashMap<>();
        mapping.put(TenderSupplyType.WORKS, new ArrayList<>());
        mapping.put(TenderSupplyType.SERVICES, new ArrayList<>());
        mapping.put(TenderSupplyType.SUPPLIES, new ArrayList<>());
                
        for (String type : types) {
            if (type.contains("Works")) {
                mapping.get(TenderSupplyType.WORKS).add(type);
            } else if (type.contains("Equipment")) {
                mapping.get(TenderSupplyType.SUPPLIES).add(type);
            } else {
                mapping.get(TenderSupplyType.SERVICES).add(type);
            }
        }

        mapping.put(null, Collections.singletonList(NULL_VALUE));

        return mapping;
    }

    /**
     * @return mapping for World Bank project status
     */
    public static HashMap<Enum, List<String>> getProjectStatusMapping() {
        HashMap<Enum, List<String>> mapping = CodeTableUtils.enumToMapping(ProjectStatus.class);

        mapping.put(null, Collections.singletonList(NULL_VALUE));

        return mapping;
    }

    /**
     * @return mapping for World Bank project lending instrument types
     */
    public static HashMap<Enum, List<String>> getLendingInstrumentTypeMapping() {
        HashMap<Enum, List<String>> mapping = CodeTableUtils.enumToMapping(LendingInstrumentType.class);

        mapping.put(null, Collections.singletonList(NULL_VALUE));

        return mapping;
    }

    /**
     * @return mapping for World Bank project lending instruments
     */
    public static HashMap<Enum, List<String>> getLendingInstrumentMapping() {
        HashMap<Enum, List<String>> mapping = CodeTableUtils.enumToMapping(LendingInstrument.class,
            e -> e.name().replace("_", " "));

        mapping.put(null, Collections.singletonList(NULL_VALUE));

        return mapping;
    }
}
