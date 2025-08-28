

package com.dracoul.tech.todobackend.service;
import com.dracoul.tech.todobackend.dto.TaskRequestDTO;
import com.dracoul.tech.todobackend.entity.Project;
import com.dracoul.tech.todobackend.entity.Tag;
import com.dracoul.tech.todobackend.entity.Task;
import com.dracoul.tech.todobackend.entity.User;
import com.dracoul.tech.todobackend.repo.ProjectRepository;
import com.dracoul.tech.todobackend.repo.TagRepository;
import com.dracoul.tech.todobackend.repo.TaskRepository;
import com.dracoul.tech.todobackend.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Task createTask(TaskRequestDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        Project project = null;
        if (dto.getProjectId() != null) {
            project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé avec id " + dto.getProjectId()));
        }

        List<Tag> tags = (dto.getTagIds() != null) ? tagRepository.findAllById(dto.getTagIds()) : List.of();

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        task.setAssignedTo(user);
        task.setProject(project);
        task.setTags(tags);

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task updateTask(Long id, TaskRequestDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec id " + id));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
            task.setProject(project);
        }

        if (dto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            task.setTags(tags);
        }

        return taskRepository.save(task);
    }

    public List<TaskRequestDTO> getTasksForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return taskRepository.findByAssignedTo(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TaskRequestDTO mapToDto(Task task) {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        return dto;
    }
}
