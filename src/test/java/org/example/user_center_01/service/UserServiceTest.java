package org.example.user_center_01.service;

import jakarta.annotation.Resource;
import org.example.user_center_01.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 *
 * @author AirSpace
 */

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;            // 这里是测试 UserService 的测试类，需要创建被测试类的实例，所以要进行引入

    @Test
    public void testAddUser() {
        User user = new User();                 // 创建一个 user 对象，将其存入数据库
        user.setId(0L);
        user.setUserName("dogXuan");
        user.setUserAccount("123");
        user.setAvatarUrl("https://tse3.mm.bing.net/th/id/OIP.MEmVm4wGyALRbVNZdc5zDQAAAA?rs=1&pid=ImgDetMain");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123");
        user.setIsDelete(0);

        boolean result = userService.save(user);                         // save 方法可以向数据库中插入数据
        System.out.println(user.getId());
        Assertions.assertTrue(result);                                  // 断言判断，希望 result 为 true
    }

    @Test
    void userRegister() {

        // 密码为空
        String userAccount = "Jixuan";
        String userPassword = "";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);                // 添加断言，希望 result 为-1，因为上面是各种错误的测试

        // 密码小于8 位
        userAccount = "Jixuan";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 含有特殊字符
        userAccount = "Ji xuan";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 校验密码与密码不相同
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账号重复
        userAccount = "dogXuan";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 正确，希望能够插入数据库
        userAccount = "JixuanZhang";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);


    }
}