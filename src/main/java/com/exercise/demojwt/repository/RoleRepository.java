package com.exercise.demojwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.demojwt.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
    public Role findByName(String name);
}
