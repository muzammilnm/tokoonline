package com.tokoonline.demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tokoonline.demo.applicationuser.ApplicationUserRepository;
import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.privilege.Privilege;
import com.tokoonline.demo.privilege.PrivilegeRepository;
import com.tokoonline.demo.role.Role;
import com.tokoonline.demo.role.RoleRepository;

import jakarta.transaction.Transactional;

@Component
public class Setup implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private ApplicationUserRepository userRepository;
 
    @Autowired
    private RoleRepository roleRepository;
 
    @Autowired
    private PrivilegeRepository privilegeRepository;
 
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
 
        if (alreadySetup)
            return;
        Privilege readPrivilege
          = createPrivilegeIfNotFound("READ");
        Privilege writePrivilege
          = createPrivilegeIfNotFound("WRITE");
 
        List<Privilege> adminPrivileges = Arrays.asList(
          readPrivilege, writePrivilege);
        createRoleIfNotFound("ADMIN", adminPrivileges);
        createRoleIfNotFound("USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("USER");
        ApplicationUser user = ApplicationUser.builder()
            .firstName("Test")
            .lastName("Test")
            .password(passwordEncoder.encode("test"))
            .email("test@test.com")
            .username("test test")
            .roles(Arrays.asList(adminRole))
            .build();
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
 
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = Privilege.builder().name(name).build();
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
      String name, Collection<Privilege> privileges) {
 
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = Role.builder().name(name).privileges(privileges).build();
            roleRepository.save(role);
        }
        return role;
    }
}
