package org.example.taskmanager;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("tasks", taskRepository.findAll());
        modelAndView.addObject("task", new Task());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addTask(@Valid Task task, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("tasks", taskRepository.findAll());
            return modelAndView;
        }
        taskRepository.save(task);
        return new ModelAndView("redirect:/");
    }
}
