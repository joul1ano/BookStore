package com.bookstore.controllers;

import com.bookstore.DTOs.PublisherDTO;
import com.bookstore.config.JwtAuthenticationFilter;
import com.bookstore.config.SecurityConfig;
import com.bookstore.mappers.PublisherMapper;
import com.bookstore.service.PublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = PublisherController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
public class PublisherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @MockBean
    private PublisherMapper publisherMapper; // if needed by Spring context

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllPublishers_ReturnsList() throws Exception {
        List<PublisherDTO> publishers = List.of(
                new PublisherDTO(1L, "Publisher A"),
                new PublisherDTO(2L, "Publisher B")
        );

        when(publisherService.getAllPublishers()).thenReturn(publishers);

        mockMvc.perform(get("/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Publisher A"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPublisherById_ReturnsPublisher() throws Exception {
        PublisherDTO dto = new PublisherDTO(1L, "Test Publisher");

        when(publisherService.getPublisherById(1L)).thenReturn(dto);

        mockMvc.perform(get("/publishers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Publisher"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPublisher_ReturnsCreatedPublisher() throws Exception {
        PublisherDTO input = new PublisherDTO(null, "New Publisher");
        PublisherDTO returned = new PublisherDTO(10L, "New Publisher");

        when(publisherService.createPublisher(any())).thenReturn(returned);

        mockMvc.perform(post("/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                         { "name": "New Publisher" }
                         """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("New Publisher"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPublisher_InvalidRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                         { "name": "" }
                         """))
                .andExpect(status().isBadRequest());
    }


}
