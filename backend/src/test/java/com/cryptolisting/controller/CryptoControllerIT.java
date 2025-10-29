package com.cryptolisting.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CryptoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnSeededCrypto() throws Exception {
        mockMvc.perform(get("/api/cryptos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("BTC"));
    }

    @Test
    void shouldCreateAndDeleteCrypto() throws Exception {
        String token = registerAndGetToken("trader", "password123");

        JsonNode payload = objectMapper.createObjectNode()
                .put("symbol", "SOL")
                .put("name", "Solana")
                .put("description", "High throughput chain")
                .put("category", "Layer1")
                .put("marketCap", BigDecimal.valueOf(90_000_000_000L));

        MvcResult result = mockMvc.perform(post("/api/cryptos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(body.get("symbol").asText()).isEqualTo("SOL");
        String id = body.get("id").asText();

        mockMvc.perform(delete("/api/cryptos/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    private String registerAndGetToken(String username, String password) throws Exception {
        JsonNode payload = objectMapper.createObjectNode()
                .put("username", username)
                .put("password", password);

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("token").asText();
    }
}
