package org.intelehealth.app.partogram;

import org.intelehealth.app.partogram.model.ParamInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PartogramConstants {
    public static String DROPDOWN_SINGLE_SELECT_TYPE = "A";
    public static String DROPDOWN_MULTI_SELECT_TYPE = "B";

    public static String INPUT_TXT_TYPE = "C";
    public static String INPUT_TXT_2_CHR_TYPE = "D";

    public static String INPUT_INT_1_DIG_TYPE = "E";
    public static String INPUT_INT_2_DIG_TYPE = "F";
    public static String INPUT_INT_3_DIG_TYPE = "G";
    public static String INPUT_DOUBLE_4_DIG_TYPE = "H";


    public static String[] SECTION_LIST = {
            "Supportive care",
            "BABY",
            "Woman",
            "Labour Progress",
            "Medication",
            "Shared Decision Making",
            "Initials"};

    public static TreeMap<String, List<ParamInfo>> getSectionParamInfoMasterMap() {
        TreeMap<String, List<ParamInfo>> sectionParamInfoMap = new TreeMap<>();
        //Supportive care
        List<ParamInfo> stringList = new ArrayList<ParamInfo>();
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[0]);
        paramInfo.setParamName("Companion");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"Yes", "No", "Woman declines"});
        paramInfo.setValues(new String[]{"Y", "N", "D"});
        paramInfo.setConceptUUID("5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[0]);
        paramInfo.setParamName("Pain relief");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"Yes", "No", "Woman refuses to receive pain releif"});
        paramInfo.setValues(new String[]{"Y", "N", "D"});
        paramInfo.setConceptUUID("9d313f72-538f-11e6-9cfe-86f436325720");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[0]);
        paramInfo.setParamName("Oral fluid");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"Yes", "No", "Woman declines"});
        paramInfo.setValues(new String[]{"Y", "N", "D"});
        paramInfo.setConceptUUID("9d31451b-538f-11e6-9cfe-86f436325720");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[0]);
        paramInfo.setParamName("Posture");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"supine", "Mobile"});
        paramInfo.setValues(new String[]{"SP", "MO"});
        paramInfo.setConceptUUID("9d3148b1-538f-11e6-9cfe-86f436325720");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        sectionParamInfoMap.put(SECTION_LIST[0], stringList);

        //BABY
        stringList = new ArrayList<>();

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[1]);
        paramInfo.setParamName("Baseline FHR");
        paramInfo.setParamDateType(INPUT_INT_3_DIG_TYPE);
        paramInfo.setHalfHourField(true);
        paramInfo.setFifteenMinField(true);
        paramInfo.setConceptUUID("9d315400-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[1]);
        paramInfo.setParamName("FHR deceleration");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"No", "Early", "Late", "Variable"});
        paramInfo.setValues(new String[]{"N", "E", "L", "V"});
        paramInfo.setHalfHourField(true);
        paramInfo.setFifteenMinField(true);
        paramInfo.setConceptUUID("9d31573c-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[1]);
        paramInfo.setParamName("Amniotic fluid meconium");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"Intact", "clear", "Meconium-stained fluid (Non-significant)", "Meconium-stained fluid (Medium)",
                "Meconium-stained fluid (Thick)", "Blood stained"});
        paramInfo.setValues(new String[]{"I", "C", "M+", "M++", "M+++", "B"});
        paramInfo.setConceptUUID("9d3160a6-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[1]);
        paramInfo.setParamName("Fetal position");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"Any occiput anterior position", "Any occiput posterior position", "Any occiput transverse position"});
        paramInfo.setValues(new String[]{"OA", "OP", "OT"});
        paramInfo.setConceptUUID("9d316387-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[1]);
        paramInfo.setParamName("Caput");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"None", "+(Marked)", "++(Marked)", "+++(Marked)"});
        paramInfo.setValues(new String[]{"N", "+", "++", "+++"});
        paramInfo.setConceptUUID("9d316761-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[1]);
        paramInfo.setParamName("Moulding");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"None", "Sutures apposed", "Sutures overlapped but reducible", "Sutures overlapped and not reducible"});
        paramInfo.setValues(new String[]{"N", "+", "++", "+++"});
        paramInfo.setConceptUUID("9d316823-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        sectionParamInfoMap.put(SECTION_LIST[1], stringList); //BABY

        //Woman
        stringList = new ArrayList<>();

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[2]);
        paramInfo.setParamName("Pulse");
        paramInfo.setParamDateType(INPUT_INT_3_DIG_TYPE);
        paramInfo.setConceptUUID("5087AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[2]);
        paramInfo.setParamName("Systolic BP");
        paramInfo.setParamDateType(INPUT_INT_3_DIG_TYPE);
        paramInfo.setConceptUUID("5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[2]);
        paramInfo.setParamName("Diastolic BP");
        paramInfo.setParamDateType(INPUT_INT_3_DIG_TYPE);
        paramInfo.setConceptUUID("5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[2]);
        paramInfo.setParamName("Temperature(C)"); // in centigrade i.e. C
        paramInfo.setParamDateType(INPUT_DOUBLE_4_DIG_TYPE);
        paramInfo.setConceptUUID("5088AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[2]);
        paramInfo.setParamName("Urine protein");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"No proteinuria", "Trace of proteinuria (+)", "Trace of proteinuria (++)", "Trace of proteinuria (+++)"});
        paramInfo.setValues(new String[]{"P", "P1", "P2", "P3"});
        paramInfo.setConceptUUID("9d3168a7-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        sectionParamInfoMap.put(SECTION_LIST[2], stringList);//Woman

        //Labour Progress
        stringList = new ArrayList<>();

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[3]);
        paramInfo.setParamName("Contractions per 10 min");
        paramInfo.setParamDateType(INPUT_INT_1_DIG_TYPE);
        paramInfo.setHalfHourField(true);
        paramInfo.setFifteenMinField(true);
        paramInfo.setConceptUUID("9d316929-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[3]);
        paramInfo.setParamName("Duration of contractions");
        paramInfo.setParamDateType(INPUT_INT_2_DIG_TYPE);
        paramInfo.setHalfHourField(true);
        paramInfo.setFifteenMinField(true);
        paramInfo.setConceptUUID("9d3169af-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[3]);
        paramInfo.setParamName("Cervix Plot[X]");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"10", "9", "8", "7", "6", "5"});
        paramInfo.setValues(new String[]{"10", "9", "8", "7", "6", "5"});
        paramInfo.setConceptUUID("9d316ab5-538f-11e6-9cfe-86f436325720");
        paramInfo.setFifteenMinField(true);
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[3]);
        paramInfo.setParamName("Descent Plot[O]");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"5", "4", "3", "2", "1", "0"});
        paramInfo.setValues(new String[]{"5", "4", "3", "2", "1", "0"});
        paramInfo.setConceptUUID("9d316d41-538f-11e6-9cfe-86f436325720");
        stringList.add(paramInfo);

        sectionParamInfoMap.put(SECTION_LIST[3], stringList);//Labour Progress

        //Medication
        stringList = new ArrayList<>();

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[4]);
        paramInfo.setParamName("Oxytocin (U/L, drops/min)");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"Yes", "No"});
        paramInfo.setValues(new String[]{"Y", "N"});
        paramInfo.setConceptUUID("9d316d82-538f-11e6-9cfe-86f436325720");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[4]);
        paramInfo.setParamName("Medicine");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"Yes", "No"});
        paramInfo.setValues(new String[]{"Y", "N"});
        paramInfo.setConceptUUID("c38c0c50-2fd2-4ae3-b7ba-7dd25adca4ca");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[4]);
        paramInfo.setParamName("IV fluids");
        paramInfo.setParamDateType(DROPDOWN_SINGLE_SELECT_TYPE);
        paramInfo.setOptions(new String[]{"Yes", "No"});
        paramInfo.setValues(new String[]{"Y", "N"});
        paramInfo.setConceptUUID("98c5881f-b214-4597-83d4-509666e9a7c9");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        sectionParamInfoMap.put(SECTION_LIST[4], stringList);//Medication

        //Shared Decision Making
        stringList = new ArrayList<>();

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[5]);
        paramInfo.setParamName("Assessment");
        paramInfo.setParamDateType(INPUT_TXT_TYPE);
        paramInfo.setConceptUUID("67a050c1-35e5-451c-a4ab-fff9d57b0db1");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[5]);
        paramInfo.setParamName("Plan");
        paramInfo.setParamDateType(INPUT_TXT_TYPE);
        paramInfo.setConceptUUID("162169AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        paramInfo.setOnlyOneHourField(true);
        stringList.add(paramInfo);

        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[5]);
        paramInfo.setParamName("Supervisor Doctor");
        paramInfo.setParamDateType(INPUT_TXT_TYPE);
        paramInfo.setConceptUUID("7a9cb7bc-9ab9-4ff0-ae82-7a1bd2cca93e");
        stringList.add(paramInfo);

        sectionParamInfoMap.put(SECTION_LIST[5], stringList);//Shared Decision Making

        //Initials
        stringList = new ArrayList<>();
        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName(SECTION_LIST[6]);
        paramInfo.setParamName("Initial");
        paramInfo.setParamDateType(INPUT_TXT_TYPE);
        paramInfo.setConceptUUID("165171AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        stringList.add(paramInfo);

        sectionParamInfoMap.put(SECTION_LIST[6], stringList);//Initials

        //Actions
        stringList = new ArrayList<>();
        paramInfo = new ParamInfo();
        paramInfo.setParamSectionName("Action");
        paramInfo.setParamName("Birth Outcome");
        paramInfo.setParamDateType(INPUT_TXT_TYPE);
        paramInfo.setConceptUUID("23601d71-50e6-483f-968d-aeef3031346d");
        stringList.add(paramInfo);
        sectionParamInfoMap.put("Action", stringList);//Actions

        return sectionParamInfoMap;
    }
}
