package com.example.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entities.Task;
import com.example.entities.TaskDependency;
import com.example.exception.CircularDependencyException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.TaskDependencyRepository;
import com.example.repository.TaskRepository;

@Service
public class TaskDependencyService {

	@Autowired
	private TaskDependencyRepository dependencyRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Transactional
	public TaskDependency addDependency(UUID taskId, UUID dependsOnTaskId) {

		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

		Task dependsOnTask = taskRepository.findById(dependsOnTaskId).orElseThrow(
				() -> new ResourceNotFoundException("Dependency task not found with id: " + dependsOnTaskId));

		if (taskId.equals(dependsOnTaskId)) {
			throw new CircularDependencyException("Task cannot depend on itself");
		}

		if (task.getDueDate() != null && dependsOnTask.getDueDate() != null) {
			if (task.getDueDate().isBefore(dependsOnTask.getDueDate())) {
				throw new IllegalArgumentException("A task cannot depend on another task that has a later due date");
			}
		}

		if (dependencyRepository.existsByTaskIdAndDependsOnTaskId(taskId, dependsOnTaskId)) {
			throw new IllegalArgumentException("Dependency already exists");
		}

		if (willCreateCircularDependency(taskId, dependsOnTaskId, new HashSet<>())) {
			throw new CircularDependencyException("Adding this dependency would create a circular reference");
		}

		TaskDependency dependency = new TaskDependency();
		dependency.setTaskId(taskId);
		dependency.setDependsOnTaskId(dependsOnTaskId);
		return dependencyRepository.save(dependency);
	}

	private boolean willCreateCircularDependency(UUID originalTaskId, UUID currentTaskId, Set<UUID> visited) {
		// If we've revisited a task, we have a cycle
		if (!visited.add(currentTaskId)) {
			return false;
		}

		// Get all tasks that depend on the current task
		List<TaskDependency> dependencies = dependencyRepository.findByDependsOnTaskId(currentTaskId);

		for (TaskDependency dependency : dependencies) {
			UUID nextTaskId = dependency.getTaskId();

			// If this is our original task, we found a cycle
			if (nextTaskId.equals(originalTaskId)) {
				return true;
			}

			// Recursively check for cycles
			if (willCreateCircularDependency(originalTaskId, nextTaskId, new HashSet<>(visited))) {
				return true;
			}
		}

		return false;
	}

	@Transactional
	@CacheEvict(value = { "dependenciesCache", "allDependenciesCache" }, allEntries = true)
	public void removeDependency(UUID taskId, UUID dependsOnTaskId) {
		dependencyRepository.deleteByTaskIdAndDependsOnTaskId(taskId, dependsOnTaskId);
	}

	@Cacheable(value = "dependenciesCache",  key = "T(java.util.Objects).toString(#taskId, '')", unless = "#taskId == null")
	public List<Task> getDirectDependencies(UUID taskId) {
		List<TaskDependency> dependencies = dependencyRepository.findByTaskId(taskId);
		List<Task> dependencyTasks = new ArrayList<>();

		for (TaskDependency dependency : dependencies) {
			taskRepository.findById(dependency.getDependsOnTaskId()).ifPresent(dependencyTasks::add);
		}

		return dependencyTasks;
	}

	@Cacheable(value = "allDependenciesCache", key = "T(java.util.Objects).toString(#taskId, '')", unless = "#taskId == null")
	public List<Task> getAllDependencies(UUID taskId) {
	    if (taskId == null) {
	        throw new IllegalArgumentException("Task ID cannot be null");
	    }

	    Set<UUID> visited = new HashSet<>();
	    List<Task> allDependencies = new ArrayList<>();
	    collectAllDependencies(taskId, visited, allDependencies);
	    return allDependencies;
	}
	
	public List<TaskDependency> getAllTaskDependencies() {
	    return dependencyRepository.findAll();
	}



	private void collectAllDependencies(UUID taskId, Set<UUID> visited, List<Task> result) {
		if (!visited.add(taskId)) {
			return;
		}

		List<TaskDependency> dependencies = dependencyRepository.findByTaskId(taskId);

		for (TaskDependency dependency : dependencies) {
			UUID dependsOnTaskId = dependency.getDependsOnTaskId();

			taskRepository.findById(dependsOnTaskId).ifPresent(task -> {
				result.add(task);
				collectAllDependencies(dependsOnTaskId, visited, result);
			});
		}
	}
}
