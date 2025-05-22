package com.ideas2it.training.patient.metrics.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.patient.metrics.model.AuditPayload;
import com.ideas2it.training.patient.metrics.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuditLogController.class,
    excludeAutoConfiguration = {OAuth2ResourceServerAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "USER")
@TestPropertySource(properties = "VAULT_URL=http://localhost:8200")
class AuditLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuditLogService auditLogService;

    private AuditPayload auditPayload;

    @BeforeEach
    void setUp() {
        auditPayload = new AuditPayload();
        auditPayload.setUserId(101L);
        auditPayload.setPatientId(201L);
        auditPayload.setEntityType("Patient");
        auditPayload.setEntityId(301L);
        auditPayload.setLogDate(LocalDateTime.now());
    }

    @Test
    void testGetLogsByUserIdAndDateRange() throws Exception {
        Mockito.when(auditLogService.getLogsByUserIdAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(Collections.singletonList(auditPayload));

        mockMvc.perform(get("/api/audit-logs/by-user")
                .param("userId", "101")
                .param("from", "2023-01-01T00:00:00")
                .param("to", "2023-12-31T23:59:59"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(auditPayload))));
    }

    @Test
    void testGetLogsByPatientIdAndDateRange() throws Exception {
        Mockito.when(auditLogService.getLogsByPatientIdAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(Collections.singletonList(auditPayload));

        mockMvc.perform(get("/api/audit-logs/by-patient")
                .param("patientId", "201")
                .param("from", "2023-01-01T00:00:00")
                .param("to", "2023-12-31T23:59:59"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(auditPayload))));
    }

    @Test
    void testGetLogsByEntityTypeAndDateRange() throws Exception {
        Mockito.when(auditLogService.getLogsByEntityTypeAndDateRange(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(Collections.singletonList(auditPayload));

        mockMvc.perform(get("/api/audit-logs/by-entity-type")
                .param("entityType", "Patient")
                .param("from", "2023-01-01T00:00:00")
                .param("to", "2023-12-31T23:59:59"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(auditPayload))));
    }

    @Test
    void testGetLogsByEntityId() throws Exception {
        Mockito.when(auditLogService.getLogsByEntityId(anyLong()))
            .thenReturn(Collections.singletonList(auditPayload));

        mockMvc.perform(get("/api/audit-logs/by-entity-id/{entityId}", 301L))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(auditPayload))));
    }

    @Test
    void testFilterAuditLogs() throws Exception {
        Mockito.when(auditLogService.getFilteredAuditLogs(anyLong(), anyLong(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(Collections.singletonList(auditPayload));

        mockMvc.perform(get("/api/audit-logs/filter")
                .param("userId", "101")
                .param("patientId", "201")
                .param("entityType", "Patient")
                .param("from", "2023-01-01T00:00:00")
                .param("to", "2023-12-31T23:59:59"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(auditPayload))));
    }
}
