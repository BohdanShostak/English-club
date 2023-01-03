package com.inventorsoft.english.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenRefreshRequest {

    private String refreshToken;
}
