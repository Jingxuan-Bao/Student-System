package test;

import files.FileInfoReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roles.Professor;
import roles.Student;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    // the fullname of the prof
    String fullname;
    // the id of the prof
    String id;
    // the last name of the prof
    String lastname;
    // the password of the prof
    String password;
    // the FileInfoReader
    FileInfoReader fr;
    // the prof
    Professor prof1;
    // the prof
    Professor prof2;

    @BeforeEach
    void setup() {
        fr = new FileInfoReader();
        fr.fileread();
        prof1 = new Professor(fr, "Krakowsky");
        prof2 = new Professor(fr, "Bhusnurmath");

    }

    @Test
    void viewcourses() {
        // 1. test for prof1 Brandon L Krakowsky
        assertEquals(fr.getCourseInfoTech().get("Krakowsky").get(0), "CIT590");

        // 2. test for prog2 Arvind Bhusnurmath
        assertEquals(fr.getCourseInfoTech().get("Bhusnurmath").get(0), "CIT591");

    }

    @Test
    void viewstudentlist() {
        // 1. test for prof1 Brandon L Krakowsky
        // add a student to CIT590
        Student studenttest1 = new Student(fr, "testStudent01");
        studenttest1.addcourse("CIT590");
        assertEquals(fr.getCourseInfoStudent().get("CIT590").size(), 1);
        assertEquals(fr.getCourseInfoStudent().get("CIT590").get(0), "testStudent01");

        // 2. test for prof2 Arvind Bhusnurmath
        // add a student to CIT591
        studenttest1.addcourse("CIT591");
        assertEquals(fr.getCourseInfoStudent().get("CIT591").size(), 1);
        assertEquals(fr.getCourseInfoStudent().get("CIT591").get(0), "testStudent01");

    }

    @Test
    void checkpassword() {
        fr = new FileInfoReader();
        fr.fileread();
        prof1 = new Professor(fr, "Krakowsky");
        prof2 = new Professor(fr, "Bhusnurmath");
        // 1. test for prof1 Brandon L Krakowsky
        assertTrue(prof1.checkpassword("password590"));
        assertFalse(prof1.checkpassword("password5901"));

        // 2. test for prof2 Arvind Bhusnurmath
        assertTrue(prof2.checkpassword("password590"));
        assertFalse(prof2.checkpassword("password5901"));
    }
}