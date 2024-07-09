package org.example.user_center_01.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.user_center_01.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Jaychou
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-04-08 20:54:08
*/
public interface UserService extends IService<User> {       // 只定义方法，在 UserServiceImpl 类中进行实现


    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);         // 定义用户注册方法，并在实现类中实现，由于返回的是id，所以为 long 型

    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);              // 定义用户登录方法，在实现类中实现，返回的是脱敏后的用户信息，所以为 user 型

    /**
     * 用户脱敏
     * @param originUser 未脱敏用户信息
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User originUser);

    /**
     *
     * 用户注销
     * @param request 前端传来的请求
     */
    int userLogout(HttpServletRequest request);
}
