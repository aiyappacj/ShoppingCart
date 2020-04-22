package com.spring.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.spring.service.WatsonAssistantConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import com.spring.util.WatsonAssistantMessage;

@Service
public class WatsonAssistantService {

    private final WatsonAssistantConfig config;
    private SessionResponse session;
    private Assistant service;

    public WatsonAssistantService(WatsonAssistantConfig config) {
        this.config = config;
    }

    private SessionResponse createSession(Assistant service) {

        CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(config.getId()).build();
        return service.createSession(createSessionOptions).execute().getResult();
    }

    private Assistant connect() {

        try {
            Authenticator authenticator = new IamAuthenticator(config.getApikey());
            Assistant service = new Assistant(config.getVersionDate(), authenticator);
            service.setServiceUrl(config.getUrl());

            return service;
        } catch (Error e) {
            throw new Error(e);
        }
    }
    public WatsonAssistantMessage sendMessage(String message) throws IOException {

        try {
            if (this.service == null) {
                this.service = connect();
                this.session = createSession(service);
            }
            ObjectMapper mapper = new ObjectMapper();
            //System.out.println(session.getSessionId());
            MessageInput input = new MessageInput.Builder()
                    .text(message)
                    .build();
            MessageOptions messageOptions = new MessageOptions.Builder()
                    .assistantId(config.getId())
                    .sessionId(session.getSessionId())
                    .input(input)
                    .build();
            MessageResponse messageResponse = service.message(messageOptions).execute().getResult();
            return mapper.readValue(messageResponse.toString(), WatsonAssistantMessage.class);
        } catch (JsonMappingException e) {
            throw new Error(e);
        }

    }
}