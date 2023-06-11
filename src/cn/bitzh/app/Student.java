package cn.bitzh.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private String stuName;
    private String className;
    private List<Score> stuScores;

    public Student(String stuName, String className) {
        this.stuName = stuName;
        this.className = className;
        this.stuScores = new ArrayList<>();
    }

    public void addScore(Score score) {
        stuScores.add(score);
    }

    public double getTotalScore() {
        double total = 0;
        for (Score score : stuScores)
            total += score.getCourseScore();
        return total;
    }

    public double getAverageScore() {
        return getTotalScore() / stuScores.size();
    }

    public String getScoreReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("======成绩报告单======").append("\n");
        sb.append("学生姓名：").append(stuName).append("\n");
        sb.append("所属班级：").append(className).append("\n");

        for (Score score : stuScores)
            sb.append(score.getCourseName()).append("：").append(score.getCourseScore()).append("\n");

        sb.append("总分：").append(getTotalScore()).append("\t");
        sb.append("平均分：").append(getAverageScore()).append("\n");

        return sb.toString();
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Score> getStuScores() {
        return stuScores;
    }

    public void setStuScores(List<Score> stuScores) {
        this.stuScores = stuScores;
    }
}
