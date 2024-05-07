package weblogic.application.library;

import weblogic.management.runtime.RuntimeMBean;

public class LibraryReferencer {
   private RuntimeMBean runtime;
   private final String name;
   private final String error;

   public LibraryReferencer(String var1, RuntimeMBean var2, String var3) {
      this.name = var1;
      this.runtime = var2;
      this.error = var3;
   }

   public String getReferencerName() {
      return this.name;
   }

   public RuntimeMBean getReferencerRuntime() {
      return this.runtime;
   }

   public void setReferencerRuntime(RuntimeMBean var1) {
      this.runtime = var1;
   }

   public String getUnresolvedError() {
      return this.error;
   }
}
