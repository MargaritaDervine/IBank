package bank.services;

import bank.domain.Account;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TransactionValidatorImplTest {
    TransactionValidatorImpl validator = new TransactionValidatorImpl();
    Account fromAcc;
    List<String> errors;
    String amount;


    @Test
    public void validateTransaction() {
       // validator.validateTransaction()
    }

    @Test
    public void validateAmount() {
        fromAcc = new Account();
    validator.validateAmount(fromAcc, amount, errors);


    }

    @Test
    public void validateAccounts() {
    }
}