package weblogic.entitlement.expression;

import weblogic.entitlement.engine.ESubject;
import weblogic.entitlement.engine.PredicateRegistry;
import weblogic.entitlement.engine.ResourceNode;
import weblogic.security.service.ContextHandler;

public final class Inverse extends Function {
   public Inverse(EExprRep var1) {
      super(var1);
      if (var1 == null) {
         throw new IllegalArgumentException("Inverse: non-null argument is expected");
      }
   }

   public boolean evaluate(ESubject var1, ResourceNode var2, ContextHandler var3, PredicateRegistry var4) {
      return !this.mArgs[0].evaluate(var1, var2, var3, var4);
   }

   char getTypeId() {
      return '~';
   }

   protected void writeExternalForm(StringBuffer var1) {
      if (this.Enclosed) {
         var1.append('{');
      }

      var1.append(this.getTypeId());
      this.mArgs[0].writeExternalForm(var1);
      if (this.Enclosed) {
         var1.append('}');
      }

   }
}
