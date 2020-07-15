package ru.spbstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
