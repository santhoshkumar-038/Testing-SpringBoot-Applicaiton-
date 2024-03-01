package com.santhosh.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santhosh.springboottesting.model.Employee;
import com.santhosh.springboottesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTest{

    @Autowired
    private MockMvc mockMvc; // we use this to call rest api's

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper; // we use to convert object to json format

    // JSON test for createEmployee controller
    @DisplayName("JSON test for createEmployee controller")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_ThenReturnEmployeeObject() throws Exception{
        // given pre-condition or test
        Employee employee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation -> invocation.getArgument(0)));

        // when - any action performed
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify output using assert statements
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    //Junit test for Get All Employees method
    @DisplayName("Junit test for Get All Employees method")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeObject() throws Exception{
        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Santhosh").lastName("Naroju").email("santhoshnaroju@gmail").build());
        employeeList.add(Employee.builder().firstName("Ved").lastName("Sharma").email("vedsharma@gmail").build());
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeList);

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }

    //Positive Scenario - Valid employee id
    //Junit test for GetEmployeeById Method
    @DisplayName("Junit test for GetEmployeeById Method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception{
        //given - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Negative Scenario - InValid employee id
    //Junit test for GetEmployeeById Method
    @DisplayName("Junit test for GetEmployeeById Method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
        //given - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Junit test for update Employee Rest API - Positive Scenario
    @DisplayName("Junit test for update Employee Rest API")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception{
        //given - precondition or setup
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Santhu")
                .lastName("Naroju")
                .email("santhunaroju@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }

    // Junit test for update Employee Rest API - Negative Scenario
    @DisplayName("Junit test for update Employee Rest API")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception{
        //given - precondition or setup
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Santhu")
                .lastName("Naroju")
                .email("santhunaroju@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //Junit test for Delete Employee
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{
        //given - precondition or setup
        Long employeeId = 1L;
        BDDMockito.willDoNothing().given(employeeService).DeleteEmployee(employeeId);

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
