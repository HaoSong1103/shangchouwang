package com.atguigu.crowd.entity.vo;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname MemberLoginVO
 * @Description TODO
 * @Date 2020/7/16 22:19
 */
public class MemberLoginVO {
    private String username;

    private String email;

    private Integer id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MemberLoginVO() {
    }

    public MemberLoginVO(String username, String email, Integer id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }
}
