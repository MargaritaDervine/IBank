package bank.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @Column(name="transaction_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "from_account_number", nullable = false)
    Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_number", nullable = false)
    Account toAccount;

    @Column(name="dateTime", nullable = false)
    LocalDateTime dateTime;

    @Column(name="amount", nullable = false)
    double amount;

    public void setId(Long id) {
        this.id = id;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public void setDateTime(Timestamp timestamp) {
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        this.dateTime = dateTime;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public Transaction() {
    }

    public Long getId(){return id;}

}
