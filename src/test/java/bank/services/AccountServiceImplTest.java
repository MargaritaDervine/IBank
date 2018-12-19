package bank.services;

import bank.database.AccountRepository;
import bank.domain.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    AccountServiceImpl accountService;

    @Mock
    AccountRepository repository;

    @Before
    public void setUp() throws Exception {
        accountService = spy(AccountServiceImpl.class);
    }

    @Test
    public void changeBalance() {
        double amount = 5d;
        String accN = "123";
        Account account = new Account();
        account.setBalance(0.5d);
        doReturn(account).when(accountService).getAccount(accN);
        accountService.setRepository(repository);
        accountService.changeBalance(amount, accN);
        assertEquals(java.util.Optional.ofNullable(5.5), java.util.Optional.ofNullable(account.getBalance()));
    }
}