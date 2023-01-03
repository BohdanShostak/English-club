package com.inventorsoft.english.users.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserRegistrationDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!#$%^&*+=\\-\\[\\]‘;,./{}|“:<>?_@]).{8,30})",
            message = "Password must be minimum 8 symbols and contain at least one uppercase, one lowercase, one special symbol and one digit")
    private String password;
}
