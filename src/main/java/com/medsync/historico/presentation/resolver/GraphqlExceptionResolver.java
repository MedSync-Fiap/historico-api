package com.medsync.historico.presentation.resolver;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Slf4j
@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {

        Optional<GraphQLError> errorFromAnnotation = getErrorFromResponseStatusAnnotation(ex, env);
        if (errorFromAnnotation.isPresent()) {
            return errorFromAnnotation.get();
        }

        if (ex instanceof IllegalArgumentException) {
            return buildError(env, ErrorType.BAD_REQUEST, ex.getMessage());
        }

        log.error("Unexpected error occurred", ex);
        return buildError(env, ErrorType.INTERNAL_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    protected Optional<GraphQLError> getErrorFromResponseStatusAnnotation(Throwable ex, DataFetchingEnvironment env) {
        ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        if (status != null) {
            ErrorType errorType = convertHttpStatusToErrorType(status.code());
            return Optional.of(buildError(env, errorType, ex.getMessage()));
        }
        return Optional.empty();
    }

    protected ErrorType convertHttpStatusToErrorType(HttpStatus httpStatus) {
        return switch (httpStatus) {
            case NOT_FOUND -> ErrorType.NOT_FOUND;
            case BAD_REQUEST -> ErrorType.BAD_REQUEST;
            case UNAUTHORIZED -> ErrorType.UNAUTHORIZED;
            case FORBIDDEN -> ErrorType.FORBIDDEN;
            default -> ErrorType.INTERNAL_ERROR;
        };
    }

    protected GraphQLError buildError(DataFetchingEnvironment env, ErrorType errorType, String message) {
        return GraphqlErrorBuilder
                .newError(env)
                .errorType(errorType)
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

}
