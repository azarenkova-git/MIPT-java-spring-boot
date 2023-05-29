package com.example.mipt;

import com.example.mipt.dto.UserDto;
import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.UserRepository;
import com.example.mipt.utils.JsonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setup() {
        UserModel user = new UserModel();
        user.setUsername("testUsername");
        userRepository.save(user);
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("anotherUsername");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create")
                .contentType("application/json")
                .content(JsonUtils.toJson(userDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("anotherUsername"));
    }

    @Test
    public void testFindUser() throws Exception {
        UserModel user = userRepository.findByUsername("testUsername");

        System.out.println(user.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/find/" + user.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void testFindAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/find"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
