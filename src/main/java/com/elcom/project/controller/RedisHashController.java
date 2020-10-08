/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.project.controller;

import com.elcom.project.model.dto.UserDTO;
import com.elcom.project.utils.StringUtil;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/redis-hash")
public class RedisHashController {

    final String keyHash = "my-key-hash";
    final String hashListId = "LIST";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping()
    public List<UserDTO> list() {
        return (List<UserDTO>) redisTemplate.opsForHash().get(keyHash, hashListId);
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable String id) {
        return (UserDTO) redisTemplate.opsForHash().get(keyHash, id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody UserDTO input) {
        redisTemplate.opsForHash().put(keyHash, id, input);
        List<UserDTO> list = (List<UserDTO>) redisTemplate.opsForHash().get(keyHash, hashListId);
        if (list == null) {
            list = new ArrayList<>();
            list.add(input);
        } else {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                UserDTO tmp = list.get(i);
                if (tmp.getId().equals(id)) {
                    list.set(i, input);
                    break;
                }
            }
        }
        redisTemplate.opsForHash().put(keyHash, hashListId, list);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody UserDTO input) {
        List<UserDTO> list = (List<UserDTO>) redisTemplate.opsForHash().get(keyHash, hashListId);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(input);
        redisTemplate.opsForHash().put(keyHash, hashListId, list);
        redisTemplate.opsForHash().put(keyHash, input.getId(), input);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (redisTemplate.opsForHash().hasKey(keyHash, id)) {
            redisTemplate.opsForHash().delete(keyHash, id);
            List<UserDTO> list = (List<UserDTO>) redisTemplate.opsForHash().get(keyHash, hashListId);
            if (list != null && !list.isEmpty()) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    UserDTO tmp = list.get(i);
                    if (tmp.getId().equals(id)) {
                        list.remove(i);
                        break;
                    }
                }
            }
            redisTemplate.opsForHash().put(keyHash, hashListId, list);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
