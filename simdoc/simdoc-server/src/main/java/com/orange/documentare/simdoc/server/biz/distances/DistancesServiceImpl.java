package com.orange.documentare.simdoc.server.biz.distances;

import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesDistances;
import org.springframework.stereotype.Service;

@Service
public class DistancesServiceImpl implements DistancesService {
  @Override
  public DistancesRequestResult compute(DistancesRequest req) {
    BytesDistances bytesDistances = new BytesDistances();
    DistancesArray distancesArray = bytesDistances.computeDistancesBetweenCollections(
      new BytesData[] {req.element}, req.elements
    );

    return DistancesRequestResult.with(distancesArray.getDistancesFor(0));
  }
}
