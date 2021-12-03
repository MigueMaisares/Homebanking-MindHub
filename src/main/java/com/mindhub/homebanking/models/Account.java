package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;//alfanumerico
    private LocalDateTime creationDate;
    private double balance;
    @ManyToOne(fetch = FetchType.EAGER)//le dice a JPA que se asegure de que cuando se cargue una cuenta desde BBDD, también se carguen los datos del cliente
    @JoinColumn(name = "client_id")//qué columna tiene el ID del dueño de la cuenta
    private Client client;//@ManyToOne vincula este campo Client con la tabla
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();
    private AccountType type;

    public Account() {
    }
    public Account(String number, LocalDateTime creationDate, double balance, AccountType type) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.type = type;
    }

    public Long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = this.balance + balance;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }
    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    public AccountType getType() {
        return type;
    }
    public void setType(AccountType type) {
        this.type = type;
    }
}
