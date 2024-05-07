package weblogic.security.jacc;

import javax.security.jacc.PolicyContextException;
import javax.security.jacc.PolicyContextHandler;

public class DelegatingPolicyContextHandler implements PolicyContextHandler {
   private String[] keys;

   public DelegatingPolicyContextHandler(String[] var1) {
      this.keys = var1;
   }

   public Object getContext(String var1, Object var2) throws PolicyContextException {
      if (var2 != null && var2 instanceof PolicyContextHandlerData) {
         PolicyContextHandlerData var3 = (PolicyContextHandlerData)var2;
         return var3.getContext(var1);
      } else {
         return null;
      }
   }

   public String[] getKeys() throws PolicyContextException {
      return this.keys;
   }

   public boolean supports(String var1) throws PolicyContextException {
      if (var1 != null && this.keys != null) {
         for(int var2 = 0; var2 < this.keys.length; ++var2) {
            if (var1.equals(this.keys[var2])) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
