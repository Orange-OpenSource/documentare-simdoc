package com.orange.documentare.simdoc.server.biz.distances;

import org.springframework.stereotype.Service;

@Service
public class DistancesServiceImpl implements DistancesService {
  @Override
  public DistancesRequestResult compute(DistancesRequest req) {
    return DistancesRequestResult.with(new int[]{9, 9});
  }
}
