package br.com.cwi.crescer.api.representation.response;

import lombok.Data;

@Data
public class UserResponse {

    private String username;
    private String email;
    private String name;
    private Integer ranking;
}
