package com.usecase.realtimestatistics.service;

import com.usecase.realtimestatistics.exception.GlobalException;
import com.usecase.realtimestatistics.model.Transaction;
import com.usecase.realtimestatistics.model.TransactionResponse;
import com.usecase.realtimestatistics.model.TransactionStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    private static Date NOW = Date.from(Instant.now());

    private Map<String, Transaction> transactionMap;

    @PostConstruct
    public void init(){

        transactionMap = new HashMap<>();

        transactionMap.put("karthik",
                Transaction.builder().amount(BigDecimal.valueOf(100.02)).timeStamp("2021-09-04T07:02:51Z").build());
        transactionMap.put("Logesh",
                Transaction.builder().amount(BigDecimal.valueOf(56.02)).timeStamp("2021-09-04T07:36:51Z").build());
        transactionMap.put("Sachin",
                Transaction.builder().amount(BigDecimal.valueOf(77.25)).timeStamp("2021-09-04T07:02:51Z").build());
        transactionMap.put("Prem",
                Transaction.builder().amount(BigDecimal.valueOf(10.34)).timeStamp("2021-09-04T07:02:51Z").build());
        transactionMap.put("Prakash",
                Transaction.builder().amount(BigDecimal.valueOf(66.40)).timeStamp("2021-09-04T07:02:51Z").build());
    }
    @Override
    public Mono<ResponseEntity<TransactionResponse>> save(Transaction transaction) {

        ResponseEntity<TransactionResponse> entity = ResponseEntity.noContent().build();
        if(getStatus(transaction.getTimeStamp())==201){

            entity = ResponseEntity.status(HttpStatus.CREATED)
                    .body( TransactionResponse.builder().statusCode(getStatus(transaction.getTimeStamp())).build());
        }

        return Mono.just(entity);
    }



    @Override
    public Mono<ResponseEntity<TransactionStatistics>> getStatistics() {
        TransactionStatistics statistics = new TransactionStatistics();
       List<Transaction> filteredList = transactionMap.entrySet().stream().filter(val -> getStatus(val.getValue().getTimeStamp())==201).map(res ->
             res.getValue()).collect(Collectors.toList());

        statistics.setSum(BigDecimal.valueOf( filteredList.stream().mapToDouble(sum -> sum.getAmount().doubleValue()).sum()));
        statistics.setAvg(BigDecimal.valueOf(filteredList.stream().mapToDouble(avg -> avg.getAmount().doubleValue()).average().orElse(0.0)));
        statistics.setMin(BigDecimal.valueOf(filteredList.stream().mapToDouble(min -> min.getAmount().doubleValue()).min().orElse(0.0)));
        statistics.setMax(BigDecimal.valueOf(filteredList.stream().mapToDouble(max -> max.getAmount().doubleValue()).max().orElse(0.0)));
        statistics.setCount(filteredList.stream().count());
        return Mono.just(ResponseEntity.ok(statistics));
    }

    @Override
    public Mono<ResponseEntity> delete() {
        transactionMap.clear();
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    public int getStatus(String requestTime){
        int statuCode = 0;
        log.info("current Time {}", Instant.now());
        Date requestTimeStamp  = Date.from(Instant.parse(requestTime));
        long diff =   NOW.getTime() - requestTimeStamp.getTime();
        long diffInMins = diff / (60 * 1000) % 60;

        if(diff<0){
            throw new GlobalException(HttpStatus.UNPROCESSABLE_ENTITY,"The timeframe cannot be futere value");
        }
        else if (diffInMins<1){
            statuCode = HttpStatus.CREATED.value();
        }
        else {
            statuCode = HttpStatus.NO_CONTENT.value();

        }
        return  statuCode;
    }
}
