package com.usecase.realtimestatistics.service;

import com.usecase.realtimestatistics.model.Transaction;
import com.usecase.realtimestatistics.model.TransactionResponse;
import com.usecase.realtimestatistics.model.TransactionStatistics;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<TransactionResponse> save(Transaction transaction);

    Mono<ResponseEntity<TransactionStatistics>> getStatistics();
}
