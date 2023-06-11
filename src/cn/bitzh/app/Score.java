package cn.bitzh.app;

import java.io.Serializable;

public class Score implements Serializable {
    private String stuName;
    private String courseName;
    private double courseScore;

    public Score(String stuName, String courseName, double courseScore) {
        this.stuName = stuName;
        this.courseName = courseName;
        this.courseScore = courseScore;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(double courseScore) {
        this.courseScore = courseScore;
    }
}
