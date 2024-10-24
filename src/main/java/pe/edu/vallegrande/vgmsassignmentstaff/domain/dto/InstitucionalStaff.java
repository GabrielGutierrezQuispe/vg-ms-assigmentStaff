package pe.edu.vallegrande.vgmsassignmentstaff.domain.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class InstitucionalStaff {

    @Id
    private String id_institucional_staff;
    private String document_type;
    private String document_number;
    private String father_lastname;
    private String mother_lastname;
    private String name;
    private String rol;
    private String status;
}
