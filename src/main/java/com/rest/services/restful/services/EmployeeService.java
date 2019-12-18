package com.rest.services.restful.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.services.restful.entities.EmployeeDto;
import com.rest.services.restful.entities.EmployeeEntity;
import com.rest.services.restful.entities.EmployeeRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final MapperFacade mapperFacade;

    private final ObjectMapper objectMapper;

    public EmployeeService(EmployeeRepository employeeRepository, MapperFacade mapperFacade, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.mapperFacade = mapperFacade;
        this.objectMapper = objectMapper;
    }

    public EmployeeDto getEmployee(int id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id);

        return mapperFacade.map(employeeEntity, EmployeeDto.class);
    }

    public EmployeeDto addEmployeeDto(EmployeeDto employeeDto) {

        EmployeeEntity employeeEntity = mapperFacade.map(employeeDto, EmployeeEntity.class);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

        return mapperFacade.map(savedEmployeeEntity, EmployeeDto.class);
    }

    public EmployeeDto putEmployee(EmployeeDto employeeDto, Integer id) {

        EmployeeEntity employeeEntity = mapperFacade.map(employeeDto, EmployeeEntity.class);


        return employeeRepository.findById(id).map(employeeEntity1 -> {
            employeeEntity1.setName(employeeEntity.getName());
            employeeEntity1.setDescription(employeeEntity.getDescription());

            EmployeeDto employeeDtoResponse = mapperFacade.map(employeeRepository.save(employeeEntity1), EmployeeDto.class);
            return employeeDtoResponse;
        })
                .orElseGet(() -> {
                    employeeEntity.setId(id);
                    EmployeeDto employeeDtoResponse = mapperFacade.map(employeeRepository.save(employeeEntity), EmployeeDto.class);
                    return employeeDtoResponse;
                });
    }

    public ResponseEntity patchEmployee(EmployeeDto employeeDto, Integer id) {

        EmployeeEntity employeeEntity = mapperFacade.map(employeeDto, EmployeeEntity.class);

        return employeeRepository.findById(id).map(employeeEntity1 -> {
            employeeEntity1.setName(employeeEntity.getName());
            employeeEntity1.setDescription(employeeEntity.getDescription());
            employeeRepository.save(employeeEntity1);
            return ResponseEntity.ok(HttpStatus.OK);
        })
                .orElseGet(() -> {
                    employeeEntity.setId(id);
                    employeeRepository.save(employeeEntity);
                    return ResponseEntity.ok(HttpStatus.CREATED);
                });

    }

    public void patchMapEmployee(Map<String, Object> employeeDto, int id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id);

    }

}