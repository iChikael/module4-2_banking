package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping({"/","/customers"})
public class CustomerController {

    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("/customer/list");
    }
//    @Autowired
//    private ICustomerService customerService;
//
//    @Autowired
//    private IDepositService depositService;
//
//
//    @GetMapping
//    public String showListPage(Model model) {
//
//        List<Customer> customers = customerService.findALl();
//
//        model.addAttribute("customers", customers);
//
//        return "customer/list";
//    }
//
//    @GetMapping("/create")
//    public String showCreatePage(Model model) {
//        model.addAttribute("customer", new Customer());
//
//        return "customer/create";
//    }
//
////    @PostMapping("/create")
////    public String doCreate(@ModelAttribute Customer customer, BindingResult bindingResult, Model model) {
////
////        new Customer().validate(customer, bindingResult);
////
////        if (bindingResult.hasFieldErrors()) {
////            model.addAttribute("customer", customer);
////            model.addAttribute("error", true);
////            return "customer/create";
////        }
////
////        customer.setBalance(BigDecimal.ZERO);
////        customerService.save(customer);
////        model.addAttribute("success", true);
////
////        model.addAttribute("messages", "Create customer success");
////        return "customer/create";
////    }
//
//    @GetMapping("/update/{id}")
//    public String showUpdatePage(@PathVariable Long id, Model model) {
//
//        Optional<Customer> customerOptional = customerService.findById(id);
//
//        if (!customerOptional.isPresent()) {
//            model.addAttribute("error", true);
//        } else {
//            Customer customer = customerOptional.get();
//            model.addAttribute("customer", customer);
//        }
//        return "customer/update";
//    }
//
//
//    @PostMapping("/update/{id}")
//    public String doUpdate(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {
//
//        Optional<Customer> customerOptional = customerService.findById(id);
//
//        if (!customerOptional.isPresent()) {
//            model.addAttribute("error", true);
//        } else {
//            customer.setId(id);
//            customerService.save(customer);
//            model.addAttribute("messageSuccess", "Update customer success");
//            model.addAttribute("customer", customer);
//        }
//        return "customer/update";
//    }
//
////    @GetMapping("/delete/{id}")
////    public String showDeletePage(@PathVariable Long id, Model model) {
////        Optional<Customer> customerOptional = customerService.findById(id);
////        if (!customerOptional.isPresent()) {
////            model.addAttribute("error", true);
////            List<Customer> customers = customerService.findAllByDeletedIsFalse();
////            model.addAttribute("customers", customers);
////            return "customer/list";
////        } else {
////            Customer customer = customerOptional.get();
////            model.addAttribute("customer", customer);
////            return "customer/delete";
////        }
////    }
//
////    @PostMapping("/delete/{id}")
////    public String showDeletePage(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {
////
////        Optional<Customer> customerOptional = customerService.findById(id);
////
////        if (!customerOptional.isPresent()) {
////            model.addAttribute("error", true);
////        } else {
////            customer.setId(id);
////            customerService.delete(customer);
////            model.addAttribute("messageSuccess", "Delete customer success");
////            List<Customer> customers = customerService.findAllByDeletedIsFalse();
////            model.addAttribute("customers", customers);
////        }
////        return "customer/list";
////    }
//
//    @GetMapping("deposit/{id}")
//    public String showDeposit(@PathVariable Long id, Model model) {
//
//        Optional<Customer> customerOptional = customerService.findById(id);
//
//        if (!customerOptional.isPresent()) {
//            model.addAttribute("error", true);
//            model.addAttribute("messages", "Customer not found");
//        } else {
//            Customer customer = customerOptional.get();
////            model.addAttribute("customer", customer);
//            Deposit deposit = new Deposit();
//            deposit.setCustomer(customer);
//
//            model.addAttribute("deposit", deposit);
//        }
//        return "customer/deposit";
//    }
//
//    @PostMapping("/deposit/{customerId}")
//    public String doDeposit(@PathVariable Long customerId, @ModelAttribute Deposit deposit, Model model) {
//
//        Optional<Customer> customerOptional = customerService.findById(customerId);
//
//        if (!customerOptional.isPresent()) {
//            model.addAttribute("error", true);
//            model.addAttribute("messages", "Customer not found");
//        } else {
//            Customer customer = customerOptional.get();
//
//            deposit.setCustomer(customer);
//            deposit = customerService.deposit(deposit);
//
//            deposit.setTransactionAmount(BigDecimal.ZERO);
//
//            model.addAttribute("deposit", deposit);
//        }
//
//        model.addAttribute("success", true);
//        model.addAttribute("messageSuccess", "Deposit successful");
//        return "customer/deposit";
//    }
//
//    @GetMapping("withdraw/{id}")
//    public String showWithdraw(@PathVariable Long id, Model model) {
//
//        Optional<Customer> customerOptional = customerService.findById(id);
//
//        if (!customerOptional.isPresent()) {
//            model.addAttribute("error", true);
//            model.addAttribute("messages", "Customer not found");
//        } else {
//            Customer customer = customerOptional.get();
//            Withdraw withdraw = new Withdraw();
//            withdraw.setCustomer(customer);
//            model.addAttribute("withdraw", withdraw);
//
//        }
//        return "customer/withdraw";
//    }
//
//    @PostMapping("/withdraw/{customerId}")
//    public String doWithdraw(@PathVariable Long customerId, @ModelAttribute Withdraw withdraw, Model model) {
//
//        Optional<Customer> customerOptional = customerService.findById(customerId);
//
//        if (!customerOptional.isPresent()) {
//            model.addAttribute("error", true);
//        } else {
//            Customer customer = customerOptional.get();
//            BigDecimal balance = customer.getBalance();
//            BigDecimal withdrawTransactionAmount = withdraw.getTransactionAmount();
//            if (withdrawTransactionAmount.compareTo(balance) > 0) {
//                model.addAttribute("error", true);
//                model.addAttribute("messages", "insufficient balance");
//            } else if (withdrawTransactionAmount.compareTo(new BigDecimal(100000)) < 0) {
//                model.addAttribute("error", true);
//                model.addAttribute("messages", "withdrawal amount is not less than 100000");
//            } else if (withdrawTransactionAmount.compareTo(new BigDecimal(1000000000)) > 0) {
//                model.addAttribute("error", true);
//                model.addAttribute("messages", "withdrawal amount is not more than 1000000000");
//            } else {
//                withdraw.setCustomer(customer);
//                withdraw = customerService.withdraw(withdraw);
//                withdraw.setTransactionAmount(BigDecimal.ZERO);
//                model.addAttribute("withdraw", withdraw);
//                model.addAttribute("success", true);
//                model.addAttribute("messages", "Withdraw successful");
//            }
//        }
//        return "customer/withdraw";
//    }
//
//    @GetMapping("transfer/{senderId}")
//    public String showTransfer(@PathVariable Long senderId, Model model) {
//
//        Optional<Customer> senderOptional = customerService.findById(senderId);
//
//        if (!senderOptional.isPresent()) {
//            model.addAttribute("errorSenderID", true);
//            model.addAttribute("messages", "Customer not found");
//            return "customer/transfer";
//        }
//        else {
//            Customer sender = senderOptional.get();
//
//            TransferRequestDTO transferDTO = new TransferRequestDTO();
//            transferDTO.setSender(sender);
//
//            model.addAttribute("transferDTO", transferDTO);
//
//            List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(senderId);
//
//            model.addAttribute("recipients", recipients);
//        }
//        return "customer/transfer";
//    }
//
//    @PostMapping("/transfer/{customerSenderId}")
//    public String doTransfer(@PathVariable Long customerSenderId, @ModelAttribute TransferRequestDTO transferRequestDTO, BindingResult bindingResult, Model model) {
//
//// lấy id người gửi
////        lấy id người nhận
////        check số tiền ngời gửi
////        lấy số tiền gửi
////        lấy fee
////        trừ blance của người người
////        cộng blance người nhận
//        // lưu vào transfer
//
//        // validate
//        new TransferRequestDTO().validate(transferRequestDTO, bindingResult);
//
//        //tìm id người gởi
//        Optional<Customer> senderOptional = customerService.findById(customerSenderId);
//
//        // lấy lại list người nhận truyền ra lại
//        List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(customerSenderId);
//        model.addAttribute("recipients", recipients);
//        model.addAttribute("transferDTO", transferRequestDTO);
//
//        // ném lỗi truyền vào
//        if (bindingResult.hasFieldErrors()) {
//            model.addAttribute("error", true);
//            return "customer/transfer";
//        }
//
//        // ném lỗi không tìm thấy id người gửi
//        if (!senderOptional.isPresent()) {
//            model.addAttribute("errorSenderID", true);
//            model.addAttribute("messages", "Customer not found");
//            return "customer/transfer";
//        }
//
//        //check id người nhận
//        Long recipientId = transferRequestDTO.getRecipient().getId();
//        Optional<Customer> recipientOptional = customerService.findById(recipientId);
//        if (!recipientOptional.isPresent()) {
//            model.addAttribute("errorControl", true);
//            model.addAttribute("messages", "Recipient not valid");
//            return "customer/transfer";
//        }
//        if (customerSenderId.equals(recipientId)) {
//            model.addAttribute("errorControl", true);
//            model.addAttribute("messages", "Sender ID must be different from Recipient ID");
//            return "customer/transfer";
//        }
//
//        //check số tiền người gửi
//
//        //số tiền hiện có của người gửi
//        BigDecimal senderCurrentBalance = senderOptional.get().getBalance();
//
//        //số tiền cần chuyển String
//        String transferAmountStr = transferRequestDTO.getTransferAmount();
//
//        //số tiền cần chuyển BigDecimal
//        BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferAmountStr));
//
//        //% phí
//        long fees = 10L;
//
//        //số tiền phí
//        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
//
//        //tổng số tiền gửi + phí
//        BigDecimal transactionAmount = transferAmount.add(feesAmount);
//
//        if (senderCurrentBalance.compareTo(transactionAmount) < 0) {
//            model.addAttribute("errorControl", true);
//            model.addAttribute("messages", "Sender balance not enough to transfer");
//            return "customer/transfer";
//        }
//
//        // set lại transfer
//        Transfer transfer = new Transfer();
//        transfer.setSender(senderOptional.get());
//        transfer.setRecipient(recipientOptional.get());
//        transfer.setTransferAmount(transferAmount);
//        transfer.setFees(fees);
//        transfer.setFeesAmount(feesAmount);
//        transfer.setTransactionAmount(transactionAmount);
//
//        // lưu vào database
//        customerService.transfer(transfer);
//
//        //set lại transferRequestDTO để truyền ra ngoài
//        transferRequestDTO.setSender(transfer.getSender());
//        transferRequestDTO.setTransferAmount(null);
//        transferRequestDTO.setTransactionAmount(null);
//        model.addAttribute("transferDTO", transferRequestDTO);
//        model.addAttribute("success", true);
//        model.addAttribute("messages", "Transfer success");
//        return "customer/transfer";
//
//    }
}