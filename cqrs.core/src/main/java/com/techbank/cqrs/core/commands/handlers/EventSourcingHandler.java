package com.techbank.cqrs.core.commands.handlers;

import com.techbank.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandler<T> {
    void save(AggregateRoot aggregate);
    T getById(String id);
}
