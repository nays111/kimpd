package com.clnine.kimpd.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JsonUtil {

    public static Map<String, Object> jsonStringToMap(String result) throws IOException, JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(result, Map.class);
        return map;
    }
}