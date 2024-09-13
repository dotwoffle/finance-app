package com.dotwoffle.api.model;

import com.dotwoffle.api.exceptions.MalformedQueryException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

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
    private static final String ALLOWED_TYPES_PARAM_NAME = "allowedTypes";
    private static final String ALLOWED_CATEGORIES_PARAM_NAME = "allowedCategories";
    /**A lower bound on monetary amount.*/
    private BigDecimal lowerAmountBound = null;
    /**An upper bound on monetary amount.*/
    private BigDecimal upperAmountBound = null;
    /**A lower bound on transaction date.*/
    private LocalDate lowerDateBound = null;
    /**An upper bound on transaction date.*/
    private LocalDate upperDateBound = null;
    /**A list of transaction types that are allowed by the filter.*/
    private Collection<FinanceRecord.Type> allowedTypes = null;
    /**A list of transaction categories that are allowed by the filter.*/
    private Collection<FinanceRecord.Category> allowedCategories = null;

    /**Constructs a new empty query object.*/
    private FinanceRecordQuery() {}

    private void processQueryParam(String queryParam) throws MalformedQueryException {

        if(queryParam.startsWith(AMOUNT_PARAM_NAME)) {
            processAmountParam(queryParam);
        }
        else if(queryParam.startsWith(DATE_PARAM_NAME)) {
            processDateParam(queryParam);
        }
        else if(queryParam.startsWith(ALLOWED_TYPES_PARAM_NAME))
        {
            processAllowedTypesParam(queryParam);
        }
        else if(queryParam.startsWith(ALLOWED_CATEGORIES_PARAM_NAME)) {
            processAllowedCategoriesParam(queryParam);
        }

    }

    private void processAmountParam(String queryParam) throws MalformedQueryException {

        String queryParamCopy = queryParam.substring(1);

        if(queryParamCopy.startsWith("<")) {

            if(upperAmountBound != null) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Query already specified upper bound for amount");
            }

            queryParamCopy = queryParamCopy.substring(1);

            try {
                if(queryParamCopy.startsWith("=")) {
                    upperAmountBound = new BigDecimal(queryParamCopy.substring(1));
                }
                else {
                    upperAmountBound = new BigDecimal(queryParamCopy).add(BigDecimal.valueOf(0.01));
                }
            }
            catch(NumberFormatException e) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, queryParamCopy + "is not a valid value for " + AMOUNT_PARAM_NAME);
            }

            if(lowerAmountBound != null && lowerAmountBound.compareTo(upperAmountBound) > 0) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Upper bound for amount contradicts the previously set lower bound of " + lowerAmountBound);
            }

        }
        else if(queryParamCopy.startsWith(">")) {

            if(lowerAmountBound != null) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Query already specified lower bound for amount");
            }

            queryParamCopy = queryParamCopy.substring(1);

            try {
                if(queryParamCopy.startsWith("=")) {
                    lowerAmountBound = new BigDecimal(queryParamCopy.substring(1));
                }
                else {
                    lowerAmountBound = new BigDecimal(queryParamCopy).add(BigDecimal.valueOf(0.01));
                }
            }
            catch(NumberFormatException e) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, queryParamCopy + "is not a valid value for " + AMOUNT_PARAM_NAME);
            }

            if(upperAmountBound != null && upperAmountBound.compareTo(lowerAmountBound) > 0) {
                throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Lower bound for amount contradicts the previously set upper bound of " + lowerAmountBound);
            }

        }
        else {
            throw new MalformedQueryException(AMOUNT_PARAM_NAME, "Invalid operator in " + queryParam);
        }

    }

    private void processDateParam(String queryParam) throws MalformedQueryException {

        String queryParamCopy = queryParam.substring(1);

        if(queryParamCopy.startsWith("<")) {

            if(upperDateBound != null) {
                throw new MalformedQueryException(DATE_PARAM_NAME, "Query already specified upper bound for date");
            }

            queryParamCopy = queryParamCopy.substring(1);

            try {
                if(queryParamCopy.startsWith("=")) {
                    upperDateBound = LocalDate.parse(queryParamCopy.substring(1));
                }
                else {
                    upperDateBound = LocalDate.parse(queryParamCopy.substring(1)).minusDays(1);
                }
            }
            catch(NumberFormatException e) {
                throw new MalformedQueryException(DATE_PARAM_NAME, queryParamCopy + "is not a valid value for " + DATE_PARAM_NAME);
            }

            if(lowerDateBound != null && lowerDateBound.isAfter(upperDateBound)) {
                throw new MalformedQueryException(DATE_PARAM_NAME, "Upper bound for date contradicts the previously set lower bound of " + lowerDateBound);
            }

        }
        else if(queryParamCopy.startsWith(">")) {

            if(lowerDateBound != null) {
                throw new MalformedQueryException(DATE_PARAM_NAME, "Query already specified lower bound for date");
            }

            queryParamCopy = queryParamCopy.substring(1);

            try {
                if(queryParamCopy.startsWith("=")) {
                    lowerDateBound = LocalDate.parse(queryParamCopy.substring(1));
                }
                else {
                    lowerDateBound = LocalDate.parse(queryParamCopy.substring(1)).plusDays(1);
                }
            }
            catch(NumberFormatException e) {
                throw new MalformedQueryException(DATE_PARAM_NAME, queryParamCopy + "is not a valid value for " + DATE_PARAM_NAME);
            }

            if(upperDateBound != null && upperDateBound.isBefore(lowerDateBound)) {
                throw new MalformedQueryException(DATE_PARAM_NAME, "Lower bound for date contradicts the previously set upper bound of " + lowerDateBound);
            }

        }
        else {
            throw new MalformedQueryException(DATE_PARAM_NAME, "Invalid operator in " + queryParam);
        }

    }

    private void processAllowedTypesParam(String queryParam) throws MalformedQueryException {

        if(allowedTypes != null) {
            throw new MalformedQueryException(ALLOWED_TYPES_PARAM_NAME, "Query already specified allowed types list");
        }

        String queryParamCopy = queryParam.substring(1);
        allowedTypes = new ArrayList<>();

        for(String allowedType : queryParamCopy.split(",")) {
            try {
                allowedTypes.add(FinanceRecord.Type.valueOf(allowedType));
            }
            catch(IllegalArgumentException e) {
                throw new MalformedQueryException(ALLOWED_TYPES_PARAM_NAME, allowedType + " is not a valid type");
            }
        }

    }

    private void processAllowedCategoriesParam(String queryParam) throws MalformedQueryException {

        if(allowedCategories != null) {
            throw new MalformedQueryException(ALLOWED_CATEGORIES_PARAM_NAME, "Query already specified allowed categories list");
        }

        String queryParamCopy = queryParam.substring(1);
        allowedCategories = new ArrayList<>();

        for(String allowedCategory : queryParamCopy.split(",")) {
            try {
                allowedCategories.add(FinanceRecord.Category.valueOf(allowedCategory));
            }
            catch(IllegalArgumentException e) {
                throw new MalformedQueryException(ALLOWED_CATEGORIES_PARAM_NAME, allowedCategory + " is not a valid category");
            }
        }

    }

}
