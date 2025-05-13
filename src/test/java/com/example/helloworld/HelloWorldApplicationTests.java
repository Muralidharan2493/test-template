package com.example.helloworld;

import com.example.helloworld.controller.HelloWorldController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloWorldApplicationTests {

    @Autowired
    private HelloWorldController helloWorldController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Ensure that the controller is created inside the application context
        assertNotNull(helloWorldController);
    }

    @Test
    void testHelloWorldEndpoint() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World! - Release1"));
    }

    @Test
    void testMainMethod() {
        HelloWorldApplication.main(new String[]{});
    }
}