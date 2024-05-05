package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PersonDao;
import com.bcb.vetra.models.Person;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/owners")
public class OwnerController {
    private PersonDao personDao;
    public OwnerController(PersonDao personDao) {this.personDao = personDao;}
    @RequestMapping(method = RequestMethod.GET)
    public List<Person> getAll() {
        return personDao.getPersons();
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Person get(@PathVariable int id) {
        return personDao.getPersonById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Person create(@Valid @RequestBody Person person) {
        return personDao.createPerson(person);
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Person update(@Valid @RequestBody Person person, @PathVariable int id) {
        return personDao.updatePerson(person);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        personDao.deletePerson(id);
    }
}
