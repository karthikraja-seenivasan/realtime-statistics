package com.usecase.realtimestatistics.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorHandling extends AbstractErrorWebExceptionHandler {
    public GlobalErrorHandling(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                               ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errors = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());

        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(errors));

    }

    @ExceptionHandler(GlobalException.class)
    public Map<String, Object> handleUserNotFound(ServerRequest request, ErrorAttributeOptions options){
        Map<String, Object> map = super.getErrorAttributes(request, options.including(ErrorAttributeOptions.Include.MESSAGE));
        GlobalException ex = (GlobalException) getError(request);
        map.put("exception", ex.getClass().getSimpleName());
        map.put("message", ex.getMessage());
        map.put("status", ex.getStatus().value());
        map.put("error", ex.getStatus().getReasonPhrase());
        return map;
    }
}
