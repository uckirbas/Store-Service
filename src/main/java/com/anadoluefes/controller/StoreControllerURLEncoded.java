package com.anadoluefes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@CrossOrigin
@RestController
@EnableWebMvc
public class StoreControllerURLEncoded {

    /* Sample For multipart/form-data */

    final String SELECT_BY_ID = "SELECT store_pair -> ? AS store_pair_value FROM test_hstore";
    final String SELECT_ALL_QUERY = "SELECT * FROM test_store";
    final String SELECT_BY_NAME = "SELECT * FROM test_store WHERE name=?";

    final String INSERT_QUERY = "INSERT INTO test (name,store_pair) VALUES (?,?)";
    final String INSERT_QUERY_HSTORE = "INSERT INTO test_hstore (name,store_pair) VALUES (?,?)";
    final String UPDATE_QUERY_HSTORE = "UPDATE test_hstore SET store_pair = ? WHERE name = ?";
    final String DELETE_QUERY_HSTORE = "DELETE FROM test_hstore WHERE name = ?";

    final String INSERT_QUERY_JSON = "INSERT INTO test_store (name,json_field) VALUES (?,to_json(?::json))";
    final String UPDATE_QUERY_JSON = "UPDATE test_store SET json_field = to_json(?::json) WHERE name = ?";
    final String DELETE_QUERY_JSON = "DELETE FROM test_store WHERE name = ?";



    @Autowired
    private JdbcTemplate jdbcTemplate;



    /* URLENCODED_VALUE POST */
    @RequestMapping(
            value = "wform/store",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String create(@RequestBody MultiValueMap<String, String> formData)
    {
        try {

            formData.forEach((k, v) -> {
                System.out.println("Key: " + k + ", Value: " + v.toString());
                StringBuilder sb = new StringBuilder();
                for (String s : v)
                {
                    sb.append(s);
                    sb.append("\t");
                }
                jdbcTemplate.update(INSERT_QUERY_JSON,k ,sb);
            });
        } catch (DuplicateKeyException ex) {
            System.out.println("kullanici var");
            return "kullanici var";
        }

        return "ok";
    }


    /* URLENCODED_VALUE PUT */
    @RequestMapping(
            value = "wform/store/{name}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String update(@RequestBody MultiValueMap<String, String> formData,
                         @PathVariable("name") String name) {
        formData.forEach((k, v) -> {
            System.out.println("Key: " + k + ", Value: " + v);
            jdbcTemplate.update(UPDATE_QUERY_JSON,v.toString(),name);
        });
        return "put";
    }





}
