package test;

import files.FileInfoReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roles.Student;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    //student object
    Student student1;

    //student object
    Student student2;

    // the FileInfoReader
    FileInfoReader fr;

    // the name of the student
    String name;

    // the id of the student
    String id;

    // the password of the student
    String password;

    // the coursemap: course(key), grades(value) of the student
    Map<String, String> coursemap;

    @BeforeEach
    void setup() {
        // rewrite studentInfo.txt before each test
        try {
            FileWriter fw = new FileWriter("studentInfo.txt");
            PrintWriter pw = new PrintWriter(fw);
            pw.println("001; StudentName1; testStudent01; password590; CIS191: A, CIS320: A");
            pw.println("002; StudentName2; testStudent02; password590; CIT592: A, CIT593: A-");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fr = new FileInfoReader();
        fr.fileread();
        student1 = new Student(fr, "testStudent01");
        student2 = new Student(fr, "testStudent02");
    }

    @Test
    void addcourse() {
        // 1. test for student1
        assertFalse(student1.getCoursemap().containsKey("CIT590"));
        student1.addcourse("CIT590");
        assertTrue(student1.getCoursemap().containsKey("CIT590"));
        // test an unexist course
        assertFalse(student1.getCoursemap().containsKey("CIT889"));
        // test an already take course
        assertTrue(student1.getCoursemap().containsKey("CIS320"));

        // 2. test for student2
        assertFalse(student2.getCoursemap().containsKey("CIS195"));
        student2.addcourse("CIS195");
        assertTrue(student2.getCoursemap().containsKey("CIS195"));
    }

    @Test
    void dropcourse() {
        // test for student1
        assertTrue(student1.getCoursemap().containsKey("CIS191"));
        student1.dropcourse("CIS191");
        assertFalse(student1.getCoursemap().containsKey("CIS191"));
        // test a course not in the student list
        student1.dropcourse("CIS545");
        assertFalse(student1.getCoursemap().containsKey("CIS545"));

        // test for student2
        assertTrue(student2.getCoursemap().containsKey("CIT592"));
        student2.dropcourse("CIT592");
        assertFalse(student2.getCoursemap().containsKey("CIT592"));
    }

    @Test
    void checkpassword() {
        // 1. test for student1
        assertEquals(student1.getPassword(), "password590");
        // test for an incorrect password
        assertFalse(student1.getPassword().equals("password591"));

        // 2. test for sutdent2
        assertEquals(student2.getPassword(), "password590");

    }

    @Test
    void timeconflict() {
        // 1.test for sutdent1
        // test for adding an already exist course
        assertFalse(student1.timeconflict("CIS320"));
        // test for a course with time conflict
        // CIS455; Internet and Web Systems; Zachary Ives; MW; 15:00; 16:30; 100
        assertFalse(student1.timeconflict("CIS455"));

        // 2. test for student2
        // test for adding an already exist course
        assertFalse(student2.timeconflict("CIT592"));
        // test for a course withour time conflict
        // CIS191; Linux/Unix Skills; Swapneel Sheth; F; 13:30; 15:00; 15
        assertTrue(student2.timeconflict("CIS191"));
    }

}