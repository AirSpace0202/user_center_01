package org.example.user_center_01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.user_center_01.model.domain.User;
import org.example.user_center_01.service.UserService;
import org.example.user_center_01.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Jaychou
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-04-08 20:54:08
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>          // 定义了 UserService 接口以及实现类
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        // 1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 账户不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }

        // 密码与校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();     // 用于构建 SQL 查询的 WHERE 条件
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);                  // this 为当前类的对象实例（正在执行代码的对象）
        if (count > 0) {                    // 说明该账户已经被注册
            return -1;
        }

        // 2、加密
        final String SALT = "jixuan";               // 加盐，加强混淆程度
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3、 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }

        return user.getId();
    }
}




