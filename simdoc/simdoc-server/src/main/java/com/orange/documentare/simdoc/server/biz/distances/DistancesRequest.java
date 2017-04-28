package com.orange.documentare.simdoc.server.biz.distances;

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.simdoc.server.biz.clustering.RequestValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class DistancesRequest {
  @ApiModelProperty(example = "{ id: 'elem0', bytes: [1, 4, 45, ...]")
  public final BytesData element;
  @ApiModelProperty(example = "{[{'id':'elem1', 'filepath':'/home/titi', 'bytes':[0x1,0x3...]},...])")
  public final BytesData[] elements;

  public static DistancesRequestBuilder builder() {
    return new DistancesRequestBuilder();
  }

  public RequestValidation validate() {
    boolean valid = false;
    String error = null;
    if (element == null || elements == null) {
      error = "element or elements are missing";
    } else {
      valid = true;
    }
    return new RequestValidation(valid, error);
  }

  public static class DistancesRequestBuilder {
    private BytesData element;
    private BytesData[] elements;

    public DistancesRequest build() {
      return new DistancesRequest(element, elements);
    }

    public DistancesRequestBuilder element(BytesData element) {
      this.element = element;
      return this;
    }

    public DistancesRequestBuilder compareTo(BytesData[] elements) {
      this.elements = elements;
      return this;
    }
  }
}
