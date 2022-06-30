package com.github.senocak.clinked.repository;

import com.github.senocak.clinked.entity.Role;
import com.github.senocak.clinked.util.AppConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(AppConstants.RoleName roleName);
}
