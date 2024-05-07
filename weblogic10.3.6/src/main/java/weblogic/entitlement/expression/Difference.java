package weblogic.entitlement.expression;

import weblogic.entitlement.engine.ESubject;
import weblogic.entitlement.engine.PredicateRegistry;
import weblogic.entitlement.engine.ResourceNode;
import weblogic.security.service.ContextHandler;

public final class Difference extends Function {
   public Difference(EExprRep var1, EExprRep var2) {
      super(var1, var2);
   }

   public Difference(EExprRep[] var1) {
      super(var1);
      if (var1.length < 2) {
         throw new IllegalArgumentException("Difference: at least two arguments are expected");
      }
   }

   public boolean evaluate(ESubject var1, ResourceNode var2, ContextHandler var3, PredicateRegistry var4) {
      if (!this.mArgs[0].evaluate(var1, var2, var3, var4)) {
         return false;
      } else {
         int var5 = 1;

         for(int var6 = this.mArgs.length; var5 < var6; ++var5) {
            if (this.mArgs[var5].evaluate(var1, var2, var3, var4)) {
               return false;
            }
         }

         return true;
      }
   }

   char getTypeId() {
      return '-';
   }
}
