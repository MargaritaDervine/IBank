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

    @Override
    public List<String> validateTransaction(Account accFrom, Account accTo, String amount, User user) {
        List<String> errors = new ArrayList<>();

        validateUser(user, errors);

        if (errors.isEmpty()) {
            validateAccounts(accFrom, accTo, errors, user);
            validateAmount(accFrom, amount, errors);
        }
        return errors;
    }

    @Override
    public void validateAmount(Account accFrom, String amount, List<String> errors) {
        double amountDouble = 0.0d;
        try {
            amountDouble  = Double.parseDouble(amount);
        } catch (NumberFormatException e){
            errors.add(amount+ " is not a number");
        }


        if (accFrom != null && accFrom.getBalance() < amountDouble) {
            errors.add(accFrom.getNumber() + " has not enough money");
        }

        if (amountDouble < 0.01) {
            errors.add("Amount cannot be negative or zero");
        }
    }

    @Override
    public void validateAccounts(Account accFrom, Account accTo, List<String> errors, User user) {
        if (accFrom == null) {
            errors.add(accFrom + " account does not exist");
        }
        if (accTo == null) {
            errors.add(accTo + " account does not exist");
        }

        if (accFrom != null) {
            List<Account> userAccounts = accountService.getAvailabeAccounts(user);
            if (!userAccounts.contains(accFrom)) {
                errors.add("Account does not belong to user");
            }
            if (accFrom.equals(accTo)) {
                errors.add("Not possible to transfer money within same account");
            }
        }

    }

    private void validateUser(User user, List<String> validationErrors) {
        if (user == null) {
            validationErrors.add("username not found");
        }
    }

}
