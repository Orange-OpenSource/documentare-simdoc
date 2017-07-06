package com.orange.documentare.simdoc.server.biz.distances;

import com.orange.documentare.core.comp.distance.Distance;
import com.orange.documentare.simdoc.server.biz.CachesStats;
import com.orange.documentare.simdoc.server.biz.RemoteTask;
import com.orange.documentare.simdoc.server.biz.clustering.RequestValidation;
import com.orange.documentare.simdoc.server.biz.task.Tasks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_SERVICE_UNAVAILABLE;

@Slf4j
@RestController
public class DistancesController implements DistancesApi {

  @Autowired
  Tasks tasks;

  @Override
  public RemoteTask distances(
    @RequestBody DistancesRequest req, HttpServletResponse res) throws IOException {
    log.info("[DISTANCES REQ] for element id: " + req.element.id);

    RequestValidation validation = req.validate();
    if (!validation.ok) {
      res.sendError(SC_BAD_REQUEST, validation.error);
      return new RemoteTask();
    }

    String taskId;
    if (tasks.canAcceptNewTask()) {
      taskId = tasks.newTask();
      tasks.run(() -> run(taskId, req), taskId);
    } else {
      res.sendError(SC_SERVICE_UNAVAILABLE, "can not accept more tasks");
      return new RemoteTask();
    }

    return new RemoteTask(taskId);
  }

  private void run(String taskId, DistancesRequest req) {
    try {
      DistancesRequestResult result = compute(req);
      tasks.addResult(taskId, result);
      CachesStats.log();
    } catch (IOException e) {
      DistancesRequestResult result = DistancesRequestResult.error(e.getMessage());
      tasks.addErrorResult(taskId, result);
    }
  }

  private DistancesRequestResult compute(DistancesRequest req) throws IOException {
    Distance distance = new Distance();
    int[] distances = distance.compute(req.element, req.elements);
    return DistancesRequestResult.with(distances);
  }
}
