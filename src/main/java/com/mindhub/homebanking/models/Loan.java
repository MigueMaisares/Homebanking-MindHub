package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private double maxAmount;
    @ElementCollection//Spring crea la entidad y la relacion de uno a muchos entre payments y Loan
    @Column(name = "payment")
    private List<Integer> payments = new ArrayList<>();
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();
    @ElementCollection
    @Column(name = "interest")
    private List<Double> interest;

    public Loan() {
    }
    public Loan(String name, double maxAmount, List<Integer> payments, List<Double> interest) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    public void addClientLoan (ClientLoan clientLoan) {
        clientLoans.add(clientLoan);
        clientLoan.setLoan(this);
    }
    @JsonIgnore
    public List<Client> getClients() {
        return clientLoans.stream().map(loan -> loan.getClient()).collect(Collectors.toList());
    }
    public List<Double> getInterest() {
        return interest;
    }
    public void setInterest(List<Double> interest) {
        this.interest = interest;
    }
}
