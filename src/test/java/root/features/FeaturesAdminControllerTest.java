package root.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import root.BasicIT;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Transactional
class FeaturesAdminControllerTest extends BasicIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jackson;

    @WithMockUser(roles = "CLIENT")
    @Test
    void findAllShouldNotBeAccessedByClient() throws Exception {
        mockMvc.perform(get("/admin/features/common"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = "CLIENT")
    @Test
    void saveAllShouldNotBeAccessedByClient() throws Exception {
        mockMvc.perform(post("/admin/features/common")
                        .contentType(APPLICATION_JSON)
                        .content("[]")
                )
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = "CLIENT")
    @Test
    void findAllClientShouldNotBeAccessedByClient() throws Exception {
        mockMvc.perform(get("/admin/features/client/42"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = "CLIENT")
    @Test
    void saveAllClientShouldNotBeAccessedByClient() throws Exception {
        mockMvc.perform(post("/admin/features/client/42")
                        .contentType(APPLICATION_JSON)
                        .content("[]")
                )
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void findAllIsEmpty() throws Exception {
        mockMvc.perform(get("/admin/features/common"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldSaveAll() throws Exception {
        var features1 = jackson.writeValueAsString(Set.of(new Feature("1", true), new Feature("2", false)));
        mockMvc.perform(post("/admin/features/common")
                        .contentType(APPLICATION_JSON)
                        .content(features1)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(features1));

        var features2 = jackson.writeValueAsString(Set.of(new Feature("2", false), new Feature("3", true), new Feature("5", true)));
        mockMvc.perform(post("/admin/features/common")
                        .contentType(APPLICATION_JSON)
                        .content(features2)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().json(features2));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldNotHaveDuplicates() throws Exception {
        var features = jackson.writeValueAsString(Set.copyOf(List.of(new Feature("1", true), new Feature("1", false))));
        mockMvc.perform(post("/admin/features/common")
                        .contentType(APPLICATION_JSON)
                        .content(features)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("1")));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteAll() throws Exception {
        var features = jackson.writeValueAsString(Set.of(new Feature("1", true), new Feature("2", false)));
        mockMvc.perform(post("/admin/features/common")
                        .contentType(APPLICATION_JSON)
                        .content(features)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(features));

        mockMvc.perform(post("/admin/features/common")
                        .contentType(APPLICATION_JSON)
                        .content("[]")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
