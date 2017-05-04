package com.orange.documentare.simdoc.server.biz.distances;

import java.io.IOException;

public interface DistancesService {
  DistancesRequestResult compute(DistancesRequest req) throws IOException;
}
