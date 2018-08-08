package com.example.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "no_task", nullable = false, unique = true)
    private String noTask;

    private String description;

    private String assignee;
}
