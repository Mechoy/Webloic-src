package weblogic.rmi.cluster;

import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import weblogic.ejb.spi.BaseEJBHomeIntf;
import weblogic.protocol.LocalServerIdentity;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.internal.activation.ActivatableServerRef;

public final class ClusterActivatableServerRef extends ActivatableServerRef {
   private final String jndiName;

   public ClusterActivatableServerRef(Object var1, Activator var2) throws RemoteException {
      super(var1.getClass(), var2);
      EJBHome var3 = ((EJBObject)var1).getEJBHome();
      this.jndiName = ((BaseEJBHomeIntf)var3).getJNDINameAsString();
   }

   public RemoteReference getActivatableRef(Object var1) {
      return new ClusterActivatableRemoteRef(this.getObjectID(), LocalServerIdentity.getIdentity(), var1, this.jndiName);
   }
}
