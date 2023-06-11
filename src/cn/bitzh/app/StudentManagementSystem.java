package cn.bitzh.app;

import java.io.*;
import java.util.*;

public class StudentManagementSystem {
    private static final String DATA_FILE = "data.bin";
    private static final String USER_FILE = "user.bin";
    private static final Scanner scanner = new Scanner(System.in);
    private static Map<String, String> userMap = new HashMap<>();
    private static String currentUser;
    private static boolean isAdmin = false;
    private static List<Course> courses = new ArrayList<>();
    private static List<Class> classes = new ArrayList<>();

    private static Map<String, Class> classMap = new HashMap<>();
    private static Map<String, Course> courseMap = new HashMap<>();
    private static Map<String, Student> studentMap = new HashMap<>();


    public static void main(String[] args) throws IOException {
        checkFileExist();
        loadUser();
        login();
        System.out.println("=========欢迎来到北理珠学生成绩管理系统=========");
        if(isAdmin) {
            System.out.println("当前用户：" + currentUser);
            while(true)
                commandListAdmin();
        } else {
            loadData();
            System.out.println("当前用户：" + currentUser);
            while (true)
                commandList();
        }
    }

    private static void commandList() throws IOException {
        System.out.println();
        System.out.println("1. 添加学生");
        System.out.println("2. 添加成绩");
        System.out.println("3. 查询个人成绩");
        System.out.println("4. 查询单科成绩");
        System.out.println("5. 查询年级总排名");
        System.out.println("6. 查询单科排名");
        System.out.println("7. 查询班级排名");
        System.out.println("8. 清除所有数据");
        System.out.println("9. 保存修改并退出");
        System.out.println("请输入：");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                addStudent();
                break;
            case 2:
                addScore();
                break;
            case 3:
                queryStudent();
                break;
            case 4:
                queryCourse();
                break;
            case 5:
                showAllRanking();
                break;
            case 6:
                showRanking();
                break;
            case 7:
                showClassRanking();
                break;
            case 8:
                if(clearData())
                    System.exit(0);
                break;
            case 9:
                saveData();
                System.exit(0);
            default:
                System.out.println("输入错误，请重输!");
        }
    }

    private static void commandListAdmin() {
        System.out.println();
        System.out.println("1. 添加用户");
        System.out.println("2. 修改用户密码");
        System.out.println("3. 删除用户");
        System.out.println("4. 保存并退出");
        System.out.println("请输入：");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                addUser();
                break;
            case 2:
                changePassword();
                break;
            case 3:
                deleteUser();
                break;
            case 4:
                saveUserData();
                System.exit(0);
            default:
                System.out.println("输入错误，请重输!");
        }
    }

    private static void loadUser() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            userMap = (Map<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("用户数据损坏或为空，请用户管理员进行操作：");
        }
    }

    private static void addUser() {
        System.out.println("请输入用户名：");
        String userName = scanner.nextLine();
        System.out.println("请输入用户密码：");
        String password = scanner.nextLine();

        if(userMap.containsKey(userName)) {
            System.out.println("用户已存在！");
            return;
        }
        userMap.put(userName, password);
        System.out.println("添加成功！");
    }

    private static void changePassword() {
        System.out.println("请输入用户名：");
        String userName = scanner.nextLine();
        if(!userMap.containsKey(userName)) {
            System.out.println("用户不存在！");
            return;
        }
        System.out.println("请输入用户密码：");
        String password = scanner.nextLine();
        userMap.put(userName, password);
        System.out.println("修改成功！");
    }

    private static void deleteUser() {
        System.out.println("请输入用户名：");
        String userName = scanner.nextLine();
        if(!userMap.containsKey(userName)) {
            System.out.println("用户不存在！");
            return;
        }
        userMap.remove(userName);
        System.out.println("删除成功！");
    }

    private static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(userMap);
            System.out.println("保存成功！");
        } catch (IOException e) {
            System.out.println("保存失败！");
        }
    }

    private static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            classes = (List<Class>) ois.readObject();
            courses = (List<Course>) ois.readObject();
            for (Class clazz : classes) {
                classMap.put(clazz.getClassName(), clazz);
                for(Student student : clazz.getClassStus())
                    studentMap.put(student.getStuName(), student);
            }
            for (Course course : courses)
                courseMap.put(course.getCourseName(), course);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("当前系统数据为空，需要初始化，请添加班级和课程：");
            boolean classOK = false, courseOK = false;
            while(true) {
                System.out.println("1. 添加班级");
                System.out.println("2. 添加课程");
                System.out.println("3. 完成初始化");
                System.out.println("请输入：");
                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        addClass();
                        classOK = true;
                        break;
                    case 2:
                        addCourse();
                        courseOK = true;
                        break;
                    case 3:
                        if(classOK && courseOK) {
                            for(Class clazz : classes)
                                classMap.put(clazz.getClassName(), clazz);
                            for(Course course : courses)
                                courseMap.put(course.getCourseName(), course);
                            return;
                        }
                        else {
                            System.out.println("班级和课程都需要添加！");
                            break;
                        }
                    default:
                        System.out.println("输入错误，请重输!");
                }
            }
        }
    }

    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            classes = new ArrayList<>(classMap.values());
            courses = new ArrayList<>(courseMap.values());
            oos.writeObject(classes);
            oos.writeObject(courses);
            System.out.println("保存成功！");
        } catch (IOException e) {
            System.out.println("保存失败！");
        }
    }

    private static void login() {
        while (true) {
            System.out.println("请输入用户名：");
            String userName = scanner.nextLine();
            System.out.println("请输入密码：");
            String password = scanner.nextLine();

            if(userName.equals("admin") && password.equals("admin")) {
                currentUser = "用户管理员";
                isAdmin = true;
                break;
            }

            if(userMap.containsKey(userName)) {
                if(userMap.get(userName).equals(password)) {
                    currentUser = userName;
                    isAdmin = false;
                    break;
                }
            }
            System.out.println("账号密码错误，请重新输入！");
        }
    }

    private static void addStudent() {
        System.out.println("请输入学生姓名：");
        String stuName = scanner.nextLine();
        System.out.println("请输入该学生所属班级：");
        String className = scanner.nextLine();

        if(!classMap.containsKey(className)) {
            System.out.println("班级不存在！");
            return;
        }

        Student student = new Student(stuName, className);
        studentMap.put(stuName, student);
        classMap.get(className).addStudent(student);
        System.out.println("添加成功！");
    }

    private static void addCourse() {
        System.out.println("请输入课程名：");
        String courseName = scanner.nextLine();
        courseMap.put(courseName, new Course(courseName));
        System.out.println("添加成功！");
    }

    private static void addClass() {
        System.out.println("请输入班级名：");
        String className = scanner.nextLine();
        classMap.put(className, new Class(className));
        System.out.println("添加成功！");
    }

    private static void addScore() {
        System.out.println("请输入学生姓名：");
        String stuName = scanner.nextLine();
        if (!studentMap.containsKey(stuName)) {
            System.out.println("学生不存在！");
            return;
        }

        Student student = studentMap.get(stuName);

        System.out.println("请输入课程名：");
        String courseName = scanner.nextLine();

        if(!courseMap.containsKey(courseName)) {
            System.out.println("课程不存在！");
            return;
        }

        System.out.println("请输入分数：");
        double courseScore = scanner.nextDouble();
        scanner.nextLine();

        Score score = new Score(stuName, courseName, courseScore);

        student.addScore(score);
        courseMap.get(courseName).addScore(score);
        System.out.println("添加成功！");
    }

    private static void queryStudent() {
        System.out.println("请输入学生姓名：");
        String stuName = scanner.nextLine();

        if (!studentMap.containsKey(stuName)) {
            System.out.println("该学生不存在！");
            return;
        }

        Student student = studentMap.get(stuName);
        System.out.println(student.getScoreReport());
    }

    private static void queryCourse() {
        System.out.println("请输入课程名：");
        String courseName = scanner.nextLine();

        if(!courseMap.containsKey(courseName)) {
            System.out.println("该课程不存在！");
            return;
        }

        Course course = courseMap.get(courseName);
        System.out.println(course.getScoreReport());
    }

    private static void showAllRanking() {
        List<Student> ranking = new ArrayList<>(studentMap.values());
        ranking.sort(Comparator.comparingDouble(Student::getAverageScore).reversed());
        System.out.println("=====年级排名(平均分)=====");
        for (int i = 0; i < ranking.size(); ++i)
            System.out.println("第" + (i + 1) + "名：" + ranking.get(i).getStuName() + " (" + ranking.get(i).getAverageScore() + ")");
    }

    private static void showRanking() {
        System.out.println("请输入课程名：");
        String courseName = scanner.nextLine();

        if(!courseMap.containsKey(courseName)) {
            System.out.println("该课程不存在！");
            return;
        }
        List<Score> ranking = courseMap.get(courseName).getRanking();
        System.out.println("=====" + courseName + "课程排名=====");
        for (int i = 0; i < ranking.size(); ++i)
            System.out.println("第" + (i + 1) + "名：" + ranking.get(i).getStuName() + " (" + ranking.get(i).getCourseScore() + ")");
    }

    private static void showClassRanking() {
        System.out.println("请输入班级名：");
        String className = scanner.nextLine();

        if(!classMap.containsKey(className)) {
            System.out.println("该班级不存在！");
            return;
        }
        List<Student> ranking = classMap.get(className).getRanking();
        System.out.println("=====" + className + "班排名(平均分)=====");
        for (int i = 0; i < ranking.size(); ++i)
            System.out.println("第" + (i + 1) + "名：" + ranking.get(i).getStuName() + " (" + ranking.get(i).getAverageScore() + ")");
    }

    private static boolean clearData() throws IOException {
        System.out.println("该操作不可逆，确定么？(Y/N)");
        String option;
        while(true) {
            option = scanner.nextLine();
            if(option.equals("Y")) {
                File file = new File(DATA_FILE);
                file.delete();
                file.createNewFile();
                System.out.println("清除成功，请重启程序！");
                break;
            } else if(option.equals("N")) {
                return false;
            } else {
                System.out.println("输入错误，请重输：");
            }
        }
        return true;
    }

    public static void checkFileExist() throws IOException {
        File userFile = new File(USER_FILE);
        File dataFile = new File(DATA_FILE);
        if(!userFile.exists())
            userFile.createNewFile();
        if(!dataFile.exists())
            dataFile.createNewFile();
    }
}

