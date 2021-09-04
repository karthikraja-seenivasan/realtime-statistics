package com.usecase.realtimestatistics;

import com.usecase.realtimestatistics.model.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

@SpringBootApplication
public class RealtimeStatisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealtimeStatisticsApplication.class, args);

	}

}
