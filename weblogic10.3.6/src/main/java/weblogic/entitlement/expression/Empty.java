package weblogic.entitlement.expression;

import weblogic.entitlement.engine.ESubject;
import weblogic.entitlement.engine.PredicateRegistry;
import weblogic.entitlement.engine.ResourceNode;
import weblogic.security.service.ContextHandler;

public final class Empty extends EExprRep {
   public static final Empty EMPTY = new Empty();

   protected int getDependsOnInternal() {
      return DEPENDS_ON_UNKNOWN;
   }

   public boolean evaluate(ESubject var1, ResourceNode var2, ContextHandler var3, PredicateRegistry var4) {
      return false;
   }

   char getTypeId() {
      return 'e';
   }

   void outForPersist(StringBuffer var1) {
      var1.append(this.getTypeId());
   }

   protected void writeExternalForm(StringBuffer var1) {
      var1.append('{');
      var1.append('}');
   }
}
