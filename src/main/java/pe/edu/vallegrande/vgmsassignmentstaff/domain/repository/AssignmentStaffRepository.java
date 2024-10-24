package pe.edu.vallegrande.vgmsassignmentstaff.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.model.AssignmentStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AssignmentStaffRepository extends ReactiveMongoRepository<AssignmentStaff, String> {
    Flux<AssignmentStaff> findByStatus(String status);
    Mono<AssignmentStaff> findByTeacher(String teacher);
    Flux<AssignmentStaff> findByAssignmentIdAndStatus(String assignmentId, String status);
}
