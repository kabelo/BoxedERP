package za.co.boxedcode.boxederp;

public class UserInformation {

    public String email, surname, contact_no, id_no, user_type, status, created_date;

    public UserInformation() {
    }

    public UserInformation(String email, String surname, String contact_no, String id_no, String user_type, String status, String created_date) {
        this.email = email;
        this.surname = surname;
        this.contact_no = contact_no;
        this.id_no = id_no;
        this.user_type = user_type;
        this.status = status;
        this.created_date = created_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
