package weblogic.application;

import java.io.File;
import java.io.IOException;
import weblogic.utils.jars.VirtualJarFile;

public abstract class ApplicationFileManager implements SplitDirectoryConstants {
   public static ApplicationFileManager newInstance(String var0) throws IOException {
      return new ApplicationFileManagerImpl(var0);
   }

   public static ApplicationFileManager newInstance(File var0) throws IOException {
      return new ApplicationFileManagerImpl(var0);
   }

   public static ApplicationFileManager newInstance(SplitDirectoryInfo var0) throws IOException {
      return new ApplicationFileManagerImpl(var0);
   }

   public abstract boolean isSplitDirectory();

   public abstract void registerLink(String var1, String var2) throws IOException;

   public abstract VirtualJarFile getVirtualJarFile() throws IOException;

   public abstract VirtualJarFile getVirtualJarFile(String var1) throws IOException;

   public abstract String getClasspath(String var1);

   public abstract String getClasspath(String var1, String var2);

   public abstract File getSourcePath();

   public abstract File getSourcePath(String var1);

   public abstract File getOutputPath();

   public abstract File getOutputPath(String var1);
}
