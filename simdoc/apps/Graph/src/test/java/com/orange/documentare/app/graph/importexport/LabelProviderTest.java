package com.orange.documentare.app.graph.importexport;

import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.system.filesid.FilesIdMap;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class LabelProviderTest {

  @Test
  public void escape_to_html() {
    // Given
    FilesIdMap map = new FilesIdMap();
    map.put(0, "Léon Tolstoï, nom francisé de Lev Nikolaïevitch Tolstoï (en russe : Лев Никола́евич Толсто́й");
    LabelProvider labelProvider = new LabelProvider(map);
    GraphItem graphItem = new GraphItem();
    graphItem.setVertexName("0");

    // When
    String escapedLabel = labelProvider.getVertexName(graphItem);

    // Then
    Assertions.assertThat(escapedLabel).isEqualTo("L&eacute;on Tolsto&iuml;, nom francis&eacute; de Lev Nikola&iuml;evitch Tolsto&iuml; (en russe : Лев Никола́евич Толсто́й");
  }
}