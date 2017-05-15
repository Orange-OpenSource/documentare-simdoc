package com.orange.documentare.simdoc.server.biz.distances;

import com.orange.documentare.core.comp.distance.Distance;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesDistances;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DistancesServiceImpl implements DistancesService {
  private final Distance distance = new Distance();

  @Override
  public DistancesRequestResult compute(DistancesRequest req) throws IOException {
    BytesData element = BytesData.withBytes(req.element);
    BytesData[] elements = BytesData.withBytes(req.elements);
    int[] distances = distance.compute(element, elements);
    return DistancesRequestResult.with(distances);
  }
}
