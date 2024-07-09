package org.example.user_center_01.mapper;

import org.example.user_center_01.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Jaychou
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-04-08 20:54:08
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {            // 继承的 BaseMapper 接口中有一些实现数据库操作的方法（CURD等）

}




