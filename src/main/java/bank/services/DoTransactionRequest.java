package bank.services;

public class DoTransactionRequest {
    private String accFrom;
    private String accTo;
    private String amt;
    String username;
    Long userId;

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }

    public DoTransactionRequest(String accFrom, String accTo, String amt, Long userId) {
        this.accFrom = accFrom;
        this.accTo = accTo;
        this.amt = amt;
        this.userId = userId;
    }

    public String getAccFrom() {
        return accFrom;
    }

    public String getAccTo() {
        return accTo;
    }

    public String getAmt() {
        return amt;
    }
}
