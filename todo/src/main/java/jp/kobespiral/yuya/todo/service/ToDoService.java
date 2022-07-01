package jp.kobespiral.yuya.todo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.kobespiral.yuya.todo.repository.ToDoRepository;
import jp.kobespiral.yuya.todo.entity.ToDo;
import jp.kobespiral.yuya.todo.exception.ToDoAppException;
import jp.kobespiral.yuya.todo.dto.ToDoForm;

@Service
public class ToDoService {
    @Autowired
    ToDoRepository toDoRepo;

    /**
     * todoを作成する
     * 
     * @param mid
     * @param form
     * @return
     */
    public ToDo createToDo(String mid, ToDoForm form) {
        ToDo toDo = new ToDo(null, form.getTitle(), mid, false, new Date(), null);

        return toDoRepo.save(toDo);
    }

    public ToDo getToDo(Long seq) {
        ToDo toDo = toDoRepo.findById(seq).orElseThrow(
                () -> new ToDoAppException(ToDoAppException.NO_SUCH_MEMBER_EXISTS, seq + ": No such member exists"));

        return toDo;
    }

    public List<ToDo> getToDoList(String mid) {
        return toDoRepo.findByMidAndDone(mid, false);
    }

    public List<ToDo> getDoneList(String mid) {
        return toDoRepo.findByMidAndDone(mid, true);
    }

    public List<ToDo> getToDoList() {
        return toDoRepo.findByDone(false);
    }

    public List<ToDo> getDoneList() {
        return toDoRepo.findByDone(true);
    }

    public void deleteToDo(Long seq) {
        ToDo toDo = getToDo(seq);
        toDoRepo.delete(toDo);
    }

    public void doToDo(Long seq) {
        ToDo toDo = getToDo(seq);
        toDo.setDone(true);
        toDo.setDoneAt(new Date());
        toDoRepo.save(toDo);
    }
}
