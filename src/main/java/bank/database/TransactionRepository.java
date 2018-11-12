package bank.database;

import bank.domain.Account;
import bank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from Transaction t where t.fromAccount=:acc or t.fromAccount=:acc")
    List<Transaction> findByAccount(@Param("acc")String acc);

 //   Transaction saveAndFlush(Transaction transaction);

}
