package pl.redhat.samples.insurance.controller;

import org.springframework.web.bind.annotation.*;
import pl.redhat.samples.insurance.client.PersonClient;
import pl.redhat.samples.insurance.domain.Insurance;
import pl.redhat.samples.insurance.domain.InsuranceDetails;
import pl.redhat.samples.insurance.repository.InsuranceRepository;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/insurances")
public class InsuranceController {

    private PersonClient personClient;
    private InsuranceRepository repository;

    public InsuranceController(PersonClient personClient, InsuranceRepository repository) {
        this.personClient = personClient;
        this.repository = repository;
    }

    @GetMapping
    public List<Insurance> getAll() {
        // TODO - implement
        return new ArrayList<>(0);
    }

    @GetMapping("/{id}")
    public Insurance getById(@PathVariable("id") Integer id) {
        // TODO - implement
        return null;
    }

    @PostMapping
    public Insurance addNew(@RequestBody Insurance person) {
        // TODO - implement
        return null;
    }

    @GetMapping("/{id}/details")
    public InsuranceDetails getInsuranceDetailsById(@PathParam("id") Long id) {
        // TODO - implement
        return null;
    }
}
