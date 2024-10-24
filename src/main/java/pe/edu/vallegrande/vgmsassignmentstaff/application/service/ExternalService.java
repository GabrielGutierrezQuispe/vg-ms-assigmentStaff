package pe.edu.vallegrande.vgmsassignmentstaff.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.dto.DidacticUnit;
import pe.edu.vallegrande.vgmsassignmentstaff.domain.dto.InstitucionalStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ExternalService {

    @Value("${services.didactic-unit.url}")
    private String didacticUnitUrl;

    @Value("${services.institucional-staff.url}")
    private String institucionalStaffUrl;

    private  final WebClient.Builder webClientBuilder;

    public  ExternalService(WebClient.Builder webClientBuilder){
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<InstitucionalStaff> validateTeacher(String staffId) {
        return fetchData(institucionalStaffUrl + "/get/",
                staffId, InstitucionalStaff.class);
    }

    public Mono<DidacticUnit> validateDidacticUnit(String didacticUnitId) {
        return fetchData(didacticUnitUrl + "/",
                didacticUnitId, DidacticUnit.class);
    }

    public Flux<DidacticUnit> getDidacticUnits() {
        return fetchDataList(didacticUnitUrl + "/list/active",
                DidacticUnit.class);
    }

    public Flux<InstitucionalStaff> getInstitucionalStaff() {
        return fetchDataList(institucionalStaffUrl + "/list/docente",
                InstitucionalStaff.class);
    }

    private <T> Mono<T> fetchData(String baseUrl, String id, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + id)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(e -> {
                    log.error("Error fetching data: ", e);
                    return Mono.empty();
                });
    }

    private <T> Flux<T> fetchDataList(String baseUrl, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl) 
                .retrieve()
                .bodyToFlux(responseType) 
                .onErrorResume(e -> {
                    log.error("Error fetching data: ", e);
                    return Flux.empty(); 
                });
    }
}
