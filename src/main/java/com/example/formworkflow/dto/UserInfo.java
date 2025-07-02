package com.example.formworkflow.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private Long id;
    private String uuid;
    private String userName;
    private String name;
    private String mobileNumber;
    private String emailId;
    private String locale;
    private String type;
    private Boolean active;
    private String tenantId;
    private List<Role> roles;
}
