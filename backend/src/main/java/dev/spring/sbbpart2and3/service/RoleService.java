package dev.spring.sbbpart2and3.service;

import dev.spring.sbbpart2and3.domain.Role;
import dev.spring.sbbpart2and3.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByRoleName(String roleName) throws RoleNotFoundException {
        return roleRepository.findByRoleName(roleName).orElseThrow(RoleNotFoundException::new);
    }
}
