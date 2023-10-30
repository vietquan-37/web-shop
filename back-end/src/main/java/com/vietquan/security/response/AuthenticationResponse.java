package com.vietquan.security.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vietquan.security.enumPackage.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthenticationResponse {
    private Integer userId;
    private String accessToken;
    private String refreshToken;
    private boolean mfaEnable;
    private String secretImage;
    private Role role;

}
