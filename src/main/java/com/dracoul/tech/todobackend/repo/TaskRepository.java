package com.dracoul.tech.todobackend.repo;

import com.dracoul.tech.todobackend.entity.Task;
import com.dracoul.tech.todobackend.entity.User;
import com.dracoul.tech.todobackend.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);

    List<Task> findByAssignedTo(User user);

}

