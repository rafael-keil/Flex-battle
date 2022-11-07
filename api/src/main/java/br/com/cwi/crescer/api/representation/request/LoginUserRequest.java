package br.com.cwi.crescer.api.representation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
