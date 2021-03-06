package com.orange.documentare.simdoc.server.biz.clustering;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.simdoc.server.biz.CachesStats;
import com.orange.documentare.simdoc.server.biz.FileIO;
import com.orange.documentare.simdoc.server.biz.RemoteTask;
import com.orange.documentare.simdoc.server.biz.SharedDirectory;
import com.orange.documentare.simdoc.server.biz.task.Tasks;
import io.swagger.annotations.ApiParam;
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
public class ClusteringController implements ClusteringApi {

  @Autowired
  ClusteringService clusteringService;

  @Autowired
  private SharedDirectory sharedDirectory;

  @Autowired
  Tasks tasks;


  @Override
  public RemoteTask clustering(
    @RequestBody ClusteringRequest req, HttpServletResponse res) throws IOException {
    log.info("[Clustering request] " + req);

    RequestValidation validation = req.validate();
    if (!validation.ok) {
      res.sendError(SC_BAD_REQUEST, validation.error);
      return new RemoteTask();
    }

    FileIO fileIO = new FileIO(sharedDirectory, req);
    validation = fileIO.validate();
    if (!validation.ok) {
      res.sendError(SC_BAD_REQUEST, validation.error);
      return new RemoteTask();
    }

    String taskId;
    if (tasks.canAcceptNewTask()) {
      taskId = tasks.newTask();
      tasks.run(() -> run(taskId, req, fileIO), taskId);
    } else {
      res.sendError(SC_SERVICE_UNAVAILABLE, "can not accept more tasks");
      return new RemoteTask();
    }

    return new RemoteTask(taskId);
  }

  private void run(String taskId, ClusteringRequest req, FileIO fileIO) {
    try {
      ClusteringRequestResult result = doClustering(fileIO, req);
      if (result != null) {
        tasks.addResult(taskId, result);
      } else {
        tasks.addErrorResult(taskId, ClusteringRequestResult.error("Result is null"));
      }
      CachesStats.log();
    } catch (IOException e) {
      ClusteringRequestResult result = ClusteringRequestResult.error(e.getMessage());
      tasks.addErrorResult(taskId, result);
    }
  }

  private ClusteringRequestResult doClustering(FileIO fileIO, ClusteringRequest req) throws IOException {
    fileIO.deleteAllClusteringFiles();
    fileIO.writeRequest(req);
    return clusteringService.build(fileIO, req);
  }
}
