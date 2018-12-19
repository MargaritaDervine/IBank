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
        Account fromAcc = accountService.getAccount(accFrom);
        Account toAcc = accountService.getAccount(accTo);
        User user = userService.getUser(request.getUserId());


        List<AppError> validationErrors = transactionValidator.validateTransaction(fromAcc, toAcc,
                amount, user);
        if (!validationErrors.isEmpty()) {
            return new DoTransactionResponse(validationErrors);
        }
        double doubleAmt = Double.parseDouble(amount);
        Transaction transaction = createTransaction(doubleAmt, fromAcc, toAcc);

        accountService.changeBalance(-doubleAmt, accFrom);
        accountService.changeBalance(doubleAmt, accTo);

        transactionRepository.save(transaction);
        return new DoTransactionResponse(transaction.getId());
    }

    private Transaction createTransaction(double amount, Account fromAcc, Account toAcc) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setFromAccount(fromAcc);
        transaction.setToAccount(toAcc);
        return transaction;
    }


    public List<Transaction> getTransactionsByAccount(Account acc) {
        return transactionRepository.findByAccount(acc);
    }

}

