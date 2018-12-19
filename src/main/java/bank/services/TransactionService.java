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

    public void setAccountService(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @Transactional
    public DoTransactionResponse doTransaction(DoTransactionRequest request) {
        String fromAccString = request.getAccFrom();
        String toAccString = request.getAccTo();
        String amountString = request.getAmt();
        Account fromAcc = getAccount(fromAccString);
        Account toAcc = getAccount(toAccString);
        User user = getUser(request);

        List<AppError> validationErrors = getValidationErrors(amountString, fromAcc, toAcc, user);
        if (!validationErrors.isEmpty()) {
            return new DoTransactionResponse(validationErrors);
        }

        double doubleAmt = getDoubleAmt(amountString);
        Transaction transaction = createTransaction(doubleAmt, fromAcc, toAcc);

        changeBalance(fromAccString, -doubleAmt);
        changeBalance(toAccString, doubleAmt);

        transactionRepository.save(transaction);
        return new DoTransactionResponse(transaction.getId());
    }

    double getDoubleAmt(String amount) {
        amount = amount.replace(",", ".");
        return Double.parseDouble(amount);
    }

    private void changeBalance(String accFrom, double v) {
        accountService.changeBalance(v, accFrom);
    }

    List<AppError> getValidationErrors(String amount, Account fromAcc, Account toAcc, User user) {
        return transactionValidator.validateTransaction(fromAcc, toAcc,
                amount, user);
    }

    User getUser(DoTransactionRequest request) {
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

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}

