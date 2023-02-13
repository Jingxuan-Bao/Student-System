package roles;

import files.FileInfoReader;

import java.util.List;

/**
 * the student class extends the user class
 *  @Author Jingxuan Bao and Yaoqi Deng
 *  @Time 2021-12-08
 */
public class Professor extends User{
    // the fullname of the prof
    private String fullname;
    // the id of the prof
    private String id;
    // the last name of the prof
    private String lastname;
    // the password of the prof
    private String password;
    // the FileInfoReader
    private FileInfoReader fr;

    /**
     * the constructor of the prof
     * @param fr the fileinforeader of prof
     * @param username the fullname of prof
     */
    public Professor(FileInfoReader fr, String username) {
        super(fr, username);
        // set the basic variables
        this.fullname = fr.getProInfo().get(username).get(0);
        this.id = fr.getProInfo().get(username).get(1);
        this.password = fr.getProInfo().get(username).get(2);
    }

    /**
     * view the courses the teacher is taking
     */
    public void viewcourses() {
        // get all the couses user is taking
        List<String> courses = fr.getCourseInfoTech().get(getusername());
        // loop the courses
        // print the courses information
        for(int i = 0; i < courses.size(); i ++) {
            System.out.println(courses.get(i) + "|" + fr.getCourseInfo().get(courses.get(i)).get(0)
                    + " " + fr.getCourseInfo().get(courses.get(i)).get(1)
                    + " " + fr.getCourseInfo().get(courses.get(i)).get(2)
                    + " " + fr.getCourseInfo().get(courses.get(i)).get(3)
                    + " " + fr.getCourseInfo().get(courses.get(i)).get(4)
                    + " " + fr.getCourseInfo().get(courses.get(i)).get(5));
        }
    }

    /**
     * view the student list for the particular course
     * @Param course the course going to be checked
     */
    public void viewstudentlist(String course) {
        // if the course the teacher do not take
        if(! fr.getCourseInfoTech().get(getusername()).contains(course)) {
            System.out.println("Sorry, you do not tech this course.");
            return;
        }
        // if there is no student in this course
        if(! fr.getCourseInfoStudent().containsKey(course)) {
            System.out.println("Sorry, no student in this course.");
            return;
        }
        // print the students base on the CourseInfoStudent Map
        if(fr.getCourseInfoStudent().containsKey(course)) {
            List<String> students = fr.getCourseInfoStudent().get(course);
            for(String student : students) {
                System.out.println(fr.getStudentInfo().get(student).get(0)
                                   + " " + fr.getStudentInfo().get(student).get(1));
            }
        }
    }

    /**
     * check the password is right or not
     * @param password the password going to be checked
     * @return the password is right or not
     */

    @Override
    public boolean checkpassword(String password) {
        if(! password.equals(fr.getProInfo().get(getusername()).get(2))) {
            return false;
        }
        return true;
    }

    /**
     * set the fullname of the prof
     * @param username the input username as the fullname of the prof
     */
    @Override
    public void setusername(String username) {
        this.fullname = username;
    }
}
