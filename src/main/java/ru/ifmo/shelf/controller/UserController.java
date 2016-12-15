package ru.ifmo.shelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.ifmo.shelf.jdbc.UserDao;
import ru.ifmo.shelf.model.User;

import java.sql.*;

/**
 * Created by nikge on 08.11.2016.
 */

@Controller
public class UserController {

    private static UserDao userDao;

    UserController() throws SQLException {
        userDao = new UserDao();
    }

    @GetMapping("/")
    public String greetingForm(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/enter")
    public String enterSite(Model model, @ModelAttribute User user) throws SQLException {
        userDao.insert(user);
        model.addAttribute("num", new Integer(userDao.getUsersQuantity()));
        return "userpage";
    }

    @PostMapping("/register")
    public String registerSite(@ModelAttribute User user) {
        return "userpage";
    }
}