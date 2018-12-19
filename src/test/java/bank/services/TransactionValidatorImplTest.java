package bank.services;

import bank.AppError;
import bank.domain.Account;
import bank.domain.User;
import bank.domain.ValidationField;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class TransactionValidatorImplTest {
    TransactionValidatorImpl validator = new TransactionValidatorImpl();
    Account fromAcc;
    Account toAcc;
    List<AppError> errors = new ArrayList<>();
    String amount;
    User user;


    @Before
    public void setUp() throws Exception {
        validator = spy(TransactionValidatorImpl.class);
    }

    @Test
    public void validateTransaction_noErrors() {
        fromAcc = new Account();
        fromAcc.setBalance(1000.0d);
        toAcc = new Account();
        amount = "100,0";
        user = new User();
        List<Account> userAccounts = new ArrayList<>();
        userAccounts.add(fromAcc);
        doReturn(userAccounts).when(validator).getAvailabeAccounts(user);
        assertEquals(errors, validator.validateTransaction(fromAcc, toAcc, amount, user));
    }
    @Test
    public void validateTransaction_UserError() {
        fromAcc = new Account();
        toAcc = new Account();
        assertEquals(1, validator.validateTransaction(fromAcc, toAcc, amount, user).size());
        assertEquals(ValidationField.User, validator.validateTransaction(fromAcc, toAcc, amount, user).get(0).getField());
        assertEquals("user not found", validator.validateTransaction(fromAcc, toAcc, amount, user).get(0).getDescription());
    }

    @Test
    public void validateTransaction_userOk_FromAccError_otherOk() {
        toAcc = new Account();
        user = new User();
        amount = "100.0";
        /*List<Account> userAccounts = new ArrayList<>();
        doReturn(userAccounts).when(validator).getAvailabeAccounts(user);*/
        assertEquals(1, validator.validateTransaction(fromAcc, toAcc, amount, user).size());
        assertEquals(ValidationField.FromAccount, validator.validateTransaction(fromAcc, toAcc, amount, user).get(0).getField());
        assertEquals("Account does not exist", validator.validateTransaction(fromAcc, toAcc, amount, user).get(0).getDescription());
    }

    @Test
    public void validateTransaction_userOk_FromAccError_ToAccError() {
        user = new User();
        amount = "100.0";
        assertEquals(2, validator.validateTransaction(fromAcc, toAcc, amount, user).size());
        assertEquals(ValidationField.FromAccount, validator.validateTransaction(fromAcc, toAcc, amount, user).get(0).getField());
        assertEquals(ValidationField.ToAccount, validator.validateTransaction(fromAcc, toAcc, amount, user).get(1).getField());
        assertEquals("Account does not exist", validator.validateTransaction(fromAcc, toAcc, amount, user).get(0).getDescription());
        assertEquals("Account does not exist", validator.validateTransaction(fromAcc, toAcc, amount, user).get(1).getDescription());
    }

    @Test
    public void validateTransaction_userOk_FromAccError_ToAccError_AmountError() {
        user = new User();
        amount = "";
        assertEquals(3, validator.validateTransaction(fromAcc, toAcc, amount, user).size());
        assertEquals(ValidationField.FromAccount, validator.validateTransaction(fromAcc, toAcc, amount, user).get(0).getField());
        assertEquals(ValidationField.ToAccount, validator.validateTransaction(fromAcc, toAcc, amount, user).get(1).getField());
        assertEquals(ValidationField.Amount, validator.validateTransaction(fromAcc, toAcc, amount, user).get(2).getField());
        assertEquals("Account does not exist", validator.validateTransaction(fromAcc, toAcc, amount, user).get(0).getDescription());
        assertEquals("Account does not exist", validator.validateTransaction(fromAcc, toAcc, amount, user).get(1).getDescription());
        assertEquals(" is not a number", validator.validateTransaction(fromAcc, toAcc, amount, user).get(2).getDescription());

    }

    @Test
    public void validateTransaction_userOk_otherOk() {
        toAcc = new Account();
        fromAcc = new Account();
        fromAcc.setBalance(1000.0);
        user = new User();
        amount = "100.0";
        List<Account> userAccounts = new ArrayList<>();
        userAccounts.add(fromAcc);
        doReturn(userAccounts).when(validator).getAvailabeAccounts(user);
        assertEquals(0, validator.validateTransaction(fromAcc, toAcc, amount, user).size());
    }

    @Test
    public void validateUser_Null() {
        validator.validateUser(user, errors);
        assertEquals(1, errors.size());
        assertEquals("user not found", errors.get(0).getDescription());
    }

    @Test
    public void validateUser_NotNull() {
        user = new User();
        validator.validateUser(user, errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void validateAmount_validAmount_enoughMoney_notZero() {
        fromAcc = new Account();
        fromAcc.setBalance(1000d);
        amount = "100.1";
        validator.validateAmount(fromAcc, amount, errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void validateAmount_validAmount_enoughMoney_Zero() {
        fromAcc = new Account();
        fromAcc.setBalance(1000d);
        amount = "0.00";
        validator.validateAmount(fromAcc, amount, errors);
        assertEquals(1, errors.size());
        assertEquals("Amount cannot be negative or zero", errors.get(0).getDescription());
    }

    @Test
    public void validateAmount_validAmount_NotenoughMoney_NotZero() {
        //0, abc, 1,10, -1,10
        fromAcc = new Account();
        fromAcc.setBalance(0.0d);
        fromAcc.setNumber("123");
        amount = "100.1";
        validator.validateAmount(fromAcc, amount, errors);
        assertEquals(1, errors.size());
        assertEquals("123 has not enough money", errors.get(0).getDescription());
    }

    @Test
    public void validateAmount_NotvalidAmount() {
        //0, abc, 1,10, -1,10
        fromAcc = new Account();
        amount = "bad";
        validator.validateAmount(fromAcc, amount, errors);
        assertEquals(1, errors.size());
        assertEquals("bad is not a number", errors.get(0).getDescription());
    }


    @Test
    public void validateFromAccount_AccNull() {
        validator.validateFromAccount(fromAcc, errors, user);
        assertEquals(1, errors.size());
        assertEquals("Account does not exist", errors.get(0).getDescription());
    }

    @Test
    public void validateFromAccount_AccNotNull_doesNotBelong() {
        fromAcc = new Account();
        List<Account> userAccounts = new ArrayList<>();
        doReturn(userAccounts).when(validator).getAvailabeAccounts(user);
        validator.validateFromAccount(fromAcc, errors, user);
        assertEquals(1, errors.size());
        assertEquals("Account does not belong to user", errors.get(0).getDescription());
    }

    @Test
    public void validateFromAccount_AccNotNull_Belong() {
        fromAcc = new Account();
        List<Account> userAccounts = new ArrayList<>();
        userAccounts.add(fromAcc);
        doReturn(userAccounts).when(validator).getAvailabeAccounts(user);
        validator.validateFromAccount(fromAcc, errors, user);
        assertEquals(0, errors.size());
    }

    @Test
    public void validateToAccount_AccNull() {
        validator.validateToAccount(toAcc, errors);
        assertEquals(1, errors.size());
        assertEquals("Account does not exist", errors.get(0).getDescription());
    }

    @Test
    public void validateToAccount_AccNotNull() {
        toAcc = new Account();
        validator.validateToAccount(toAcc, errors);
        assertEquals(0, errors.size());
    }


    @Test
    public void validateAccounts_fromNotNull_toAccNull() {
    }
}