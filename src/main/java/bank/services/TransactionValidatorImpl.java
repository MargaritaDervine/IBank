package bank.services;

import bank.AppError;
import bank.domain.Account;
import bank.domain.User;
import bank.domain.ValidationField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidatorImpl implements TransactionValidator {
    @Autowired
    AccountServiceImpl accountService;

    @Override
    public List<AppError> validateTransaction(Account accFrom, Account accTo, String amount, User user) {
        List<AppError> errors = new ArrayList<>();

        validateUser(user, errors);

        if (errors.isEmpty()) {
            validateFromAccount(accFrom, errors, user);
            validateToAccount(accTo, errors);
            validateAmount(accFrom, amount, errors);
        }
        return errors;
    }

    @Override
    public void validateAmount(Account accFrom, String amount, List<AppError> errors) {
        Double amountDouble = null;
        amount = amount.replace(",", ".");
        try {
            amountDouble = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            errors.add(new AppError(ValidationField.Amount, amount + " is not a number"));
        } finally {
            if (amountDouble != null) {
                if (accFrom != null && accFrom.getBalance() < amountDouble) {
                    errors.add(new AppError(ValidationField.Amount, accFrom.getNumber() + " has not enough money"));
                }
                if (amountDouble < 0.01) {
                    errors.add(new AppError(ValidationField.Amount,"Amount cannot be negative or zero" ));
                }
            }
        }
    }

    @Override
    public void validateFromAccount(Account accFrom, List<AppError> errors, User user) {
        if (accFrom == null) {
            errors.add(new AppError(ValidationField.FromAccount,"Account does not exist" ));
        } else {
            List<Account> userAccounts = getAvailabeAccounts(user);
            if (!userAccounts.contains(accFrom)) {
                errors.add(new AppError(ValidationField.FromAccount,"Account does not belong to user"));
            }
        }
    }

    List<Account> getAvailabeAccounts(User user) {
        return accountService.getAvailabeAccounts(user);
    }

    @Override
    public void validateToAccount(Account accTo, List<AppError> errors) {
        if (accTo == null) {
            errors.add(new AppError(ValidationField.ToAccount,"Account does not exist"));
        }
    }

    void validateUser(User user, List<AppError> errors) {
        if (user == null) {
            errors.add(new AppError(ValidationField.User,"user not found"));
        }
    }

}
