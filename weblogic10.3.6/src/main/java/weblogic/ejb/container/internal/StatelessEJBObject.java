package weblogic.ejb.container.internal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb20.internal.HandleImpl;
import weblogic.logging.Loggable;
import weblogic.rmi.extensions.NotificationListener;

public abstract class StatelessEJBObject extends StatelessRemoteObject implements Remote, NotificationListener, EJBObject {
   protected final Object getPrimaryKeyObject() throws RemoteException {
      Loggable var1 = EJBLogger.logsessionBeanCannotCallGetPrimaryKeyLoggable();
      throw new RemoteException(var1.getMessage());
   }

   protected Handle getHandleObject() throws RemoteException {
      if (debugLogger.isDebugEnabled()) {
         debug("Getting handle in eo:" + this);
      }

      return new HandleImpl(this);
   }

   public void remove(MethodDescriptor var1) throws RemoteException, RemoveException {
      this.checkMethodPermissions(var1, EJBContextHandler.EMPTY);
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatelessEJBObject] " + var0);
   }
}
