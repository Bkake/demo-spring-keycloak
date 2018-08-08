package com.example.controller;

import com.example.config.DemoSecurityTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = DemoController.class)
@Import(DemoSecurityTestConfiguration.class)
public class DemoControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void unsecuredPathIsAllowForAllTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/unsecured"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("This is an unsecured endpoint payload"));
    }

    @Test
    @WithMockUser(roles = "toto")
    public void userPathIsNotAllowedForAllTest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void adminPathIsNotAllowedForAllTest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void adminPathIsOnlyAllowedForAdminProfilTest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("This is an ADMIN endpoint payload"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void adminPathIsNotAllowedForUserProfilTest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "USER")
    public void userPathIsOnlyAllowedForUserProfilTest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("This is an USER endpoint payload"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void userPathIsNotAllowedForAdminProfilTest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isForbidden());
    }

}
