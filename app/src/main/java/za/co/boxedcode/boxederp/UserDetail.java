package za.co.boxedcode.boxederp;

public class UserDetail {
    public String firstname, surname, id_no, contact_no, user_type;

    public UserDetail(){

    }

    public UserDetail(String firstname, String surname, String id_no, String contact_no, String user_type) {
        this.firstname = firstname;
        this.surname = surname;
        this.id_no = id_no;
        this.contact_no = contact_no;
        this.user_type = user_type;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
