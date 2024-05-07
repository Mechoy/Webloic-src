package weblogic.entitlement.expression;

import weblogic.entitlement.engine.ESubject;
import weblogic.entitlement.engine.PredicateRegistry;
import weblogic.entitlement.engine.ResourceNode;
import weblogic.security.service.ContextHandler;

public final class UserIdentifier extends Identifier {
   public UserIdentifier(String var1) {
      super(var1);
   }

   public boolean evaluate(ESubject var1, ResourceNode var2, ContextHandler var3, PredicateRegistry var4) {
      return var1.isUser((String)this.getId());
   }

   char getTypeId() {
      return 'u';
   }

   public String getIdTag() {
      return "Usr";
   }
}
