package dev.spring.sbbpart2and3.service;

import dev.spring.sbbpart2and3.domain.Role;
import dev.spring.sbbpart2and3.domain.SiteUser;
import dev.spring.sbbpart2and3.exception.UserNotFoundException;
import dev.spring.sbbpart2and3.repository.RoleRepository;
import dev.spring.sbbpart2and3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void createUser(String username, String password, String email) {
        SiteUser user = new SiteUser(username, passwordEncoder.encode(password), email);
        Role userRole = roleRepository.findByRoleName("USER").orElseThrow(UserNotFoundException::new);
        user.addRole(userRole);
        userRepository.save(user);
    }

    public SiteUser findUserByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(UserNotFoundException::new);
    }

    public void assignRoleToUser(String username, Role role) {
        SiteUser user = findUserByUsername(username);
        user.addRole(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(String username, Role role) {
        SiteUser user = findUserByUsername(username);
        user.getRoles().remove(role);
        userRepository.save(user);
    }
}
