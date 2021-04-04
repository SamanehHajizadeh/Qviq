package com.springboot.Qviq;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Message, Long> {
}
