package cn.bitzh.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Class implements Serializable {
    private String className;
    private List<Student> classStus;

    public Class(String className) {
        this.className = className;
        this.classStus = new ArrayList<>();
    }

    public void addStudent(Student student) {
        classStus.add(student);
    }

    public List<Student> getRanking() {
        List<Student> ranking = new ArrayList<>(classStus);
        ranking.sort(Comparator.comparingDouble(Student::getAverageScore).reversed());
        return ranking;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Student> getClassStus() {
        return classStus;
    }

    public void setClassStus(List<Student> classStus) {
        this.classStus = classStus;
    }
}
