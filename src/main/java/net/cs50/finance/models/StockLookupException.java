package net.cs50.finance.models;

/**
 * Created by Chris Bay on 5/17/15.
 */
public class StockLookupException extends Exception {

    private String symbol;

    public StockLookupException(){}

    public StockLookupException(String message, String symbol) {
        super(message);
        this.symbol = symbol;
    }

    public StockLookupException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return ("Unable to lookup data for stock: " + symbol + ", " + super.getMessage());
    }

    public String getSymbol() {
        return symbol;
    }
}
