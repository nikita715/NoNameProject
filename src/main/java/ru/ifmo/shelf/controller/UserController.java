package ru.ifmo.shelf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.ifmo.shelf.jdbc.CategoryDao;
import ru.ifmo.shelf.jdbc.TaskDao;
import ru.ifmo.shelf.jdbc.UserDao;
import ru.ifmo.shelf.model.Category;
import ru.ifmo.shelf.model.Task;
import ru.ifmo.shelf.model.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by nikge on 08.11.2016.
 */

@Controller
public class UserController {

    private final UserDao userDao;
    private final TaskDao taskDao;
    private final CategoryDao categoryDao;

    private static User currentUser;

    @Autowired
    public UserController(UserDao userDao, TaskDao taskDao, CategoryDao categoryDao) {
        this.userDao = userDao;
        this.taskDao = taskDao;
        this.categoryDao = categoryDao;
    }

    @GetMapping("/")
    public String greetingForm(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/enter")
    public String enterSite(Model model, @ModelAttribute User user) throws SQLException {

        user.setName(user.getName().toLowerCase());

        if (userDao.connect(user) == 0) {
            model.addAttribute("error", "wrongEnterData");
            return "redirect:/";
        } else {
            user.setId(userDao.connect(user));
            currentUser = user;
            return "redirect:/" + user.getName();
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

    @GetMapping("/{username}")
    public String userpage(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("category", new Category());
        model.addAttribute("datedTasksSet", taskDao.getInitialTasks(currentUser));
        model.addAttribute("tasksSelector", "initial");
        model.addAttribute("categoriesSet", categoryDao.getCategories(currentUser));
//        WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
//        context.setVariable("user", currentUser);
//        context.setVariable("task", new Task());
        return "userpage";
    }

    @GetMapping("/{username}/today")
    public String todayTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("category", new Category());
        model.addAttribute("datedTasksSet", taskDao.getTasksForToday(currentUser));
        model.addAttribute("tasksSelector", "today");
        model.addAttribute("categoriesSet", categoryDao.getCategories(currentUser));
        return "userpage";
    }

    @GetMapping("/{username}/tomorrow")
    public String tomorrowTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("category", new Category());
        model.addAttribute("datedTasksSet", taskDao.getTasksForTomorrow(currentUser));
        model.addAttribute("tasksSelector", "tomorrow");
        model.addAttribute("categoriesSet", categoryDao.getCategories(currentUser));
        return "userpage";
    }

    @GetMapping("/{username}/week")
    public String weekTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("category", new Category());
        model.addAttribute("datedTasksSet", taskDao.getTasksForWeek(currentUser));
        model.addAttribute("tasksSelector", "week");
        model.addAttribute("categoriesSet", categoryDao.getCategories(currentUser));
        return "userpage";
    }

    @GetMapping("/{username}/unfinished")
    public String oldTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("category", new Category());
        model.addAttribute("datedTasksSet", taskDao.getUnfinishedTasks(currentUser));
        model.addAttribute("tasksSelector", "unfinished");
        model.addAttribute("categoriesSet", categoryDao.getCategories(currentUser));
        return "userpage";
    }

    @GetMapping("/{username}/completed")
    public String completedTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("category", new Category());
        model.addAttribute("datedTasksSet", taskDao.getCompletedTasks(currentUser));
        model.addAttribute("tasksSelector", "completed");
        model.addAttribute("categoriesSet", categoryDao.getCategories(currentUser));
        return "userpage";
    }

    @GetMapping("/{username}/category/{category}")
    public String tasksByCategory(Model model, @PathVariable String username, @PathVariable String category) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("category", new Category());
        model.addAttribute("datedTasksSet", taskDao.getTasksByCategory(currentUser, category));
        model.addAttribute("tasksSelector", category);
        model.addAttribute("categoriesSet", categoryDao.getCategories(currentUser));
        return "userpage";
    }

    @PostMapping("/{username}/add")
    public String addTask(@ModelAttribute Task task, @PathVariable String username, HttpServletRequest request) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        task.setUser(currentUser.getId());
        taskDao.insert(task);
        return "redirect:/" + request.getHeader("Referer").substring(22);
    }

    @PostMapping("/{username}/addcategory")
    public String addCategory(@ModelAttribute Category category, @PathVariable String username, HttpServletRequest request) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        category.setUserId(currentUser.getId());
        categoryDao.insert(category);
        return "redirect:/" + request.getHeader("Referer").substring(22);
    }

    @GetMapping("/{username}/delete/{id}")
    public String deleteTask(@PathVariable int id, HttpServletRequest request) throws SQLException {
        if (currentUser == null) {
            return "redirect:/";
        }
        taskDao.delete(id);
        return "redirect:/" + request.getHeader("Referer").substring(22);
    }

    @GetMapping("/{username}/deletecategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpServletRequest request) throws SQLException {
        if (currentUser == null) {
            return "redirect:/";
        }
        categoryDao.delete(id);
        return "redirect:/" + request.getHeader("Referer").substring(22);
    }

    @GetMapping("/{username}/complete/{id}")
    public String completeTask(@PathVariable int id, HttpServletRequest request) throws SQLException {
        if (currentUser == null) {
            return "redirect:/";
        }
        taskDao.complete(id);
        return "redirect:/" + request.getHeader("Referer").substring(22);
    }
}