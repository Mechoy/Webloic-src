package weblogic.ant.taskdefs.utils;

import java.io.File;
import org.apache.tools.ant.BuildException;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;

public class LibraryElement {
   private File dir = null;
   private File location = null;
   private String name = null;
   private String specVersion = null;
   private String implVersion = null;

   public void setDir(File var1) {
      this.dir = var1;
   }

   public File getDir() {
      return this.dir;
   }

   public void setFile(File var1) {
      this.location = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setSpecificationVersion(String var1) {
      this.specVersion = var1;
   }

   public void setImplementationVersion(String var1) {
      this.implVersion = var1;
   }

   public File getFile() {
      return this.location;
   }

   public String getName() {
      return this.name;
   }

   public String getSpecificationVersion() {
      return this.specVersion;
   }

   public String getImplementationVersion() {
      return this.implVersion;
   }

   public LibraryData getLibraryData() {
      LibraryData var1 = null;

      try {
         var1 = LibraryLoggingUtils.initLibraryData(this.getName(), this.getSpecificationVersion(), this.getImplementationVersion(), this.getFile());
         return var1;
      } catch (LoggableLibraryProcessingException var3) {
         throw new BuildException(var3.getLoggable().getMessage());
      }
   }
}
