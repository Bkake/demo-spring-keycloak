DROP TABLE IF EXISTS task;
CREATE TABLE task (
    task_id BIGINT PRIMARY KEY auto_increment,
    no_task VARCHAR(10),
    description VARCHAR(128),
    assignee VARCHAR(256)
);

