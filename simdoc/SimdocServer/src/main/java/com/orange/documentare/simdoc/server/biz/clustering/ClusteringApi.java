package com.orange.documentare.simdoc.server.biz.clustering;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "Clustering", description = "Clustering API")
public interface ClusteringApi {

  @ApiOperation(value = "Build clustering", response = ClusteringResult.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "successful operation", response = ClusteringResult.class),
    @ApiResponse(code = 400, message = "bad request, check response error message", response = ClusteringResult.class) })
  @RequestMapping(
    value = "/clustering",
    produces =  "application/json",
    method = RequestMethod.POST)
  ClusteringResult clustering(
    @ApiParam(value = "Clustering request parameters", required=true)
    @RequestBody
      ClusteringRequest req, HttpServletResponse res) throws IOException;
}
