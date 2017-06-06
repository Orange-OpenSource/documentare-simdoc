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

  @ApiOperation(value = "Compute distances", response = DistancesRequestResult.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "successful operation", response = DistancesRequestResult.class),
    @ApiResponse(code = 400, message = "bad request, check response error message", response = DistancesRequestResult.class) })
  @RequestMapping(
    value = "/distances",
    produces =  "application/json",
    method = RequestMethod.POST)
  RemoteTask distances(
    @ApiParam(value = "Distances request parameters", required=true)
    @RequestBody
      DistancesRequest req, HttpServletResponse res) throws IOException;
}
