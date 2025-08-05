
package com.dracoul.tech.todobackend.repo;

import com.dracoul.tech.todobackend.entity.Project;
import com.dracoul.tech.todobackend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository  extends JpaRepository<Project, Long> {
}

