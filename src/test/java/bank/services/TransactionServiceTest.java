package bank.services;

import bank.AppError;
import bank.database.TransactionRepository;
import bank.domain.Account;
import bank.domain.Transaction;
import bank.domain.User;
import bank.domain.ValidationField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    TransactionService transactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountServiceImpl accountService;

    @Before
    public void setUp() throws Exception {
        transactionService = spy(TransactionService.class);
    }

    @Test
    public void doTransaction_noErrors() {
        String accFromString = "from";
        String accToString = "to";
        String amt = "100,0";
        double amount = 100.0d;
        Account accFrom = new Account();
        Account accTo = new Account();
        Long id = 2L;
        LocalDateTime time = LocalDateTime.now();

        DoTransactionRequest request = new DoTransactionRequest(accFromString, accToString, amt, id);
        List<AppError> errorsEmpty = new ArrayList<>();
        User user = new User();
        doReturn(user).when(transactionService).getUser(request);
        doReturn(errorsEmpty).when(transactionService).getValidationErrors(amt, accFrom, accTo, user);
        doReturn(accFrom).when(transactionService).getAccount(accFromString);
        doReturn(accTo).when(transactionService).getAccount(accToString);

        transactionService.setTransactionRepository(transactionRepository);
        Transaction transaction = createTestTransaction(amount, accFrom, accTo, time);
        transaction.setId(6L);
        doReturn(transaction).when(transactionService).createTransaction(amount, accFrom, accTo);
        DoTransactionResponse expectedResponse = new DoTransactionResponse(6L);
        transactionService.setAccountService(accountService);
        assertEquals(expectedResponse, transactionService.doTransaction(request));
    }

    @Test
    public void doTransaction_Errors() {
        String accFromString = "from";
        String accToString = "to";
        String amt = "100,0";
        Account accFrom = new Account();
        Account accTo = new Account();
        Long id = 2L;

        DoTransactionRequest request = new DoTransactionRequest(accFromString, accToString, amt, id);
        List<AppError> errors = new ArrayList<>();
        errors.add(new AppError(ValidationField.Amount, "description"));
        User user = new User();
        doReturn(user).when(transactionService).getUser(request);
        doReturn(errors).when(transactionService).getValidationErrors(amt, accFrom, accTo, user);
        doReturn(accFrom).when(transactionService).getAccount(accFromString);
        doReturn(accTo).when(transactionService).getAccount(accToString);

        DoTransactionResponse expectedResponse = new DoTransactionResponse(errors);
        assertEquals(expectedResponse, transactionService.doTransaction(request));
    }


    private Transaction createTestTransaction(double amount, Account fromAcc, Account toAcc, LocalDateTime time) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setFromAccount(fromAcc);
        transaction.setToAccount(toAcc);
        transaction.setDateTime(time);
        return transaction;
    }

    @Test
    public void createTransaction() {
        double amount = 0.2d;
        Account fromAcc = new Account();
        Account toAcc = new Account();
        LocalDateTime time = LocalDateTime.now();
        Transaction expectedTr = createTestTransaction(amount, fromAcc, toAcc, time);
        doReturn(time).when(transactionService).getTime();
        assertEquals(expectedTr, transactionService.createTransaction(amount, fromAcc, toAcc));

    }

    @Test
    public void getDoubleAmt_dot() {
        assertEquals(10.1d, transactionService.getDoubleAmt("10.1"), 0.01);
    }

    @Test
    public void getDoubleAmt_comma() {
        assertEquals(10.1d, transactionService.getDoubleAmt("10,1"), 0.01);
    }
}