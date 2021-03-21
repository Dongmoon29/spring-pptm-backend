package io.dm29.ppmtool.repositories;

import io.dm29.ppmtool.domain.Backlog;
import org.springframework.data.repository.CrudRepository;

public interface BacklogRepository extends CrudRepository<Backlog, Long> {
    Backlog findByProjectIdentifier(String identifier);
}
