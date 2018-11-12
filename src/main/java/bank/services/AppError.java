package bank.services;

public class AppError {
    private String field;
    private String description;

    public AppError(String field, String description) {
        this.field = field;
        this.description = description;
    }

    public String getField() {
        return field;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString(){
        return this.getField() + this.getDescription();
    }


}
