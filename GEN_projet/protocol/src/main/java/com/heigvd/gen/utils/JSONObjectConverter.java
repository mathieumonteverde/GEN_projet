package com.heigvd.gen.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * This class is a tool to convert Java object from JSON to java and from java
 * to JSON.
 * 
 * In order to be correctly converted, a message object class must have 
 * an empty constructor, and methods to set/get each attribute following
 * the syntax (set/get)NameAttribute() when the name of the attribute 
 * is nameAttribute.
 */
public class JSONObjectConverter {

   private static ObjectMapper mapper = new ObjectMapper();

   public static String toJSON(Object o) throws JsonProcessingException {
      return mapper.writeValueAsString(o);
   }

   public static <T> T fromJSON(String json, Class<T> type) throws IOException {
      return mapper.readValue(json, type);
   }
}
