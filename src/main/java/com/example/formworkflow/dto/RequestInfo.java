package com.example.formworkflow.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestInfo {
    private String apiId;
    private String ver;
    private String ts;
    private String action;
    private String did;
    private String key;
    private String msgId;
    private String authToken;
    private UserInfo userInfo;
}
