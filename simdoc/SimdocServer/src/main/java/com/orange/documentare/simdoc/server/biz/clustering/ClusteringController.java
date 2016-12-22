package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.simdoc.server.biz.SimdocResult;
import com.orange.documentare.simdoc.server.biz.clustering.ClusteringRequest.RequestValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
@RestController
public class ClusteringController {

  @RequestMapping(value = "/clustering", method = RequestMethod.POST)
  public SimdocResult clustering(@RequestBody ClusteringRequest req, HttpServletResponse res) throws IOException {
    log.info("[Clustering request] " + req);

    SimdocResult result;
    RequestValidation validation = req.validate();
    if (validation.ok) {
      result = new ClusteringResult(false, null);
    } else {
      res.sendError(SC_BAD_REQUEST, validation.error);
      result = SimdocResult.error(validation.error);
    }
    return result;
  }
}
