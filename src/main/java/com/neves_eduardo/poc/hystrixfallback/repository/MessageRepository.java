package com.neves_eduardo.poc.hystrixfallback.repository;

import com.neves_eduardo.poc.hystrixfallback.dto.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message,Integer> {
}
