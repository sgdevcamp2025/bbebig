package com.bbebig.serviceserver.category.repository;

import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.server.entity.Server;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT MAX(c.position) FROM Category c WHERE c.server.id = :serverId")
    Optional<Integer> findMaxPositionByServerId(@Param("serverId") Long serverId);

    void deleteAllByServerId(Long serverId);

    List<Category> findAllByServerId(Long serverId);
}
