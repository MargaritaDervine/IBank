package bank.domain;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_number")
    private String number;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "currency", nullable = false)
    private String currency;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account that = (Account) o;

        if (number != null ? !number.equals(that.number) : that.number != null)
            return false;
        else return true;
    }
}
