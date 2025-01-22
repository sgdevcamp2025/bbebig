package com.bbebig.serviceserver.server.repository;

import com.bbebig.serviceserver.server.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
}
