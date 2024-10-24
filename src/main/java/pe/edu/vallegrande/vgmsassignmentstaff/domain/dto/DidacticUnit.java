package pe.edu.vallegrande.vgmsassignmentstaff.domain.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class DidacticUnit {

    @Id
    private String didacticId;
    private String name;
    private String status;
}
