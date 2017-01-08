package ru.ifmo.shelf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.ifmo.shelf.jdbc.impl.TaskDaoImpl;
import ru.ifmo.shelf.jdbc.impl.UserDaoImpl;
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

    private final UserDaoImpl userDaoImpl;
    private final TaskDaoImpl taskDaoImpl;

    private static User currentUser;

    @Autowired
    public UserController(UserDaoImpl userDaoImpl, TaskDaoImpl taskDaoImpl) {
        this.userDaoImpl = userDaoImpl;
        this.taskDaoImpl = taskDaoImpl;
    }

    @GetMapping("/")
    public String greetingForm(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/enter")
    public String enterSite(Model model, @ModelAttribute User user) throws SQLException {

        user.setName(user.getName().toLowerCase());

//        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());
//        try {
//
//            Authentication authentication = authenticationManager
//                    .authenticate(authenticationToken);
//
//
//            SecurityContext securityContext = SecurityContextHolder
//                    .getContext();
//
//            securityContext.setAuthentication(authentication);
//
//            HttpSession session = request.getSession(true);
//            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
//
//            return "sucess";
//        } catch (AuthenticationException ex) {
//            return "fail " + ex.getMessage();
//        }

        if (userDaoImpl.connect(user) == 0) {
            model.addAttribute("error", "wrongEnterData");
            return "redirect:/";
        } else {
            user.setId(userDaoImpl.connect(user));
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
        if (userDaoImpl.nameAlreadyTaken(user)) {
            model.addAttribute("error", "usernameTaken");
            return "redirect:/";
        } else if (!user.getPasswordСheck().equals(user.getPassword())) {
            model.addAttribute("error", "passwordMismatch");
            return "redirect:/";
        } else {
            userDaoImpl.insert(user);
            user.setId(userDaoImpl.connect(user));
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
        model.addAttribute("datedTasksSet", taskDaoImpl.getInitialTasks(currentUser));
        model.addAttribute("tasksSelector", "");
        return "userpage";
    }

    @GetMapping("/{username}/today")
    public String todayTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDaoImpl.getTasksForToday(currentUser));
        model.addAttribute("tasksSelector", "today");
        return "userpage";
    }

    @GetMapping("/{username}/tomorrow")
    public String tomorrowTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDaoImpl.getTasksForTomorrow(currentUser));
        model.addAttribute("tasksSelector", "tomorrow");
        return "userpage";
    }

    @GetMapping("/{username}/week")
    public String weekTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDaoImpl.getTasksForWeek(currentUser));
        model.addAttribute("tasksSelector", "week");
        return "userpage";
    }

    @GetMapping("/{username}/unfinished")
    public String oldTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDaoImpl.getUnfinishedTasks(currentUser));
        model.addAttribute("tasksSelector", "unfinished");
        return "userpage";
    }

    @GetMapping("/{username}/completed")
    public String completedTasks(Model model, @PathVariable String username) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("task", new Task());
        model.addAttribute("datedTasksSet", taskDaoImpl.getCompletedTasks(currentUser));
        model.addAttribute("tasksSelector", "completed");
        return "userpage";
    }

    @PostMapping("/{username}/add")
    public String addTask(@ModelAttribute Task task, @PathVariable String username, HttpServletRequest request) throws SQLException, ParseException {
        if (currentUser == null) {
            return "redirect:/";
        }
        task.setUser(currentUser.getId());
        taskDaoImpl.insert(task);
        return "redirect:/" + request.getHeader("Referer").substring(22);
    }

    @GetMapping("/{username}/delete/{id}")
    public String deleteTask(@PathVariable int id, HttpServletRequest request) throws SQLException {
        if (currentUser == null) {
            return "redirect:/";
        }
        taskDaoImpl.delete(id);
        return "redirect:/" + request.getHeader("Referer").substring(22);
    }

    @GetMapping("/{username}/complete/{id}")
    public String completeTask(@PathVariable int id, HttpServletRequest request) throws SQLException {
        if (currentUser == null) {
            return "redirect:/";
        }
        taskDaoImpl.complete(id);
        return "redirect:/" + request.getHeader("Referer").substring(22);
    }
}