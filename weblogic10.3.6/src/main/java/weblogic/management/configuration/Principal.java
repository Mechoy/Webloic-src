package weblogic.management.configuration;

import java.io.Serializable;

/** @deprecated */
public class Principal implements Serializable {
   private String name;

   protected Principal(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }
}
