package com.orange.documentare.core.system.filesid;

import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FilesIdBuilderTest {

  private final static String[] TEST_FILES = {
    "a_1& é ~ # { à \" weird file name . point.dïû",
    "b_pretty-file-name",
    "subdir/garfield",
    "w_abcd"
  };

  private final static String SRC_DIR = "test_tmp/files_id_src";
  private final static String DEST_ROOT_DIR = "test_tmp/fake_parent_to_test_parent_creation/files_id_dest";
  private final static String DEST_DIR = DEST_ROOT_DIR + "/safe-input-dir";

  @Before
  @After
  public void cleanup() throws IOException {
    FileUtils.deleteDirectory(new File(SRC_DIR));
    FileUtils.deleteDirectory(new File(DEST_ROOT_DIR));
  }

  @Test
  public void create_files_id_directory() throws IOException {
    // Given
    FilesIdBuilder builder = new FilesIdBuilder();
    buildSourceDir(SRC_DIR, TEST_FILES);

    // When
    builder.createFilesIdDirectory(SRC_DIR, DEST_DIR, DEST_ROOT_DIR);

    // Then
    for (int index = 0; index < TEST_FILES.length; index++) {
      File srcFile = new File(SRC_DIR + "/" + TEST_FILES[index]);
      File destFile = new File(DEST_DIR + "/" + index);
      Assertions.assertThat(FileUtils.readFileToString(destFile)).isEqualTo(FileUtils.readFileToString(srcFile));
    }
  }

  @Test
  public void create_files_id_mapping() throws IOException {
    // Given
    FilesIdBuilder builder = new FilesIdBuilder();
    buildSourceDir(SRC_DIR, TEST_FILES);

    // When
    builder.createFilesIdDirectory(SRC_DIR, DEST_DIR, DEST_ROOT_DIR);
    FilesIdMap map = builder.readMapIn(DEST_ROOT_DIR);

    // Then
    map.keySet().forEach(index -> {
      File srcFile = new File(SRC_DIR + "/" + TEST_FILES[index]);
      Assertions.assertThat(map.get(index)).isEqualTo(srcFile.getAbsolutePath());
    });
  }

  private void buildSourceDir(String srcDir, String[] filenames) throws IOException {
    for (String filename : filenames) {
      FileUtils.writeStringToFile(new File(srcDir + "/" + filename), filename);
    }
  }
}
