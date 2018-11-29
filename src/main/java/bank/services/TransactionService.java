package bank.services;

import bank.database.TransactionRepository;
import bank.domain.Account;
import bank.domain.Transaction;
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


    @Transactional
    public DoTransactionResponse doTransaction(DoTransactionRequest request) {
        String accFrom = request.getAccFrom();
        String accTo = request.getAccTo();
        double amount = request.getAmt();
        Account fromAcc = accountService.getAccount(accFrom);
        Account toAcc = accountService.getAccount(accTo);

        List<AppError> validationErrors = transactionValidator.validateTransaction(fromAcc, toAcc,
                amount, request.getUserId());
        if (!validationErrors.isEmpty()) {
            return new DoTransactionResponse(validationErrors);
        }

        Transaction transaction = createTransaction(amount, fromAcc, toAcc);

        accountService.changeBalance(-amount, accFrom);
        accountService.changeBalance(amount, accTo);

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

  /* public List<Transaction> getTransactionsByAccount(String acc) {
        return transactionRepository.findByAccount(acc);
    }*/

    public List<Transaction> getTransactionsByAccount(Account acc) {
        return transactionRepository.findByAccount(acc);
    }

}

