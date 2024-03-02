package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {


    private final CustomerService customerService;

    private final AccountService accountService;

    @GetMapping
    public List<Account> findAll(){
        return accountService.findAll();
    }

    @PostMapping("/{customerId}")
    public AccountResponse save(@RequestBody Account account, @PathVariable long customedId){
        Customer customer = customerService.find(customedId);
        customer.getAccounts().add(account);
        account.setCustomer(customer);
        Account savedAccount = accountService.save(account);
        return new AccountResponse(savedAccount.getId(),savedAccount.getAccountName(),savedAccount.getMoneyAmount(),
                new CustomerResponse(customer.getId(),customer.getEmail(),customer.getSalary()));

    }

    @PutMapping("/{customerId}")
    public AccountResponse update(@RequestBody Account account,@PathVariable long customerId){
        Customer customer = customerService.find(customerId);
        Account foundAccount = null;
        for(Account a : customer.getAccounts()){
            if(account.getId() == a.getId()){
                foundAccount= a;
            }
        }
        if(foundAccount == null){
            throw new RuntimeException("account is not found!");
        }
        int indexOfFound = customer.getAccounts().indexOf(foundAccount);
        customer.getAccounts().set(indexOfFound,account);
        account.setCustomer(customer);
        accountService.save(account);

        return new AccountResponse(account.getId(),account.getAccountName(),account.getMoneyAmount(),
                new CustomerResponse(customer.getId(),customer.getEmail(),customer.getSalary()));


    }

    @DeleteMapping("/{id}")
    public AccountResponse remove(@PathVariable long id){
        Account account = accountService.find(id);
        accountService.delete(id);
        return new AccountResponse(account.getId(),account.getAccountName(),account.getMoneyAmount(),
                new CustomerResponse(account.getCustomer().getId(),account.getCustomer().getEmail(),account.getCustomer().getSalary()));
    }
}
