package pe.edu.vallegrande.vgmsassignmentstaff.application.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.dto.AssignmentStaffDto;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.dto.DidacticUnit;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.dto.InstitucionalStaff;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.model.AssignmentStaff;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.repository.AssignmentStaffRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AssignmentStaffService {

    private final AssignmentStaffRepository assignmentStaffRepository;
    private final ExternalService externalService;

    public AssignmentStaffService(AssignmentStaffRepository assignmentStaffRepository,
            ExternalService externalService) {
        this.assignmentStaffRepository = assignmentStaffRepository;
        this.externalService = externalService;
    }

    public Flux<AssignmentStaffDto> findByStatus(String status) {
        log.info("Buscando asignaciones con estado {}", status);
        return assignmentStaffRepository.findByStatus(status)
                .flatMap(this::converTo)
                .collectList()
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<AssignmentStaff> create(AssignmentStaff assignmentStaff) {
        return externalService.validateTeacher(assignmentStaff.getTeacher())
                .flatMap(staff -> {
                    if (!"docente".equalsIgnoreCase(staff.getRol())) {
                        return Mono.error(new IllegalArgumentException("El staff con ID "
                                + staff.getId_institucional_staff() + " no tiene el rol de Docente"));
                    }
                    // Buscar si el docente ya tiene una asignación
                    return assignmentStaffRepository.findByTeacher(assignmentStaff.getTeacher())
                            .flatMap(existingAssignment -> {
                                // Verificar si la unidad didáctica ya está asignada al docente
                                if (existingAssignment.getDidacticUnit()
                                        .contains(assignmentStaff.getDidacticUnit().get(0))) {
                                    return Mono.error(new IllegalArgumentException(
                                            "La unidad didáctica ya está asignada a este docente"));
                                }
                                // Si no está asignada, añadirla a la lista
                                existingAssignment.getDidacticUnit().add(assignmentStaff.getDidacticUnit().get(0));
                                log.info("El docente ya existe. Se le asignará una nueva unidad didáctica");
                                return assignmentStaffRepository.save(existingAssignment);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                // Si el docente no existe, crear una nueva asignación
                                assignmentStaff.setStatus("A");
                                log.info("Asignando docente {} a la unidad didáctica {}", assignmentStaff.getTeacher(),
                                        assignmentStaff.getDidacticUnit().get(0));
                                return assignmentStaffRepository.save(assignmentStaff);
                            }));
                });
    }

    public Mono<AssignmentStaff> update(String id, AssignmentStaff assignmentStaff) {
        return assignmentStaffRepository.findByAssignmentIdAndStatus(id, "A")
                .next()
                .flatMap(existingAssignment -> {
                    existingAssignment.setTeacher(assignmentStaff.getTeacher());
                    existingAssignment.setDidacticUnit(assignmentStaff.getDidacticUnit());
                    log.info("Actualizando asignación con ID {}", id);
                    return assignmentStaffRepository.save(existingAssignment);
                });
    }

    public Mono<AssignmentStaff> changeStatus(String id, String status) {
        return assignmentStaffRepository.findById(id)
                .flatMap(assignment -> {
                    assignment.setStatus(status);
                    log.info("Cambiando estado de la asignación {} a {}", id, status);
                    return assignmentStaffRepository.save(assignment);
                });
    }

    public Mono<AssignmentStaff> getById(String id) {
        log.info("Buscando asignación con ID {}", id);
        return assignmentStaffRepository.findById(id);
    }

    public Flux<DidacticUnit> getDidacticUnits() {
        log.info("Obteniendo unidades didácticas");
        return externalService.getDidacticUnits();
    }

    public Flux<InstitucionalStaff> getInstitucionalStaff() {
        log.info("Obteniendo staff institucional");
        return externalService.getInstitucionalStaff();
    }

    private Mono<AssignmentStaffDto> converTo(AssignmentStaff assignment) {
        AssignmentStaffDto dto = new AssignmentStaffDto();
        dto.setAssignmentId(assignment.getAssignmentId());
        dto.setStatus(assignment.getStatus());

        Mono<InstitucionalStaff> staffMono = externalService.validateTeacher(assignment.getTeacher());

        List<Mono<DidacticUnit>> didacticUnitMonos = assignment.getDidacticUnit().stream()
                .map(externalService::validateDidacticUnit)
                .toList();

        Mono<List<DidacticUnit>> didacticUnitsMono = Flux.merge(didacticUnitMonos)
                .collectList();

        return Mono.zip(staffMono, didacticUnitsMono)
                .map(tuple -> {
                    InstitucionalStaff staff = tuple.getT1();
                    List<DidacticUnit> didacticUnits = tuple.getT2();

                    dto.setTeacher(staff);
                    dto.setDidacticUnit(didacticUnits);
                    return dto;
                });
    }

}
