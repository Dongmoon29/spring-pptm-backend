package io.dm29.ppmtool.services;

import io.dm29.ppmtool.domain.Backlog;
import io.dm29.ppmtool.domain.ProjectTask;
import io.dm29.ppmtool.exceptions.ProjectNotFoundException;
import io.dm29.ppmtool.repositories.BacklogRepository;
import io.dm29.ppmtool.repositories.ProjectRepository;
import io.dm29.ppmtool.repositories.ProjectTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectTaskService {

    private final BacklogRepository backlogRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectService projectService;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        // PTs to be added to a specific project BL exists
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

        // set bl to pt
        backlog.getProjectTasks().add(projectTask);
        projectTask.setBacklog(backlog);

        // we want our project sequence to be like IDPRO-1,IDPRO-2 ...100
        Integer backlogSequence = backlog.getPTSequence();

        // Update the BL sequence
        backlogSequence++;

        backlog.setPTSequence(backlogSequence);

        // add sequence to Project task
        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        // Initial priority when priority null
        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }
        // Initial status when status is null
        if (projectTask.getStatus() == null) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

        projectService.findProjectByIdentifier(id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
        // make sure we are searching on the right backlog
        projectService.findProjectByIdentifier(backlog_id, username);

        // make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Not found Task ID'" + pt_id + "'");
        }

        //make sure that the backlog/project_id in the path corresponds to the right project
        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Project Task '" +
                    pt_id + "' does not exist in project'" + backlog_id + "'");
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id,
                                               String pt_id, String username) {

        projectService.findProjectByIdentifier(backlog_id, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if(projectTask == null) {
            throw new ProjectNotFoundException("Project Task '" + pt_id + "' does not exist in project: "+backlog_id);
        }
        projectTask = updatedTask;
        return projectTask;
    }

    public void deletePTByProjectSequence(String backlog_id,
                                          String pt_id, String username) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);

    }
}
