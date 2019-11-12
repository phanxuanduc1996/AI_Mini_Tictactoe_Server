package com.codelovers.controller;

import com.codelovers.model.Users;
import com.codelovers.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import javax.transaction.Transactional;

/**
 * Created by Admin on 1/4/2017.
 */

@RequestMapping("/admin/users")
@Controller
public class UsersController {
    @Autowired
    UsersService usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("users", usersService.findAll());
        int countRound = 1;
		int countUser = 0;
		List<Users> lUsers = usersService.findAll();
		for(Users u : lUsers){
			if(u.getRole()!=1){
				countUser++;
			}
		}
		while(countUser >= 2){
			countUser = countUser/2 + countUser%2;
			countRound++;
		}
		
		 model.addAttribute("lRound", countRound);
        return "admin/users/index";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/create")
    public String create() {
        return "admin/users/create";
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseEntity<String> insert(Users users,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        if (usersService.findByUsername(users.getUsername()) != null) {
            return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
        }
        Users u = new Users();
        u = users;
        u.setPassword(passwordEncoder.encode(users.getPassword()));
        usersService.save(u);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("users", usersService.findById(id));
        model.addAttribute("id", id);
        return "admin/users/edit";
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> update(@PathVariable Long id, Users users) {
        Users u = usersService.findById(id);
        if (!u.equals(null)) {
            u.setEmail(users.getEmail());
            u.setRole(users.getRole());
            usersService.save(u);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    @Secured("ROLE_ADMIN")
    @Transactional
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> delete(@PathVariable Long id, Users users) {
        Users u = usersService.findById(id);
        if (!u.equals(null)) {
            usersService.delete(u);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    @Secured("ROLE_ADMIN")
    @Transactional
    @RequestMapping(value = "/resetPassword/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> resetPassword(@PathVariable Long id, Users users) {
        Users u = usersService.findById(id);
        if (!u.equals(null)) {
            u.setPassword(passwordEncoder.encode("123"));
            usersService.save(u);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/updateByUsers/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> updateByUsers(@PathVariable Long id, Users users) {
        Users u = usersService.findById(id);
        if (!u.equals(null)) {
            u.setEmail(users.getEmail());
            usersService.save(u);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
