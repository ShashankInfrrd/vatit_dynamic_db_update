package ai.infrrd.vatit_dynamic_db_update.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DataSupplier {
   @Id
    int studentId;
    String firstName;
    String secondName;
    String emailId;

    public DataSupplier(int studentId, String firstName, String secondName, String emailId) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.emailId = emailId;
    }


    public DataSupplier() {
    }



    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


}
