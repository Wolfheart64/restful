package com.rest.services.restful.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.services.restful.PatchHelper;
import com.rest.services.restful.entities.EmployeeDto;
import com.rest.services.restful.entities.EmployeeEntity;
import com.rest.services.restful.entities.EmployeeRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import javax.json.JsonPatch;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final MapperFacade mapperFacade;

    private final PatchHelper patchHelper;

    public EmployeeService(EmployeeRepository employeeRepository, MapperFacade mapperFacade, ObjectMapper objectMapper, PatchHelper patchHelper) {
        this.employeeRepository = employeeRepository;
        this.mapperFacade = mapperFacade;
        this.patchHelper = patchHelper;
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

    public EmployeeDto patchMapEmployee(Integer id, JsonPatch jsonPatchDocument) throws Exception {
        EmployeeDto employeeDtoDataObject = getEmployee(id);
        EmployeeEntity employeeEntity = mapperFacade.map(employeeDtoDataObject, EmployeeEntity.class);
        EmployeeEntity employeeEntityPatched = patchHelper.patch(jsonPatchDocument, employeeEntity, EmployeeEntity.class);
        employeeRepository.save(employeeEntityPatched);
        return mapperFacade.map(employeeEntityPatched, EmployeeDto.class);

    }

}