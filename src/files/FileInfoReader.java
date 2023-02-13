package files;

// library import
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileInfoReader class used to read data from different files.
 * @Author Jingxuan Bao and Yaoqi Deng
 * @Time 2021-12-08
 */
public class FileInfoReader {

    // Map to store the information in AdminInfo file
    private Map<String, List<String>> AdminInfo;

    // Map to store the information in CourseInfo file
    private Map<String, List<String>> CourseInfo;

    // Map to store the information in ProInfo file
    private Map<String, List<String>> ProInfo;

    // Map to store the information in studentInfo file
    private Map<String, List<String>> StudentInfo;

    // Map to store the relation between teacher and the course the teacher teach
    private Map<String, List<String>> CourseInfoTech;

    // Map to store the relation between the course and the students who take the course
    private Map<String, List<String>> CourseInfoStudent;

    // A link to store the relation between the content data and different map
    private Map<String, Map<String, List<String>>> link;

    /**
     * the method build the link
     * help the loop to read the data
     */
    public void buildlink() {
        // create the HashMap
        link = new HashMap<>();
        AdminInfo = new HashMap<>();
        CourseInfo = new HashMap<>();
        ProInfo = new HashMap<>();
        StudentInfo = new HashMap<>();
        // build the link through the link map
        this.link.put("adminInfo.txt", AdminInfo);
        this.link.put("courseInfo.txt", CourseInfo);
        this.link.put("profInfo.txt", ProInfo);
        this.link.put("studentInfo.txt", StudentInfo);
    }

    /**
     * read the information in courseinfo file
     */
    public void fileread() {
        // create a string array
        String[] files = new String[4];
        // loop the string
        files[0] = "adminInfo.txt";
        files[1] = "courseInfo.txt";
        files[2] = "profInfo.txt";
        files[3] = "studentInfo.txt";
        // build the link relation
        this.buildlink();
        // go to the loop
        for(String file : files) {
            //create a File object
            File myFile = new File(file);
            //create a FileReader with the given File object
            try {
                //create a BufferedReader, which takes a FileReader as an argument
                BufferedReader bufferedReader = new BufferedReader( new FileReader(myFile));

                String s = bufferedReader.readLine();

                while(s != null) {
                    // create a string array
                    String[] strs = s.split(";");
                    // create am empty list to store value in map
                    List<String> list = new ArrayList<>();
                    // loop the strs array
                    // if the file is studentInfo, profoInfo, or adminInfo
                    // set the student username as the key
                    // another information as an array list
                    if(file.equals("studentInfo.txt") || file.equals("profInfo.txt") || file.equals("adminInfo.txt")) {
                        for(int i = 0; i < strs.length; i ++) {
                            if(i == 2) {
                                continue;
                            }
                            list.add(strs[i].trim());
                        }
                        // add the info into the map
                        link.get(file).put(strs[2].trim(), list);
                    }
                    // if the file is not studentInfo, profoInfo, or adminInfo
                    // set the fist String as key
                    else {
                        for(int i = 1; i < strs.length; i ++) {
                            list.add(strs[i].trim());
                        }
                        // add the info into the map
                        link.get(file).put(strs[0].trim(), list);
                    }
                    // continue read next line
                    s = bufferedReader.readLine();
                }
                // close reader
                bufferedReader.close();
            }
            // catch the IO error
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        buildTechMap();
        buildCourseStudentMap();
    }

    /**
     * build a CourseInfoTech to store the relation between teacher (key) and the course the teacher teach (value)
     */
    public void buildTechMap() {
        // create the hashmap
        CourseInfoTech = new HashMap<>();
        try {
            // read the file
            BufferedReader bufferedReader = new BufferedReader( new FileReader("courseInfo.txt"));
            String s = bufferedReader.readLine();
            while(s != null) {
                // loop every line
                String[] strs = s.split(";");
                String[] strsTemp = strs[2].split(" ");
                // check whether the teacher already in the map
                if(! CourseInfoTech.containsKey(strsTemp[strsTemp.length - 1].trim())) {
                    List<String> list = new ArrayList<>();
                    list.add(strs[0]);
                    CourseInfoTech.put(strsTemp[strsTemp.length - 1].trim(), list);
                }
                else{
                    CourseInfoTech.get(strsTemp[strsTemp.length - 1].trim()).add(strs[0]);
                }
                s = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        // catch the error
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * build a CourseInfoTech to store the relation between course (key) and the students take the course (value)
     */
    public void buildCourseStudentMap() {
        // create the hashmap
        CourseInfoStudent = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader( new FileReader("studentInfo.txt"));
            String s = bufferedReader.readLine();
            while(s != null) {
                // loop every line
                String[] strs = s.split(";");
                String[] courses = strs[4].split(",");
                // check whether the course already in the map
                for(int i = 0; i < courses.length; i ++) {
                    if(! CourseInfoStudent.containsKey(courses[i].split(":")[0].trim())) {
                        List<String> list = new ArrayList<>();
                        list.add(strs[2].trim());
                        CourseInfoStudent.put(courses[i].split(":")[0].trim(), list);
                    }
                    else{
                        CourseInfoStudent.get(courses[i].split(":")[0].trim()).add(strs[2].trim());
                    }
                }
                s = bufferedReader.readLine();
            }
            bufferedReader.close();

        }
        // catch the error
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return return the AdminInfo Map
     */
    public Map<String, List<String>> getAdminInfo() {
        return AdminInfo;
    }

    /**
     * @return return the CourseInfo Map
     */
    public Map<String, List<String>> getCourseInfo() {
        return CourseInfo;
    }

    /**
     * @return return the ProInfo Map
     */
    public Map<String, List<String>> getProInfo() {
        return ProInfo;
    }

    /**
     * @return reutnr the StudentInfo Map
     */
    public Map<String, List<String>> getStudentInfo() {
        return StudentInfo;
    }

    /**
     * @return return the CourseInfoTech Map
     */
    public Map<String, List<String>> getCourseInfoTech() {
        return CourseInfoTech;
    }

    /**
     * @return return the CourseInfoStudent Map
     */
    public Map<String, List<String>> getCourseInfoStudent() {
        return CourseInfoStudent;
    }

}
