package com.demo9.controller;

import com.demo9.model.Customer;
import com.demo9.service.customer.ICustomerService;
import com.demo9.service.transfer.ITransferService;
import com.demo9.service.withdraw.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/index")
public class TestController {

//    @Autowired
//    private ICustomerService customerService;
////
//
//    //    @Autowired
//    private IWithdrawService withdrawService;
//    //
//    @Autowired
//    private ITransferService transferService;


    @GetMapping
    public String showListPage(Model model) {

//        List<Customer> customers = customerService.findAllByDeletedIsFalse();
//
//        model.addAttribute("customers", customers);

        return "/ep/index";
    }
}
