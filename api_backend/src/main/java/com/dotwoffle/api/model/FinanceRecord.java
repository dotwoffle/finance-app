package com.dotwoffle.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
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
        String description,
        /**The category of the transaction.*/
        Category category
) {

    /**This enum represents a broad type of transaction.*/
    public enum Type
    {
        EXPENSE,
        INCOME
    }

    /**This enum represents a specific category for transactions.*/
    public enum Category {
        FOOD,
        GROCERY,
        BILLS_AND_SUBSCRIPTIONS,
        RENT,
        PAYCHECK,
        OTHER
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return this.getClass().getName();
        }
    }
}
