package org.intelehealth.ekalhelpline.models;


public class ActivePatientModel {

    String uuid;
    String patientuuid;
    String startdate;
    String enddate;
    String openmrs_id;
    String first_name;
    String middle_name;
    String last_name;
    String date_of_birth;
    String phone_number;
    String sync;
    String visit_speciality;

    //This constructor is for default operation of sync....
    public ActivePatientModel(String uuid, String patientuuid, String startdate, String enddate, String openmrs_id,
                              String first_name, String middle_name, String last_name, String date_of_birth,
                              String phone_number, String sync) {
        this.uuid = uuid;
        this.patientuuid = patientuuid;
        this.startdate = startdate;
        this.enddate = enddate;
        this.openmrs_id = openmrs_id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.sync = sync;
    }

    //This constructor is for getting the visit speciality values...
    public ActivePatientModel(String patientuuid, String visit_speciality) {
        this.patientuuid = patientuuid;
        this.visit_speciality = visit_speciality;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPatientuuid() {
        return patientuuid;
    }

    public void setPatientuuid(String patientuuid) {
        this.patientuuid = patientuuid;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getOpenmrs_id() {
        return openmrs_id;
    }

    public void setOpenmrs_id(String openmrs_id) {
        this.openmrs_id = openmrs_id;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getVisit_speciality() {
        return visit_speciality;
    }

    public void setVisit_speciality(String visit_speciality) {
        this.visit_speciality = visit_speciality;
    }
}
