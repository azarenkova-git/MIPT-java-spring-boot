package com.example.mipt;

import com.example.mipt.dto.UserDto;
import com.example.mipt.utils.AdminData;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(AdminData.USERNAME)
    public void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();

        String username = "username";
        String password = "password";

        userDto.setUsername(username);
        userDto.setPassword(password);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create")
                        .contentType("application/json")
                        .param("username", userDto.getUsername())
                        .param("password", userDto.getPassword())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(AdminData.USERNAME)
    public void testUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasItem(
                        Matchers.allOf(
                                Matchers.hasProperty("username", Matchers.is(AdminData.USERNAME))
                        )
                )));
    }
}
