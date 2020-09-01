package tech.iwish.taxi.other;

public class User_DetailsList {

    private String name ;
    private String email;
    private String contact ;
    private String refer_code ;

    public User_DetailsList(String name, String email, String contact, String refer_code) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.refer_code = refer_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRefer_code() {
        return refer_code;
    }

    public void setRefer_code(String refer_code) {
        this.refer_code = refer_code;
    }
}
