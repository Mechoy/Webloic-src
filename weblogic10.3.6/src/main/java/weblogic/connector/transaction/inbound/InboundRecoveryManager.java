package weblogic.connector.transaction.inbound;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.transaction.SystemException;
import javax.transaction.xa.XAResource;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.common.Utils;
import weblogic.connector.exception.RAInboundException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.internal.IgnoreXAResource;

public class InboundRecoveryManager {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private HashMap<ActivationSpec, String> activationSpec2ResName = new HashMap();

   public synchronized void onActivateEndpoint(RAInstanceManager var1, ActivationSpec var2, String var3) throws SystemException {
      XAResource[] var4 = null;
      ClassLoader var5 = Thread.currentThread().getContextClassLoader();

      try {
         Thread.currentThread().setContextClassLoader(var1.getClassloader());
         Debug.xaIn("set context classloader to adapter's CL: " + var1.getClassloader());
         var4 = var1.getAdapterLayer().getXAResources(var1.getResourceAdapter(), new ActivationSpec[]{var2}, kernelId);
      } catch (ResourceException var11) {
         Debug.xaIn("Unable to get XAResource from adapter due to exception " + var11 + "; recovery is disabled for endpoint " + var3);
      } finally {
         Debug.xaIn("restore context classloader to original CL: " + var5);
         Thread.currentThread().setContextClassLoader(var5);
      }

      if (var4 != null && var4.length == 1 && var4[0] != null) {
         XAResource var6 = var4[0];
         if (var6 instanceof IgnoreXAResource) {
            Debug.xaIn("Adatper returns an IgnoreXAResource; skip recovery for endpoint " + var3);
         } else {
            String var7 = var1.getJndiName() + "_" + var3;
            Debug.xaIn("Register for recovery: resource name: " + var7 + "; XAResource: " + var6 + "; activationSpec: " + var2);
            Hashtable var8 = new Hashtable();
            var8.put("weblogic.transaction.registration.type", "dynamic");
            var8.put("weblogic.transaction.registration.settransactiontimeout", "true");
            ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).registerResource(var7, var6, var8);
            this.activationSpec2ResName.put(var2, var7);
         }
      } else {
         Debug.xaIn("Adatper doesn't support recovery for endpoint " + var3);
      }

   }

   public synchronized void onDeActivateEndpoint(ActivationSpec var1) throws SystemException {
      String var2 = (String)this.activationSpec2ResName.remove(var1);
      this.unregister(var1, var2);
   }

   public synchronized void onRAStop(RAInboundException var1) {
      Iterator var2 = this.activationSpec2ResName.keySet().iterator();

      while(var2.hasNext()) {
         ActivationSpec var3 = (ActivationSpec)var2.next();

         try {
            String var4 = (String)this.activationSpec2ResName.get(var3);
            this.unregister(var3, var4);
         } catch (Exception var5) {
            Utils.consolidateException(var1, var5);
         }
      }

      this.activationSpec2ResName.clear();
   }

   private void unregister(ActivationSpec var1, String var2) throws SystemException {
      if (var2 != null) {
         Debug.xaIn("Unregister: resource name: " + var2 + "; activationSpec: " + var1);
         ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).unregisterResource(var2);
      }

   }
}
