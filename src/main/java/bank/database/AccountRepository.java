package bank.database;

import bank.domain.Account;
import bank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


 public interface  AccountRepository extends JpaRepository<Account, String> {

    List<Account> findByUser(User user);
    Account findByNumber(String accNumber);
   // void save(Account account);

}
