package jp.kobespiral.yuya.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ToDoForm {
    @NotBlank
    @Size(min = 1, max = 64)
    String title;
}
