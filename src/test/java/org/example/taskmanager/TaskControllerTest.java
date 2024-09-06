package org.example.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.List;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp(){
        task = new Task();
        task.setId(1L);
        task.setName("111AutoTest task name");
        task.setDescription("111AutoTest task description");
    }

    @Test
    void testIndexPage() throws Exception {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("tasks",hasSize(1)))
                .andExpect(model().attribute("task",instanceOf(Task.class)));
    }

    @Test
    void testAddTask() throws Exception {
        when(taskRepository.save(task)).thenReturn(task);

        mockMvc.perform(post("/add")
                .param("title","New task autotest"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(taskRepository).save(task);
    }

    @Test
    void addTaskWithErrors() throws Exception {
        mockMvc.perform(post("/add")
                .param("title",""))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tasks"));
    }

}
