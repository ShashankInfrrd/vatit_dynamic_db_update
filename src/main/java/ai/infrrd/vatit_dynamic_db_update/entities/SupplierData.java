package ai.infrrd.vatit_dynamic_db_update.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SupplierData {
    @Id
    @GeneratedValue
    int studentId;
    String firstName;
    String secondName;
    String emailId;

}
