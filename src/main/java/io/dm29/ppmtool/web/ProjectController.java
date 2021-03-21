package io.dm29.ppmtool.web;

import io.dm29.ppmtool.domain.Project;
import io.dm29.ppmtool.services.MapValidationErrorService;
import io.dm29.ppmtool.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Project savedProject = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {
        Project project = projectService.findProjectByIdentifier(projectId.toUpperCase(), principal.getName());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProject(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
    }
}
