package weblogic.jndi;

import java.io.Serializable;

public class Alias implements Serializable {
   private String realName;

   public Alias(String var1) {
      this.realName = var1;
   }

   public String getRealName() {
      return this.realName;
   }
}
