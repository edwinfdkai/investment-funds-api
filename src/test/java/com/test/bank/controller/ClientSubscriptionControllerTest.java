package com.test.bank.controller;

import com.test.bank.config.RestExceptionHandlerTestConfig;
import com.test.bank.service.FundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientSubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandlerTestConfig.class)
class ClientSubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FundService fundService;

    @Test
    void postSubscriptions_returnsCreatedWhenBodyValid() throws Exception {
        doNothing().when(fundService).subscribeToFund(argThat(r -> true));

        mockMvc.perform(post("/api/v1/clients/1L/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fundId\":\"100L\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Subscription created successfully"));

        verify(fundService).subscribeToFund(argThat(r ->
                "1L".equals(r.getClientId()) && "100L".equals(r.getFundId())));
    }

    @Test
    void postSubscriptions_returnsBadRequestWhenFundIdMissing() throws Exception {
        mockMvc.perform(post("/api/v1/clients/1L/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void deleteSubscription_returnsOkAndBuildsCancelRequestFromPath() throws Exception {
        doNothing().when(fundService).cancelFund(argThat(r -> true));

        mockMvc.perform(delete("/api/v1/clients/1L/subscriptions/100L"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Subscription canceled successfully"));

        verify(fundService).cancelFund(argThat(r ->
                "1L".equals(r.getClientId()) && "100L".equals(r.getFundId())));
    }
}
