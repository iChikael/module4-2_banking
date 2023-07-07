package com.cg.controller;


import com.cg.model.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @GetMapping
    public String listPage(Model model) {

        List<Customer> customers = new ArrayList<Customer>();
        Customer cus1 = new Customer(1L, "NVA", "nva@co.cc", "2345", "28 Nguyễn Tri Phương");
        Customer cus2 = new Customer(2L, "NVB", "nvb@co.cc", "3456", "29 Nguyễn Tri Phương");
        Customer cus3 = new Customer(3L, "NVC", "nvc@co.cc", "4567", "38 Nguyễn Tri Phương");
        Customer cus4 = new Customer(4L, "NVD", "nvd@co.cc", "5678", "39 Nguyễn Tri Phương");


        customers.add(cus1);
        customers.add(cus2);
        customers.add(cus3);
        customers.add(cus4);

        model.addAttribute("customers", customers);

        return "/cp/customer/list";
    }

    @GetMapping("/create")
    public String createPage() {
        return "/cp/customer/create";
    }
}
