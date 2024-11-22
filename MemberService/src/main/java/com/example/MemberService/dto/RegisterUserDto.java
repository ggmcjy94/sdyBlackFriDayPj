package com.example.MemberService.dto;

public class RegisterUserDto {


    public String loginId;
    public String userName;


    public RegisterUserDto() {
    }

    public RegisterUserDto(String loginId, String userName) {
        this.loginId = loginId;
        this.userName = userName;
    }
}
