package br.com.cwi.crescer.api.representation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeBattleResponse {

    private Integer objects;
    private List<List<String>> answer = new ArrayList<>();

}
