package com.example.howtodoinjava.springhystrixschoolservice.controller;

import com.example.howtodoinjava.springhystrixschoolservice.service.CustomServiceDelegate;
import com.example.howtodoinjava.springhystrixschoolservice.service.MasterServiceDelegate;
import com.example.howtodoinjava.springhystrixschoolservice.service.StudentServiceDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    StringBuilder builder = new StringBuilder();

    @GetMapping(value = "/getMasterDetails")
    public String getSchoolDetails() {
        callSellerBySellerInManualConfigForGroups();
        return builder.toString();
    }

    private void call300Sellers(){
        for (int i = 1; i < 300; i++) {
            builder.append(studentServiceDelegate.executeRequest(urlStudent, String.format("Group seller"), String.format("seller %d", i), urlAlternativeFlow));
            builder.append("<br />");
            builder.append("<br />");
        }
    }

    private void callSellersInGroups() {
        int group = 1;
        int seller = 1;
        int sellerCount = 10;

        for (;group < 10; group++) {
            for (;seller < sellerCount; seller++) {
                builder.append(studentServiceDelegate.executeRequest(urlStudent, String.format("%d Group seller", group), String.format("%d seller", seller), urlAlternativeFlow));
                builder.append("<br />");
                builder.append("<br />");
            }
            seller = seller;
            sellerCount += 30;
        }
    }

    private void callSellerBySellerInManualConfigForGroups() {
        builder.append(masterServiceDelegate.executeRequest(urlMaster, "Master, Student and Custom", "MASTER KEY", urlStudent));
        builder.append("<br />");
        builder.append("<br />");
        builder.append(studentServiceDelegate.executeRequest(urlStudent, "Student", "STUDENT KEY", ""));
        builder.append("<br />");
        builder.append("<br />");
        builder.append(customServiceDelegate.executeRequest(urlAlternativeFlow, "Master, Student and Custom","CUSTOM KEY", urlMaster));
    }
}
