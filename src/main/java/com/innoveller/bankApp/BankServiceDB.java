package com.innoveller.bankApp;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class BankServiceDB implements BankService {
    Connection connection;
    { try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank_app_db", "postgres", "innoveller");
        } catch (SQLException e) {
        System.out.println("connection error");
        }
    }

    public void saveTransaction(double amount, TransactionType transactionType, Long bankAccountId) throws SQLException {
        LocalDate date = LocalDate.now(ZoneId.systemDefault());
        String sql = "INSERT INTO transaction(transaction_date,amount,transaction_type,bank_account_id) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDate(1, Date.valueOf(date));
        statement.setDouble(2, amount);
        statement.setString(3, String.valueOf(transactionType));
        statement.setLong(4, bankAccountId);
        statement.executeUpdate();
    }

    @Override
    public BankAccount findAccount(Long id) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from bank_account where bank_account.id=" + id);
        rs.next();
        BankAccountType bankAccountType;
        if (rs.getString(4).equals(BankAccountType.DEPOSIT)) {
            bankAccountType = BankAccountType.DEPOSIT;
        } else {
            bankAccountType = BankAccountType.SAVING;
        }
        BankAccount bankAccount = new BankAccount(rs.getLong(1), rs.getInt(2), rs.getString(3), bankAccountType, LocalDate.parse(rs.getString(5)), rs.getDouble(6));
        return bankAccount;
    }

    @Override
    public BankAccount createAccount(String accountHolder, BankAccountType accountType, double balance) throws SQLException {
        int accountNo = (int) (Math.random() * 100000000);
        LocalDate date = LocalDate.now(ZoneId.systemDefault());
        String sql = "INSERT INTO bank_account(account_no,account_holder,account_type,open_date,balance) VALUES (?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, accountNo);
        statement.setString(2, accountHolder);
        statement.setString(3, String.valueOf(accountType));
        statement.setDate(4, Date.valueOf(date));
        statement.setDouble(5, balance);
        statement.executeUpdate();
        statement.close();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from bank_account where bank_account.account_no =" + accountNo);
        rs.next();
        BankAccountType bankAccountType;
        if (rs.getString(4).equals(BankAccountType.DEPOSIT.toString())) {
            bankAccountType = BankAccountType.DEPOSIT;
        } else {
            bankAccountType = BankAccountType.SAVING;
        }
        BankAccount bankAccount = new BankAccount(rs.getLong(1), rs.getInt(2), rs.getString(3), bankAccountType, LocalDate.parse(rs.getString(5)), rs.getDouble(6));
        saveTransaction(balance, TransactionType.DEPOSIT, bankAccount.getId());
        return bankAccount;
    }

    @Override
    public void deposit(BankAccount account, double amount) throws SQLException {
        double balance = account.balance + amount;
        Statement stmt = connection.createStatement();
        String sql = "UPDATE bank_account " +
                "SET balance = " + balance + " WHERE account_no =" + account.getAccountNo();
        stmt.executeUpdate(sql);
        saveTransaction(amount, TransactionType.DEPOSIT, account.getId());
    }

    @Override
    public void withdraw(BankAccount account, double amount) throws SQLException {
        if (amount > account.balance) {
            System.out.println("Fail Withdraw Transaction");
        } else {
            double balance = account.balance - amount;
            Statement stmt = connection.createStatement();
            String sql = "UPDATE bank_account " +
                    "SET balance = " + balance + " WHERE account_no =" + account.getAccountNo();
            stmt.executeUpdate(sql);
            saveTransaction(amount, TransactionType.WITHDRAW, account.getId());
        }
    }

    @Override
    public void transfer(BankAccount fromAccount, BankAccount toAccount, double amount) throws SQLException {
        if (amount > fromAccount.balance) {
            System.out.println("Fail Transfer Transaction");
        } else {
            double fromAccBal = fromAccount.balance - amount;
            double toAccBal = toAccount.balance + amount;
            Statement stmt = connection.createStatement();
            String fromAccSql = "UPDATE bank_account " +
                    "SET balance = " + fromAccBal + "WHERE account_no =" + fromAccount.getAccountNo();
            stmt.executeUpdate(fromAccSql);
            Statement statement = connection.createStatement();
            String toAccSql = "UPDATE bank_account " +
                    "SET balance = " + toAccBal + "WHERE account_no =" + toAccount.getAccountNo();
            statement.executeUpdate(toAccSql);

            saveTransaction(amount, TransactionType.TRANSFER, fromAccount.getId());
            saveTransaction(amount, TransactionType.TRANSFER, toAccount.getId());
        }
    }

    @Override
    public List<Transaction> getAccountTransaction(BankAccount account) throws SQLException {
        List<Transaction> transactionList = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from transaction where transaction.bank_account_id=" + account.getId());

        while (rs.next()) {
            TransactionType transactionType;
            if (rs.getString(4).equals(TransactionType.DEPOSIT.toString())) {
                transactionType = TransactionType.DEPOSIT;
            } else if (rs.getString(4).equals(TransactionType.WITHDRAW.toString())) {
                transactionType = TransactionType.WITHDRAW;
            } else {
                transactionType = TransactionType.TRANSFER;
            }
            Transaction transaction = new Transaction(rs.getDouble(3), transactionType, rs.getLong(5));
            transactionList.add(transaction);
        }
        return transactionList;
    }
}
