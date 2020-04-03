package com.cts.training.employeestream.sink;

import akka.Done;
import akka.stream.javadsl.Sink;
import com.cts.training.employeestream.model.Employee;

import java.util.concurrent.CompletionStage;

public class EmployeeSink {
    public Sink<Employee, CompletionStage<Done>> sink(){
        return Sink.foreach(System.out :: println);
    }
}
