package com.cg.api;
import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.model.dto.*;
import com.cg.service.customer.ICustomerService;
import com.cg.service.transfer.ITransferService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/customers")

public class CustomerAPI {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ITransferService transferService;



    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        List<CustomerDTO> customerDTOS = customerService.findAllCustomerDTOByDeletedIsFalse();

        return new ResponseEntity<>(customerDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> doCreate(@RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {

        new CustomerDTO().validate(customerDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean existsEmail = customerService.existsByEmailEquals(customerDTO.getEmail());

        if (existsEmail){
            throw new ResourceNotFoundException("The email is exists");
        }

        customerDTO.setId(null);
        customerDTO.setBalance(BigDecimal.ZERO);
        customerDTO.getLocationRegion().setId(null);
        Customer customer = customerDTO.toCustomer();

        customerService.save(customer);
        customerDTO = customer.toCustomerDTO();

        return new ResponseEntity<>(customerDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            throw new ResourceNotFoundException("Customer not found !");
        }

        Customer customer = customerOptional.get();

        return new ResponseEntity<>(customer.toCustomerDTO(), HttpStatus.OK);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<?> update(@PathVariable Long customerId , @Validated @RequestBody CustomerDTO customerDTO , BindingResult bindingResult){
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if(!customerOptional.isPresent()){
            throw new ResourceNotFoundException("Customer not found");
        }

        new CustomerDTO().validate(customerDTO,bindingResult);

        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Customer customer = customerDTO.toCustomer();
        customer.setId(customerId);
        customerService.save(customer);

        return new ResponseEntity<>(customer.toCustomerDTO() , HttpStatus.OK);
    }

    @PatchMapping("/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId){
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if(!customerOptional.isPresent()){
            throw new NullPointerException("Customer not found!");
        }

        Customer customer = customerOptional.get();
        customer.setDeleted(true);


        customerService.save(customer);
        CustomerDTO customerDTO = customer.toCustomerDTO();
        customerDTO.setDeleted(true);
        return new ResponseEntity<>(customerDTO , HttpStatus.OK);
    }

    @PostMapping("/deposits/{id}")
    public ResponseEntity<?> deposit(@PathVariable long id, @Validated @RequestBody DepositDTO depositDTO, BindingResult bindingResult) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found this customer");
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Customer customer = customerOptional.get();

        BigDecimal transactionAmount = BigDecimal.valueOf(Long.parseLong(depositDTO.getTransactionAmount()));

        Deposit deposit = new Deposit();
        deposit.setCustomer(customer);
        deposit.setTransactionAmount(transactionAmount);

        customerService.deposit(deposit);

        customer = customerService.findById(id).get();

        return new ResponseEntity<>(customer.toCustomerDTO(), HttpStatus.OK);
    }

    @PostMapping("/withdraws/{id}")
    public ResponseEntity<?> withdraw(@PathVariable long id, @Validated @RequestBody WithdrawDTO withdrawDTO, BindingResult bindingResult) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found this customer");
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Customer customer = customerOptional.get();

        BigDecimal transactionAmount = BigDecimal.valueOf(Long.parseLong(withdrawDTO.getTransactionAmount()));

        Withdraw withdraw = new Withdraw();
        withdraw.setCustomer(customer);
        withdraw.setTransactionAmount(transactionAmount);

        customerService.withdraw(withdraw);

        customer = customerService.findById(id).get();

        return new ResponseEntity<>(customer.toCustomerDTO(), HttpStatus.OK);
    }

    @PostMapping("/transfers")
    public ResponseEntity<?> transfer( @Validated @RequestBody TransferDTO transferDTO, BindingResult bindingResult) {
       Long senderId = Long.parseLong(transferDTO.getSenderId());
        Optional<Customer> optionalSender = customerService.findById(senderId);
        long recipientId = Long.parseLong(transferDTO.getRecipientId());
        Optional<Customer> optionalRecipient = customerService.findById(recipientId);

        if ( senderId == recipientId) {
            throw new DataInputException("Invalid Recipient");
        }

        if (!(optionalSender.isPresent()) || !(optionalRecipient.isPresent())) {
            throw new ResourceNotFoundException("Not found Sender or Recipient");
        }

        Customer sender = optionalSender.get();
        Customer recipient = optionalRecipient.get();


        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        BigDecimal senderBalance = sender.getBalance();

        BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferDTO.getTransferAmount()));
        Long fees = 10L;


        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100L));
        BigDecimal totalAmount = transferAmount.add(feesAmount);

        if (senderBalance.compareTo(totalAmount) < 0) {
            throw new DataInputException("Your balance not enough to make this transaction");
        }

        Transfer transfer = new Transfer();
        transfer.setTransferAmount(transferAmount);
        transfer.setFeesAmount(feesAmount);
        transfer.setTransactionAmount(totalAmount);
        transfer.setSender(sender);
        transfer.setRecipient(recipient);
        transfer = customerService.transfer(transfer);

//        List<Customer> customers = new ArrayList<>();
//        customers.add(new Customer());
//        customers.add(new Customer());
//        Map<String, CustomerDTO> customerMap = new HashMap<>();
//        customerMap.put("sender", customerService.findById(senderId).get().toCustomerDTO());
//        customerMap.put("recipient", customerService.findById(recipientId).get().toCustomerDTO());
        TransferResDTO transferResDTO = new TransferResDTO();
        transferResDTO.setSender(customerService.findById(senderId).get().toCustomerDTO());
        transferResDTO.setRecipient(customerService.findById(recipientId).get().toCustomerDTO());

        return new ResponseEntity<>(transferResDTO, HttpStatus.OK);
    }

    @GetMapping("/transfers")
    public ResponseEntity<?> showTransferHistory(){
        List<TransferViewDTO> transferViewDTOList = transferService.findAllTransferViewDTO();
        return new ResponseEntity<>(transferViewDTOList, HttpStatus.OK);
    }
}
