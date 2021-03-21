package io.dm29.ppmtool.web;

import io.dm29.ppmtool.domain.ProjectTask;
import io.dm29.ppmtool.services.MapValidationErrorService;
import io.dm29.ppmtool.services.ProjectTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
@RequiredArgsConstructor
public class BacklogController {

    private final ProjectTaskService projectTaskService;
    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, Principal principal,
                                            @PathVariable String backlog_id) {

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) {
            return errorMap;
        }

        ProjectTask savedProjectTask = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());
        return new ResponseEntity<>(savedProjectTask, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id, Principal principal) {
        return projectTaskService.findBacklogById(backlog_id, principal.getName());
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id,
                                            @PathVariable String pt_id, Principal principal) {

        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, pt_id, principal.getName());
        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }
    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                               @PathVariable String backlog_id,
                                               @PathVariable String pt_id,
                                               Principal principal,
                                               BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) {
            return errorMap;
        }

        ProjectTask updatedProjectTask =
                projectTaskService.updateByProjectSequence(projectTask, backlog_id, pt_id, principal.getName());

        return new ResponseEntity<>(updatedProjectTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deletePTByProjectSequence(@PathVariable String backlog_id,
                                               @PathVariable String pt_id, Principal principal) {
        projectTaskService.deletePTByProjectSequence(backlog_id, pt_id, principal.getName());
        return new ResponseEntity<>("Project Task " + pt_id + " was deleted successfully", HttpStatus.OK);
    }
}

