package jp.kobespiral.yuya.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class MemberIdForm {
    @NotBlank
    @Pattern(regexp ="[a-z0-9_\\-]{4,16}")
    String mid; // メンバーID．
}