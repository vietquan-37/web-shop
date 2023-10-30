package com.vietquan.security.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class verifyRequest {
    private String email;
    private String code;
}
