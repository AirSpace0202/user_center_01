package org.example.user_center_01.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.example.user_center_01.model.domain.User;
import org.example.user_center_01.model.domain.request.UserLoginRequest;
import org.example.user_center_01.model.domain.request.UserRegisterRequest;
import org.example.user_center_01.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.user_center_01.constant.UserConstant.ADMIN_ROLE;
import static org.example.user_center_01.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */

@RestController
@RequestMapping("/user")            // 用于接收处理 user 类型的请求
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")           // 处理 register 类型的请求
    // 类 UserRegisterRequest 定义了用户注册所需信息的一个数据传输对象
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        return  userService.userRegister(userAccount, userPassword, checkPassword);
    }



    @PostMapping("/login")       // 处理 login 类型的请求
    // 需要获取到HttpServletRequest对象来操作session
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return  userService.userLogin(userAccount, userPassword, request);
    }



    @GetMapping("/search")              // 处理 search 类型的请求，用于用户查询
    public List<User> searchUsers(String userName, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(userName)) {                    // 查询数据库中列名为 userName 的数据中包含 userName 的数据
            queryWrapper.like("userName", userName);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());     // 先将 userList 转换为数据流，进行脱敏，再转换成 list
    }



    @PostMapping("/delete")              // 处理 delete 类型的请求，用于用户删除
    public boolean deleteUsers(@RequestBody long id, HttpServletRequest request) {                // 根据前端传来的用户 id 值进行删除
        if (!isAdmin(request)) {
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);              // 返回一个 bool 类型，为逻辑删除
    }



    private boolean isAdmin(HttpServletRequest request) {

        // 仅管理员可查询删除
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);      // 获取用户信息
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
