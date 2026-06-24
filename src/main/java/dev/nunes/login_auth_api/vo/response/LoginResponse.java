package dev.nunes.login_auth_api.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String name;

    private String token;
}
