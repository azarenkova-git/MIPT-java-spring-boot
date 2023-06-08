package com.example.mipt;

import com.example.mipt.dto.ChatDto;
import com.example.mipt.utils.AdminData;
import com.example.mipt.utils.NotificationsChatData;
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
public class ChatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(AdminData.USERNAME)
    public void testCreateChat() throws Exception {
        ChatDto chatDto = new ChatDto();

        String name = "test chat";
        chatDto.setName(name);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat/create")
                        .contentType("application/json")
                        .param("name", chatDto.getName())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(AdminData.USERNAME)
    public void testUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/chats"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("chats"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("chats"))
                .andExpect(MockMvcResultMatchers.model().attribute("chats", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.model().attribute("chats", Matchers.hasItem(
                        Matchers.allOf(
                                Matchers.hasProperty("name", Matchers.is(NotificationsChatData.NAME))
                        )
                )));
    }
}
