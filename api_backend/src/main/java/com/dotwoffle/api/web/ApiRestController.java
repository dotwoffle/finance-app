package com.dotwoffle.api.web;

import com.dotwoffle.api.exceptions.MalformedQueryException;
import com.dotwoffle.api.model.FinanceRecord;
import com.dotwoffle.api.model.FinanceRecordQuery;
import com.mysql.cj.jdbc.Driver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**The REST controller for the API backend. This class handles all API requests sent to available routes.*/
@RestController
public class ApiRestController {

    private final Connection DATABASE_CONNECTION;

    public ApiRestController() throws SQLException {

        DriverManager.registerDriver(new Driver());

        this.DATABASE_CONNECTION = DriverManager.getConnection(
                "jdbc:mysql://localhost/finance_app",
                "financeapp",
                "FinanceAppDev12345!"
        );

    }

    /**Generates a (optionally sorted) list of filtered finance records from the database.
     * @param queryString The query string passed into the request URL. This can be turned into a
     * {@link FinanceRecordQuery FinanceRecordQuery} object.
     * @return A list of all records from the database matching the query.*/
    @GetMapping("/api/get-records")
    private ResponseEntity<Collection<FinanceRecord>> getRecords(
            @RequestParam(name="q", required=false) String queryString
    ) throws MalformedQueryException {

        FinanceRecordQuery actualQuery = queryString == null ? null : FinanceRecordQuery.fromString(queryString);

        try(ResultSet queryResults = applyQuery(actualQuery)) {

            Collection<FinanceRecord> recordList = new HashSet<>();

            while(queryResults.next()) {

                UUID id = UUID.fromString(queryResults.getString("recordId"));
                FinanceRecord.Type type = FinanceRecord.Type.valueOf(queryResults.getString("type"));
                BigDecimal amount = queryResults.getBigDecimal("amount");
                LocalDate date = queryResults.getDate("date").toLocalDate();
                String description = queryResults.getString("description");
                FinanceRecord.Category category = FinanceRecord.Category.valueOf(queryResults.getString("category"));
                FinanceRecord record = new FinanceRecord(
                        id,
                        type,
                        amount,
                        date,
                        description,
                        category
                );

                recordList.add(record);

            }

            return ResponseEntity.ok(recordList);

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**Adds a finance record that was posted by the client to the database.
     * @param financeRecord The deserialized {@link com.dotwoffle.api.model.FinanceRecord} object that was posted.*/
    @PostMapping("/api/create-record")
    private void postRecord(@RequestBody FinanceRecord financeRecord) {

        try {

            PreparedStatement insertStatement = DATABASE_CONNECTION.prepareStatement("INSERT INTO finance_records(recordId, type, category, description, amount, date) VALUES(?, ?, ?, ?, ?, ?)");

            insertStatement.setString(1, financeRecord.id().toString());
            insertStatement.setString(2, financeRecord.type().toString());
            insertStatement.setString(3, financeRecord.category().toString());
            insertStatement.setString(4, financeRecord.description());
            insertStatement.setBigDecimal(5, financeRecord.amount());
            insertStatement.setDate(6, Date.valueOf(financeRecord.date()));

            insertStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**Applies a query to the database to return a filtered list of finance records.
     * @param query The query to apply.
     * @return The {@link ResultSet} object returned by the SQL query.*/
    private ResultSet applyQuery(FinanceRecordQuery query) {

        String selectStatementSql = "SELECT * FROM finance_records";

        if(query != null) {

            List<String> whereClauses = new ArrayList<>();

            selectStatementSql += " WHERE ";

            if(query.getLowerAmountBound() != null) {
                whereClauses.add("amount >= " + query.getLowerAmountBound());
            }
            if(query.getUpperAmountBound() != null) {
                whereClauses.add("amount <= " + query.getUpperAmountBound());
            }
            if(query.getLowerDateBound() != null) {
                whereClauses.add("date >= '" + query.getLowerDateBound() + "'");
            }
            if(query.getUpperDateBound() != null) {
                whereClauses.add("date <= '" + query.getUpperDateBound() + "'");
            }
            if(query.getAllowedTypes() != null) {

                String typeListAsString = query.getAllowedTypes().stream()
                        .map(FinanceRecord.Type::toString)
                        .collect(Collectors.joining(","));

                whereClauses.add("FIND_IN_SET(type, '" + typeListAsString + "')");

            }
            if(query.getAllowedCategories() != null) {

                String categoryListAsString = query.getAllowedCategories().stream()
                        .map(FinanceRecord.Category::toString)
                        .collect(Collectors.joining(","));

                whereClauses.add("FIND_IN_SET(type, '" + categoryListAsString + "')");

            }

            selectStatementSql += String.join(" AND ", whereClauses);

        }

        try {
            Statement selectStatement = DATABASE_CONNECTION.createStatement();
            selectStatement.execute(selectStatementSql);
            return selectStatement.getResultSet();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
