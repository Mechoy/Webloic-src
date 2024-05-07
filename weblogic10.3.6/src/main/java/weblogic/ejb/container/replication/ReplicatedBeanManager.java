package weblogic.ejb.container.replication;

import java.io.Serializable;
import java.rmi.Remote;

public interface ReplicatedBeanManager {
   void becomePrimary(Object var1);

   Remote createSecondary(Object var1);

   Remote createSecondaryForBI(Object var1, Class var2);

   void removeSecondary(Object var1);

   void updateSecondary(Object var1, Serializable var2);
}
