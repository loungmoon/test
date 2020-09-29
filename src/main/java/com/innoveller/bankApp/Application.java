package com.innoveller.bankApp;
import java.sql.SQLException;
import java.util.List;
public class Application {
    public static void main(String[] args) throws SQLException {
          BankService service = new BankServiceDB();
           //service.createAccount("Daw Hla",BankAccountType.DEPOSIT,10000);
          // service.createAccount("U Aung",BankAccountType.DEPOSIT,20000);
           BankAccount DawHla = service.findAccount(1L);
         //  BankAccount UAung  = service.findAccount(2L);

        //   service.deposit(DawHla,1000);
         //  service.withdraw(UAung,100);
         //  service.transfer(DawHla,UAung,1000);

          List<Transaction> transactions = service.getAccountTransaction(DawHla);
          for(Transaction transaction:transactions){
            System.out.println(transaction.getTransactionType() + " Amount "+transaction.getAmount()+" At Date"+transaction.getTransactionDate());
        }
    }

//        BankService service = new BankServiceImplement();
//
//        BankAccount uTun   = service.createAccount("U Tun",BankAccountType.SAVING,100);
//        BankAccount dawHla = service.createAccount("Daw Hla",BankAccountType.DEPOSIT,100);
//
//        service.deposit(dawHla,100);
//        service.withdraw(dawHla,10);
//        service.transfer(dawHla,uTun,50);
//
//        service.deposit(uTun,1000);
//        service.transfer(uTun,dawHla,20);
//
//        List<Transaction> transactions = service.getAccountTransaction(dawHla);
//
//        for(Transaction transaction:transactions){
//            System.out.println(transaction.getTransactionType() + " Amount "+transaction.getAmount()+" At Date"+transaction.getTransactionDate());
//        }
//
//        BankAccount account = service.findAccount(2L);
//        {
//            System.out.println("Id :"+account.getId()+" AccountNo :" + account.getAccountNo()+" Account Holder :"+account.getAccountHolder()+" Balance :"+account.balance+" At Date"+account.getOpenDate());
//        }
//    }
}
