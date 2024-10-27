package pe.edu.vallegrande.vgmsassignmentstaff.domain.model;

import lombok.Data;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "teacher_assignmet")
public class AssignmentStaff {

    @Id
    private String assignmentId;
    private String teacher;
    List<String> didacticUnit;
    private String status;

}
