package jp.kobespiral.yuya.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.kobespiral.yuya.todo.dto.MemberIdForm;
import jp.kobespiral.yuya.todo.dto.ToDoForm;
import jp.kobespiral.yuya.todo.dto.LoginForm;
import jp.kobespiral.yuya.todo.dto.UserDetailsImpl;
import jp.kobespiral.yuya.todo.entity.Member;
import jp.kobespiral.yuya.todo.entity.ToDo;
import jp.kobespiral.yuya.todo.exception.ToDoAppException;
import jp.kobespiral.yuya.todo.service.MemberService;
import jp.kobespiral.yuya.todo.service.ToDoService;

@Controller
public class ToDoController {
    @Autowired
    ToDoService toDoService;
    @Autowired
    MemberService memberService;
    /**
     * トップページ
     */
    @GetMapping("/sign-in")
    String showIndex(@RequestParam Map<String, String> params, @ModelAttribute LoginForm form, Model model) {
        // パラメータ処理．ログアウト時は?logout, 認証失敗時は?errorが帰ってくる（WebSecurityConfig.java参照）
        // model.addAttribute("loginForm", form);
        if (params.containsKey("sign-out")) {
            model.addAttribute("message", "サインアウトしました");
        } else if (params.containsKey("error")) {
            model.addAttribute("message", "サインインに失敗しました");
        }
        model.addAttribute("loginForm", form);
        return "signin";
    }

    /**
     * ログイン処理
     */
    @GetMapping("/sign-in-success")
    String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (m.getRole().equals("ADMIN")) {
            return "redirect:/admin/register";
        }
        return "redirect:/users/" + m.getMid();
    }

    /*
     * @GetMapping("/login")
     * String login(@Validated @ModelAttribute(name = "MemberIdForm") MemberIdForm
     * form, BindingResult bindingResult,
     * Model model) {
     * 
     * if (bindingResult.hasErrors()) {
     * return showMainMenu(form, model);
     * }
     * 
     * String mid = form.getMid();
     * if (memberService.checkExistById(mid)) {
     * return "redirect:/users/" + mid;
     * } else {
     * throw new ToDoAppException(ToDoAppException.NO_SUCH_MEMBER_EXISTS, mid +
     * ": No such member exists");
     * }
     * }
     */

    @GetMapping("/users/{user}")
    String showUserMenu(@ModelAttribute ToDoForm form, @PathVariable String user, Model model) {
        checkIdentity(user);

        Member member = memberService.getMember(user);
        model.addAttribute("member", member);
        model.addAttribute("ToDoForm", form);

        model.addAttribute("toDoList", toDoService.getToDoList(user));
        model.addAttribute("doneList", toDoService.getDoneList(user));

        return "list";
    }

    @PostMapping("/users/{user}/todos")
    String createToDo(@Validated @ModelAttribute(name = "ToDoForm") ToDoForm form, BindingResult bindingResult,
            @PathVariable String user, Model model) {
        checkIdentity(user);

        if (bindingResult.hasErrors()) {
            return showUserMenu(form, user, model);
        }

        toDoService.createToDo(user, form);

        return "redirect:/users/" + user;
    }

    @GetMapping("/users/{user}/complete/{seq}")
    String doToDo(@PathVariable String user, @PathVariable String seq, Model model) {
        checkIdentity(user);
        toDoService.doToDo(Long.parseLong(seq));

        return "redirect:/users/" + user;
    }

    @GetMapping("/users/{user}/delete/{seq}")
    String deleteToDo(@PathVariable String user, @PathVariable String seq, Model model) {
        checkIdentity(user);

        toDoService.deleteToDo(Long.parseLong(seq));

        return "redirect:/users/" + user;
    }

    @GetMapping("/users/todos")
    String showAllToDo(Model model) {
        List<ToDo> toDoList = toDoService.getToDoList();
        List<ToDo> doneList = toDoService.getDoneList();

        model.addAttribute("AlltoDoList", toDoList);
        model.addAttribute("AlltoDoList", doneList);

        return "all-list";
    }

    /**
     * 認可チェック．与えられたmidがログイン中のmidに等しいかチェックする
     */
    private void checkIdentity(String mid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (!mid.equals(m.getMid())) {
            throw new ToDoAppException(ToDoAppException.INVALID_TODO_OPERATION,
                    m.getMid() + ": not authorized to access resources of " + mid);
        }
    }

}
