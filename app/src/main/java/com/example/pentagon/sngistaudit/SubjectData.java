package com.example.pentagon.sngistaudit;

/**
 * Created by LAPIS-ANTONY on 3/14/2018.
 */

public class SubjectData {
    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    // {"subject":[{"subject_id":"1","course_id":"1"
    // ,"name":"Networking","semester":"3"}]}
    public String subject_id,course_id,name,semester;

    @Override
    public String toString() {
        return subject_id;
    }
}
