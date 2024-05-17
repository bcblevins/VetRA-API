package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
/**
 * Controller class for the person model, only meant to retrieve persons that !isDoctor.
 */

@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {
    private UserDao userDao;
    public UserController(UserDao userDao) {this.userDao = userDao;}
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping(path = "/users")
    public List<User> getAll() {
        return userDao.getUsers();
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping(path = "/users/{username}")
    public User get(@PathVariable String username) {
        return userDao.getUserByUsername(username);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users")
    public User create(@Valid @RequestBody User user) {
        return userDao.createUser(user);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/users/{username}")
    public User update(@Valid @RequestBody User user, @PathVariable String username) {
        user.setUsername(username);
        return userDao.updateUser(user);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "users/{username}/password")
    public User updatePassword(@RequestBody String password, @PathVariable String username) {
        User user = userDao.getUserByUsername(username);
        user.setPassword(password);
        return userDao.updatePassword(user);
    }
    @PutMapping(path = "/password")
    public User updatePassword(@RequestBody String password, Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());
        user.setPassword(password);
        return userDao.updatePassword(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "users/{username}")
    public void delete(@PathVariable String username) {
        userDao.deleteUser(username);
    }

    //------------------------
    // Roles
    //------------------------

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping(path = "users/{username}/roles")
    public List<String> getRoles(@PathVariable String username) {
        return userDao.getRoles(username);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "users/{username}/roles")
    public User addRole(@PathVariable String username, @RequestBody String role) {
        return userDao.addRole(username, role.toUpperCase());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "users/{username}/roles/{role}")
    public void deleteRole(@PathVariable String username, @PathVariable String role) {
        userDao.deleteRole(username, role.toUpperCase());
    }

}
