package fr.kphoenix.minarium.launcher;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OS {
   public static Path getAppDir(String serverName) throws IOException {
      String OS = System.getProperty("os.name").toUpperCase();
      String dir;
      if (OS.contains("WIN")) {
         dir = System.getenv("AppData");
      } else {
         dir = System.getProperty("user.home") + "/Library/Application Support";
      }

      Path path = Paths.get(dir + "/." + serverName);
      return path;
   }
}
