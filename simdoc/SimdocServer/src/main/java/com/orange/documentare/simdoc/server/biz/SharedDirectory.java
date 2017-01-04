package com.orange.documentare.simdoc.server.biz;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Getter
@Accessors(fluent = true)
@Component
public class SharedDirectory {

  @Value("${app.shared.directory.root.path}")
  private String sharedDirectoryRootPath;

  private boolean sharedDirectoryAvailable;

  public void updateSharedDirectory(String rootPath) {
    sharedDirectoryRootPath = rootPath;
    init();
  }

  @PostConstruct
  private void init() {
    File directory = new File(sharedDirectoryRootPath);
    sharedDirectoryAvailable = directory.exists() && directory.isDirectory();
    sharedDirectoryRootPath += "/";
  }
}
