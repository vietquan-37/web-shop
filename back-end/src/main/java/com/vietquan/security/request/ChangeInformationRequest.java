package com.vietquan.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ChangeInformationRequest {
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    @Pattern(regexp = "\\d{10,11}", message = "Phone number must have 10-11 numeric characters")
    private String phone;
    MultipartFile avatarFile;

}
