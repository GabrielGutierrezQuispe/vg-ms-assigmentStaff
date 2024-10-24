package pe.edu.vallegrande.vgmsassignmentstaff.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class AssignmentStaffDto {

    private String assignmentId;
    private InstitucionalStaff teacher;
    private List<DidacticUnit> didacticUnit;
    private String status;

}
