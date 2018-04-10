package com.example.pentagon.sngistaudit;

/**
 * Created by LAPIS-ANTONY on 3/21/2018.
 */

public class CoursePlanData {
    //{"courseplan":[{"course_plan_id":"1","title":"test","date":"2018-03-21 17:37:36","subject_id":"1","staff_id":"bindhu","file_path":"http:\/\/lapisapps.in\/auditing\/courseplan\/1521634056.pdf"}]}

    public String getCourse_plan_id() {
        return course_plan_id;
    }

    public void setCourse_plan_id(String course_plan_id) {
        this.course_plan_id = course_plan_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String course_plan_id,title,date,subject_id,staff_id,file_path;

}
