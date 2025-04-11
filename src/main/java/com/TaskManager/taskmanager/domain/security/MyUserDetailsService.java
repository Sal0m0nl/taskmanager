package com.TaskManager.taskmanager.domain.security;

import com.TaskManager.taskmanager.web.services.DataBase.DBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private DBUserService dbUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return dbUserService.findByUsername(username)
                .map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

}
