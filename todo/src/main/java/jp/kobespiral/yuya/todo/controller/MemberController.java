package jp.kobespiral.yuya.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.kobespiral.yuya.todo.dto.MemberForm;
import jp.kobespiral.yuya.todo.entity.Member;
import jp.kobespiral.yuya.todo.service.MemberService;

@Controller
@RequestMapping("/admin")
public class MemberController {
    @Autowired
    MemberService memberService;

    /**
     * 管理者用・ユーザ登録ページ HTTP-GET /admin/register
     * 
     * @param model
     * @return
     */
    @GetMapping("/register")
    String showUserForm(@ModelAttribute MemberForm form, Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("MemberForm", form);

        return "register";
    }

    /**
     * 管理者用・ユーザ登録確認ページを表示 HTTP-POST /admin/check
     * 
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/check")
    String checkUserForm(@ModelAttribute(name = "MemberForm") MemberForm form, Model model) {
        model.addAttribute("MemberForm", form);
        return "check";
    }

    /**
     * 管理者用・ユーザ登録処理 -> 完了ページ HTTP-POST /admin/register
     * 
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/register")
    String createUser(@Validated @ModelAttribute(name = "MemberForm") MemberForm form, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return showUserForm(form, model);
        }

        Member member = memberService.createMember(form);
        model.addAttribute("MemberForm", member);
        return "registered";
    }

    /**
     * 管理者用・ユーザ削除処理 HTTP-GET /admin/delete/{mid}
     * 
     * @param mid
     * @param model
     * @return
     */
    @GetMapping("/delete/{mid}")
    String deleteUser(@PathVariable String mid, Model model) {
        memberService.deleteMember(mid);
        return "redirect:/admin/register";
    }
}
