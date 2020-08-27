package com.anadoluefes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public class StoreMultipleFormController {

    final String INSERT_QUERY_JSON = "INSERT INTO test (name,store_pair) VALUES (?,to_json(?::json))";


    /* Sample For multipart/form-data */

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @RequestMapping(value = "/store_multiple", method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})

    String create(@RequestPart(value = "key")  String key ,
                  @RequestPart(value = "json")  String json) {
        System.out.println(key);
        System.out.println(json);

        jdbcTemplate.update(INSERT_QUERY_JSON,key ,json);
        return "create";
    }

/*
    @RequestMapping(
            value = "/deneme",
            method = RequestMethod.POST
            // consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public void forTest(@RequestBody Map<String,String> body)
            throws Exception {
        System.out.println(body);

    }


    @PostMapping(value = "/deneme2", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = "application/json")
    String createc(Map<String, Object> input) {
        String email=(String) input.get("email");
        System.out.println(email);
        return "create";
    }
*/

}
