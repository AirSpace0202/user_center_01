package org.example.user_center_01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.user_center_01.model.domain.User;
import org.example.user_center_01.service.UserService;
import org.example.user_center_01.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.user_center_01.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Jaychou
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-04-08 20:54:08
*/
@Service
@Slf4j                  // 可以生成日志

public class UserServiceImpl extends ServiceImpl<UserMapper, User>          // 定义了 UserService 接口以及实现类
    implements UserService {


    @Resource
    private UserMapper userMapper;

    private static final String SALT = "jixuan";               // 加盐，加强混淆程度


    @Override       // 实现用户注册方法
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

    @Override       // 定义用户登录方法
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        // 1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        // 账户不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        // 2、加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 账户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();     // 用于构建 SQL 查询的 WHERE 条件
        queryWrapper.eq("userAccount", userAccount);        // 账号和密码都要与数据库中的相匹配
        queryWrapper.eq("userPassword", newPassword);
        User user = userMapper.selectOne(queryWrapper);         // 在数据库中查找一个和 queryWrapper 相匹配的数据
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount can not match userPassword");              // 在日志中记录一下信息，即账号与密码不匹配
            return null;
        }

        // 3、用户脱敏，返回脱敏后的用户信息

        User safetyUser = getSafetyUser(user);
        // 4、记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);        // 后端拿到前端传来的 cookie，找到对应的 session，后端取出 session 并设置变量

        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser 未脱敏用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());

        return safetyUser;
    }
}





