

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
        // Récupère l’utilisateur connecté
        String usename = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(usename)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + usename));

        // Récupère le projet (optionnel)
//        Project project = null;
//        if (dto.getProjectId() != null) {
//            project = projectRepository.findById(dto.getProjectId())
//                    .orElseThrow(() -> new RuntimeException("Projet non trouvé avec id " + dto.getProjectId()));
//        }

        // Récupère les tags (optionnels)
        List<Tag> tags = dto.getTagIds() != null ? tagRepository.findAllById(dto.getTagIds()) : List.of();

        // Construction de la tâche
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setAssignedTo(user);
       // task.setProject(project);
       // task.setTags(tags);

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

    public List<TaskRequestDTO> getTasksForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        List<Task> tasks = taskRepository.findByAssignedTo(user);
        return tasks.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    private TaskRequestDTO mapToDto(Task task) {
        TaskRequestDTO dto = new TaskRequestDTO();
//        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
//        dto.setStatus(task.getStatus());
//        dto.setCompleted(task.isCompleted());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
//        dto.setCreatedAt(task.getCreatedAt());
//        dto.setUpdatedAt(task.getUpdatedAt());

        // Optionnel selon ton besoin
//        if (task.getAssignedTo() != null) {
//            dto.setAssignedToUsername(task.getAssignedTo().getUsername());
//        }

        return dto;
    }




}

