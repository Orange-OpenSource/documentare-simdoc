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

import com.orange.documentare.simdoc.server.biz.RemoteTask;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "Clustering", description = "Clustering API")
public interface ClusteringApi {

  @ApiOperation(value = "Enqueue Build clustering task", response = RemoteTask.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "successful operation", response = RemoteTask.class) })
  @RequestMapping(
    value = "/clustering",
    produces =  "application/json",
    method = RequestMethod.POST)
  RemoteTask clustering(
    @ApiParam(value = "Clustering request parameters", required=true)
    @RequestBody
      ClusteringRequest req, HttpServletResponse res) throws IOException;
}
