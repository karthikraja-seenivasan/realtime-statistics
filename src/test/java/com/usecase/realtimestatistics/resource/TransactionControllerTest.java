package com.usecase.realtimestatistics.resource;

import com.usecase.realtimestatistics.model.Transaction;
import com.usecase.realtimestatistics.model.TransactionResponse;
import com.usecase.realtimestatistics.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TransactionsController.class)
@Import(TransactionService.class)
public class TransactionControllerTest {

    @MockBean
    TransactionService service;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testCreateTransaction() {
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(100.02))
                .timeStamp(Instant.now().toString())
                .build();

        ResponseEntity<TransactionResponse> response = ResponseEntity
                .status(HttpStatus.NO_CONTENT).body(TransactionResponse.builder().statusCode(204).build());

        when(service.save(transaction)).thenReturn(Mono.just(response));

        webClient.post()
                .uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transaction))
                .exchange()
                .expectStatus().isNoContent()
                 .expectBody()
                .jsonPath("$.statusCode").isEqualTo(response.getBody().getStatusCode());

    }

}
