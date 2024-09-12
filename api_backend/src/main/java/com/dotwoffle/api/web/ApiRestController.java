package com.dotwoffle.api.web;

import com.dotwoffle.api.model.FinanceRecord;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**The REST controller for the API backend. This class handles all API requests sent to available routes.*/
@RestController
public class ApiRestController {

    private final Collection<FinanceRecord> FAKE_DATABASE = new ArrayList<>();

    /**Generates a (optionally sorted) list of filtered finance records from the database.
     * @param queryString The query string passed into the request URL. This can be turned into a
     * {@link com.dotwoffle.api.model.FinanceRecordQuery FinanceRecordQuery} object.
     * @return A list of all records from the database matching the query.*/
    @GetMapping("/api/get-records")
    private Collection<FinanceRecord> getRecords(@RequestParam(name="q") String queryString) {
        System.out.println("Query: " + queryString);
        return FAKE_DATABASE.stream()
                .filter(record -> record.amount().compareTo(BigDecimal.valueOf(3)) == -1)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/create-record")
    private void postRecord(@RequestBody FinanceRecord financeRecord) {
        System.out.println(financeRecord);
        FAKE_DATABASE.add(financeRecord);
    }

}
