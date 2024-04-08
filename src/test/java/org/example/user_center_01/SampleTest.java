package org.example.user_center_01;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import jakarta.annotation.Resource;
import org.example.user_center_01.mapper.UserMapper;
import org.example.user_center_01.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        System.out.println(userList.size());
        Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }

}