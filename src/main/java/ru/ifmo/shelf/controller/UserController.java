package ru.ifmo.shelf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.shelf.jdbc.impl.TaskDao;
import ru.ifmo.shelf.jdbc.impl.UserDao;
import ru.ifmo.shelf.model.Task;
import ru.ifmo.shelf.model.User;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by nikge on 08.11.2016.
 */

@Controller
public class UserController {

    private final UserDao userDao;
    private final TaskDao taskDao;

    private static User currentUser;

    @Autowired
    public UserController(UserDao userDao, TaskDao taskDao) {
        this.userDao = userDao;
        this.taskDao = taskDao;
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
        model.addAttribute("datedTasksSet", taskDao.getInitialTasks(currentUser));
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
        model.addAttribute("datedTasksSet", taskDao.getTasksForToday(currentUser));
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
        model.addAttribute("datedTasksSet", taskDao.getTasksForTomorrow(currentUser));
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
        model.addAttribute("datedTasksSet", taskDao.getTasksForWeek(currentUser));
        model.addAttribute("tasksSelector", "week");
        return "userpage";
    }

    @GetMapping("/unfinished")
    public String oldTasks(Model model) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDao.getUnfinishedTasks(currentUser));
        model.addAttribute("tasksSelector", "unfinished");
        return "userpage";
    }

    @GetMapping("/completed")
    public String completedTasks(Model model) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDao.getCompletedTasks(currentUser));
        model.addAttribute("tasksSelector", "completed");
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
    public String enterSite(Model model, @ModelAttribute User user) throws SQLException {
        if (userDao.connect(user) == 0) {
            model.addAttribute("error", "wrongEnterData");
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
    public String registerSite(Model model, @ModelAttribute User user) throws SQLException {
        if (userDao.nameAlreadyTaken(user)) {
            model.addAttribute("error", "usernameTaken");
            return "redirect:/";
        } else if (!user.getPasswordСheck().equals(user.getPassword())) {
            model.addAttribute("error", "passwordMismatch");
            return "redirect:/";
        } else {
            userDao.insert(user);
            user.setId(userDao.connect(user));
            currentUser = user;
            return "redirect:/userpage";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id) throws SQLException {
        if (currentUser == null) {
            return "redirect:/";
        }
        taskDao.delete(id);
        return "redirect:/userpage";
    }

    @GetMapping("/complete/{id}")
    public String completeTask(@PathVariable int id) throws SQLException {
        if (currentUser == null) {
            return "redirect:/";
        }
        taskDao.complete(id);
        return "redirect:/userpage";
    }
}