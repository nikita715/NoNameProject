package ru.ifmo.shelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.ifmo.shelf.jdbc.TaskDao;
import ru.ifmo.shelf.jdbc.UserDao;
import ru.ifmo.shelf.model.DatedTasksGroup;
import ru.ifmo.shelf.model.Task;
import ru.ifmo.shelf.model.User;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashSet;

/**
 * Created by nikge on 08.11.2016.
 */

@Controller
public class UserController {

    private static UserDao userDao;
    private static TaskDao taskDao;
    private static User currentUser;

    UserController() throws SQLException {
        userDao = new UserDao();
        taskDao = new TaskDao();
    }

    @GetMapping("/")
    public String greetingForm(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @GetMapping("/userpage")
    public String userpage(Model model) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());

        LinkedHashSet<DatedTasksGroup> tasksGroups =  taskDao.getOldTasks(currentUser);
        tasksGroups.addAll(taskDao.getTodayTasks(currentUser));

        model.addAttribute("datedTasksSet", tasksGroups);
        model.addAttribute("tasksSelector", "today");
        return "userpage";
    }

    @GetMapping("/today")
    public String todayTasks(Model model) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDao.getTodayTasks(currentUser));
        model.addAttribute("tasksSelector", "today");
        return "userpage";
    }

    @GetMapping("/tomorrow")
    public String tomorrowTasks(Model model) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDao.getTomorrowTasks(currentUser));
        model.addAttribute("tasksSelector", "tomorrow");
        return "userpage";
    }

    @GetMapping("/week")
    public String weekTasks(Model model) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDao.getWeekTasks(currentUser));
        model.addAttribute("tasksSelector", "week");
        return "userpage";
    }

    @GetMapping("/old")
    public String oldTasks(Model model) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDao.getOldTasks(currentUser));
        model.addAttribute("tasksSelector", "old");
        return "userpage";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        task.setUser(currentUser.getId());
        taskDao.insert(task);
        return "redirect:/userpage";
    }

    @PostMapping("/enter")
    public String enterSite(@ModelAttribute User user) throws SQLException {
        if (userDao.connect(user) == 0) {
            return "redirect:/";
        } else {
            user.setId(userDao.connect(user));
            currentUser = user;
            return "redirect:/userpage";
        }
    }

    @GetMapping("/exit")
    public String exitSite() throws SQLException {
        currentUser = null;
        return "redirect:/";
    }

    @PostMapping("/register")
    public String registerSite(@ModelAttribute User user) throws SQLException {
        if (user.getPasswordСheck().equals(user.getPassword())) {
            userDao.insert(user);
            user.setId(userDao.connect(user));
            currentUser = user;
            return "redirect:/userpage";
        } else {
            return "redirect:/";
        }
    }
}