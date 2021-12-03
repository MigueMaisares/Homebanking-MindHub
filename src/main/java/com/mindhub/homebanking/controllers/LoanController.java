package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> creditApplication (@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){//Debe recibir un objeto de solicitud de crédito con los datos del préstamo:id del préstamo, monto, cuotas y número de cuenta de destino

        Loan loanRequested = loanRepository.findById(loanApplicationDTO.getId());
        Client clientCurrent = clientRepository.findByEmail(authentication.getName());
        Account accountTarget = accountRepository.findByNumber(loanApplicationDTO.getNumber());

        if (loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayments() == 0 || loanApplicationDTO.getNumber().isEmpty()){
            return new ResponseEntity<>("Incorrect loan data", HttpStatus.FORBIDDEN);
        }
        if (loanRequested == null){
            return new ResponseEntity<>("The loan does not exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loanRequested.getMaxAmount()){
            return new ResponseEntity<>("The requested amount exceeds the maximum amount of the loan", HttpStatus.FORBIDDEN);
        }
        if (!loanRequested.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The payments are not among the available loan proceeds", HttpStatus.FORBIDDEN);
        }
        if (accountTarget == null){
            return new ResponseEntity<>("Target account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!clientCurrent.getAccounts().contains(accountTarget)){
            return new ResponseEntity<>("The destination account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() + loanApplicationDTO.getAmount()*loanApplicationDTO.getInterest(), loanApplicationDTO.getPayments(), clientCurrent, loanRequested, loanApplicationDTO.getInterest());
        clientLoanRepository.save(clientLoan);
        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), loanRequested.getName() + " loan approved", LocalDateTime.now(), TransactionType.CREDIT, accountTarget.getBalance() + loanApplicationDTO.getAmount());
        accountTarget.addTransaction(transaction);
        transactionRepository.save(transaction);
        accountTarget.setBalance(loanApplicationDTO.getAmount());
        accountRepository.save(accountTarget);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }
}