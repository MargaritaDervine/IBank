package bank;

import bank.domain.ValidationField;

public class AppError {
    ValidationField field;
    String description;

    public ValidationField getField() {
        return field;
    }

    public String getDescription() {
        return description;
    }

    public void setField(ValidationField field) {
        this.field = field;
    }

    public AppError(ValidationField field, String description) {
        this.field = field;
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
