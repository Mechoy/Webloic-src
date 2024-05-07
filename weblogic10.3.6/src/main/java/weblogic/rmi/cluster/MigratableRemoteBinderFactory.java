package weblogic.rmi.cluster;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.StateFactory;
import weblogic.jndi.internal.WLNamingManager;
import weblogic.utils.AssertionError;

public final class MigratableRemoteBinderFactory implements StateFactory {
   public Object getStateToBind(Object var1, Name var2, Context var3, Hashtable var4) throws NamingException {
      MigratableRemoteObject var5 = null;
      if (MigratableRemoteObject.isEOS(var1)) {
         try {
            if (var1 instanceof Remote) {
               var5 = new MigratableRemoteObject((Remote)var1);
            }
         } catch (RemoteException var8) {
            ConfigurationException var7 = new ConfigurationException("Failed to bind clusterable object");
            var7.setRootCause(var8);
            throw var7;
         }
      }

      return var5;
   }

   public static void initialize() {
      try {
         WLNamingManager.addStateFactory(new MigratableRemoteBinderFactory());
      } catch (NamingException var1) {
         throw new AssertionError("impossible exception", var1);
      }
   }
}
