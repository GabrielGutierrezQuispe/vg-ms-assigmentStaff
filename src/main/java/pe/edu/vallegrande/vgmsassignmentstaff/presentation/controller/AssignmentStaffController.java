package pe.edu.vallegrande.vgmsassignmentstaff.presentation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vgmsassignmentstaff.application.service.AssignmentStaffService;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.dto.AssignmentStaffDto;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.dto.DidacticUnit;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.dto.InstitucionalStaff;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.model.AssignmentStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/management/${api.version}/assignment-staff")
public class AssignmentStaffController {

    private final AssignmentStaffService assignmentStaffService;

    public AssignmentStaffController(AssignmentStaffService assignmentStaffService) {
        this.assignmentStaffService = assignmentStaffService;
    }

    @GetMapping("/list/active")
    public Flux<AssignmentStaffDto> getAllActive() {
        return assignmentStaffService.findByStatus("A");
    }

    @GetMapping("/list/inactive")
    public Flux<AssignmentStaffDto> getAllInactive() {
        return assignmentStaffService.findByStatus("I");
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AssignmentStaff> assignTeacherToUnit(@RequestBody AssignmentStaff assignmentStaff) {
        return assignmentStaffService.create(assignmentStaff);
    }

    @GetMapping("/{id}")
    public Mono<AssignmentStaff> getById(@PathVariable String id){
        return assignmentStaffService.getById(id);
    }

    @PutMapping("/update/{id}")
    public Mono<AssignmentStaff> updateAssignment(@PathVariable String id, @RequestBody AssignmentStaff assignmentStaff){
        return assignmentStaffService.update(id, assignmentStaff);
    }

    @PutMapping("/inactive/{id}")
    public Mono<AssignmentStaff> inactiveAssignment(@PathVariable String id){
        return assignmentStaffService.changeStatus(id, "I");
    }

    @PutMapping("/active/{id}")
    public Mono<AssignmentStaff> activeAssignment(@PathVariable String id){
        return assignmentStaffService.changeStatus(id, "A");
    }

    @GetMapping("/didactic")
    public Flux<DidacticUnit> getDidacticUnits(){
        return assignmentStaffService.getDidacticUnits();
    }

    @GetMapping("/staff")
    public Flux<InstitucionalStaff> getInstitucionalStaff(){
        return assignmentStaffService.getInstitucionalStaff();
    }

}
