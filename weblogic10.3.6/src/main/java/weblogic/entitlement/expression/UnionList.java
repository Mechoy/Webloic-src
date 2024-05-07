package weblogic.entitlement.expression;

import weblogic.entitlement.engine.ESubject;
import weblogic.entitlement.engine.PredicateRegistry;
import weblogic.entitlement.engine.ResourceNode;
import weblogic.security.service.ContextHandler;

public abstract class UnionList extends Function {
   public UnionList(EExprRep[] var1) {
      super(var1);
   }

   public boolean evaluate(ESubject var1, ResourceNode var2, ContextHandler var3, PredicateRegistry var4) {
      int var5 = 0;

      for(int var6 = this.mArgs.length; var5 < var6; ++var5) {
         if (this.mArgs[var5].evaluate(var1, var2, var3, var4)) {
            return true;
         }
      }

      return false;
   }

   protected void writeExternalForm(StringBuffer var1) {
      if (this.Enclosed) {
         var1.append('{');
      }

      var1.append(this.getListTag());
      var1.append('(');
      if (this.mArgs.length > 0) {
         for(int var2 = 0; var2 < this.mArgs.length; ++var2) {
            if (var2 > 0) {
               var1.append(',');
            }

            ((Identifier)this.mArgs[var2]).writeExternalIdentifier(var1);
         }
      }

      var1.append(')');
      if (this.Enclosed) {
         var1.append('}');
      }

   }

   public abstract String getListTag();
}
