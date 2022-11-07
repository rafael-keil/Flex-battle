package br.com.cwi.crescer.api.representation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputRequest {

    @NotEmpty
    String input;
}
