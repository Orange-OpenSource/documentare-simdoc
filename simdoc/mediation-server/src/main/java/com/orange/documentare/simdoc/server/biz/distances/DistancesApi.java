package com.orange.documentare.simdoc.server.biz.distances;

import com.orange.documentare.simdoc.server.biz.RemoteTask;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "Distances", description = "Distances API")
public interface DistancesApi {

  @ApiOperation(value = "Enqueue distance computation task", response = RemoteTask.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "successful operation", response = RemoteTask.class) })
  @RequestMapping(
    value = "/distances",
    produces =  "application/json",
    method = RequestMethod.POST)
  RemoteTask distances(
    @ApiParam(value = "Distances request parameters", required=true)
    @RequestBody
      DistancesRequest req, HttpServletResponse res) throws IOException;
}
