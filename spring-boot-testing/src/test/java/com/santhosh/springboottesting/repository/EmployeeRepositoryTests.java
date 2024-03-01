package com.santhosh.springboottesting.repository;

import com.santhosh.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;

    @BeforeEach
    public void setUp() {
        employee1 = Employee.builder()
                .firstName("Santhosh")
                .lastName("Naroju")
                .email("santhoshnaroju@gmail.com")
                .build();
    }

    //Junit test for save employee object
    @DisplayName("Junit test for save employee object")
    @Test
    public void givenEmployeeObject_whenSaved_thenReturnSavedEmployee(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();

        //when - action or behaviour that we are going to perform
        Employee savedEmployee = employeeRepository.save(employee1);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit test for get all employee operation
    @DisplayName("Junit test for get all employee operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        Employee employee2 = Employee.builder()
                .firstName("Ved")
                .lastName("Sharma")
                .email("vedsharma@gmail.com")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when - action or behaviour that we are going to perform
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit test for get employee by id
    @DisplayName("Junit test for get employee by id")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        employeeRepository.save(employee1);

        // when - action or behaviour that we are going to perform
        Employee employeeDB = employeeRepository.findById(employee1.getId()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //Junit test for getEmployee by email operation
    @DisplayName("Junit test for getEmployee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        employeeRepository.save(employee1);

        //when - action or behaviour that we are going to perform
        Employee employeeDB = employeeRepository.findByEmail(employee1.getEmail()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //Junit test for update employee object
    @DisplayName("Junit test for update employee object")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        employeeRepository.save(employee1);

        // when - action or behaviour that we are going to perform
        Employee savedEmployee = employeeRepository.findById(employee1.getId()).get();
        savedEmployee.setEmail("santhosh@gmail.com");
        savedEmployee.setFirstName("santhu");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("santhosh@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("santhu");
    }

    //Junit test for delete employee operation
    @DisplayName("Junit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        employeeRepository.save(employee1);

        //when - action or behaviour that we are going to perform
        employeeRepository.deleteById(employee1.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee1.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    //Junit test for custom query using JPQL with index
    @DisplayName("Junit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        employeeRepository.save(employee1);
        String firstName = "Santhosh";
        String lastName = "Naroju";

        //when - action or behaviour that we are going to perform
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for custom query using JPQL with Named Params
    @DisplayName("Junit test for custom query using JPQL with Named Params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        employeeRepository.save(employee1);
        String firstName = "Santhosh";
        String lastName = "Naroju";

        //when - action or behaviour that we are going to perform
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for custom query using JPQL with index
    @DisplayName("Junit test for custom query using Native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        employeeRepository.save(employee1);

        //when - action or behaviour that we are going to perform
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee1.getFirstName(), employee1.getLastName());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for custom query using Native SQL with Named Params
    @DisplayName("Junit test for custom query using Native SQL with Named Params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Santhosh")
//                .lastName("Naroju")
//                .email("santhoshnaroju@gmail.com")
//                .build();
        employeeRepository.save(employee1);

        //when - action or behaviour that we are going to perform
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee1.getFirstName(), employee1.getLastName());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
