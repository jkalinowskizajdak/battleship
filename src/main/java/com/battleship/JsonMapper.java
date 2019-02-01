package com.battleship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class JsonMapper extends JacksonJsonProvider {

    public JsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        setMapper(mapper);
    }
}