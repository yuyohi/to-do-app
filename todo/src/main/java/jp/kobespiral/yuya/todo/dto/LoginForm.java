package jp.kobespiral.yuya.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @NotBlank
    String mid;
    @NotBlank
    @Size(min = 8)
    String password;
}
