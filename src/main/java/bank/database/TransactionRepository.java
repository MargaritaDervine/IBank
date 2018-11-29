package bank.database;

import bank.domain.Account;
import bank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   /* @Query("select t from Transaction t where t.fromAccount=:acc or t.toAccount=:acc")
    List<Transaction> findByAccount(@Param("acc")Account acc);*/

    @Query(value = "select * from Transactions t where t.from_account_number=:acc or t.to_account_number=:acc", nativeQuery =  true)
    List<Transaction> findByAccount(@Param("acc")Account acc);

}
