package com.itxiaoer.commons.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author : liuyk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("WeakerAccess")
public class LoginDto {

    @NotBlank
    private String loginName;

    @NotBlank
    private String password;

    private String code;
}
