package bank.services;

import java.util.List;

public class DoTransactionResponse {
    private boolean success;
    private Long transactionId;
    private List<String> errors;

    public DoTransactionResponse(Long transactionId) {
        this.success = true;
        this.transactionId = transactionId;
    }

    public DoTransactionResponse(List<String> errors) {
        this.success = false;
        this.errors = errors;
    }
    public boolean isSuccess() {
        return success;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public List<String> getErrors() {
        return errors;
    }
}
