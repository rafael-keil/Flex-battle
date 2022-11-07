package br.com.cwi.crescer.api.representation.response;

import lombok.Data;

@Data
public class BattleUsersResponse {

    private UserResponse owner;
    private UserResponse opponent;
}
