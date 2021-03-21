package io.dm29.ppmtool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Backlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer PTSequence = 0;

    @Column(updatable = false)
    private String projectIdentifier;

    // OneToOne with project
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;


    // OneToMany with projectTask
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "backlog", cascade = CascadeType.REFRESH, orphanRemoval = true)
    private List<ProjectTask> projectTasks = new ArrayList<>();


}
