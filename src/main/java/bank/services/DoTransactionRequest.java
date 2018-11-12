package bank.services;

public class DoTransactionRequest {
    private String accFrom;
    private String accTo;
    private double amt;
    String username;
    Long userId;

    public String getUsername() {
        return username;
    }
    public Long getUserId(){return userId;}

    public DoTransactionRequest(String accFrom, String accTo, double amt, String username) {
        this.accFrom = accFrom;
        this.accTo = accTo;
        this.amt = amt;
        this.username = username;
    }

    public String getAccFrom() {
        return accFrom;
    }

    public String getAccTo() {
        return accTo;
    }

    public double getAmt() {
        return amt;
    }
}
