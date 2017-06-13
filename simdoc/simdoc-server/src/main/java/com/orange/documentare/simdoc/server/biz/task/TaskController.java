package com.orange.documentare.simdoc.server.biz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class TaskController implements TaskApi {

  @Autowired
  Tasks tasks;

  @Override
  public Object task(@PathVariable String id, HttpServletResponse res) throws IOException {
    log.info("[TASK REQ] for task id: " + id);

    if(!tasks.exists(id)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return null;
    }

    if (!tasks.isDone(id)) {
      res.setStatus(HttpServletResponse.SC_NO_CONTENT);
      return null;
    }

    Task task = tasks.pop(id);
    if (task.error) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    return task.result.get();
  }

  @Override
  public void killAll() {
    tasks.killAll();
  }
}
