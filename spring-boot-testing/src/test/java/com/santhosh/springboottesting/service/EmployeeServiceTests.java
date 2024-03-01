package com.santhosh.springboottesting.service;

import com.santhosh.springboottesting.exception.ResourceNotFoundException;
import com.santhosh.springboottesting.model.Employee;
import com.santhosh.springboottesting.repository.EmployeeRepository;
import com.santhosh.springboottesting.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.module.ResolutionException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
    }

    // Junit test for saveEmployee method
    @Test
    @DisplayName("Junit test for saveEmployee method")
    public void givenEmployeeObject_whenSavedEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        System.out.println(employeeRepository);
        System.out.println(employeeService);

        //when - action or behaviour that we are going to perform
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);

        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    // Junit test for saveEmployee method which throws exception
    @Test
    @DisplayName("Junit test for saveEmployee method which throws exception")
    public void givenExistingEmail_whenSavedEmployee_thenThrowException() {
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
//        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
//        System.out.println(employeeRepository);
//        System.out.println(employeeService);

        //when - action or behaviour that we are going to perform
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    //Junit test for getAllEmployees method
    @Test
    @DisplayName("Junit test for getAllEmployees method")
    public void givenEmployeeObject_whenGetAllEmployees_thenReturnEmployees() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Ved")
                .lastName("Sharma")
                .email("vedsharma@gmail.com")
                .build();
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or behaviour that we are going to perform
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    // Junit test for getAllEmployees method (Negative Scenario)
    @DisplayName("Junit test for getAllEmployees method (Negative Scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Ved")
                .lastName("Sharma")
                .email("vedsharma@gmail.com")
                .build();
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or behaviour that we are going to perform
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    //Junit test for getEmployeeById Method
    @DisplayName("Junit test for getEmployeeById Method")
    @Test
    public void givenEmployeeObject_whenGetEmployeeById_thenReturnEmployeeObject() {
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or behaviour that we are going to perform
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    // Junit test for updateEmployeeMethod
    @DisplayName("Junit test for updateEmployeeMethod")
    @Test
    public void givenUpdatedEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("santhukumar@gmail.com");
        employee.setFirstName("santhu");

        //when - action or behaviour that we are going to perform
        Employee updatedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("santhukumar@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("santhu");
    }

    //Junit test for DeleteEmployee Method
    @DisplayName("Junit test for DeleteEmployee Method")
    @Test
    public void givenEmployeeObject_whenDeleteMethod_thenReturnNothing() {
        //given - precondition or setup
        Long employeeId = 1L;
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(employeeId);

        //when - action or behaviour that we are going to perform
        employeeService.DeleteEmployee(employeeId);

        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
