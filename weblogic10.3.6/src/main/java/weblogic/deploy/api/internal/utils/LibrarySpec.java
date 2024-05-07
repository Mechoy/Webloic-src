package weblogic.deploy.api.internal.utils;

import java.io.File;
import weblogic.application.internal.library.util.DeweyDecimal;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;

public class LibrarySpec {
   private LibraryData lib;
   private boolean mark = false;
   private Object key;

   public LibrarySpec(String var1, String var2, String var3, File var4) throws IllegalArgumentException {
      try {
         ConfigHelper.checkParam("File", var4);
         this.lib = LibraryLoggingUtils.initLibraryData(var1, var2, var3, var4);
      } catch (LoggableLibraryProcessingException var6) {
         throw new IllegalArgumentException(var6);
      }
   }

   public LibraryData getLibraryData() {
      return this.lib;
   }

   public String getName() {
      return this.getLibraryData().getName();
   }

   public String getSpecVersion() {
      DeweyDecimal var1 = this.getLibraryData().getSpecificationVersion();
      return var1 == null ? null : var1.toString();
   }

   public String getImplVersion() {
      return this.getLibraryData().getImplementationVersion();
   }

   public File getLocation() {
      return this.getLibraryData().getLocation();
   }

   public void mark() {
      this.mark = true;
   }

   public boolean isMarked() {
      return this.mark;
   }

   public void setKey(Object var1) {
      this.key = var1;
   }

   public Object getKey() {
      return this.key;
   }
}
