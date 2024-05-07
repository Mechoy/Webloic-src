package weblogic.ejb20.swap;

import java.io.Serializable;

public final class EJBReplacer {
   public static class EnvironmentContextReplacement implements Serializable {
      private static final long serialVersionUID = -2851211579184774248L;
      private String name;

      public EnvironmentContextReplacement(String var1) {
         this.name = var1;
      }

      public String getName() {
         return this.name;
      }
   }

   public static class UserTransactionReplacement implements Serializable {
      private static final long serialVersionUID = 2950221351886689885L;
   }

   public static class EJBContextReplacement implements Serializable {
      private static final long serialVersionUID = -4694003232181369460L;
   }
}
