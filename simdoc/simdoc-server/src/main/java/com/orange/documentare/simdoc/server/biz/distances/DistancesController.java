package com.orange.documentare.simdoc.server.biz.distances;

import com.orange.documentare.core.comp.distance.Distance;
import com.orange.documentare.simdoc.server.biz.CachesStats;
import com.orange.documentare.simdoc.server.biz.clustering.RequestValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
@RestController
public class DistancesController implements DistancesApi {

  @Override
  public DistancesRequestResult distances(
    @RequestBody DistancesRequest req, HttpServletResponse res) throws IOException {

    log.info("[DISTANCES REQ] for element id: " + req.element.id);

    RequestValidation validation = req.validate();
    if (!validation.ok) {
      return error(res, validation.error);
    }

    try {
      DistancesRequestResult result = compute(req);
      CachesStats.log();
      return result;
    } catch (IOException e) {
      return error(res, e.getMessage());
    }
  }

  private DistancesRequestResult compute(DistancesRequest req) throws IOException {
    Distance distance = new Distance();
    int[] distances = distance.compute(req.element, req.elements);
    return DistancesRequestResult.with(distances);
  }

  private DistancesRequestResult error(HttpServletResponse res, String error) throws IOException {
    res.sendError(SC_BAD_REQUEST, error);
    return DistancesRequestResult.error(error);
  }
}
