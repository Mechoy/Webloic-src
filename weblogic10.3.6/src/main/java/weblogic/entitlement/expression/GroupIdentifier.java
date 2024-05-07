package weblogic.entitlement.expression;

import weblogic.entitlement.engine.ESubject;
import weblogic.entitlement.engine.PredicateRegistry;
import weblogic.entitlement.engine.ResourceNode;
import weblogic.security.service.ContextHandler;

public final class GroupIdentifier extends Identifier {
   public GroupIdentifier(String var1) {
      super(var1);
   }

   public final boolean evaluate(ESubject var1, ResourceNode var2, ContextHandler var3, PredicateRegistry var4) {
      return var1.isMemberOf((String)this.getId());
   }

   char getTypeId() {
      return 'g';
   }

   public String getIdTag() {
      return "Grp";
   }
}
