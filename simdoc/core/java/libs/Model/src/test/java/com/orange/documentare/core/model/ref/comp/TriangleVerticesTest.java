package com.orange.documentare.core.model.ref.comp;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import lombok.Getter;
import lombok.Setter;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(ZohhakRunner.class)
public class TriangleVerticesTest {

  class Item implements ClusteringItem {
    @Getter
    @Setter
    NearestItem[] nearestItems;

    @Override public Integer getClusterId() { return null; }
    @Override public void setClusterId(Integer id) {}
    @Override public void setClusterCenter(boolean isCenter) {}
    @Override
    public boolean triangleVerticesAvailable() { return false; }
    @Override public TriangleVertices getTriangleVertices() { return null; }
    @Override public String getHumanReadableId() { return null; }
    @Override public byte[] getBytes() { return new byte[0]; }
  }

  @Test
  public void build_a_triangle() {
    // Given
    ClusteringItem[] items = built3Items();
    ClusteringItem clusteringItem = items[0];

    // When
    TriangleVertices triangleVertices = new TriangleVertices(clusteringItem, items);

    // Then
    Assertions.assertThat(triangleVertices.isOrphan()).isFalse();
    Assertions.assertThat(triangleVertices.getEdge12()).isEqualTo(10);
    Assertions.assertThat(triangleVertices.getEdge23()).isEqualTo(20);
    Assertions.assertThat(triangleVertices.getEdge13()).isEqualTo(30);
    Assertions.assertThat(triangleVertices.getVertex2().getIndex()).isEqualTo(1);
    Assertions.assertThat(triangleVertices.getVertex3().getIndex()).isEqualTo(2);
  }

  @TestWith({ "2, true", "3, false" })
  public void item_0_may_be_orphan_due_to_knn_criteria(int k, boolean orphan) {
    // Given
    ClusteringItem[] items = built4ItemsToTestKnn();
    ClusteringItem clusteringItem = items[0];

    // When
    TriangleVertices triangleVertices = new TriangleVertices(clusteringItem, items, k);

    // Then
    Assertions.assertThat(triangleVertices.isOrphan()).isEqualTo(orphan);
  }

  @TestWith({ "1, true", "2, false" })
  public void item_1_may_be_orphan_due_to_knn_criteria(int k, boolean orphan) {
    // Given
    ClusteringItem[] items = built4ItemsToTestKnn();
    ClusteringItem clusteringItem = items[1];

    // When
    TriangleVertices triangleVertices = new TriangleVertices(clusteringItem, items, k);

    // Then
    Assertions.assertThat(triangleVertices.isOrphan()).isEqualTo(orphan);
  }

  private Item[] built3Items() {
    List<Item> items = IntStream.range(0, 3)
            .mapToObj(index -> new Item())
            .collect(Collectors.toList());

    items.get(0).setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, 10), new NearestItem(2, 30)});
    items.get(1).setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(0, 10), new NearestItem(2, 20)});
    items.get(2).setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(1, 20), new NearestItem(0, 30)});

    return items.toArray(new Item[items.size()]);
  }

  private ClusteringItem[] built4ItemsToTestKnn() {
    List<Item> items = IntStream.range(0, 4)
            .mapToObj(index -> new Item())
            .collect(Collectors.toList());

    items.get(0).setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, 20), new NearestItem(2, 25), new NearestItem(3, 30)});
    items.get(1).setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(3, 10), new NearestItem(0, 20), new NearestItem(2, 45)});
    items.get(2).setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, 25), new NearestItem(1, 45), new NearestItem(3, 55)});
    items.get(3).setNearestItems(new NearestItem[]{ new NearestItem(3, 0), new NearestItem(1, 10), new NearestItem(0, 30), new NearestItem(2, 55)});

    return items.toArray(new Item[items.size()]);  }
}

