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

    @GetMapping("/api/get-records")
    private Collection<FinanceRecord> getRecords(@RequestParam(name="q") String queryString) {
        System.out.println("Query: " + queryString);
        return FAKE_DATABASE.stream()
                .filter(record -> record.amount().compareTo(BigDecimal.valueOf(3)) == -1)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/create-record")
    private void postRecord(@RequestBody FinanceRecord financeRecord) {
        System.out.println("date: " + financeRecord.date());
        System.out.println("amount: " + financeRecord.amount());
        System.out.println("description: " + financeRecord.description());
        FAKE_DATABASE.add(financeRecord);
    }

}
