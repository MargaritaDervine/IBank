package bank.services;

import bank.AppError;
import bank.domain.Account;
import bank.domain.User;

import java.util.List;

public interface TransactionValidator {
    List<AppError> validateTransaction(Account accFrom, Account accTo, String amount, User user);

    void validateAmount(Account accFrom, String amount, List<AppError> errors);

    void validateFromAccount(Account accFrom, List<AppError> errors, User user);

    void validateToAccount(Account accTo, List<AppError> errors);
}
