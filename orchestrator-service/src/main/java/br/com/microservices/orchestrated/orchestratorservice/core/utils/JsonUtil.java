package br.com.microservices.orchestrated.orchestratorservice.core.utils;

import br.com.microservices.orchestrated.orchestratorservice.core.dto.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class JsonUtil {

    private  final ObjectMapper objectMapper;

    public String toJson(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        }catch (Exception ex){
            log.error(ex.getMessage());
            return ex.getMessage();
        }
    }

    public Event toEvent(String json){
        try {
            return objectMapper.readValue( json, Event.class);
        }catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }
    }
}
