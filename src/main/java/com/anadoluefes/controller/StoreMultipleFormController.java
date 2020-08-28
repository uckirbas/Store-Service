package com.anadoluefes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;

@RestController
@EnableWebMvc
public class StoreMultipleFormController {

    /* Sample For multipart/form-data */

    final String SELECT_BY_ID = "SELECT store_pair -> ? AS store_pair_value FROM test_hstore";
    final String SELECT_ALL_QUERY = "SELECT * FROM test_store";
    final String SELECT_BY_NAME = "SELECT * FROM test_store WHERE name=?";

    final String INSERT_QUERY = "INSERT INTO test (name,store_pair) VALUES (?,?)";
    final String INSERT_QUERY_HSTORE = "INSERT INTO test_hstore (name,store_pair) VALUES (?,?)";
    final String UPDATE_QUERY_HSTORE = "UPDATE test_hstore SET store_pair = ? WHERE name = ?";
    final String DELETE_QUERY_HSTORE = "DELETE FROM test_hstore WHERE name = ?";

    final String INSERT_QUERY_JSON = "INSERT INTO test_store (name,json) VALUES (?,to_json(?::json))";
    final String UPDATE_QUERY_JSON = "UPDATE test_store SET json = to_json(?::json) WHERE name = ?";
    final String DELETE_QUERY_JSON = "DELETE FROM test_store WHERE name = ?";



    @Autowired
    private JdbcTemplate jdbcTemplate;


    @RequestMapping(value = "/store_multiple", method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})

    String create(@RequestPart(value = "key")  String key ,
                  @RequestPart(value = "json")  String json) {
        System.out.println(key);
        System.out.println(json);
        try {
            jdbcTemplate.update(INSERT_QUERY_JSON,key ,json);
        }
        catch (DuplicateKeyException ex) {
            System.out.println("kullanici var");
            return "kullanici var";
        }

        return "ok";

    }


}
