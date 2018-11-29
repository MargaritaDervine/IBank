package bank.services;

import bank.domain.Account;
import bank.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidatorImpl implements TransactionValidator {
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    UserService userService;

    @Override
    public List<AppError> validateTransaction(Account accFrom, Account accTo, double amount, Long userId) {
        List<AppError> errors = new ArrayList<>();
        User user = userService.getUser(userId);
        validateUser(user, errors);

        if (errors.isEmpty()) {
            validateAccounts(accFrom, accTo, errors, user);
            validateAmount(accFrom, amount, errors);
        }
        return errors;
    }

    @Override
    public void validateAmount(Account accFrom, double amount, List<AppError> errors) {
        if (accFrom != null && accFrom.getBalance() < amount) {
            AppError appError = new AppError(accFrom.getNumber(), " has not enough money");
            errors.add(appError);
        }
    }

    @Override
    public void validateAccounts(Account accFrom, Account accTo, List<AppError> errors, User user) {

        if (accFrom == null) {
            AppError appError = new AppError("", "Account does not exist");
            errors.add(appError);
        }
        if (accTo == null) {
            AppError appError = new AppError("", "Account does not exist");
            errors.add(appError);
        }

        if (accFrom != null) {
            List<Account> userAccounts = accountService.getAvailabeAccounts(user);
            if (!userAccounts.contains(accFrom)) {
                errors.add(new AppError("Account", " does not belong to user"));
            }
        }
    }

    private void validateUser(User user, List<AppError> validationErrors) {
        if (user == null) {
            validationErrors.add(new AppError("username", "Not found"));
        }
    }

}
