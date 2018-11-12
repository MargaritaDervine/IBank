package bank.services;

import bank.database.AccountRepository;
import bank.database.UserRepository;
import bank.domain.Account;
import bank.domain.User;

import java.util.List;

public interface TransactionValidator {
    List<AppError> validateTransaction(Account accFrom, Account accTo, double amount,Long userId);

    void validateAmount(Account accFrom, double amount, List<AppError> errors);

    void validateAccounts(Account accFrom, Account accTo, List<AppError> errors, User user);
}
