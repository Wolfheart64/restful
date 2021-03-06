package com.rest.services.restful.controllers;

import com.rest.services.restful.PatchHelper;
import com.rest.services.restful.PatchMediaType;
import com.rest.services.restful.configurations.OrikaMapperConfig;
import com.rest.services.restful.entities.EmployeeDto;
import com.rest.services.restful.entities.EmployeeRepository;
import com.rest.services.restful.services.EmployeeService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.json.JsonPatch;
import java.net.URI;

@RestController
@RequestMapping("/")
public class EmployeesController {

    private final EmployeeService employeeService;

    public EmployeesController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/ping")
    @ResponseStatus(code = HttpStatus.OK)
    public String ping() {
        return "This service is up";
    }

    @GetMapping("/employees/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EmployeeDto hello(@PathVariable int id) throws HttpServerErrorException {
        if (id == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return employeeService.getEmployee(id);
    }


    @PostMapping("/employees")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<EmployeeDto> addEmployeeDto(@RequestBody EmployeeDto employeeDto) {

        EmployeeDto newEmployeeDto = employeeService.addEmployeeDto(employeeDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEmployeeDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(newEmployeeDto);
    }

    @PutMapping("/employees/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public EmployeeDto putEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable Integer id) {

        return employeeService.putEmployee(employeeDto, id);
    }

    @PatchMapping(value = "employees-map/{id}", consumes = PatchMediaType.APPLICATION_JSON_PATCH_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDto patchEmployee(@PathVariable Integer id, @RequestBody JsonPatch JsonPatchDocument) throws Exception {

        return employeeService.patchMapEmployee(id, JsonPatchDocument);
    }

}
