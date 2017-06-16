package com.orange.documentare.app.clusteringremote;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Getter
@Accessors(fluent = true)
@Slf4j
@Component
public class SharedDirectory {

  @Value("${shared.directory.available}")
  private boolean sharedDirectoryAvailable;

  @Value("${shared.directory.root.path}")
  private String sharedDirectoryRootPath;

  @PostConstruct
  private void init() throws IOException {
    if (sharedDirectoryAvailable) {
      File directory = new File(sharedDirectoryRootPath);
      boolean directoryExists =directory.exists() && directory.isDirectory();
      sharedDirectoryRootPath += "/";
      if (!directoryExists) {
        throw new IOException("Shared directory is not accessible: '" + sharedDirectoryRootPath + "'");
      }
    }
    log.info("Shared directory, available = {}, path = {}", sharedDirectoryAvailable, sharedDirectoryRootPath);
  }
}
