package com.orange.documentare.app.ncdremote;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class DistancesRequestResult {
  public final int[] distances;
  public final boolean error;
  public final String errorMessage;

  public DistancesRequestResult() {
    distances = null;
    error = false;
    errorMessage = "ctor only for deserialize feature";
  }
}
