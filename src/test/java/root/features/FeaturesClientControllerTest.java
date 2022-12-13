package root.features;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import root.BasicIT;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
class FeaturesClientControllerTest extends BasicIT {

    private static final String CLIENT_USERNAME = "batman";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CommonFeaturesService commonFeaturesService;

    @Autowired
    ClientFeaturesService clientFeaturesService;

    @WithMockUser(username = CLIENT_USERNAME, roles = "CLIENT")
    @Test
    void findAllIsEmpty() throws Exception {
        mockMvc.perform(get("/client/features"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json("""
                            {
                              "common": [],
                              "custom": []
                            }
                        """));
    }

    @WithMockUser(username = CLIENT_USERNAME, roles = "CLIENT")
    @Test
    void findAllWithCommonOnly() throws Exception {
        common(Set.of(new Feature("f1", true), new Feature("f2", true)));

        mockMvc.perform(get("/client/features"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json("""
                            {
                              "common": [{"name": "f1", "enabled": true}, {"name": "f2", "enabled": true}],
                              "custom": []
                            }
                        """));
    }

    @WithMockUser(username = CLIENT_USERNAME, roles = "CLIENT")
    @Test
    void findAllWithCommonAndCustom() throws Exception {
        common(Set.of(new Feature("f1", true), new Feature("f2", true)));
        custom(CLIENT_USERNAME, Set.of(new Feature("f1", false)));

        mockMvc.perform(get("/client/features"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json("""
                            {
                              "common": [{"name": "f1", "enabled": true}, {"name": "f2", "enabled": true}],
                              "custom": [{"name": "f1", "enabled": false}]
                            }
                        """));
    }

    private void common(Set<Feature> features) {
        commonFeaturesService.saveAll(features);
    }


    private void custom(String id, Set<Feature> features) {
        clientFeaturesService.saveAll(id, features);
    }
}
