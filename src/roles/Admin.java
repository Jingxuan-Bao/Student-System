package roles;

import files.FileInfoReader;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Admin extends User{
    /**
     * @param fr FileInfoReader
     * @param id The User id input
     */
    // the FileInfoReader
    private FileInfoReader fr;

    // the name of the Admin
    private String name;

    // the id of the Admin
    private String id;

    // the password of the admin
    private String password;
    
    // the global variable used in timeConflict check
    private  String timeConflictCourse;
    
    public Admin(FileInfoReader fr, String username) {
        super(fr, username);
        // set the basic variable
        this.fr = fr;
        this.id = fr.getAdminInfo().get(username).get(0);
        this.name = fr.getAdminInfo().get(username).get(1);
        this.password = fr.getAdminInfo().get(username).get(2);

    }

    /**
     * view all the courses in the courseInfo Map
     */
    public void viewAllCourses() {
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
     * TODO need to do the prof check in the controller class
     * @param courseName
     * @param courseID
     * @param startTime
     * @param endTime
     * @param capacity
     * @param profUsername
     * @return
     */
    public boolean addCourse(String courseName, String courseID, String date, String startTime, String endTime, String capacity, String profUsername) {
        //check if the course exist already
        if (fr.getCourseInfo().containsKey(courseID)) {
            System.out.println("The course already exit.");
            return false;
        }
        //see if there is a time conflict
        String courseWithConflict = null;
        if(! timeConflict(profUsername, date, startTime, endTime)) {
            System.out.println("Sorry there is a time conflict with course "
                    + fr.getCourseInfo().get(timeConflictCourse));
            return false;
        }
        ArrayList<String> info = new ArrayList<String>();
        String profName = fr.getProInfo().get(profUsername).get(0);
        info.add(courseName);
        info.add(profName);
        info.add(date);
        info.add(startTime);
        info.add(endTime);
        info.add(capacity);
        fr.getCourseInfo().put(profUsername, info);
        updateCourseInfo();
        updateProfInfo();
        fr.buildCourseStudentMap(); //update the course student map
        fr.buildTechMap();
        return true;
    }


    /**
     *
     * @param profUsername
     * @param date
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean timeConflict(String profUsername, String date, String startTime, String endTime) {
        // split the date of the course
        char[] days = new char[date.length()];
        for(int i = 0; i < days.length; i ++) {
            days[i] = date.charAt(i);
        } //[M,W]...
        ArrayList <String> courseTeach = (ArrayList<String>) fr.getCourseInfoTech().get(profUsername);
        // loop the courses the prof teach now
        for(String courseID : courseTeach) {
            String dateCourse = fr.getCourseInfo().get(courseID).get(2);
            String startCourse = fr.getCourseInfo().get(courseID).get(3);
            String endCourse = fr.getCourseInfo().get(courseID).get(4);
            // check whether student can take the course or not
            for(char day : days) {
                for (int i = 0; i < dateCourse.length(); i ++) {
                    if(i < days.length && days[i] == dateCourse.charAt(i)) {
                        if(! timeHelp(startTime, startCourse, endTime, endCourse)) {
                            timeConflictCourse = courseID;
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
    public boolean timeHelp(String start, String startCourse, String end, String endCourse) {
        // convert int to string
        int startInt = timeToInt(start);
        int endInt = timeToInt(end);
        int startCourseInt = timeToInt(startCourse);
        int endCourseInt = timeToInt(endCourse);
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
    public int timeToInt(String time) {
        String timeT = time.split(":")[0] + time.split(":")[1];
        return Integer.parseInt(timeT);
    }



    /**
     * I did not check if the prof exist beacuse the checking should be better performed in the controller
     * @param name
     * @param ID
     * @param userName
     * @param password
     */

    public boolean addProf(String name, String ID, String userName, String password){
        if(fr.getProInfo().containsKey(userName)){ //exist user name
            System.out.println("This User Name is unavailable");
            return false;
        }
        for (Map.Entry<String, List<String>> entry : fr.getProInfo().entrySet()){ //exist ID
            if(entry.getValue().get(1).contains(ID)){
                System.out.println("This ID is already in the system.");
                return false;
            }
        }
        //no exist id or username
        ArrayList<String> info = new ArrayList<String>();
        info.add(name);
        info.add(ID);
        info.add(userName);
        info.add(password);
        fr.getProInfo().put(userName, info);
        updateProfInfo();
        System.out.println("This professor is added to the system");
        return true;
    }

    /**
     *
     * @param name
     * @param ID
     * @param userName
     * @param password
     */
    public boolean addStudent(String name, String ID, String userName, String password, ArrayList<String> courseAndGrade){
        if(fr.getStudentInfo().containsKey(userName)){ //exist user name
            System.out.println("This User Name is unavailable");
            return false;
        }
        for (Map.Entry<String, List<String>> entry : fr.getProInfo().entrySet()){ //exist ID
            if(entry.getValue().get(0).contains(ID)){
                System.out.println("This ID is already in the system.");
                return false;
            }
        }
        ArrayList<String> info = new ArrayList<String>();
        info.add(ID);
        info.add(name);
        info.add(userName);
        info.add(password);
        info.add(String.valueOf(courseAndGrade));
        fr.getStudentInfo().put(userName, info);
        updateStudentInfo();
        fr.buildCourseStudentMap(); //update the course student map
        return true;
    }

    /**
     * Delete the course from the list
     * @param CourseId
     * @return
     */
    public boolean deleteCourse(String CourseId){
        if(! fr.getCourseInfo().containsKey(CourseId)) {
            System.out.println("The course does not exist.");
            return false;
        }else{
            fr.getCourseInfo().remove(CourseId);
            updateCourseInfo(); //write in the courseInfo text
            //make change to student info map
            deleteCourseFromStudentInfoMap(CourseId);
            updateStudentInfo();
            //make change to course info map
            fr.getCourseInfo().remove(CourseId);
            updateCourseInfo();
            fr.buildCourseStudentMap();
            fr.buildTechMap();
            System.out.println("Course has been deleted.");
            return true;
        }
    }

    /**
     * helper to delete the course from StudentInfoMap
     * @param courseID
     */
    public void deleteCourseFromStudentInfoMap(String courseID){
        for (Map.Entry<String, List<String>> entry : fr.getStudentInfo().entrySet()){ //exist ID
            if(entry.getValue().get(4).contains(courseID)){
                String[] courseAndGrade = entry.getValue().get(4).split(",");
                int index = 0;
                String modifiedCourseGrade = "";
                for (int i = 0; i < courseAndGrade.length; i++){
                    if(courseAndGrade[i].substring(0,6).equals(courseID)){
                        index = i;
                    }
                }
                for (int i = 0;i< courseAndGrade.length; i++){
                    if(i!=index){
                        modifiedCourseGrade = modifiedCourseGrade + courseAndGrade[i].trim() + ",";
                    }
                }
                entry.getValue().remove(4);
                entry.getValue().add(modifiedCourseGrade);
            }
        }
    }

    /**
     * note this should input student username to delete
     * @param studentUserName
     */
    public boolean deleteStudent(String studentUserName){
        if(! fr.getStudentInfo().containsKey(studentUserName)) {
            System.out.println("This student does not exist.");
            return false;
        }else{
            fr.getStudentInfo().remove(studentUserName); //does it really remove in the list?
            updateStudentInfo();
            fr.buildCourseStudentMap(); //rebuild the course student map
            System.out.println("This student has been deleted.");
            return true;
        }
    }

    /**
     * this should be profusername
     * @param profUserName
     */
    public boolean deleteProf(String profUserName){
        if(! fr.getProInfo().containsKey(profUserName)) {
            System.out.println("This professor does not exist.");
            return false;
        }else{
            fr.getProInfo().remove(profUserName);
            updateProfInfo();
            String profName=fr.getProInfo().get(profUserName).get(0);
            deleteProfFromCourseInfoMap(profName);
            System.out.println("This professor has been deleted.");
            return true;
        }
    }

    /**
     * provide proName, find it in the courseinfomap, delete it
     * @param profName
     */
    public void deleteProfFromCourseInfoMap(String profName){
        String keyToRemove = "";
        for (Map.Entry<String, List<String>> entry : fr.getCourseInfo().entrySet()) { //exist ID
            if (entry.getValue().get(1).contains(profName)) {
                keyToRemove = entry.getKey();
            }
        }
        fr.getCourseInfo().remove(keyToRemove);
        updateCourseInfo();
        fr.buildCourseStudentMap();
        fr.buildTechMap();
    }


    /**
     * update the content into the studentInfo.txt
     */
    public void updateStudentInfo() {
        // update the studentInfo txt
        try {
            FileWriter fw = new FileWriter("studentInfo.txt");
            PrintWriter pw = new PrintWriter(fw);
            // loop every student in the txt file
            for(String studentUsername : fr.getStudentInfo().keySet()) {
                // rewrite the txt file
                pw.println( fr.getStudentInfo().get(studentUsername).get(0)
                        + "; " + fr.getStudentInfo().get(studentUsername).get(1)
                        + "; " + studentUsername
                        + "; " + fr.getStudentInfo().get(studentUsername).get(2)
                        + "; " + fr.getStudentInfo().get(studentUsername).get(3));
            }
            pw.close();
        }
        // catch the error
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * update the content into the courseInfo.txt
     */
    public void updateCourseInfo() {
        // update the courseInfo txt
        try {
            FileWriter fw = new FileWriter("courseInfo.txt");
            PrintWriter pw = new PrintWriter(fw);
            // loop every student in the txt file
            for(String courseId : fr.getCourseInfo().keySet()) {
                // rewrite the txt file
                pw.println(courseId
                        + "; " + fr.getCourseInfo().get(courseId).get(0)
                        + "; " + fr.getCourseInfo().get(courseId).get(1)
                        + "; " + fr.getCourseInfo().get(courseId).get(2)
                        + "; " + fr.getCourseInfo().get(courseId).get(3)
                        + "; " + fr.getCourseInfo().get(courseId).get(4)
                        + "; " + fr.getCourseInfo().get(courseId).get(5)
                );
            }
            pw.close();
        }
        // catch the error
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * update the content into the profInfo.txt
     */
    public void updateProfInfo() {
        // update the profInfo txt
        try {
            FileWriter fw = new FileWriter("profInfo.txt");
            PrintWriter pw = new PrintWriter(fw);
            // loop every student in the txt file
            for(String profUserName : fr.getProInfo().keySet()) {
                // rewrite the txt file
                pw.println( fr.getProInfo().get(profUserName).get(0)
                        + "; " + fr.getProInfo().get(profUserName).get(1)
                        + "; " + profUserName
                        + "; " + fr.getProInfo().get(profUserName).get(2)
                        + "; " + fr.getProInfo().get(profUserName).get(3));
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
     * @param password the password going to be checked
     * @return the password is right or not
     */
    @Override
    public boolean checkpassword(String password) {
        if(! password.equals(fr.getAdminInfo().get(getusername()).get(2))) {
            return false;
        }
        return true;
    }

    public String getPassword() {
        return password;
    }


}

