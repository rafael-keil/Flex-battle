package br.com.cwi.crescer.api.representation.response;

import lombok.Data;

@Data
public class UserLeaderboardResponse {
    
    private String name;
    private Integer ranking;
}
