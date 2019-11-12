package com.codelovers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codelovers.model.Users;
import com.codelovers.service.UsersService;

/**
 * Created by Admin on 1/3/2017.
 */
@Controller
public class LoginController {
    @Autowired
    UsersService usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = {"/index", "/login"},method = RequestMethod.GET)
    public String index() {
        return "public/login";
    }

    @RequestMapping("/clearStoring")
    public String logout() {
        return "public/logout";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "public/login";
    }

    @RequestMapping("/session-invalid")
    public String sessionInvalid(Model model) {
        model.addAttribute("invalidSession", true);
        return "public/login";
    }

    @RequestMapping("/session-expired")
    public String sessionExpired(Model model) {
        model.addAttribute("expiredSession", true);
        return "public/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerView(Users users) {
        return "public/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(Users users, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        if (usersService.findByUsername(users.getUsername()) != null) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        Users u = new Users();
        u.setUsername(users.getUsername());
        u.setPassword(passwordEncoder.encode(users.getPassword()));
        u.setRole(2);
        u.setEmail(users.getEmail());
        usersService.save(u);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/registerRedirect", method = RequestMethod.GET)
    public String registerRedirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messages", "create account successful.Login Now");
        return "redirect:/login";
    }

}
