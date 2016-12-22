package com.orange.documentare.simdoc.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.orange.documentare.simdoc.server.ClusteringRequest.ValidRequest;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class ClusteringController {

  @RequestMapping(value = "/clustering", method = RequestMethod.POST)
  public ValidRequest clustering(@RequestBody ClusteringRequest req, HttpServletResponse res) {
    log.info("[Clustering request] " + req);
    ClusteringRequest.ValidRequest validRequest = req.validate();
    if (validRequest.valid) {

    } else {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    return validRequest;
  }
}
