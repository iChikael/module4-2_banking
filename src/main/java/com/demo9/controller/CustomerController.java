package com.demo9.controller;

import com.demo9.model.Customer;
import com.demo9.model.Deposit;
import com.demo9.model.Transfer;
import com.demo9.model.Withdraw;
import com.demo9.model.dto.TransferRequestDTO;
import com.demo9.service.customer.ICustomerService;
import com.demo9.service.deposit.IDepositService;
import com.demo9.service.transfer.ITransferService;
import com.demo9.service.withdraw.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

   @Autowired
    private ICustomerService customerService;
//

    @Autowired
    private IWithdrawService withdrawService;

    @Autowired
    private ITransferService transferService;

    @Autowired
    private IDepositService depositService;


    @GetMapping
    public String showListPage(Model model) {

        List<Customer> customers = customerService.findAllByDeletedIsFalse();

        model.addAttribute("customers", customers);


        if (model.containsAttribute("success")) {
            model.addAttribute("success", model.getAttribute("success"));
            model.addAttribute("messages", model.getAttribute("messages"));
        }
        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }
        if (model.containsAttribute("customer")) {
            model.addAttribute("customer", model.getAttribute("customer"));
        }

        return "/customer/list";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("customer", new Customer());
        return "/customer/create";
    }

    @GetMapping("/update/{id}")
    public String showUpdatePage(@PathVariable Long id, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else {
            Customer customer = customerOptional.get();
            model.addAttribute("customer", customer);
        }

        return "/customer/update";
    }

    @GetMapping("/deposit/{id}")
    public String showDepositPage(@PathVariable Long id, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Customer not found");
        }
        else {
            Customer customer = customerOptional.get();

            Deposit deposit = new Deposit();
            deposit.setCustomer(customer);

            model.addAttribute("deposit", deposit);
        }

        return "/customer/deposit";
    }


    @GetMapping("/withdraw/{id}")
    public String showWithdrawPage(@PathVariable Long id, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Customer not found");
        }
        else {
            Customer customer = customerOptional.get();

            Withdraw withdraw = new Withdraw();
            withdraw.setCustomer(customer);

            model.addAttribute("withdraw", withdraw);
        }

        return "/customer/withdraw";
    }

    @GetMapping("/transfer/{senderId}")
    public String showTransferPage(@PathVariable Long senderId, Model model) {

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (!senderOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender not found");
        }
        else {
            Customer sender = senderOptional.get();

            TransferRequestDTO transferDTO = new TransferRequestDTO();
            transferDTO.setSender(sender);

            model.addAttribute("transferDTO", transferDTO);

            List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(senderId);

            model.addAttribute("recipients", recipients);
        }

        return "/customer/transfer";
    }

    @PostMapping("/create")
    public String doCreate(@ModelAttribute Customer customer, BindingResult bindingResult, Model model) {

        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", true);
            return "/customer/create";
        }

        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);
        model.addAttribute("success", true);
        model.addAttribute("messages", "Create customer successful");

        return "/customer/create";
    }

    @PostMapping("/update/{id}")
    public String doUpdate(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else {
            customer.setId(id);
            BigDecimal balance = customerOptional.get().getBalance();
            customer.setBalance(balance);
            customerService.save(customer);
            model.addAttribute("customer", customer);
        }
        model.addAttribute("success", true);
        model.addAttribute("messages", "Update customer successful");
        return "/customer/update";
    }

    @PostMapping("/deposit/{customerId}")
    public String doDeposit(@PathVariable Long customerId, @Validated @ModelAttribute Deposit deposit, BindingResult bindingResult, Model model) {

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("deposit", deposit);
            return "/customer/deposit";
        }

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
        }
        else {
            Customer customer = customerOptional.get();

            deposit.setCustomer(customer);
            deposit = customerService.deposit(deposit);

            deposit.setTransactionAmount(BigDecimal.ZERO);

            model.addAttribute("deposit", deposit);
        }

        model.addAttribute("success", true);
        model.addAttribute("messages", "Deposit successful");

        return "/customer/deposit";
    }


    @PostMapping("/withdraw/{customerId}")
    public String doWithdraw(@PathVariable Long customerId, @Validated @ModelAttribute Withdraw withdraw, BindingResult bindingResult, Model model) {

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("withdraw", withdraw);
            return "/customer/withdraw";
        }

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("withdraw", withdraw);
            model.addAttribute("messages", "Không tìm thấy khách hàng");
            return "/customer/withdraw";
        }

        Customer customer = customerOptional.get();

        BigDecimal transactionAmount = withdraw.getTransactionAmount();
        BigDecimal balance = customer.getBalance();

        if (transactionAmount.compareTo(balance) > 0) {
            model.addAttribute("error", true);
            model.addAttribute("withdraw", withdraw);
            model.addAttribute("messages", "Số tiền rút lớn hơn số dư khách hàng");
            return "/customer/withdraw";
        }

        withdraw.setCustomer(customer);
        withdraw = customerService.withdraw(withdraw);

        withdraw.setTransactionAmount(BigDecimal.ZERO);

        model.addAttribute("withdraw", withdraw);
        model.addAttribute("success", true);
        model.addAttribute("messages", "Rút tiền thành công");

        return "/customer/withdraw";
    }



    @PostMapping("/transfer/{senderId}")
    public String doTransfer(@PathVariable Long senderId, @ModelAttribute TransferRequestDTO transferRequestDTO, BindingResult bindingResult, Model model) {

        new TransferRequestDTO().validate(transferRequestDTO, bindingResult);

        Optional<Customer> senderOptional = customerService.findById(senderId);
        List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(senderId);

        model.addAttribute("recipients", recipients);
        model.addAttribute("transferDTO", transferRequestDTO);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", bindingResult.getAllErrors());
            return "/customer/transfer";
        }

        if (!senderOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender not valid");

            return "/customer/transfer";
        }

        Long recipientId = transferRequestDTO.getRecipient().getId();

        Optional<Customer> recipientOptional = customerService.findById(recipientId);

        if (!recipientOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Recipient not valid");

            return "/customer/transfer";
        }

        if (senderId.equals(recipientId)) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender ID must be different from Recipient ID");

            return "/customer/transfer";
        }

        BigDecimal senderCurrentBalance = senderOptional.get().getBalance();

        String transferAmountStr = transferRequestDTO.getTransferAmount();

        BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferAmountStr));
        long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (senderCurrentBalance.compareTo(transactionAmount) < 0) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "Sender balance not enough to transfer");

            return "/customer/transfer";
        }

        Transfer transfer = new Transfer();
        transfer.setSender(senderOptional.get());
        transfer.setRecipient(recipientOptional.get());
        transfer.setTransferAmount(transferAmount);
        transfer.setFees(fees);
        transfer.setFeesAmount(feesAmount);
        transfer.setTransactionAmount(transactionAmount);

        customerService.transfer(transfer);

        transferRequestDTO.setSender(transfer.getSender());
        transferRequestDTO.setTransferAmount(String.valueOf(transferAmount));
        transferRequestDTO.setTransactionAmount(String.valueOf(transactionAmount));

        model.addAttribute("transferDTO", transferRequestDTO);

        model.addAttribute("success", true);
        model.addAttribute("messages", "Transfer success");

        return "/customer/transfer";
    }


    @GetMapping("/history")
    public String showTransferPage(Model model) {

        List<Transfer> transfers = transferService.findAll();

        model.addAttribute("transfers", transfers);
        BigDecimal totalFeesAmount = transferService.getAllFeesAmount();

        model.addAttribute("transfers", transfers);
        model.addAttribute("totalFeesAmount", totalFeesAmount);

        return "/customer/history";
    }

//    @GetMapping("/transaction-history")
//    public String showHistoryPage(Model model){
////        Optional<Customer> customerOptional = customerService.findById(id);
////        model.addAttribute("customer", customerOptional.get());
//        List<Deposit> deposits = depositService.findALl();
//        List<Withdraw> withdraws = withdrawService.findALl();
//        List<Object> combinedList = new ArrayList<>();
//        combinedList.addAll(deposits);
//        combinedList.addAll(withdraws);
//
//        model.addAttribute("combinedList", combinedList);
//
//        return "/customer/transaction-history";
//    }

    @GetMapping("/delete/{id}")
    public String deletePage(Model model, @PathVariable Long id) {
        Optional<Customer> customerOptional = customerService.findById(id);
        model.addAttribute("customer", customerOptional.get());
        return "/customer/delete";
    }

//    @PostMapping("/delete/{id}")
//    public String deletePage(Model model, @PathVariable Long id, @ModelAttribute Customer customer) {
//        Optional<Customer> customerOptional = customerService.findById(id);
//
//        if (!customerOptional.isPresent()) {
//            model.addAttribute("error", true);
//        }
//        else {
//            customer.setId(id);
//            customer.setDeleted(true);
//            customerService.save(customer);
//            model.addAttribute("customer", customer);
//        }
//        model.addAttribute("success", true);
//        model.addAttribute("messages", "Delete customer successful");
//        return "redirect:/customers";
//    }

    @PostMapping("/delete/{id}")
    public String deletePage(RedirectAttributes redirectAttributes, @PathVariable Long id, @ModelAttribute Customer customer) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("error", true);
        } else {
            customer.setId(id);
            customer.setDeleted(true);
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("customer", customer);
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("messages", "Delete customer successful");

        return "redirect:/customers";
    }


}

