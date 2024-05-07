package weblogic.t3.srvr;

import java.util.Hashtable;
import weblogic.common.T3ServicesDef;

public class ExecutionContext extends Hashtable {
   private static final String name_separator = "|";
   private static final String primordial_name = "#";
   private static long counter = 1L;
   private static ExecutionContext primordial = new ExecutionContext();
   private String ID;

   public ExecutionContext(String var1, ExecutionContext var2) {
      this.ID = null;
      this.ID = var2.getID() + "|" + var1;
   }

   public ExecutionContext(String var1) {
      this(var1, primordial);
   }

   private ExecutionContext() {
      this.ID = null;
      this.ID = "#";
   }

   public static boolean isWSID(String var0) {
      return var0 == null ? false : var0.startsWith("#");
   }

   public String getID() {
      return this.ID;
   }

   public T3ServicesDef getServices() {
      return T3Srvr.getT3Srvr().getT3Services();
   }

   public String private_putUnique(Object var1) {
      long var2;
      synchronized(this.getClass()) {
         var2 = (long)(counter++);
      }

      String var4 = var1.getClass().getName() + "-" + var2;
      this.put(var4, var1);
      return var4;
   }

   public String toString() {
      return "[ExecutionContext: " + this.getID() + "]";
   }
}
