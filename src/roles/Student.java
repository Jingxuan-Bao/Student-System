package roles;

import files.FileInfoReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the student class extends the user class
 * @Author Jingxuan Bao and Yaoqi Deng
 * @Time 2021-12-08
 */
public class Student extends User{

    // the FileInfoReader
    private FileInfoReader fr;

    // the name of the student
    private String name;

    // the id of the student
    private String id;

    // the password of the student
    private String password;

    // the coursemap: course(key), grades(value) of the student
    private Map<String, String> coursemap;

    /**
     * the constructor of student
     * @param fr filereader
     * @param username username
     */
    public Student(FileInfoReader fr, String username) {
        super(fr, username);
        // set the basic variable
        this.fr = fr;
        this.id = fr.getStudentInfo().get(username).get(0);
        this.name = fr.getStudentInfo().get(username).get(1);
        this.password = fr.getStudentInfo().get(username).get(2);
        // build the coursemap
        this.buildcoursemap(fr, username);
    }

    /**
     * a helper function of the constructor
     * build a coursemap course(key), grades(value) of the student
     */
    public void buildcoursemap(FileInfoReader fr, String username) {
        // get the course string the student take
        String course = fr.getStudentInfo().get(username).get(3);
        // new a hashmap
        this.coursemap = new HashMap<>();
        // split the string
        String[] courses = course.split(",");
        // loop the course with grades array
        for(int i = 0; i < courses.length; i ++) {
            String[] coursesTemp = courses[i].split(":");
            // put the course and grade into the coursemap
            this.coursemap.put(coursesTemp[0].trim(), coursesTemp[1].trim());
        }
    }


    /**
     * view all the courses in the courseInfo Map
     */
    public void viewallcourses() {
        // get the courseInfo map
        Map<String, List<String>> CourseInfo = this.fr.getCourseInfo();
        // loop the map and print the information
        for(String courses : CourseInfo.keySet()) {
            System.out.println(courses + "|" + CourseInfo.get(courses).get(0)
                               + " " + CourseInfo.get(courses).get(1)
                               + " " + CourseInfo.get(courses).get(2)
                               + " " + CourseInfo.get(courses).get(3)
                               + " " + CourseInfo.get(courses).get(4)
                               + " " + CourseInfo.get(courses).get(5));
        }
    }

    /**
     * view all the courses of the student
     */
    public void viewusercourses() {
        // print a friendly message
        System.out.println("The course in your list: ");
        // loop the coursemap
        for(String course : this.coursemap.keySet()) {
            // print the course information the student take
            System.out.println(course + "|" + fr.getCourseInfo().get(course).get(0)
                               + " " + fr.getCourseInfo().get(course).get(1)
                               + " " + fr.getCourseInfo().get(course).get(2)
                               + " " + fr.getCourseInfo().get(course).get(3)
                               + " " + fr.getCourseInfo().get(course).get(4)
                               + " " + fr.getCourseInfo().get(course).get(5));
        }
    }

    /**
     * add a course
     * @Param course the course going to be added
     */
    public void addcourse(String course) {
        // if student already take the course
        if(this.coursemap.containsKey(course)) {
            System.out.println("The course you selected is already in your list.");
            return;
        }
        // if there is a time conflict
        if(! timeconflict(course)) {
            System.out.println("Sorry there is a time conflict, you can not add this course.");
            return;
        }
        // put the course into coursemap
        this.coursemap.put(course, "P");
        // build a new string for update the course informaion into getStudentInfo map
        String courseMap = "";
        for(String cour : coursemap.keySet()) {
            courseMap += cour + ": " + coursemap.get(cour) + ", ";
        }
        // remove the final ',' in the string
        if(courseMap.charAt(courseMap.length() - 2) == ',') {
            courseMap = courseMap.substring(0, courseMap.length() - 2);
        }
        // update the new course information
        fr.getStudentInfo().get(getusername()).remove(fr.getStudentInfo().get(getusername()).size() - 1);
        fr.getStudentInfo().get(getusername()).add(courseMap);
        // print a friendly message
        System.out.println("You add " + course + " successfully");
        // view the courses the student take now
        this.viewusercourses();
        // update the content in the txt file
        this.updatestudentInfo();
        // update the information in the CourseInfoStudent map
        if(fr.getCourseInfoStudent().containsKey(course)) {
            if(fr.getCourseInfoStudent().get(course).contains(getusername())) {
                return;
            }
            else{
                fr.getCourseInfoStudent().get(course).add(getusername());
            }
        }
        else {
            List<String> temp = new ArrayList<>();
            temp.add(getusername());
            fr.getCourseInfoStudent().put(course, temp);
        }
    }

    /**
     * view all courses the student enrolled just as viewusercourses
     */
    public void viewenrolledcourse() {
        this.viewusercourses();
    }

    /**
     * view the grades of courses the student take
     * the courses without grade show as "P"
     */
    public void viewgrades() {
        // print a friendly message
        System.out.println("Here are some courses you already with your grades");
        // loop all the courses the student take
        for(String course : coursemap.keySet()) {
            if(coursemap.get(course).equals(" ")) {
                continue;
            }
            // print the course and the grade
            System.out.println("Grade of " + course + fr.getCourseInfo().get(course).get(0) + ": " + coursemap.get(course));
        }
    }

    /**
     * drop a course
     * @Param course the course going to be dropped
     */
    public void dropcourse(String course) {
        // if student do not take the course
        if(! this.coursemap.containsKey(course)) {
            System.out.println("The course you drop is not in your list.");
            return;
        }
        // remove the course from the coursemap
        this.coursemap.remove(course);
        // print a friendly message
        System.out.println("You drop " + course + " successfully");
        // build a new string for update the course informaion into getStudentInfo map
        String courseMap = "";
        for(String cour : coursemap.keySet()) {
            courseMap += cour + ": " + coursemap.get(cour) + ", ";
        }
        // remove the final ',' in the string
        if(courseMap.charAt(courseMap.length() - 2) == ',') {
            courseMap = courseMap.substring(0, courseMap.length() - 2);
        }
        // update the new course information
        fr.getStudentInfo().get(getusername()).remove(fr.getStudentInfo().get(getusername()).size() - 1);
        fr.getStudentInfo().get(getusername()).add(courseMap);
        // view the courses the student take now
        this.viewusercourses();
        // update the content in the txt file
        this.updatestudentInfo();
        // update the information in the CourseInfoStudent map
        fr.getCourseInfoStudent().get(course).remove(getusername());
        if(fr.getCourseInfoStudent().get(course).size() == 0) {
            fr.getCourseInfoStudent().remove(course);
        }
    }

    /**
     * update the content into the studentInfo.txt
     */
    public void updatestudentInfo() {
        // update the studentInfo txt
        try {
            FileWriter fw = new FileWriter("studentInfo.txt");
            PrintWriter pw = new PrintWriter(fw);
            // loop every student in the txt file
            for(String students : fr.getStudentInfo().keySet()) {
                // rewrite the txt file
                pw.println( fr.getStudentInfo().get(students).get(0)
                           + "; " + fr.getStudentInfo().get(students).get(1)
                           + "; " + students
                           + "; " + fr.getStudentInfo().get(students).get(2)
                           + "; " + fr.getStudentInfo().get(students).get(3));
            }
            pw.close();
        }
        // catch the error
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * check the password is right or not
     * @Param password the password going to be checked
     * @Return return the password is right or not
     */
    /*
    public boolean checkpassword(String password) {
        if(! password.equals(fr.getStudentInfo().get(getusername()).get(2))) {
            return false;
        }
        return true;
    }
    */


    /**
     * check whether there is a time conflict of the course the student want to add
     * @param course the course the student want to add
     * @return is there a time conflict or not, false(can not take the course), true(ok to take the course)
     */
    public boolean timeconflict(String course) {
        // get the course time information
        String date = fr.getCourseInfo().get(course).get(2);
        String start = fr.getCourseInfo().get(course).get(3);
        String end = fr.getCourseInfo().get(course).get(4);
        // split the date of the course
        char[] days = new char[date.length()];
        for(int i = 0; i < days.length; i ++) {
            days[i] = date.charAt(i);
        }
        // loop the courses the student take now
        for(String courses : coursemap.keySet()) {
            String dateCourse = fr.getCourseInfo().get(courses).get(2);
            String startCourse = fr.getCourseInfo().get(courses).get(3);
            String endCourse = fr.getCourseInfo().get(courses).get(4);
            // check whether student can take the course or not
            for(char day : days) {
                // make some small change in there
                for (int i = 0; i < dateCourse.length(); i ++) {
                    if(i < days.length && days[i] == dateCourse.charAt(i)) {
                        if(! timehelp(start, startCourse, end, endCourse)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * a helper method to check whether there is a time conflict between two time zones
     * @param start start time of course one
     * @param startCourse start time of course two
     * @param end end time of course one
     * @param endCourse end time of course two
     * @return return false means there is a conflict true means no
     */
    public boolean timehelp(String start, String startCourse, String end, String endCourse) {
        // convert int to string
        int startInt = timetoint(start);
        int endInt = timetoint(end);
        int startCourseInt = timetoint(startCourse);
        int endCourseInt = timetoint(endCourse);
        // check the time zone is available or not
        if(startInt > startCourseInt && startInt < endCourseInt) {
            return false;
        }
        if(endInt > startCourseInt && endInt < endCourseInt) {
            return false;
        }
        if(startInt == startCourseInt && endInt == endCourseInt) {
            return false;
        }
        return true;
    }

    /**
     * a helper method to convert a time String to Integer
     * @param time the time going to be converted
     * @return the Integer of time
     */
    public int timetoint(String time) {
        String timeT = time.split(":")[0] + time.split(":")[1];
        return Integer.parseInt(timeT);
    }

    /**
     * @return return the name of the student
     */
    public String getName() {
        return name;
    }

    /**
     * @return return the Id of the student
     */
    public String getId() {
        return id;
    }

    /**
     * @return return the password of the student
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return return the coursemap of the student
     */
    public Map<String, String> getCoursemap() {
        return coursemap;
    }

}
