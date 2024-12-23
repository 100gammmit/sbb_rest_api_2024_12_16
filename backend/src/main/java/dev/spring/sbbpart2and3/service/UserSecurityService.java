package dev.spring.sbbpart2and3.service;

import dev.spring.sbbpart2and3.domain.SiteUser;
import dev.spring.sbbpart2and3.exception.UserNotFoundException;
import dev.spring.sbbpart2and3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SiteUser user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return loadUserDetailsBySiteUser(user);
    }

    @Transactional
    public List<UserDetails> loadAllUser() throws UsernameNotFoundException {
        List<SiteUser> siteUsers = userRepository.findAll();
        return siteUsers.stream()
                .map(this::loadUserDetailsBySiteUser)
                .toList();
    }

    private UserDetails loadUserDetailsBySiteUser(SiteUser user) throws UsernameNotFoundException {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
