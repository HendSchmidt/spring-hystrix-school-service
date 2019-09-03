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

    private final String urlMaster = "http://localhost:7078/getMasterDetailsForSchool/abcschool";
    private final String urlStudent = "http://localhost:8098/getStudentDetailsForSchool/abcschool";
    private final String urlAlternativeFlow = "http://localhost:8097/getStudentDetailsForSchool/abcschool";

    @RequestMapping(value = "/getSchoolDetails/{url}", method = RequestMethod.GET)
    public String getStudents(@PathVariable String url) {
        System.out.println("Going to call student service to get data!");

        StringBuilder builder = new StringBuilder();
        builder.append(masterServiceDelegate.excuteRequest(urlMaster, "Master, Student and Custom", "MASTER KEY", urlStudent));
        builder.append("<br />");
        builder.append("<br />");
        builder.append(studentServiceDelegate.excuteRequest(urlStudent, "Master, Student and Custom", "STUDENT KEY", ""));
        builder.append("<br />");
        builder.append("<br />");
        builder.append(customServiceDelegate.excuteRequest(urlAlternativeFlow, "Master, Student and Custom","CUSTOM KEY", urlMaster));

        return builder.toString();
    }

}
