package com.techbank.account.query.infraestructure;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.infraestructure.QueryDispatcher;
import com.techbank.cqrs.core.queries.BaseQuery;
import com.techbank.cqrs.core.queries.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        var handlers = routes.get(query.getClass());
        return List.of();
    }
}
