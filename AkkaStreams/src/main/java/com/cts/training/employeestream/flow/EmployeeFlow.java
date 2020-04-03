package com.cts.training.employeestream.flow;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import com.cts.training.employeestream.model.Employee;

public class EmployeeFlow {
    public Flow<Employee, Employee, NotUsed> flow(){
        return Flow.fromFunction(employee -> {
            employee.setDept("IT");
            return employee;
        });
    }
}
