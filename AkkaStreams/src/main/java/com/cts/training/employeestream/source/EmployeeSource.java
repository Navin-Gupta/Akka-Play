package com.cts.training.employeestream.source;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.cts.training.employeestream.model.Employee;

public class EmployeeSource {
    // method that return source
    public Source<Employee, NotUsed> source(){
        return Source.range(1,100).map(val -> new Employee(val, "Dummy" + val));
    }
}
