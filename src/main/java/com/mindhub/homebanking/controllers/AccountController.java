package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }
    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam String type) {
        Client clientCurrent = clientRepository.findByEmail(authentication.getName());

        if (clientCurrent.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You have more than three accounts", HttpStatus.FORBIDDEN);
        }
        if (type.equals("SAVING")){
            Account accountCreated = new Account("VIN-" + getRandomNumber(10000000, 99999999), LocalDateTime.now(), 0, AccountType.SAVING);
            clientCurrent.addAccount(accountCreated);
            accountRepository.save(accountCreated);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        if (type.equals("CURRENT")){
            Account accountCreated = new Account("VIN-" + getRandomNumber(10000000, 99999999), LocalDateTime.now(), 0, AccountType.CURRENT);
            clientCurrent.addAccount(accountCreated);
            accountRepository.save(accountCreated);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("There is no account type", HttpStatus.FORBIDDEN);
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @DeleteMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount (@RequestParam Long id){
        if (!accountRepository.existsById(id)){
            return new ResponseEntity<>("This account not exist", HttpStatus.FORBIDDEN);
        }
        Account accountDeleted = accountRepository.findById(id).orElse(null);
        if (accountDeleted.getBalance() != 0){
            return new ResponseEntity<>("This account has no balance 0", HttpStatus.FORBIDDEN);
        }else {
            transactionRepository.deleteInBatch(accountDeleted.getTransactions());
            accountRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
