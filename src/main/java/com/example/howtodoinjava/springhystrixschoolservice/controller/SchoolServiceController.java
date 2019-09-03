package com.example.howtodoinjava.springhystrixschoolservice.controller;

import com.example.howtodoinjava.springhystrixschoolservice.service.CustomServiceDelegate;
import com.example.howtodoinjava.springhystrixschoolservice.service.MasterServiceDelegate;
import com.example.howtodoinjava.springhystrixschoolservice.service.StudentServiceDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchoolServiceController {

    @Autowired
    MasterServiceDelegate masterServiceDelegate ;

    @Autowired
    StudentServiceDelegate studentServiceDelegate ;

    @Autowired
    CustomServiceDelegate customServiceDelegate;

    @RequestMapping(value = "/getSchoolDetails/{url}", method = RequestMethod.GET)
    public String getStudents(@PathVariable String url) {
        System.out.println("Going to call student service to get data!");

        String urlMaster = "http://localhost:7078/getMasterDetailsForSchool/abcschool";
        String urlStudent = "http://localhost:8098/getStudentDetailsForSchool/abcschool";
        String urlMasterGenerics = "http://localhost:7078/getMasterDetailsForSchool/abcschool";

        StringBuilder builder = new StringBuilder();

        builder.append(masterServiceDelegate.excuteRequest(urlMaster, "Master and Student", "MASTER KEY", "CIRCUIT BREAKER ENABLED MASTER SERVICE!!! "));
        builder.append("<br />");
        builder.append("<br />");
        builder.append(studentServiceDelegate.excuteRequest(urlStudent, "Master and Student", "STUDENT KEY", "CIRCUIT BREAKER ENABLED STUDENT SERVICE!!! "));
        builder.append("<br />");
        builder.append("<br />");
        builder.append(customServiceDelegate.excuteRequest(urlMasterGenerics, "Custom","CUSTOM KEY", "CIRCUIT BREAKER ENABLED CUSTOM SERVICE!!! "));

        return builder.toString();
    }

}
