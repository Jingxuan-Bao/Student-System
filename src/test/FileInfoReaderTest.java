package test;

import files.FileInfoReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileInfoReaderTest {

    FileInfoReader fr;

    @BeforeEach
    void setup() {
        // set before each test
        fr = new FileInfoReader();
        fr.fileread();
    }

    // other methods all helper function for fileread
    // so only fileread method tested in there
    @Test
    void fileread() {
        // 1. test for Map<String, List<String>> AdminInfo;
        assertEquals(fr.getAdminInfo().get("admin01").get(1), "admin");
        assertEquals(fr.getAdminInfo().get("admin01").get(2), "password590");
        // 2. test for Map<String, List<String>> CourseInfo;
        assertEquals(fr.getCourseInfo().get("CIT590").get(1), "Brandon L Krakowsky");
        // 3. test for Map<String, List<String>> ProInfo
        assertEquals(fr.getProInfo().get("Smith").get(0), "Harry Smith");
        // 4. test for Map<String, List<String>> StudentInfo
        assertEquals(fr.getStudentInfo().get("testStudent02").get(0), "002");
        // 5. test for Map<String, List<String>> CourseInfoTech
        assertEquals(fr.getCourseInfoTech().get("Lee").get(0), "CIT595");
        // 6. test for Map<String, List<String>> CourseInfoStudent
        assertEquals(fr.getCourseInfoStudent().get("CIT593").get(0), "testStudent02");
    }

}