package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controller class for the person model, only meant to retrieve persons that !isDoctor.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private UserDao userDao;
    public UserController(UserDao userDao) {this.userDao = userDao;}
    @GetMapping
    public List<User> getAll() {
        return userDao.getUsers();
    }
    @GetMapping(path = "/{username}")
    public User get(@PathVariable String username) {
        return userDao.getUserByUsername(username);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userDao.createUser(user);
    }
    @PutMapping(path = "/{username}")
    public User update(@Valid @RequestBody User user, @PathVariable String username) {
        return userDao.updateUser(user);
    }
    @PutMapping(path = "/{username}/password")
    public User updatePassword(@Valid @RequestBody User user, @PathVariable String username) {
        return userDao.updatePassword(user);
    }
    @PostMapping(path = "/{username}/roles")
    public User addRole(@PathVariable String username, @Valid @RequestBody String role) {
        return userDao.addRole(username, role.toUpperCase());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{username}/roles/{role}")
    public void deleteRole(@PathVariable String username, @PathVariable String role) {
        userDao.deleteRole(username, role.toUpperCase());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{username}")
    public void delete(@PathVariable String username) {
        userDao.deleteUser(username);
    }
}
