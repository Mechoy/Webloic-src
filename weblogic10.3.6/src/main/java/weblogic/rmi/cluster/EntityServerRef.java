package weblogic.rmi.cluster;

import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import weblogic.ejb.spi.BaseEJBHomeIntf;
import weblogic.protocol.LocalServerIdentity;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.rmi.internal.OIDManager;

public final class EntityServerRef extends BasicServerRef {
   private static final long serialVersionUID = -5572391640574243603L;
   private static final boolean DEBUG = false;
   private final Object pk;
   private final String jndiName;
   private final EJBHome home;

   public EntityServerRef(Object var1) throws RemoteException {
      this(OIDManager.getInstance().getNextObjectID(), var1);
   }

   public EntityServerRef(int var1, Object var2) throws RemoteException {
      super(var1, var2);
      this.home = ((EJBObject)var2).getEJBHome();
      this.pk = ((EJBObject)var2).getPrimaryKey();
      this.jndiName = ((BaseEJBHomeIntf)this.home).getJNDINameAsString();
   }

   public final RemoteReference getRemoteRef() {
      return new EntityRemoteRef(this.getObjectID(), LocalServerIdentity.getIdentity(), this.jndiName, this.pk);
   }

   final String getJNDIName() {
      return this.jndiName;
   }

   final Object getPK() {
      return this.pk;
   }
}
