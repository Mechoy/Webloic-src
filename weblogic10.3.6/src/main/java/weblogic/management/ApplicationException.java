package weblogic.management;

import java.util.Enumeration;
import java.util.Hashtable;
import weblogic.utils.NestedException;

public class ApplicationException extends NestedException {
   private static final long serialVersionUID = -2538796139370040809L;
   private Hashtable moduleErrors = new Hashtable();
   private Hashtable targetExs = null;
   private String appMessage = "None";

   public ApplicationException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ApplicationException(Exception var1) {
      super(var1);
   }

   public ApplicationException(String var1) {
      super(var1);
      this.appMessage = var1;
   }

   public ApplicationException(Hashtable var1, String var2) {
      super(var2);
      this.moduleErrors = var1;
   }

   public String getApplicationMessage() {
      return this.appMessage;
   }

   public void addError(String var1, String var2) {
      this.moduleErrors.put(var1, var2);
   }

   public Hashtable getModuleErrors() {
      return this.moduleErrors;
   }

   public Exception getTargetException(String var1) {
      return this.targetExs == null ? null : (Exception)this.targetExs.get(var1);
   }

   public void setTargetException(String var1, Exception var2) {
      if (this.targetExs == null) {
         this.targetExs = new Hashtable();
      }

      this.targetExs.put(var1, var2);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("");
      Hashtable var2 = this.getModuleErrors();
      if (!var2.isEmpty()) {
         var1.append("\n{");
         Enumeration var3 = var2.keys();

         while(var3.hasMoreElements()) {
            Object var4 = var3.nextElement();
            Object var5 = var2.get(var4);
            var1.append("\nModule Name: ");
            var1.append(var4);
            var1.append(", Error: ");
            var1.append(var5);
            Exception var6 = this.getTargetException((String)var4);
            if (var6 != null) {
               var1.append("\n TargetException: " + var6);
            }
         }

         var1.append("\n}");
      }

      return super.toString() + var1;
   }
}
