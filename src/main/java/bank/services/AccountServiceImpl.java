package bank.services;

import bank.database.AccountRepository;
import bank.domain.Account;
import bank.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Component
public class AccountServiceImpl {
    @Autowired
    private AccountRepository repository;

    public Account getAccount(String accN) {
        return repository.findByNumber(accN);
    }

    @Transactional
    public void changeBalance(double amt, String accountNumber) {
        Account account = getAccount(accountNumber);
        double newBalance = account.getBalance() + amt;
        account.setBalance(newBalance);
        repository.save(account);
    }
    public List<Account> getAvailabeAccounts(User user) {
        return repository.findByUser(user);
    }

}
