package com.rest.services.restful.controllers;

import com.rest.services.restful.entities.EmployeeDto;
import com.rest.services.restful.services.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeesControllerTest {


    @InjectMocks
    EmployeesController employeesController;

    @Mock
    EmployeeService employeeService;

    @Test
    public void testPing() {
        assertEquals(employeesController.ping(), "This service is up");
    }

    @Test
    public void addEmployeeSuccess() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        EmployeeDto employeeDto = new EmployeeDto();
        EmployeeDto newEmployeeDto = createNewEmployeeDto();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEmployeeDto.getId())
                .toUri();


        when(employeeService.addEmployeeDto(employeeDto)).thenReturn(newEmployeeDto);

        ResponseEntity<EmployeeDto> employeeDtoResponseEntity = employeesController.addEmployeeDto(employeeDto);

        assertEquals(employeeDtoResponseEntity.getBody(), newEmployeeDto);
        assertEquals(employeeDtoResponseEntity.getHeaders().getLocation(), location);
    }

    @Test
    public void putEmployeeSuccess() {

    }

    private EmployeeDto createNewEmployeeDto() {
        EmployeeDto newEmployeeDto = new EmployeeDto();
        newEmployeeDto.setId(1);
        newEmployeeDto.setDescription("Managaer");
        newEmployeeDto.setName("Someboady");
        return newEmployeeDto;
    }
}