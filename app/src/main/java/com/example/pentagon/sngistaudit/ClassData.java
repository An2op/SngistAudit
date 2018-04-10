package com.example.pentagon.sngistaudit;

/**
 * Created by LAPIS-ANTONY on 3/22/2018.
 */

public class ClassData {
//    {"class":[{"batch_id":"1","staff_id":"1","subject_id":"1","semester":"1","course_id":"1","date":"2018-12-12","class_details":"tesstt","class_type":"Evening"}]}

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClass_details() {
        return class_details;
    }

    public void setClass_details(String class_details) {
        this.class_details = class_details;
    }

    public String getClass_type() {
        return class_type;
    }

    public void setClass_type(String class_type) {
        this.class_type = class_type;
    }

    public String batch_id, staff_id,subject_id,semester,course_id,date,class_details,class_type;
}
