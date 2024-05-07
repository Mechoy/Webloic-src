package weblogic.management;

import java.io.Serializable;

public final class PrincipalInfo implements Serializable {
   private static final long serialVersionUID = 7266902478036561415L;
   private String name;
   private boolean is_group;

   public PrincipalInfo(String var1, boolean var2) {
      this.name = var1;
      this.is_group = var2;
   }

   public String getName() {
      return this.name;
   }

   public boolean isGroup() {
      return this.is_group;
   }
}
