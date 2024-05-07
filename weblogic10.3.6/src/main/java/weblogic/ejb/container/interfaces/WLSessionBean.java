package weblogic.ejb.container.interfaces;

import java.util.Set;

public interface WLSessionBean extends WLEnterpriseBean {
   boolean __WL_needsSessionSynchronization();

   void __WL_setNeedsSessionSynchronization(boolean var1);

   Set getExtendedPersistenceContexts();

   void closeExtendedPersistenceContexts();
}
