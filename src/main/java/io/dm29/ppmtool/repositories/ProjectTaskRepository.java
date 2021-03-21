package io.dm29.ppmtool.repositories;

import io.dm29.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

    Iterable<ProjectTask> findByProjectIdentifierOrderByPriority(String backlog_id);
    ProjectTask findByProjectSequence(String sequence);
}
