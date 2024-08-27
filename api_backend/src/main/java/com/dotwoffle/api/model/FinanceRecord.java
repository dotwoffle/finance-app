package com.dotwoffle.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**This record represents a single finance record. A finance record can either be an expense (money lost) or income
 * (money gained).*/
public record FinanceRecord(
        /**This record's unique ID.*/
        UUID id,
        /**The type of transaction (expense or income).*/
        Type type,
        /**The monetary amount for the transaction.*/
        BigDecimal amount,
        /**The date of the transaction.*/
        LocalDate date,
        /**The description of the transaction.*/
        String description
) {

    public enum Type
    {
        EXPENSE,
        INCOME
    }

    public enum Category {

    }

}
