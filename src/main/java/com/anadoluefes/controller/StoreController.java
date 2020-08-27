package com.anadoluefes.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.util.List;
import java.util.Map;


@RestController
@EnableWebMvc
public class StoreController {

    final String SELECT_BY_KEY = "SELECT store_pair -> ? AS store_pair_value FROM test_hstore";
    final String SELECT_ALL_QUERY = "SELECT * FROM test_hstore";
    final String SELECT_BY_NAME = "SELECT * FROM test_hstore WHERE name=?";

    final String INSERT_QUERY = "INSERT INTO test (name,store_pair) VALUES (?,?)";
    final String INSERT_QUERY_JSON = "INSERT INTO test (name,store_pair) VALUES (?,to_json(?::json))";
    final String INSERT_QUERY_HSTORE = "INSERT INTO test_hstore (name,store_pair) VALUES (?,?)";
    final String UPDATE_QUERY_HSTORE = "UPDATE test_hstore SET store_pair = ? WHERE name = ?";
    final String DELETE_QUERY_HSTORE = "DELETE FROM test_hstore WHERE name = ?";


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/store/key/{name}")
    public List<Map<String, Object>> getByKey(@PathVariable("name") String name ) {
        System.out.println(name);
        List<Map<String, Object>> formdata = jdbcTemplate.queryForList(SELECT_BY_KEY,name);
        return formdata;
    }


    @GetMapping("/store")
    List<Map<String, Object>> getAll() {
        List<Map<String, Object>> formdata = jdbcTemplate.queryForList(SELECT_ALL_QUERY);
        System.out.println(formdata.size());
        return formdata;
    }

    @GetMapping("/store/{name}")
    public List<Map<String, Object>> getByName(@PathVariable("name") String name ) {
        System.out.println(name);
        List<Map<String, Object>> formdata = jdbcTemplate.queryForList(SELECT_BY_NAME,name);
        return formdata;
    }


    @RequestMapping(
            value = "/store",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public void create(@RequestBody MultiValueMap<String, String> formData)
            throws Exception {
        formData.forEach((k, v) -> {
            System.out.println("Key: " + k + ", Value: " + v);
            jdbcTemplate.update(INSERT_QUERY_HSTORE,k ,formData);
        });

    }

    @RequestMapping(
            value = "/store/{name}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    String update(@RequestBody MultiValueMap<String, String> formData,
                  @PathVariable("name") String name) {
        formData.forEach((k, v) -> {
            System.out.println("Key: " + k + ", Value: " + v);
            jdbcTemplate.update(UPDATE_QUERY_HSTORE,formData ,name);
        });

        return "put";
    }


    @RequestMapping(
            value = "/store/{name}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    String delete(@PathVariable("name") String name) {
        jdbcTemplate.update(DELETE_QUERY_HSTORE,name);
        return "delete";
    }


}
