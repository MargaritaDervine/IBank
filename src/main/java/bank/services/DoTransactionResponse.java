package bank.services;

import bank.AppError;

import java.util.List;
import java.util.Objects;

public class DoTransactionResponse {
    private boolean success;
    private Long transactionId;
    private List<AppError> errors;

    public DoTransactionResponse(Long transactionId) {
        this.success = true;
        this.transactionId = transactionId;
    }

    public DoTransactionResponse(List<AppError> errors) {
        this.success = false;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public List<AppError> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoTransactionResponse that = (DoTransactionResponse) o;
        return success == that.success &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {

        return Objects.hash(success, transactionId, errors);
    }
}
