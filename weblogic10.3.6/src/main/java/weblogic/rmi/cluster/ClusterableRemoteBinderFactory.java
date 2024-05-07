package weblogic.rmi.cluster;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.StateFactory;
import weblogic.iiop.IIOPReplacer;
import weblogic.jndi.internal.WLNamingManager;
import weblogic.rmi.extensions.server.RemoteWrapper;
import weblogic.utils.AssertionError;

public final class ClusterableRemoteBinderFactory implements StateFactory {
   public Object getStateToBind(Object var1, Name var2, Context var3, Hashtable var4) throws NamingException {
      ClusterableRemoteObject var5 = null;
      if (var1 instanceof ClusterableRemoteObject) {
         return var1;
      } else {
         try {
            if (ClusterableRemoteObject.isIDLObject(var1)) {
               var1 = IIOPReplacer.getRemoteIDLStub(var1);
            }

            if (ClusterableRemoteObject.isClusterable(var1)) {
               if (var1 instanceof Remote) {
                  var5 = new ClusterableRemoteObject((Remote)var1);
               } else if (var1 instanceof RemoteWrapper) {
                  var5 = new ClusterableRemoteObject((RemoteWrapper)var1);
               }
            }

            return var5;
         } catch (RemoteException var8) {
            ConfigurationException var7 = new ConfigurationException("Failed to bind clusterable object: " + var1);
            var7.setRootCause(var8);
            throw var7;
         }
      }
   }

   public static void initialize() {
      try {
         WLNamingManager.addStateFactory(new ClusterableRemoteBinderFactory());
      } catch (NamingException var1) {
         throw new AssertionError("impossible exception", var1);
      }
   }
}
