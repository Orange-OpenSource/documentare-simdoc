package com.orange.documentare.core.filesio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class FilesIdBuilder {

  private static final String MAP_NAME = "map.json.gz";

  public void createFilesIdDirectory(String srcDir, String destDir) {
    File src = new File(srcDir);
    if (!src.isDirectory()) {
      throw new FilesIdException("[FilesIdBuilder] Source directory does not exists: " + src.getAbsolutePath());
    }

    File dest = createDestination(destDir);

    File[] srcFiles = createFilesId(src, dest);
    createMap(srcFiles, dest);
  }

  public FilesIdMap readMapIn(String absoluteDirectoryPath) {
    ObjectMapper mapper = new ObjectMapper();
    File mapFile = new File(absoluteDirectoryPath + "/" + MAP_NAME);
    try {
      InputStream in = new GZIPInputStream(new FileInputStream(mapFile));
      FilesIdMap map = mapper.readValue(in, FilesIdMap.class);
      in.close();
      return map;
    } catch (IOException e) {
      throw new FilesIdException(String.format("[FilesIdBuilder] failed to read map in directory (%s): %s", absoluteDirectoryPath, e.getMessage()));
    }
  }

  private File[] createFilesId(File src, File dest) {
    Collection<File> files = FileUtils.listFiles(src, null, true);
    File[] srcFiles = files.toArray(new File[files.size()]);
    Arrays.sort(srcFiles);
    for (int id = 0; id < srcFiles.length; id++) {
      Path srcPath = srcFiles[id].toPath().toAbsolutePath();
      Path destPath = (new File(dest.getAbsolutePath() + "/" + id)).toPath().toAbsolutePath();
      try {
        Files.createSymbolicLink(destPath, srcPath);
      } catch (IOException e) {
        throw new FilesIdException(String.format("[FilesIdBuilder] failed to create symbolic link: %s, %s -> %s", e.getMessage(), srcPath, destPath));
      }
    }
    return srcFiles;
  }

  private void createMap(File[] srcFiles, File dest) {
    FilesIdMap map = new FilesIdMap();
    for (int id = 0; id < srcFiles.length; id++) {
      map.put(id, srcFiles[id].getAbsolutePath());
    }

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
    File mapFile = new File(dest.getAbsolutePath() + "/" + MAP_NAME);
    try {
      GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(mapFile));
      writer.writeValue(out, map);
      out.close();
    } catch (IOException e) {
      throw new FilesIdException("[FilesIdBuilder] failed to write map: " + e.getMessage());
    }
  }

  private File createDestination(String destDir) {
    File dest = new File(destDir);
    if (dest.exists()) {
      log.info("[FilesIdBuilder] Directory exists, force delete and recreate: " + destDir);
    }
    try {
      FileUtils.deleteDirectory(dest);
    } catch (IOException e) {
      throw new FilesIdException("[FilesIdBuilder] failed to recreate(delete) destination directory: " + e.getMessage());
    }
    dest.mkdirs();
    return dest;
  }
}
