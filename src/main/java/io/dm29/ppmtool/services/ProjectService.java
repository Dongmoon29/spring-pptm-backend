package io.dm29.ppmtool.services;

import io.dm29.ppmtool.domain.Backlog;
import io.dm29.ppmtool.domain.Project;
import io.dm29.ppmtool.domain.User;
import io.dm29.ppmtool.exceptions.ProjectIdException;
import io.dm29.ppmtool.exceptions.ProjectNotFoundException;
import io.dm29.ppmtool.repositories.BacklogRepository;
import io.dm29.ppmtool.repositories.ProjectRepository;
import io.dm29.ppmtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final BacklogRepository backlogRepository;
    private final UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {

        // project.getId != null
        // find by db id -> null
        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if (existingProject != null && (existingProject.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if (existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier()
                        + "' cannot be updated because it doesn't exist");
            }
        }

        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if (project.getId() == null) {
                // 프로젝트 "생성시" (id == null)
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            } else {
                Backlog existBacklog = backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase());
                project.setBacklog(existBacklog);
            }
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId);
        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId + "' does not exist");
        }
        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }
        return project;
    }

    public Iterable<Project> findAllProject(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }

}
