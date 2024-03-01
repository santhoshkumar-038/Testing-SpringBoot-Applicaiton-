package com.santhosh.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santhosh.springboottesting.model.Employee;
import com.santhosh.springboottesting.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

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
        employeeRepository.saveAll(employeeList);

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
        Employee employee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId()));

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
        Long employeeId =1L;
        Employee employee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
        employeeRepository.save(employee);

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
        Employee savedEmployee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Santhu")
                .lastName("Naroju")
                .email("santhunaroju@gmail.com")
                .build();

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", savedEmployee.getId())
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
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Santhu")
                .lastName("Naroju")
                .email("santhunaroju@gmail.com")
                .build();

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
        Employee savedEmployee = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        //when - action or behaviour that we are going to perform
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", savedEmployee.getId()));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
