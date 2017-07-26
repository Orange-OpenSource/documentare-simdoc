package com.orange.documentare.simdoc.server.biz.task;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "Task", description = "Task API")
public interface TaskApi {

  @ApiOperation(value = "Retrieve task result", response = Object.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "task finished", response = Object.class),
    @ApiResponse(code = 204, message = "task not finished yet") })
  @RequestMapping(
    value = "/task/{id}",
    produces =  "application/json",
    method = RequestMethod.GET)
  Object task(
    @ApiParam(value = "Task id", required = true)
    @PathVariable
      String id, HttpServletResponse res) throws IOException;

  @ApiOperation(value = "Kill all current tasks")
  @RequestMapping(
    value = "/kill-all-tasks",
    method = RequestMethod.POST)
  void killAll();

}
