package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private double amount;
    private int payments;
    private String number;
    private Double interest;

    public LoanApplicationDTO() {
    }
    public LoanApplicationDTO(long id, double amount, int payments, String number, Double interest) {//como no hay una clase base del DTO, la creo desde cero
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.number = number;
        this.interest = interest;
    }

    public long getId() {
        return id;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public int getPayments() {
        return payments;
    }
    public void setPayments(int payments) {
        this.payments = payments;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public Double getInterest() {
        return interest;
    }
    public void setInterest(Double interest) {
        this.interest = interest;
    }
}
