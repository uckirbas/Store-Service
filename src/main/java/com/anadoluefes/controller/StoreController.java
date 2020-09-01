package com.anadoluefes.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.*;

@CrossOrigin
@RestController
@EnableWebMvc
public class StoreController {

    final String SELECT_BY_ID = "SELECT store_pair -> ? AS store_pair_value FROM test_hstore";
    final String SELECT_ALL_QUERY = "SELECT id,name,json_field::json FROM test_store";
    final String SELECT_BY_NAME = "SELECT * FROM test_store WHERE name=?";

    final String INSERT_QUERY = "INSERT INTO test (name,store_pair) VALUES (?,?)";
    final String INSERT_QUERY_HSTORE = "INSERT INTO test_hstore (name,store_pair) VALUES (?,?)";
    final String UPDATE_QUERY_HSTORE = "UPDATE test_hstore SET store_pair = ? WHERE name = ?";
    final String DELETE_QUERY_HSTORE = "DELETE FROM test_hstore WHERE name = ?";

    final String INSERT_QUERY_JSON = "INSERT INTO test_store (name,json_field) VALUES (?,to_json(?::json))";
    final String UPDATE_QUERY_JSON = "UPDATE test_store SET json_field = to_json(?::json) WHERE name = ?";
    final String DELETE_QUERY_JSON = "DELETE FROM test_store WHERE name = ?";

    final String JSON_COL ="json_field";
    final String NAME_COL ="name";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }


    /* MULTIPART_FORM POST */
    @RequestMapping(value = "/store", method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})

    public ResponseEntity create(@RequestPart(value = "key")  String key ,
                  @RequestPart(value = "json")  String json) {
        System.out.println(key);
        System.out.println(json);
        try {
            jdbcTemplate.update(INSERT_QUERY_JSON,key ,json);
        }
        catch (DuplicateKeyException ex) {
            System.out.println("kullanici var");
            return ResponseEntity.status(HttpStatus.OK).body("Kayit Mevcut");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /* GET ALL */
    @GetMapping(value ="/store",
               produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object>  getAll()  {
        List<Map<String, Object>> mapDB = jdbcTemplate.queryForList(SELECT_ALL_QUERY);
        Map<String, Object> jsonForm =new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        mapDB.forEach((temp) -> {
            JsonNode jsonNode = null;
            try {
                jsonNode = mapper.readTree(temp.get(JSON_COL).toString());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            jsonForm.put(temp.get(NAME_COL).toString(),jsonNode);
        });

        return jsonForm;
    }

    /* GET BY FORM KEY */
    @GetMapping("/store/{name}")
    public  ResponseEntity getByName(@PathVariable("name") String name ) throws JsonProcessingException {

        Map<String, Object> mapDB;
        try {
             mapDB = jdbcTemplate.queryForMap(SELECT_BY_NAME,name);
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Kayit Yok");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode ;
        jsonNode = mapper.readTree(mapDB.get(JSON_COL).toString());

        Map<String, Object> jsonForm =new HashMap<>();
        jsonForm.put(mapDB.get(NAME_COL).toString(), jsonNode);


        return new ResponseEntity<>(jsonForm, HttpStatus.OK);
    }

    /* MULTIPART_FORM PUT */
    @RequestMapping(
            value = "store/{name}",
            method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity update(@RequestPart(value = "key")  String key ,
                         @RequestPart(value = "json")  String json,
                         @PathVariable("name") String name) {

            jdbcTemplate.update(UPDATE_QUERY_JSON,json ,name);
        return new ResponseEntity(HttpStatus.OK);
    }

    /* DELETE */
    @RequestMapping(
            value = "store/{name}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(@PathVariable("name") String name) {
        jdbcTemplate.update(DELETE_QUERY_JSON,name);
        return new ResponseEntity(HttpStatus.OK);
    }



}
