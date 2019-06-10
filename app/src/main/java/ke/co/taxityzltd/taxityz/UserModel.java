package ke.co.taxityzltd.taxityz;


public class UserModel {
    // private String UserID;
    private String userMail;
    private String userPhone;
    private String name;

    public UserModel() {

    }

    public UserModel(String userMail, String userPhone, String name) {
        this.userMail = userMail;
        this.userPhone = userPhone;
        this.name = name;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}