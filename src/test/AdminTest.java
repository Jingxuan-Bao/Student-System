package test;

import files.FileInfoReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roles.Admin;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    //student object
    Admin admin1;

    //student object
    Admin admin2;

    // the FileInfoReader
    FileInfoReader fr;

    // the name of the student
    String name;

    // the id of the student
    String id;

    // the password of the student
    String password;


    @BeforeEach
    void setup() {
        fr = new FileInfoReader();
        fr.fileread();
        admin1 = new Admin(fr, "admin01");
        admin2 = new Admin(fr, "admin02");
    }

    @Test
    void addCourse() {
        assertTrue(admin1.addCourse("Game", "ABC666", "MW", "5:00", "7:00", "200", "Krakowsky"));
        assertFalse(admin1.addCourse("Math", "CIT590", "MW", "5:00", "7:00", "200", "Krakowsky"));
        assertFalse(admin1.addCourse("Nothing", "CIT666", "MW", "17:30", "20:00", "200", "Krakowsky"));
        assertTrue(fr.getCourseInfo().containsKey("ABC666"));
        admin1.addCourse("Happy", "ZZZ888", "W", "0:00", "0:30", "200", "Krakowsky");
        assertTrue(fr.getCourseInfo().containsKey("ZZZ888"));

    }

    @Test
    void timeConflict() {
        assertTrue(admin1.timeConflict("Krakowsky", "MW", "17:30", "20:00"));
        assertFalse(admin1.timeConflict("Krakowsky", "F", "0:30", "2:00"));

    }

    @Test
    void timeHelp() {
        assertTrue(admin1.timeHelp("0:00", "5:00", "2:00", "7:00"));
        assertFalse(admin1.timeHelp("0:00", "1:00", "2:00", "3:00"));

    }

    @Test
    void timeToInt() {
        assertEquals(admin1.timeToInt("13:00"), 1300);
        assertEquals(admin1.timeToInt("19:30"), 1930);
    }

    @Test
    void addProf(String name, String ID, String userName, String password) {
        assertTrue(admin1.addProf("DYQ", "666", "smart", "6666"));
        assertTrue(admin1.addProf("BJX", "888", "sb", "6666"));
        assertFalse(admin1.addProf("Krakowsky", "999", "Krakowsky", "6666"));
        assertFalse(admin1.addProf("HAHAHA", "029", "Random", "6666"));

    }

    @Test
    void addStudent() {
        ArrayList<String> DYQ = new ArrayList<>();
        ArrayList<String> BJX = new ArrayList<>();

        DYQ.add("CIT590: A+");
        DYQ.add("CIS545: A+");
        BJX.add("CIT590: C-");
        assertTrue(admin1.addStudent("DYQ", "666", "smart", "6666", DYQ));
        assertTrue(fr.getStudentInfo().containsKey("smart"));
        admin2.addStudent("BJX", "888", "sb", "6666", BJX);
        assertFalse(fr.getStudentInfo().containsKey("sb"));

        assertFalse(admin1.addStudent("DYQ", "666", "smart", "6666", DYQ));

    }

    @Test
    void deleteCourse(String CourseId) {
        assertTrue(admin1.deleteCourse("ABC666"));
        assertFalse(admin1.deleteCourse("DYQ666"));
        admin2.deleteCourse("ZZZ888");
        assertFalse(fr.getCourseInfo().containsKey("ZZZ888"));

    }

    @Test
    void deleteStudent(String studentUserName) {
        admin1.deleteStudent("smart");
        assertFalse(fr.getStudentInfo().containsKey("smart"));
        admin1.deleteStudent("sb");
        assertFalse(fr.getStudentInfo().containsKey("smart"));

        assertFalse(admin1.deleteStudent("nobody"));

    }

    @Test
    void deleteProf(String profUserName) {
        admin1.deleteProf("smart");
        assertFalse(fr.getProInfo().containsKey("smart"));
        admin1.deleteProf("sb");
        assertFalse(fr.getProInfo().containsKey("smart"));

        assertFalse(admin1.deleteProf("nobody"));

    }


    @Test
    void checkpassword() {
        // 1. test for admin1
        assertEquals(admin1.getPassword(), "password590");
        // test for an incorrect password
        assertFalse(admin2.getPassword().equals("password599"));

        // 2. test for admin2
        assertEquals(admin2.getPassword(), "password590");

    }


}




