package com.springboot.qviq.repository;

import com.springboot.qviq.model.MessageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageConfigRepository extends JpaRepository<MessageConfig, Long> {
}
