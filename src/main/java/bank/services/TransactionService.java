package bank.services;

import bank.AppError;
import bank.database.TransactionRepository;
import bank.domain.Account;
import bank.domain.Transaction;
import bank.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Component
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionValidator transactionValidator;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private UserService userService;


    @Transactional
    public DoTransactionResponse doTransaction(DoTransactionRequest request) {
        String accFrom = request.getAccFrom();
        String accTo = request.getAccTo();
        String amount = request.getAmt();
        Account fromAcc = getAccount(accFrom);
        Account toAcc = getAccount(accTo);
        User user = getUser(request);

        List<AppError> validationErrors = getValidationErrors(amount, fromAcc, toAcc, user);
        if (!validationErrors.isEmpty()) {
            return new DoTransactionResponse(validationErrors);
        }

        amount = amount.replace(",", ".");
        double doubleAmt = Double.parseDouble(amount);
        Transaction transaction = createTransaction(doubleAmt, fromAcc, toAcc);

        changeBalance(accFrom, -doubleAmt);
        changeBalance(accTo, doubleAmt);

        transactionRepository.save(transaction);
        return new DoTransactionResponse(transaction.getId());
    }

    private void changeBalance(String accFrom, double v) {
        accountService.changeBalance(v, accFrom);
    }

    List<AppError> getValidationErrors(String amount, Account fromAcc, Account toAcc, User user) {
        return transactionValidator.validateTransaction(fromAcc, toAcc,
                amount, user);
    }

    private User getUser(DoTransactionRequest request) {
        return userService.getUser(request.getUserId());
    }

    Account getAccount(String accFrom) {
        return accountService.getAccount(accFrom);
    }

    Transaction createTransaction(double amount, Account fromAcc, Account toAcc) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDateTime(getTime());
        transaction.setFromAccount(fromAcc);
        transaction.setToAccount(toAcc);
        return transaction;
    }

    LocalDateTime getTime() {
        return LocalDateTime.now();
    }


    public List<Transaction> getTransactionsByAccount(Account acc) {
        return transactionRepository.findByAccount(acc);
    }

}

