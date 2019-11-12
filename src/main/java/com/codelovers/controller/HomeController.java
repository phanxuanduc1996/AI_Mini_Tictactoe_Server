package com.codelovers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codelovers.service.MatchService;
import com.codelovers.service.ResultMatchService;
import com.codelovers.service.UsersService;

/**
 * Created by Admin on 1/4/2017.
 */
@Controller
public class HomeController {

	@Autowired
	MatchService matchService;
	@Autowired
	UsersService usersService;
	@Autowired
	ResultMatchService resultMatchService;

	@RequestMapping("/")
	public String home(Model model) {

		return "public/home";
	}

	@RequestMapping("/document")
	public String document(Model model) {

		return "public/document";
	}
}
