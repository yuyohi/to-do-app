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

import jp.kobespiral.yuya.todo.dto.MemberIdForm;
import jp.kobespiral.yuya.todo.dto.ToDoForm;
import jp.kobespiral.yuya.todo.entity.ToDo;
import jp.kobespiral.yuya.todo.exception.ToDoAppException;
import jp.kobespiral.yuya.todo.service.MemberService;
import jp.kobespiral.yuya.todo.service.ToDoService;

@Controller
@RequestMapping("/")
public class ToDoController {
    @Autowired
    ToDoService toDoService;
    @Autowired
    MemberService memberService;

    @GetMapping("/")
    String showMainMenu(@ModelAttribute MemberIdForm form, Model model) {
        model.addAttribute("MemberIdForm", form);
        return "index";
    }

    @GetMapping("/login")
    String login(@Validated @ModelAttribute(name = "MemberIdForm") MemberIdForm form, BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return showMainMenu(form, model);
        }

        String mid = form.getMid();
        if (memberService.checkExistById(mid)) {
            return "redirect:/users/" + mid;
        } else {
            throw new ToDoAppException(ToDoAppException.NO_SUCH_MEMBER_EXISTS, mid + ": No such member exists");
        }
    }

    @GetMapping("users/{user}")
    String showUserMenu(@ModelAttribute ToDoForm form, @PathVariable String user, Model model) {
        model.addAttribute("ToDoForm", form);

        model.addAttribute("toDoList", toDoService.getToDoList(user));
        model.addAttribute("doneList", toDoService.getDoneList(user));

        return "list";
    }

    @PostMapping("users/{user}/todos")
    String createToDo(@Validated @ModelAttribute(name = "ToDoForm") ToDoForm form, BindingResult bindingResult,
            @PathVariable String user, Model model) {
        if (bindingResult.hasErrors()) {
            return showUserMenu(form, user, model);
        }

        toDoService.createToDo(user, form);

        return "redirect:/users/" + user;
    }

    @PostMapping("users/{user}/complete/{seq}")
    String doToDo(@PathVariable String user, @PathVariable String seq, Model model) {
        toDoService.doToDo(Long.parseLong(seq));

        return "redirect:/users/" + user;
    }

    @PostMapping("users/{user}/delete/{seq}")
    String deleteToDo(@PathVariable String user, @PathVariable String seq, Model model) {
        toDoService.deleteToDo(Long.parseLong(seq));

        return "redirect:/users/" + user;
    }

    @GetMapping("users/todos")
    String showAllToDo(Model model) {
        List<ToDo> toDoList = toDoService.getToDoList();
        List<ToDo> doneList = toDoService.getDoneList();

        model.addAttribute("AlltoDoList", toDoList);
        model.addAttribute("AlltoDoList", doneList);

        return "all-list";
    }

}
