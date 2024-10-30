package pe.edu.vallegrande.vgmsassignmentstaff.domain.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class InstitucionalStaff {

    @@Id
    private String id;
    private String fatherLastname;
    private String motherLastname;
    private String name;
    private LocalDate birthdate;
    private String documentType;
    private String documentNumber;
    private String status;
    private String rol;


}
