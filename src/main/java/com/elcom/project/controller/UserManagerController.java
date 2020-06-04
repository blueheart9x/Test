package com.elcom.project.controller;

import com.elcom.project.exception.ValidationException;
import com.elcom.project.model.User;
import com.elcom.project.service.UserService;
import com.elcom.project.utils.JSONConverter;
import com.elcom.project.utils.StringUtil;
import com.elcom.project.validation.UserValidation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author anhdv
 */
@RestController
//@RequestMapping("/users")
public class UserManagerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagerController.class);

    private final UserService userService;

    @Autowired
    public UserManagerController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private RedisTemplate redisTemplate;    
    
    @RequestMapping(value = "/redis-test", method = RequestMethod.GET)
    public ResponseEntity<String> redisTest() {
        
        String key = "my-key";
        String value = "my-value";
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.opsForValue().set(key+"-2", null);
        redisTemplate.opsForValue().set(key+"-3", true);
        redisTemplate.opsForValue().set(key+"-4", 123456);
        
        System.out.println("Value of key ["+key+"]: " + redisTemplate.opsForValue().get(key));
        System.out.println("Value of key ["+key+"-1] (not set): " + redisTemplate.opsForValue().get(key+"-1"));
        System.out.println("Value of key ["+key+"-2] (set null): " + redisTemplate.opsForValue().get(key+"-2"));
        System.out.println("Value of key ["+key+"-3]: " + redisTemplate.opsForValue().get(key+"-3"));
        System.out.println("Value of key ["+key+"-4]: " + redisTemplate.opsForValue().get(key+"-4"));
        
        String keyLst = "my-key-lst";
        List<String> lst = new ArrayList<>();
        lst.add("A"); lst.add("B"); lst.add("C");
        redisTemplate.opsForList().rightPushAll(keyLst, lst);
        
        Long sizeLst = redisTemplate.opsForList().size(keyLst);
        System.out.println("Key ["+keyLst+"] contains : " + sizeLst + " element");
        if( sizeLst!=null && !sizeLst.equals(0L) ) {
            List<String> lstStr = (List<String>)redisTemplate.opsForList().range(keyLst, 0, -1);
            if( lstStr!=null && !lstStr.isEmpty() ) {
                for( String str : lstStr ) {
                    System.out.println("item:" + str);
                }
                redisTemplate.delete(keyLst);
            }
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Lấy danh sách User", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Thành công, có dữ liệu"),
            @ApiResponse(code = 204, message = "Thành công, không có dữ liệu"),
            @ApiResponse(code = 401, message = "Chưa xác thực"),
            @ApiResponse(code = 403, message = "Truy cập bị cấm"),
            @ApiResponse(code = 404, message = "Không tìm thấy"),
            @ApiResponse(code = 500, message = "Lỗi BackEnd")
    })
    public ResponseEntity<List<User>> findAll(@RequestParam(defaultValue = "1") Integer currentPage, 
                                            @RequestParam(defaultValue = "10") Integer rowsPerPage,
                                            @RequestParam(defaultValue = "id") String sort) {
        
        List<User> userLst = userService.findAll(currentPage, rowsPerPage, sort);
        if (userLst.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(userLst, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        LOGGER.info("id[{}]", id);

        if (id == null || id.equals(0L))
            throw new ValidationException("id không được để trống");

        User user = userService.findById(id);

        if ( user == null)
            return new ResponseEntity<>(user, HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ApiOperation(value = "Thêm mới User", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Thêm mới thành công"),
            @ApiResponse(code = 401, message = "Chưa xác thực"),
            @ApiResponse(code = 403, message = "Truy cập bị cấm"),
            @ApiResponse(code = 404, message = "Không tìm thấy"),
            @ApiResponse(code = 500, message = "Lỗi BackEnd")
    })
    public ResponseEntity<User> create(@RequestBody User user, UriComponentsBuilder builder) {
        
        LOGGER.info("{}", JSONConverter.toJSON(user));

        new UserValidation().validateUpsertUser(user, "INSERT");

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/users/{id}").buildAndExpand(user.getId()).toUri());
        
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
        LOGGER.info("id[{}] - {}", id, JSONConverter.toJSON(user));
        
        if (id == null || id.equals(0L))
            throw new ValidationException("id không được để trống");

        new UserValidation().validateUpsertUser(user, "UPDATE");

        User currentUser = userService.findById(id);

        if ( currentUser==null )
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        //Cần override những field nào thì set hết vào đây (các thuộc tính nằm trong payLoad client gửi lên)
        currentUser.setFullName(user.getFullName());
        if( !StringUtil.isNullOrEmpty(user.getPassword()) )
            currentUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        
        userService.save(currentUser);

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        LOGGER.info("delete() --> id[{}]", id);

        if (id == null || id.equals(0L))
            throw new ValidationException("id không được để trống");

        User user = userService.findById(id);

        if ( user==null )
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        userService.remove(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(value = "/insertMultiRows", method = RequestMethod.POST)
    public ResponseEntity<User> insertTest(@RequestBody User user, UriComponentsBuilder builder) {
        userService.insertTest();
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
