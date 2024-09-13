package com.dotwoffle.api.web;

import com.dotwoffle.api.exceptions.MalformedQueryException;
import com.dotwoffle.api.model.FinanceRecord;
import com.dotwoffle.api.model.FinanceRecordQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**The REST controller for the API backend. This class handles all API requests sent to available routes.*/
@RestController
public class ApiRestController {

    private static final String FAKE_DB_FILE_PATH = "src/main/resources/fake_records.csv";
    private final Collection<FinanceRecord> FAKE_DATABASE = loadFakeDatabase();

    private static Collection<FinanceRecord> loadFakeDatabase() {


        BufferedReader fileReader;

        try {
            fileReader = new BufferedReader(new FileReader(FAKE_DB_FILE_PATH));
        } catch (FileNotFoundException e) {
            System.err.println("Unable to load " + FAKE_DB_FILE_PATH);
            return Collections.emptyList();
        }

        return fileReader.lines()
                .map(line -> {

                    String[] recordParts = line.split(",");

                    return new FinanceRecord(
                            UUID.randomUUID(),
                            FinanceRecord.Type.valueOf(recordParts[3]),
                            new BigDecimal(recordParts[1]),
                            LocalDate.parse(recordParts[0]),
                            recordParts[2],
                            FinanceRecord.Category.valueOf(recordParts[4])
                    );

                })
                .collect(Collectors.toList());

    }

    /**Generates a (optionally sorted) list of filtered finance records from the database.
     * @param queryString The query string passed into the request URL. This can be turned into a
     * {@link com.dotwoffle.api.model.FinanceRecordQuery FinanceRecordQuery} object.
     * @return A list of all records from the database matching the query.*/
    @GetMapping("/api/get-records")
    private ResponseEntity<Collection<FinanceRecord>> getRecords(
            @RequestParam(name="q", required=false) String queryString
    ) throws MalformedQueryException {

        System.out.println("Query: " + queryString);

        if(queryString == null || queryString.isEmpty()) {
            return ResponseEntity.ok(FAKE_DATABASE);
        }

        return ResponseEntity.ok(applyQuery(FinanceRecordQuery.fromString(queryString)));

    }

    @PostMapping("/api/create-record")
    private void postRecord(@RequestBody FinanceRecord financeRecord) {
        System.out.println(financeRecord);
        FAKE_DATABASE.add(financeRecord);
    }

    private List<FinanceRecord> applyQuery(FinanceRecordQuery query) {

        Stream<FinanceRecord> dbStream = FAKE_DATABASE.stream();

        if(query.getLowerAmountBound() != null) {
            dbStream = dbStream.filter(record -> record.amount().compareTo(query.getLowerAmountBound()) > 0);
        }
        if(query.getUpperAmountBound() != null) {
            dbStream = dbStream.filter(record -> record.amount().compareTo(query.getUpperAmountBound()) < 0);
        }
        if(query.getLowerDateBound() != null) {
            dbStream = dbStream.filter(record -> record.date().isAfter(query.getLowerDateBound()));
        }
        if(query.getUpperDateBound() != null) {
            dbStream = dbStream.filter(record -> record.date().isBefore(query.getUpperDateBound()));
        }
        if(query.getAllowedTypes() != null) {
            dbStream = dbStream.filter(record -> query.getAllowedTypes().contains(record.type()));
        }
        if(query.getAllowedCategories() != null) {
            dbStream = dbStream.filter(record -> query.getAllowedCategories().contains(record.category()));
        }

        return dbStream.collect(Collectors.toList());

    }

}
