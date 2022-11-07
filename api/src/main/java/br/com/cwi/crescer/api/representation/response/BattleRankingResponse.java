package br.com.cwi.crescer.api.representation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleRankingResponse {

    private Integer ownerRanking;
    private Integer opponentRanking;
}
