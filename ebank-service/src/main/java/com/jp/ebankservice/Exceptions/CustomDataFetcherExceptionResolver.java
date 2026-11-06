package com.jp.ebankservice.Exceptions;

import graphql.*;
import graphql.schema.*;
import org.springframework.graphql.execution.*;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.*;
import reactor.core.publisher.*;

import java.util.*;

@Component
public class CustomDataFetcherExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message(ex.getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }

}
