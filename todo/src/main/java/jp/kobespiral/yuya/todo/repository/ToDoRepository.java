package jp.kobespiral.yuya.todo.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import jp.kobespiral.yuya.todo.entity.ToDo;

@Repository
public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    List<ToDo> findAll();

    public List<ToDo> findByMidAndDone(String mid, boolean done);

    public List<ToDo> findByDone(boolean done);
}
