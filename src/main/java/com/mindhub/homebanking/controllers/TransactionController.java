package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.PDFExporter;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> makeTransactions (@RequestParam Double amount, @RequestParam String description,
                                                    @RequestParam String numberAccOrigin, @RequestParam String numberAccDestiny,
                                                    Authentication authentication){

        Account accountDebit = accountRepository.findByNumber(numberAccOrigin);
        Account accountCredit = accountRepository.findByNumber(numberAccDestiny.toUpperCase());

        if (amount <= 0 || description.isEmpty() || numberAccOrigin.isEmpty() || numberAccDestiny.isEmpty() ||
            accountDebit==null || accountCredit==null ||
            !clientRepository.findByEmail(authentication.getName()).getAccounts().contains(accountDebit) ||//Verificar que la cuenta de origen pertenezca al cliente autenticado
            accountDebit.getBalance() < amount || accountDebit == accountCredit){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(-amount, description + " " + numberAccDestiny, LocalDateTime.now(), TransactionType.DEBIT, accountDebit.getBalance() - amount);
        Transaction transactionCredit = new Transaction(amount, description + " " + numberAccOrigin, LocalDateTime.now(), TransactionType.CREDIT, accountCredit.getBalance() + amount);
        accountDebit.setBalance(-amount);
        accountCredit.setBalance(amount);
        accountDebit.addTransaction(transactionDebit);
        accountCredit.addTransaction(transactionCredit);
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/transactions/export/pdf")
    public void betweenDatesPDF(HttpServletResponse response, @RequestParam Long id, @RequestParam String desde, @RequestParam String hasta) throws DocumentException, IOException {
        LocalDateTime desdeM = LocalDateTime.parse(desde);
        LocalDateTime hastaM = LocalDateTime.parse(hasta);
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Transaction> transactionList = transactionRepository.findByAccount_idAndDateBetweenOrderByDateDesc(id, desdeM, hastaM);

        PDFExporter exporter = new PDFExporter(transactionList);
        exporter.export(response);
    }
    @GetMapping("/api/transactions/export/pdf")
    public void voucherPDF(HttpServletResponse response, @RequestParam Long id) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=trans_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Transaction transaction = transactionRepository.findById(id).orElse(null);

        PDFExporter exporter = new PDFExporter(transaction);
        exporter.export2(response);
    }
}