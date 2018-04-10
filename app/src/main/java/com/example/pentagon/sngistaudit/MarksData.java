package com.example.pentagon.sngistaudit;

/**
 * Created by LAPIS-ANTONY on 3/13/2018.
 */

public class MarksData {
//    {"mark":[{"mark_id":"1","student_id":"std","semester":"3","subject_id":"1",
//            "mark":"45","internal_type":"1"}]}
    //{"mark":[{"assignment_id":"1","student_id":"std","submitted_date":"2018-12-12 00:00:00","subject_id":"1","mark":"20","title":"DataStructure","staff_id":"1","semester":"2"}]}
    public String mark_id;

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String staff_id;
    public String assignment_id;

    public String getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(String assignment_id) {
        this.assignment_id = assignment_id;
    }

    public String getSubmitted_date() {
        return submitted_date;
    }

    public void setSubmitted_date(String submitted_date) {
        this.submitted_date = submitted_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String submitted_date;
    public String title;
    public String student_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String name;
    public String subject;

    public String getMark_id() {
        return mark_id;
    }

    public void setMark_id(String mark_id) {
        this.mark_id = mark_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getInternal_type() {
        return internal_type;
    }

    public void setInternal_type(String internal_type) {
        this.internal_type = internal_type;
    }

    public String semester;
    public String subject_id;
    public String mark;
    public String internal_type;
}
