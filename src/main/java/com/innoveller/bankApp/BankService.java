package com.innoveller.bankApp;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface BankService {
    BankAccount findAccount(Long id) throws SQLException;
    BankAccount createAccount(String accountHolder, BankAccountType accountType,double balance) throws SQLException;
    void deposit(BankAccount account,double amount) throws SQLException;
    void withdraw(BankAccount account,double amount) throws SQLException;
    void transfer(BankAccount fromAccount,BankAccount toAccount,double amount) throws SQLException;
    List<Transaction> getAccountTransaction(BankAccount account) throws SQLException;
}
