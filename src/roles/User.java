package roles;


import files.FileInfoReader;


/**
 * the user class
 * @Author Jingxuan Bao and Yaoqi Deng
 * @Time 2021-12-08
 */
public abstract class User {

    // FileInfoReader
    private FileInfoReader fr;

    // username
    private String username;


    /**
     * the constructor of User
     */
    public User(FileInfoReader fr, String username) {
        this.username = username;
    }

    /**
     * @return return the username of the user
     */
    public String getusername() {
        return username;
    }

    /**
     * Set the username
     * @param username
     */
    public void setusername(String username) {
        this.username = username;
    }

    /**
     * check the password is right or not
     * @Param password the password going to be checked
     * @Return return the password is right or not
     */
    public boolean checkpassword(String password) {
        if(! password.equals(fr.getStudentInfo().get(getusername()).get(2))) {
            return false;
        }
        return true;
    }

}
