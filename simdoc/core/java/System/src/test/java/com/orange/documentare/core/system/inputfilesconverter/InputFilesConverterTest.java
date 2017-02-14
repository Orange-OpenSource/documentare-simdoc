package com.orange.documentare.core.system.inputfilesconverter;

import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InputFilesConverterTest {

  private final static String[] TEST_FILES = {
    ".DS_STORE",
    "a_1& é ~ # { à \" weird file name . point.dïû",
    "b_pretty-file-name",
    "subdir/garfield",
    "w_abcd",
    ".hidden-file"
  };

  private final static File SRC_DIR = new File("test_tmp/files_id_src");
  private final static File DEST_ROOT_DIR = new File("test_tmp/fake_parent_to_test_parent_creation/files_id_dest");
  private final static File DEST_DIR = new File(DEST_ROOT_DIR.getAbsolutePath() + "/safe-input-dir");

  @Before
  @After
  public void cleanup() throws IOException {
    FileUtils.deleteDirectory(SRC_DIR);
    FileUtils.deleteDirectory(DEST_ROOT_DIR);
  }

  @Test(expected = FileConverterException.class)
  public void missing_source_directory_raises_exception() {
    InputFilesConverter.builder()
      .sourceDirectory(null)
      .destinationDirectory(DEST_DIR)
      .fileConverter(new SymbolicLinkConverter())
      .build();
  }

  @Test(expected = FileConverterException.class)
  public void missing_destination_directory_raises_exception() {
    InputFilesConverter.builder()
      .sourceDirectory(SRC_DIR)
      .destinationDirectory(null)
      .fileConverter(new SymbolicLinkConverter())
      .build();
  }

  @Test(expected = FileConverterException.class)
  public void missing_converter_raises_exception() {
    InputFilesConverter.builder()
      .sourceDirectory(SRC_DIR)
      .destinationDirectory(DEST_DIR)
      .fileConverter(null)
      .build();
  }

  @Test
  public void create_files_id_directory() throws IOException {
    // Given
    buildSourceDir(SRC_DIR, TEST_FILES);
    InputFilesConverter inputFilesConverter = InputFilesConverter.builder()
      .sourceDirectory(SRC_DIR)
      .destinationDirectory(DEST_DIR)
      .fileConverter(new SymbolicLinkConverter())
      .build();

    List<File> nonHiddenSourceFiles = nonHiddenSourceFiles(TEST_FILES);

    // When
    inputFilesConverter.createFilesIdDirectory();

    // Then
    for (int index = 0; index < nonHiddenSourceFiles.size(); index++) {
      File srcFile = nonHiddenSourceFiles.get(index);
      File destFile = new File(DEST_DIR.getAbsolutePath() + "/" + index);
      Assertions.assertThat(FileUtils.readFileToString(destFile)).isEqualTo(FileUtils.readFileToString(srcFile));
    }
  }

  @Test
  public void skip_hidden_files_in_dest_directory() throws IOException {
    // Given
    buildSourceDir(SRC_DIR, TEST_FILES);
    InputFilesConverter inputFilesConverter = InputFilesConverter.builder()
      .sourceDirectory(SRC_DIR)
      .destinationDirectory(DEST_DIR)
      .fileConverter(new SymbolicLinkConverter())
      .build();

    // When
    inputFilesConverter.createFilesIdDirectory();
    Collection<File> destFiles = FileUtils.listFiles(DEST_DIR, null, true);

    // Then
    for (File file : destFiles) {
      File target = Files.readSymbolicLink(file.toPath()).toFile();
      Assertions.assertThat(target.isHidden()).isFalse();
    }
  }

  @Test
  public void create_files_id_mapping() throws IOException {
    // Given
    buildSourceDir(SRC_DIR, TEST_FILES);
    InputFilesConverter inputFilesConverter = InputFilesConverter.builder()
      .sourceDirectory(SRC_DIR)
      .destinationDirectory(DEST_DIR)
      .fileConverter(new SymbolicLinkConverter())
      .build();
    List<File> nonHiddenSourceFiles = nonHiddenSourceFiles(TEST_FILES);

    // When
    FilesMap map = inputFilesConverter.createFilesIdDirectory();

    // Then
    map.keySet().forEach(index -> {
      File srcFile = nonHiddenSourceFiles.get(index);
      Assertions.assertThat(map.get(index)).isEqualTo(srcFile.getAbsolutePath());
    });
  }

  private void buildSourceDir(File srcDir, String[] filenames) throws IOException {
    for (String filename : filenames) {
      FileUtils.writeStringToFile(new File(srcDir.getAbsolutePath() + "/" + filename), filename);
    }
  }

  private List<File> nonHiddenSourceFiles(String[] sourceFiles) {
    return Arrays.stream(sourceFiles)
      .map(filename -> new File(SRC_DIR.getAbsolutePath() + File.separator + filename))
      .filter(file -> !file.isHidden())
      .collect(Collectors.toList());
  }
}
