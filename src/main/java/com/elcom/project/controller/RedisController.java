/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.project.controller;

import com.elcom.project.utils.StringUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    final String keyLst = "my-key-lst";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping()
    public List<String> list() {
        Long sizeLst = redisTemplate.opsForList().size(keyLst);
        System.out.println("Key [" + keyLst + "] contains : " + sizeLst + " element");
        if (sizeLst != null && !sizeLst.equals(0L)) {
            List<String> lstStr = (List<String>) redisTemplate.opsForList().range(keyLst, 0, -1);
            if (lstStr != null && !lstStr.isEmpty()) {
                lstStr.forEach((str) -> {
                    System.out.println("item:" + str);
                });
            }
            return lstStr;
        }
        return null;
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Long id) {
        Object obj = redisTemplate.opsForList().index(keyLst, id);
        return obj != null ? obj.toString() : null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody String input) {
        redisTemplate.opsForList().set(keyLst, id, input);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody String input) {
        redisTemplate.opsForList().rightPush(keyLst, input);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        String value = get(id);
        if (!StringUtil.isNullOrEmpty(value)) {
            redisTemplate.opsForList().remove(keyLst, 1, value);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public Integer find(@RequestParam String value) {
        Long sizeLst = redisTemplate.opsForList().size(keyLst);
        if (sizeLst != null && !sizeLst.equals(0L)) {
            List<String> lstStr = (List<String>) redisTemplate.opsForList().range(keyLst, 0, -1);
            if (lstStr != null && !lstStr.isEmpty()) {
                int index = 0;
                for (String tmp : lstStr) {
                    if (tmp != null && tmp.equals(value)) {
                        return index;
                    }
                    index++;
                }
            }
        }
        return -1;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error message")
    public void handleError() {
    }

}
