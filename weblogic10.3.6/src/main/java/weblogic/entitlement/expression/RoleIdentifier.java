package weblogic.entitlement.expression;

import weblogic.entitlement.engine.ESubject;
import weblogic.entitlement.engine.PredicateRegistry;
import weblogic.entitlement.engine.ResourceNode;
import weblogic.security.service.ContextHandler;

public class RoleIdentifier extends Identifier {
   public RoleIdentifier(String var1) {
      super(var1);
   }

   public boolean evaluate(ESubject var1, ResourceNode var2, ContextHandler var3, PredicateRegistry var4) {
      return var1.isInRole((String)this.getId());
   }

   protected int getDependsOnInternal() {
      return 2;
   }

   char getTypeId() {
      return 'r';
   }

   public String getIdTag() {
      return "Rol";
   }
}
