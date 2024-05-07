package weblogic.application;

import java.io.File;
import java.io.IOException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class SingleModuleFileManager extends ApplicationFileManager {
   private final File f;

   public SingleModuleFileManager(File var1) {
      this.f = var1;
   }

   public boolean isSplitDirectory() {
      return false;
   }

   public void registerLink(String var1, String var2) throws IOException {
      throw new AssertionError("Not supported");
   }

   public VirtualJarFile getVirtualJarFile() throws IOException {
      return VirtualJarFactory.createVirtualJar(this.f);
   }

   public VirtualJarFile getVirtualJarFile(String var1) throws IOException {
      return this.getVirtualJarFile();
   }

   public String getClasspath(String var1) {
      return this.getClasspath(var1, "");
   }

   public String getClasspath(String var1, String var2) {
      return this.f.getAbsolutePath();
   }

   public File getSourcePath() {
      return this.f;
   }

   public File getSourcePath(String var1) {
      return this.getSourcePath();
   }

   public File getOutputPath() {
      return this.f;
   }

   public File getOutputPath(String var1) {
      return this.getOutputPath();
   }
}
