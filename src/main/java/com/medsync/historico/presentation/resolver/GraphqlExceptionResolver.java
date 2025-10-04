package com.medsync.historico.presentation.resolver;

import com.medsync.historico.application.exceptions.AppointmentNotFoundException;
import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    private static final Map<Class<? extends Throwable>, ErrorType> EXCEPTION_TO_ERROR_TYPE_MAP = Map.of(
            MedicalHistoryNotFoundException.class, ErrorType.NOT_FOUND,
            AppointmentNotFoundException.class, ErrorType.NOT_FOUND
    );

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {

        ErrorType errorType = EXCEPTION_TO_ERROR_TYPE_MAP.get(ex.getClass());
        if (errorType != null) {
            return buildError(env, errorType, ex.getMessage());
        }

        log.error("Unexpected error occurred", ex);
        return buildError(env, ErrorType.INTERNAL_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    private GraphQLError buildError(DataFetchingEnvironment env, ErrorType errorType, String message) {
        return GraphqlErrorBuilder
                .newError(env)
                .errorType(errorType)
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

}
