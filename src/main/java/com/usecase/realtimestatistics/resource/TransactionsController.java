package com.usecase.realtimestatistics.resource;

import com.usecase.realtimestatistics.exception.GlobalException;
import com.usecase.realtimestatistics.model.Transaction;
import com.usecase.realtimestatistics.model.TransactionResponse;
import com.usecase.realtimestatistics.model.TransactionStatistics;
import com.usecase.realtimestatistics.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionService transactionService;

    @PostMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionResponse>> saveTransaction(@Valid @RequestBody Transaction transaction) {
        if (!Optional.ofNullable(transaction.getTimeStamp()).isPresent()) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Timestamp cannot be empty");
        }
        return transactionService.save(transaction);
    }


    @GetMapping(value = "/statistics", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ResponseEntity<TransactionStatistics>> getStatistics() {
        return transactionService.getStatistics().switchIfEmpty(Mono.empty());
    }

    @DeleteMapping("/transactions")
    public Mono<ResponseEntity> delete() {
        return transactionService.delete();
    }
}
