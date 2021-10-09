/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.intelehealth.ekalhelpline.utilities;

import android.widget.Spinner;

import java.io.File;
import java.util.List;

import org.intelehealth.ekalhelpline.app.IntelehealthApplication;

public final class StringUtils {
    private static final String NULL_AS_STRING = "null";
    private static final String SPACE_CHAR = " ";

    public static boolean notNull(String string) {
        return null != string && !NULL_AS_STRING.equals(string.trim());
    }

    public static boolean isBlank(String string) {
        return null == string || SPACE_CHAR.equals(string);
    }

    public static boolean notEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static String unescapeJavaString(String st) {

        StringBuilder sb = new StringBuilder(st.length());

        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == st.length() - 1) ? '\\' : st
                        .charAt(i + 1);
                // Octal escape?
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code += st.charAt(i + 1);
                        i++;
                        if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code += st.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code, 8));
                    continue;
                }
                switch (nextChar) {
                    case '\\':
                        ch = '\\';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    // Hex Unicode: u????
                    case 'u':
                        if (i >= st.length() - 5) {
                            ch = 'u';
                            break;
                        }
                        int code = Integer.parseInt(
                                "" + st.charAt(i + 2) + st.charAt(i + 3)
                                        + st.charAt(i + 4) + st.charAt(i + 5), 16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                    default:
                        // Do nothing
                        break;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    public static String getValue(String value) {
        String val = "";
        if (value != null)
            val = value;
        SessionManager sessionManager = new SessionManager(IntelehealthApplication.getAppContext());
        if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
            val = switch_hi_numberRelation_edit(val);
        }
        else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
            val = switch_mr_numberRelation_edit(val);
        }
        return val;

    }

    public static String getValue1(String value) {
        String val = " ";
        if (value != null)
            val = value;
        SessionManager sessionManager = new SessionManager(IntelehealthApplication.getAppContext());
        if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
            val = switch_hi_helplineInfo_edit(val) ;
            val = switch_hi_callerRelation_edit(val);
            val = switch_hi_numberRelation_edit(val);
        }
        else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
            val = switch_mr_helplineInfo_edit(val) ;
            val = switch_mr_callerRelation_edit(val);
            val = switch_mr_numberRelation_edit(val);
        }
            return val;

    }

    public static String getProvided(Spinner spinner) {
        String val = "";
        if (spinner.getSelectedItemPosition() == 0)
            val = "Not provided";

        else if (spinner.getSelectedItem() == null) {
            val = "Not provided";
        } else {
            val = spinner.getSelectedItem().toString();
        }

        SessionManager sessionManager = new SessionManager(IntelehealthApplication.getAppContext());
        if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
            val = switch_hi_caste(val);
            val = switch_hi_economic(val);
            val = switch_hi_education(val);
            val = switch_hi_curosityResolution(val);
        }

        return val;
    }

    private static String switch_hi_curosityResolution(String val) {
        switch (val) {
            case "अलग भाषा":
                val = "Different Language";
                break;
            case "भिन्न लिंग":
                val = "Different Gender";
                break;
            case "अन्य":
                val = "Other";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String getCheckbox_Hi_En_Hi(String checkbox_text) {
        String val = "";

        SessionManager sessionManager = new SessionManager(IntelehealthApplication.getAppContext());
        if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
            switch (checkbox_text) {
                case "जवाब देने के लिए मना कर दिया":
                    val = "Declined to answer";
                    break;
                default:
                    return val;
            }
        } else {
            val = "Declined to answer";
        }

        return val;
    }

    public static String getSpinnerHi_En(Spinner spinner) {
        String val = "";
        val = spinner.getSelectedItem().toString();

        SessionManager sessionManager = new SessionManager(IntelehealthApplication.getAppContext());
        if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
            val = switch_hi_en_occupation(val);
            val = switch_hi_en_bankaccount(val);
            val = switch_hi_en_mobile(val);
            val = switch_hi_en_whatsapp(val);
            val = switch_hi_en_sourcewater(val);
            val = switch_hi_en_watersafe(val);
            val = switch_hi_en_wateravail(val);
            val = switch_hi_en_toiletfacil(val);
            val = switch_hi_en_housestructure(val);
        }
        return val;
    }


    public static String switch_hi_housestructure_edit(String val) {
        switch (val) {
            case "Kutcha House":
                val = "कच्चा घर";
                break;
            case "Pakka House":
                val = "पक्का घर";
                break;
            case "Homeless":
                val = "घर नहीं है";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_en_housestructure(String val) {
        switch (val) {
            case "कच्चा घर":
                val = "Kutcha House";
                break;
            case "पक्का घर":
                val = "Pakka House";
                break;
            case "घर नहीं है":
                val = "Homeless";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_toiletfacil_edit(String val) {
        switch (val) {
            case "Declined to answer":
                val = "जवाब देने के लिए मना कर दिया";
                break;
            case "No facility /uses open space or field":
                val = "कोई सुविधा नहीं / खुली जगह या क्षेत्र का उपयोग करते हैं";
                break;
            case "Own toilet":
                val = "खुद का शौचालय";
                break;
            case "Community toilet":
                val = "सामुदायिक शौचालय";
                break;
            case "Shared toilet with other household":
                val = "अन्य घर के साथ साझा शौचालय";
                break;
            case "Other [Enter]":
                val = "अन्य [दर्ज करें]";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_en_toiletfacil(String val) {
        switch (val) {
            case "जवाब देने के लिए मना कर दिया":
                val = "Declined to answer";
                break;
            case "कोई सुविधा नहीं / खुली जगह या क्षेत्र का उपयोग करते हैं":
                val = "No facility /uses open space or field";
                break;
            case "खुद का शौचालय":
                val = "Own toilet";
                break;
            case "सामुदायिक शौचालय":
                val = "Community toilet";
                break;
            case "अन्य घर के साथ साझा शौचालय":
                val = "Shared toilet with other household";
                break;
            case "अन्य [दर्ज करें]":
                val = "Other [Enter]";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_en_wateravail(String val) {
        switch (val) {
            case "जवाब देने के लिए मना कर दिया":
                val = "Declined to answer";
                break;
            case "हाँ":
                val = "Yes";
                break;
            case "नहीं":
                val = "No";
                break;
            case "पता नहीं":
                val = "Don\'t know";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_en_watersafe(String val) {
        switch (val) {
            case "जवाब देने के लिए मना कर दिया":
                val = "Declined to answer";
                break;
            case "कुछ भी नहीं":
                val = "Nothing";
                break;
            case "उबलना":
                val = "Boil";
                break;
            case "अलम":
                val = "Alum";
                break;
            case "ब्लीच / क्लोरीन गोलियाँ जोड़ें":
                val = "Add Bleach/Chlorine tablets";
                break;
            case "कपड़े के माध्यम":
                val = "Strain through cloth";
                break;
            case "पानी फिल्टर (सिरेमिक / रेत / समग्र) आदि का उपयोग करें":
                val = "Use water filter(ceramic/sand/composite)etc";
                break;
            case "इलेक्ट्रॉनिक फ़िल्टर का उपयोग करें":
                val = "Use electronic filter";
                break;
            case "अन्य [दर्ज करें]":
                val = "Other[Enter]";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_en_sourcewater(String val) {
        switch (val) {
            case "जवाब देने के लिए मना कर दिया":
                val = "Declined to answer";
                break;
            case "चापाकल/हैण्ड पंप":
                val = "Chapakal/Hand Pump";
                break;
            case "कुंवा":
                val = "Wells";
                break;
            case "बोरिंग":
                val = "Boring";
                break;
            case "नदी/तालाब":
                val = "Rivers/ponds";
                break;
            case "टैंकर का पानी":
                val = "Tanker water ";
                break;
            case "कोई और":
                val = "Any other";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_en_occupation(String val) {
        switch (val) {
            case "जवाब देने के लिए मना कर दिया":
                val = "Declined to answer";
                break;
            case "सरकारी नौकरी":
                val = "Government job";
                break;
            case "बड़ा या मध्यम उद्योग":
                val = "Large scale to medium scale industry";
                break;
            case "निजी क्षेत्र में नौकरी":
                val = "Professional job in private sector";
                break;
            case "छोटा उद्योग":
                val = "Small scale industry";
                break;
            case "बड़ी दूकान के मालिक":
                val = "Big shop owner";
                break;
            case "तकनीशियन":
                val = "Technician/craftsman";
                break;
            case "छोटे दूकान का मालिक":
                val = "Small shop owner";
                break;
            case "बड़ा किसान":
                val = "Large scale farmer";
                break;
            case "दैनिक मजदूर":
                val = "Daily wage earner";
                break;
            case "छोटे किसान/ दुसरे के खेत में काम करने वाले":
                val = "Small scale farmer/farm worker";
                break;
            case "बेरोजगार":
                val = "Unemployed";
                break;
            case "ग्रहिणी":
                val = "Housewife";
                break;
            case "अन्य कुशलता (ड्राईवर,राज मिस्त्री)":
                val = "Other skills (driver,mason etc)";
                break;
            case "वर्णन करे":
                val = "[Describe]";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_en_bankaccount(String val) {
        switch (val) {
            case "जवाब देने के लिए मना कर दिया":
                val = "Declined to answer";
                break;
            case "हाँ":
                val = "Yes";
                break;
            case "नहीं":
                val = "No";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_en_mobile(String val) {
        switch (val) {
            case "साधारण फोन":
                val = "Basic Phone";
                break;
            case "स्मार्टफोन":
                val = "Smartphone";
                break;
            case "मोबाइल फोन नहीं है":
                val = "Does not own mobile phone";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_en_whatsapp(String val) {
        switch (val) {
            case "हाँ":
                val = "Yes";
                break;
            case "नहीं":
                val = "No";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_education(String val) {
        switch (val) {
            case "अशिक्षित":
                val = "Illiterate";
                break;
            case "प्रथम":
                val = "Primary";
                break;
            case "माध्यमिक":
                val = "Secondary";
                break;
            case "उच्च माध्यमिक":
                val = "Higher Secondary";
                break;
            case "स्नातक और उच्चतर":
                val = "Graduation & Higher";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_education_edit(String val) {
        switch (val) {
            case "Illiterate":
                val = "अशिक्षित";
                break;
            case "Primary":
                val = "प्रथम";
                break;
            case "Secondary":
                val = "माध्यमिक";
                break;
            case "Higher Secondary":
                val = "उच्च माध्यमिक";
                break;
            case "Graduation & Higher":
                val = "स्नातक और उच्चतर";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_economic(String val) {
        switch (val) {
            case "गरीबी रेखा से ऊपर":
                val = "APL";
                break;
            case "गरीबी रेखा से नीचे":
                val = "BPL";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_economic_edit(String val) {
        switch (val) {
            case "APL":
                val = "गरीबी रेखा से ऊपर";
                break;
            case "BPL":
                val = "गरीबी रेखा से नीचे";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_helplineInfo_edit(String val) {
        switch (val) {
            case "सामुदायिक चैंपियन":
                val = "Community Champion";
                break;
            case "हेल्पलाइन एजेंट":
                val = "Helpline Agent";
                break;
            case "मित्र / परिवार से रेफ़रल":
                val = "Referral from friend / family";
                break;
            case "एसएमएस":
                val = "SMS";
                break;
            case "आउटगोइंग फोन कॉल":
                val = "Outgoing Phone call";
                break;
            case "सामुदायिक कार्यक्रम":
                val = "Community Event";
                break;
            case "पोस्टर/फ्लायर":
                val = "Poster / flyer";
                break;
            case "रेडिओ/ मल्टीमीडिया":
                val = "Radio/ Multimedia";
                break;
            case "प्रिंट":
                val = "Print";
                break;
            case "अन्य":
                val = "Other";
                break;
            default:
                return val;
        }
        return val;
    }
    public static String switch_hi_helplineInfo(String val) {
        switch (val) {
            case "Community Champion":
                val = "सामुदायिक चैंपियन";
                break;
            case "Helpline Agent":
                val = "हेल्पलाइन एजेंट";
                break;
            case "Referral from friend / family":
                val = "मित्र / परिवार से रेफ़रल";
                break;
            case "SMS":
                val = "एसएमएस";
                break;
            case "Outgoing Phone call":
                val = "आउटगोइंग फोन कॉल";
                break;
            case "Community Event":
                val = "सामुदायिक कार्यक्रम";
                break;
            case "Poster / flyer":
                val = "पोस्टर/फ्लायर";
                break;
            case "Radio/ Multimedia":
                val = "रेडिओ/ मल्टीमीडिया";
                break;
            case "Print":
                val = "प्रिंट";
                break;
            case "Other":
                val = "अन्य";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_endFollowUp_edit(String val) {
        switch (val) {
            case "चयन करें":
                val = "Select";
                break;
            case "ठीक हो गए":
                val = "Recovered";
                break;
            case "रेफ़र कर दिया गया है":
                val = "Referred";
                break;
            case "मृत्यु हो गई है":
                val = "Died";
                break;
            case "फॉलो अप करना रह गया":
                val = "Loss to Follow up";
                break;
            case "फ़ॉलो अप के लिए मना कर रहे है":
                val = "Refusing Follow up";
                break;
            case "अनुपलब्ध":
                val = "Not Applicable";
                break;
            case "अन्य":
                val = "Other";
                break;
            case "टीएलडी बंद":
                val = "TLD Closed";
                break;
            case "टीएलडी समाधान":
                val = "TLD Resolved";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_callNoteValue_edit(String val) {
        switch (val) {
            case "कॉलर ने कॉल बैक के लिए अनुरोध किया":
                val = "Caller requested for call back";
                break;
            case "कॉल ड्रॉप हो गई":
                val = "Call Dropped";
                break;
            case "कॉल में डिस्टर्बन्स था":
                val = "Call Disturbed";
                break;
            case "डॉक्टर समाधान":
                val = "Doctor Resolution";
                break;
            case "डॉक्टर फ़ॉलो अप (डॉक्टर द्वारा सुझावित)":
                val = "Doctor Follow up (On advice by the doctor)";
                break;
            case "आउट्रीच कॉल":
                val = "Outreach call";
                break;
            case "जेनरल फ़ॉलो अप":
                val = "General Follow up";
                break;
            case "टीएलडी पहला प्रयास":
                val = "TLD 1st Attempt";
                break;
            case "टीएलडी दूसरा प्रयास":
                val = "TLD 2nd Attempt";
                break;
            case "टीएलडी तीसरा प्रयास":
                val = "TLD 3rd Attempt";
                break;
            case "अन्य":
                val = "Other";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_caller_language (String val)
    {
        switch (val) {
            case "English":
                val = "अंग्रेज़ी";
                break;
            case "Hindi":
                val = "हिंदी";
                break;
            case "Marathi":
                val = "मराठी";
                break;
            case "Kannada":
                val = "कन्नड़";
                break;
            case "Tamil":
                val = "तामिल";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_caller_language_edit(String val) {
        switch (val) {
            case "अंग्रेज़ी":
                val = "English";
                break;
            case "हिंदी":
                val = "Hindi";
                break;
            case "मराठी":
                val = "Marathi";
                break;
            case "कन्नड़":
                val = "Kannada";
                break;
            case "तामिल":
                val = "Tamil";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_mr_caller_language (String val)
    {
        switch (val) {
            case "English":
                val = "इंग्रजी";
                break;
            case "Hindi":
                val = "हिंदी";
                break;
            case "Marathi":
                val = "मराठी";
                break;
            case "Kannada":
                val = "कन्नड";
                break;
            case "Tamil":
                val = "तमिळ";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_mr_caller_language_edit(String val) {
        switch (val) {
            case "इंग्रजी":
                val = "English";
                break;
            case "हिंदी":
                val = "Hindi";
                break;
            case "मराठी":
                val = "Marathi";
                break;
            case "कन्नड":
                val = "Kannada";
                break;
            case "तमिळ":
                val = "Tamil";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_callerRelation(String val) {
        switch (val) {
            case "Patient themselves":
                val = "रोगी स्वयं";
                break;
            case "Mother":
                val = "मां";
                break;
            case "Father":
                val = "पिता";
                break;
            case "Spouse":
                val = "पति या पत्नी";
                break;
            case "Son":
                val = "बेटा";
                break;
            case "Daughter":
                val = "बेटी";
                break;
            case "Sister":
                val = "बहन";
                break;
            case "Brother":
                val = "भाई";
                break;
            case "Other Relative":
                val = "अन्य रिश्तेदार";
                break;
            default:
                return val;
        }
        return val;
    }
    public static String switch_hi_callerRelation_edit(String val) {
        switch (val) {
            case "रोगी स्वयं":
                val = "Patient themselves";
                break;
            case "मां":
                val = "Mother";
                break;
            case "पिता":
                val = "Father";
                break;
            case "पति या पत्नी":
                val = "Spouse";
                break;
            case "बेटा":
                val = "Son";
                break;
            case "बेटी":
                val = "Daughter";
                break;
            case "बहन":
                val = "Sister";
                break;
            case "भाई":
                val = "Brother";
                break;
            case "अन्य रिश्तेदार":
                val = "Other Relative";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_numberRelation(String val) {
        switch (val) {
            case "Self":
                val = "रोगी स्वयं";
                break;
            case "Community Champion":
                val = "सामुदायिक चैंपियन";
                break;
            case "Friends":
                val = "मित्र";
                break;
            case "Relatives":
                val = "रिश्तेदार";
                break;
            case "Neighbour":
                val = "पड़ोसी";
                break;
            case "Others":
                val = "अन्य";
                break;
            default:
                return val;
        }
        return val;
    }
    public static String switch_hi_numberRelation_edit(String val) {
        switch (val) {
            case "रोगी स्वयं":
                val = "Self";
                break;
            case "सामुदायिक चैंपियन":
                val = "Community Champion";
                break;
            case "मित्र":
                val = "Friends";
                break;
            case "रिश्तेदार":
                val = "Relatives";
                break;
            case "पड़ोसी":
                val = "Neighbour";
                break;
            case "अन्य":
                val = "Others";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_mr_numberRelation(String val) {
        switch (val) {
            case "Self":
                val = "रुग्ण स्वतः";
                break;
            case "Community Champion":
                val = "समुदाय विजेता";
                break;
            case "Friends":
                val = "मित्रांनो";
                break;
            case "Relatives":
                val = "नातेवाईक";
                break;
            case "Neighbour":
                val = "शेजारी";
                break;
            case "Others":
                val = "इतर";
                break;
            default:
                return val;
        }
        return val;
    }
    public static String switch_mr_numberRelation_edit(String val) {
        switch (val) {
            case "रुग्ण स्वतः":
                val = "Self";
                break;
            case "समुदाय विजेता":
                val = "Community Champion";
                break;
            case "मित्रांनो":
                val = "Friends";
                break;
            case "नातेवाईक":
                val = "Relatives";
                break;
            case "शेजारी":
                val = "Neighbour";
                break;
            case "इतर":
                val = "Others";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_mr_helplineInfo_edit(String val) {
        switch (val) {
            case "समुदाय विजेता":
                val = "Community Champion";
                break;
            case "हेल्पलाइन एजेंट":
                val = "Helpline Agent";
                break;
            case "मित्र / कुटुंबाकडून संदर्भ":
                val = "Referral from friend / family";
                break;
            case "एसएमएस":
                val = "SMS";
                break;
            case "आउटगोइंग फोन कॉल":
                val = "Outgoing Phone call";
                break;
            case "सामुदायिक कार्यक्रम":
                val = "Community Event";
                break;
            case "पोस्टर/फ्लायर":
                val = "Poster / flyer";
                break;
            case "रेडिओ/ मल्टीमीडिया":
                val = "Radio/ Multimedia";
                break;
            case "प्रिंट":
                val = "Print";
                break;
            case "इतर":
                val = "Other";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_subs_response(String val) {
        switch (val) {
            case "User has been subscribed.":
                val = "उपयोगकर्ता की सदस्यता ली गई है।";
                break;
            case "User has already Subscribed":
                val = "उपयोगकर्ता पहले ही सदस्यता ले चुका है";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_mr_helplineInfo(String val) {
        switch (val) {
            case "Community Champion":
                val = "समुदाय विजेता";
                break;
            case "Helpline Agent":
                val = "हेल्पलाइन एजेंट";
                break;
            case "Referral from friend / family":
                val = "मित्र / कुटुंबाकडून संदर्भ";
                break;
            case "SMS":
                val = "एसएमएस";
                break;
            case "Outgoing Phone call":
                val = "आउटगोइंग फोन कॉल";
                break;
            case "Community Event":
                val = "सामुदायिक कार्यक्रम";
                break;
            case "Poster / flyer":
                val = "पोस्टर/फ्लायर";
                break;
            case "Radio/ Multimedia":
                val = "रेडिओ/ मल्टीमीडिया";
                break;
            case "Print":
                val = "प्रिंट";
                break;
            case "Other":
                val = "इतर";
                break;
            default:
                return val;
        }
        return val;
    }
    public static String switch_mr_callerRelation(String val) {
        switch (val) {
            case "Patient themselves":
                val = "रुग्ण स्वतः";
                break;
            case "Mother":
                val = "आई";
                break;
            case "Father":
                val = "वडील";
                break;
            case "Spouse":
                val = "जोडीदार";
                break;
            case "Son":
                val = "पुत्र";
                break;
            case "Daughter":
                val = "मुलगी";
                break;
            case "Sister":
                val = "बहीण";
                break;
            case "Brother":
                val = "भाऊ";
                break;
            case "Other Relative":
                val = "इतर नातेवाईक";
                break;
            default:
                return val;
        }
        return val;
    }
    public static String switch_mr_callerRelation_edit(String val) {
        switch (val) {
            case "रुग्ण स्वतः":
                val = "Patient themselves";
                break;
            case "आई":
                val = "Mother";
                break;
            case "वडील":
                val = "Father";
                break;
            case "जोडीदार":
                val = "Spouse";
                break;
            case "पुत्र":
                val = "Son";
                break;
            case "मुलगी":
                val = "Daughter";
                break;
            case "बहीण":
                val = "Sister";
                break;
            case "भाऊ":
                val = "Brother";
                break;
            case "इतर नातेवाईक":
                val = "Other Relative";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_caste(String val) {
        switch (val) {
            case "सामान्य":
                val = "General";
                break;
            case "अन्य पिछड़ा वर्ग":
                val = "OBC";
                break;
            case "अनुसूचित जाति":
                val = "SC";
                break;
            case "अनुसूचित जनजाति":
                val = "ST";
                break;
            case "अन्य":
                val = "others";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String switch_hi_bankaccount_edit(String val) {
        switch (val) {
            case "Declined to answer":
                val = "जवाब देने के लिए मना कर दिया";
                break;
            case "Yes":
                val = "हाँ";
                break;
            case "No":
                val = "नहीं";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_mobiletype_edit(String val) {
        switch (val) {
            case "Basic Phone":
                val = "साधारण फोन";
                break;
            case "Smartphone":
                val = "स्मार्टफोन";
                break;
            case "Does not own mobile phone":
                val = "मोबाइल फोन नहीं है";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_watersource_edit(String val) {
        switch (val) {
            case "Declined to answer":
                val = "जवाब देने के लिए मना कर दिया";
                break;
            case "Chapakal/Hand Pump":
                val = "चापाकल/हैण्ड पंप";
                break;
            case "Wells":
                val = "कुंवा";
                break;
            case "Boring":
                val = "बोरिंग";
                break;
            case "Rivers/ponds":
                val = "नदी/तालाब";
                break;
            case "Tanker water":
                val = "टैंकर का पानी";
                break;
            case "Any other":
                val = "कोई और";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_wateravail_edit(String val) {
        switch (val) {
            case "Declined to answer":
                val = "जवाब देने के लिए मना कर दिया";
                break;
            case "Yes":
                val = "हाँ";
                break;
            case "No":
                val = "नहीं";
                break;
            case "Don\'t know":
                val = "पता नहीं";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_watersafe_edit(String val) {
        switch (val) {
            case "Declined to answer":
                val = "जवाब देने के लिए मना कर दिया";
                break;
            case "Nothing":
                val = "कुछ भी नहीं";
                break;
            case "Boil":
                val = "उबलना";
                break;
            case "Alum":
                val = "अलम";
                break;
            case "Add Bleach/Chlorine tablets":
                val = "ब्लीच / क्लोरीन गोलियाँ जोड़ें";
                break;
            case "Strain through cloth":
                val = "कपड़े के माध्यम";
                break;
            case "Use water filter(ceramic/sand/composite)etc":
                val = "पानी फिल्टर (सिरेमिक / रेत / समग्र) आदि का उपयोग करें";
                break;
            case "Use electronic filter":
                val = "इलेक्ट्रॉनिक फ़िल्टर का उपयोग करें";
                break;
            case "Other[Enter]":
                val = "अन्य [दर्ज करें]";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_whatsapp_edit(String val) {
        switch (val) {
            case "Yes":
                val = "हाँ";
                break;
            case "No":
                val = "नहीं";
                break;
            default:
                return val;
        }
        return val;
    }


    public static String switch_hi_occupation_edit(String val) {
        switch (val) {
            case "Declined to answer":
                val = "जवाब देने के लिए मना कर दिया";
                break;
            case "Government job":
                val = "सरकारी नौकरी";
                break;
            case "Large scale to medium scale industry":
                val = "बड़ा या मध्यम उद्योग";
                break;
            case "Professional job in private sector":
                val = "निजी क्षेत्र में नौकरी";
                break;
            case "Small scale industry":
                val = "छोटा उद्योग";
                break;
            case "Big shop owner":
                val = "बड़ी दूकान के मालिक";
                break;
            case "Technician/craftsman":
                val = "तकनीशियन";
                break;
            case "Small shop owner":
                val = "छोटे दूकान का मालिक";
                break;
            case "Large scale farmer":
                val = "बड़ा किसान";
                break;
            case "Daily wage earner":
                val = "दैनिक मजदूर";
                break;
            case "Small scale farmer/farm worker":
                val = "छोटे किसान/ दुसरे के खेत में काम करने वाले";
                break;
            case "Unemployed":
                val = "बेरोजगार";
                break;
            case "Housewife":
                val = "ग्रहिणी";
                break;
            case "Other skills (driver,mason etc)":
                val = "अन्य कुशलता (ड्राईवर,राज मिस्त्री)";
                break;
            case "[Describe]":
                val = "वर्णन करे";
                break;
            default:
                return val;
        }
        return val;

    }

    public static String switch_hi_caste_edit(String val) {
        switch (val) {
            case "General":
                val = "सामान्य";
                break;
            case "OBC":
                val = "अन्य पिछड़ा वर्ग";
                break;
            case "SC":
                val = "अनुसूचित जाति";
                break;
            case "ST":
                val = "अनुसूचित जनजाति";
                break;
            case "others":
                val = "अन्य";
                break;
            default:
                return val;
        }
        return val;
    }

    public static String getFileNameWithoutExtension(File file) {
        String fileName = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                fileName = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileName = "";
        }

        return fileName;

    }

    public static String getFileNameWithoutExtensionString(String filename) {
        String fileName = "";

        try {
            if (filename.indexOf(".") > 0)
                fileName = filename.substring(0, filename.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
            fileName = "";
        }

        return fileName;

    }

    public static String convertUsingStringBuilder(List<String> names) {
        StringBuilder namesStr = new StringBuilder();
        for (String name : names) {
            namesStr = namesStr.length() > 0 ? namesStr.append("','").append(name) : namesStr.append(name);
        }
        return namesStr.toString();
    }

    public static String mobileNumberEmpty(String value) {
        String val = "N/A";
        if (value != null && !value.isEmpty())
            val = value;
        return val;
    }

    public static String hi_or_as__en_month(int month_index) {
        String dob_string = "";

        switch (month_index) {
            case 0:
                dob_string = "January";
                break;
            case 1:
                dob_string = "February";
                break;
            case 2:
                dob_string = "March";
                break;
            case 3:
                dob_string = "April";
                break;
            case 4:
                dob_string = "May";
                break;
            case 5:
                dob_string = "June";
                break;
            case 6:
                dob_string = "July";
                break;
            case 7:
                dob_string = "August";
                break;
            case 8:
                dob_string = "September";
                break;
            case 9:
                dob_string = "October";
                break;
            case 10:
                dob_string = "November";
                break;
            case 11:
                dob_string = "December";
                break;

            default:
                return dob_string;
        }

        return dob_string;
    }

    public static String en__hi_dob(String dob) { //English dob is replaced to Hindi text.
        String mdob_text = dob
                .replace("January", "जनवरी")
                .replace("February", "फ़रवरी")
                .replace("March", "मार्च")
                .replace("April", "अप्रैल")
                .replace("May", "मई")
                .replace("June", "जून")
                .replace("July", "जुलाई")
                .replace("August", "अगस्त")
                .replace("September", "सितंबर")
                .replace("October", "अक्टूबर")
                .replace("November", "नवंबर")
                .replace("December", "दिसंबर");

        return mdob_text;
    }

    /*
    * @return mdob_text : Assamese month translation is passed.
    * @param dob : DOB in the format DD MMMM YYYY eg. 15 January 2021
    */
/*
    public static String en__as_dob(String dob) { //English dob is replaced to Assamese text.
        String mdob_text = dob
                .replace("January", "জানুৱাৰী")
                .replace("February", "ফেব্ৰুৱাৰী")
                .replace("March", "মাৰ্চ")
                .replace("April", "এপ্ৰিল")
                .replace("May", "মে")
                .replace("June", "জুন")
                .replace("July", "জুলাই")
                .replace("August", "আগষ্ট")
                .replace("September", "ছেপ্টেম্বৰ")
                .replace("October", "অক্টোবৰ")
                .replace("November", "নৱেম্বৰ")
                .replace("December", "ডিচেম্বৰ");

        return mdob_text;
    }
*/


    public static String en__or_dob(String dob) { //English dob is replaced to Odiya text.
        String mdob_text = dob
                .replace("January", "ଜାନୁଆରୀ")
                .replace("February", "ଫେବୃଆରୀ")
                .replace("March", "ମାର୍ଚ୍ଚ")
                .replace("April", "ଏପ୍ରିଲ୍")
                .replace("May", "ମେ")
                .replace("June", "ଜୁନ୍")
                .replace("July", "ଜୁଲାଇ")
                .replace("August", "ଅଗଷ୍ଟ")
                .replace("September", "ସେପ୍ଟେମ୍ବର")
                .replace("October", "ଅକ୍ଟୋବର")
                .replace("November", "ନଭେମ୍ବର")
                .replace("December", "ଡିସେମ୍ବର");

        return mdob_text;
    }

    public static String hi_or_as__en_noEdit(String dobString, String locale) {

        if(locale.equalsIgnoreCase("hi")) {
            String dob = dobString
                    //Hindi
                    .replace("जनवरी", "January")
                    .replace("फ़रवरी", "February")
                    .replace("मार्च", "March")
                    .replace("अप्रैल", "April")
                    .replace("मई", "May")
                    .replace("जून", "June")
                    .replace("जुलाई", "July")
                    .replace("अगस्त", "August")
                    .replace("सितंबर", "September")
                    .replace("अक्टूबर", "October")
                    .replace("नवंबर", "November")
                    .replace("दिसंबर", "December");
            return dob;
        }
        else if(locale.equalsIgnoreCase("or")) {
            //Odiya
            String dob = dobString
                    .replace("ଜାନୁଆରୀ", "January")
                    .replace("ଫେବୃଆରୀ", "February")
                    .replace("ମାର୍ଚ୍ଚ", "March")
                    .replace("ଏପ୍ରିଲ୍", "April")
                    .replace("ମେ", "May")
                    .replace("ଜୁନ୍", "June")
                    .replace("ଜୁଲାଇ", "July")
                    .replace("ଅଗଷ୍ଟ", "August")
                    .replace("ସେପ୍ଟେମ୍ବର", "September")
                    .replace("ଅକ୍ଟୋବର", "October")
                    .replace("ନଭେମ୍ବର", "November")
                    .replace("ଡିସେମ୍ବର", "December");
            return dob;
        }
/*
        else if(locale.equalsIgnoreCase("as")) {
            //Odiya
            String dob = dobString
                    .replace("জানুৱাৰী", "January")
                    .replace("ফেব্ৰুৱাৰী", "February")
                    .replace("মাৰ্চ", "March")
                    .replace("এপ্ৰিল", "April")
                    .replace("মে", "May")
                    .replace("জুন", "June")
                    .replace("জুলাই", "July")
                    .replace("আগষ্ট", "August")
                    .replace("ছেপ্টেম্বৰ", "September")
                    .replace("অক্টোবৰ", "October")
                    .replace("নৱেম্বৰ", "November")
                    .replace("ডিচেম্বৰ", "December");
            return dob;
        }
*/

        else {
            return dobString;
        }

    }
}
