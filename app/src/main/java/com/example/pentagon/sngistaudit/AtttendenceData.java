package com.example.pentagon.sngistaudit;

/**
 * Created by LAPIS-ANTONY on 3/13/2018.
 */

public class AtttendenceData {
    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPresent() {
        return Present;
    }

    public void setPresent(String present) {
        Present = present;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    //    {"mark":[{"attendance_id":"1","student_id":"std","subject_id":"1"
//            ,"date":"2018-03-02 00:00:00","hour":"3","status":"Present","semester":"3"}]}
    public String attendance_id;
    public String student_id;
    public String subject_id;
    public String date;
    public String hour;
    public String status;
    public String Present;
    public String semester;

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String student;
}
