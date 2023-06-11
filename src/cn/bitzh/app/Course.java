package cn.bitzh.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Course implements Serializable {
    private String courseName;
    private List<Score> courseScores;

    public Course(String courseName) {
        this.courseName = courseName;
        this.courseScores = new ArrayList<>();
    }

    public void addScore(Score score) {
        courseScores.add(score);
    }

    public double getAverageScore() {
        return getTotalScore() / courseScores.size();
    }

    public double getTotalScore() {
        double total = 0;
        for(Score score : courseScores)
            total += score.getCourseScore();
        return total;
    }

    public String getScoreReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("======成绩报告单======").append("\n");
        sb.append("课程名：").append(courseName).append("\n");

        for (Score score : courseScores)
            sb.append(score.getStuName()).append("：").append(score.getCourseScore()).append("\n");

        sb.append("平均分：").append(getAverageScore()).append("\n");

        return sb.toString();
    }

    public List<Score> getRanking() {
        List<Score> ranking = new ArrayList<>(courseScores);
        ranking.sort(Comparator.comparingDouble(Score::getCourseScore).reversed());
        return ranking;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<Score> getCourseScores() {
        return courseScores;
    }

    public void setCourseScores(List<Score> courseScores) {
        this.courseScores = courseScores;
    }
}
