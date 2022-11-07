package br.com.cwi.crescer.api.representation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeResponse {

    private List<List<String>> answer = new ArrayList<>();
    private Integer objects;
    private String hint;
}
