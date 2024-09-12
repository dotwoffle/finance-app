package com.dotwoffle.api.model;

import com.dotwoffle.api.exceptions.MalformedQueryException;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Optional;

/**This class represents an arbitrary query for filtering and sorting finance records from the database.*/
public class FinanceRecordQuery {

    /**Constructs a new query object from a query string as given in a request URL to the API endpoint.
     * @param queryString The query string. See class description for parameters that can be passed to this string.
     * @return A new query object representing the given query string.
     * @throws MalformedQueryException If the query string is malformed.*/
    public static FinanceRecordQuery fromString(String queryString) throws MalformedQueryException {

        FinanceRecordQuery query = new FinanceRecordQuery();
        String[] queryParts = queryString.split(" ");

        for(String queryParam : queryParts) {
            query.processQueryParam(queryParam);
        }

        return query;

    }

    private static final String AMOUNT_PARAM_NAME = "amount";
    private static final String DATE_PARAM_NAME = "date";
    /**A lower bound on monetary amount.*/
    private Optional<BigDecimal> lowerAmountBound = Optional.empty();
    /**An upper bound on monetary amount.*/
    private Optional<BigDecimal> upperAmountBound = Optional.empty();
    /**A lower bound on transaction date.*/
    private Optional<LocalDate> lowerDateBound = Optional.empty();
    /**An upper bound on transaction date.*/
    private Optional<LocalDate> upperDateBound = Optional.empty();
    /**A list of transaction types that are allowed by the filter.*/
    private Optional<Collection<FinanceRecord.Type>> allowedTypes = Optional.empty();
    /**A list of transaction categories that are allowed by the filter.*/
    private Optional<Collection<FinanceRecord.Category>> allowedCategories = Optional.empty();

    /**Constructs a new empty query object.*/
    private FinanceRecordQuery() {}

    private void processQueryParam(String queryParam) throws MalformedQueryException {

        if(queryParam.startsWith("a")) {
            processAmountParam(queryParam);
        }
        else if(queryParam.startsWith("d")) {
            processDateParam(queryParam);
        }

    }

    private void processAmountParam(String queryParam) throws MalformedQueryException {

        String queryParamCopy = queryParam.substring(1);

        if(queryParamCopy.startsWith("<")) {

            if(upperAmountBound.isPresent()) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Query already specified upper bound for amount");
            }

            queryParamCopy = queryParamCopy.substring(1);

            try {
                if(queryParamCopy.startsWith("=")) {
                    upperAmountBound = Optional.of(new BigDecimal(queryParamCopy.substring(1)));
                }
                else {
                    upperAmountBound = Optional.of(new BigDecimal(queryParamCopy).add(BigDecimal.valueOf(0.01)));
                }
            }
            catch(NumberFormatException e) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, queryParamCopy + "is not a valid value for " + AMOUNT_PARAM_NAME);
            }

            if(lowerAmountBound.isPresent() && lowerAmountBound.get().compareTo(upperAmountBound.get()) == 1) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Upper bound for amount contradicts the previously set lower bound of " + lowerAmountBound.get());
            }

        }
        else if(queryParamCopy.startsWith(">")) {

            if(lowerAmountBound.isPresent()) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Query already specified lower bound for amount");
            }

            queryParamCopy = queryParamCopy.substring(1);

            try {
                if(queryParamCopy.startsWith("=")) {
                    lowerAmountBound = Optional.of(new BigDecimal(queryParamCopy.substring(1)));
                }
                else {
                    lowerAmountBound = Optional.of(new BigDecimal(queryParamCopy).add(BigDecimal.valueOf(0.01)));
                }
            }
            catch(NumberFormatException e) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, queryParamCopy + "is not a valid value for " + AMOUNT_PARAM_NAME);
            }

            if(upperAmountBound.isPresent() && upperAmountBound.get().compareTo(lowerAmountBound.get()) == 1) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Lower bound for amount contradicts the previously set upper bound of " + lowerAmountBound.get());
            }

        }
        else {
            throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Invalid operator in " + queryParam);
        }

    }

    private void processDateParam(String queryParam) throws MalformedQueryException {

        String queryParamCopy = queryParam.substring(1);

        if(queryParamCopy.startsWith("<")) {

            if(upperDateBound.isPresent()) {
                throw new MalformedQueryException(DATE_PARAM_NAME, "Query already specified upper bound for date");
            }

            queryParamCopy = queryParamCopy.substring(1);

            try {
                if(queryParamCopy.startsWith("=")) {
                    upperDateBound = Optional.of(LocalDate.parse(queryParamCopy.substring(1)));
                }
                else {
                    upperDateBound = Optional.of(LocalDate.parse(queryParamCopy.substring(1)).minusDays(1));
                }
            }
            catch(NumberFormatException e) {
                throw new MalformedQueryException(DATE_PARAM_NAME, queryParamCopy + "is not a valid value for " + DATE_PARAM_NAME);
            }

            if(lowerDateBound.isPresent() && lowerDateBound.get().compareTo(upperDateBound.get()) == 1) {
                throw new MalformedQueryException(DATE_PARAM_NAME, "Upper bound for date contradicts the previously set lower bound of " + lowerDateBound.get());
            }

        }
        else if(queryParamCopy.startsWith(">")) {

            if(lowerDateBound.isPresent()) {
                throw new MalformedQueryException(DATE_PARAM_NAME, "Query already specified lower bound for date");
            }

            queryParamCopy = queryParamCopy.substring(1);

            try {
                if(queryParamCopy.startsWith("=")) {
                    lowerDateBound = Optional.of(LocalDate.parse(queryParamCopy.substring(1)));
                }
                else {
                    lowerDateBound = Optional.of(LocalDate.parse(queryParamCopy.substring(1)).plusDays(1));
                }
            }
            catch(NumberFormatException e) {
                throw new MalformedQueryException(DATE_PARAM_NAME, queryParamCopy + "is not a valid value for " + DATE_PARAM_NAME);
            }

            if(upperDateBound.isPresent() && upperDateBound.get().compareTo(upperDateBound.get()) == 1) {
                throw new MalformedQueryException(DATE_PARAM_NAME, "Lower bound for date contradicts the previously set upper bound of " + lowerDateBound.get());
            }

        }
        else {
            throw new MalformedQueryException(DATE_PARAM_NAME, "Invalid operator in " + queryParam);
        }

    }

}
