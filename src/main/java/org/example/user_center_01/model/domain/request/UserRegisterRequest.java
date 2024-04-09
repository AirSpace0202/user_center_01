package org.example.user_center_01.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体,作为数据传输对象，在前端和后端之间传输用户登录请求的数据
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;            // 序列化 id，可以在网上传输，或者持久化到硬盘

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}