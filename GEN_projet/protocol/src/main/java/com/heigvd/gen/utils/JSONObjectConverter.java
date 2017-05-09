/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author mathieu
 */
public class JSONObjectConverter {
   private static ObjectMapper mapper = new ObjectMapper();
   
   public static String toJSON(Object o) throws JsonProcessingException {
      return mapper.writeValueAsString(o);
   }
}
