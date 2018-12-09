package bank.services;

import bank.domain.Account;
import bank.domain.User;

import java.util.List;

public interface TransactionValidator {
    List<String> validateTransaction(Account accFrom, Account accTo, String amount,User user);

    void validateAmount(Account accFrom, String amount, List<String> errors);

    void validateAccounts(Account accFrom, Account accTo, List<String> errors, User user);
}
