package weblogic.management.mbeanservers.domainruntime.internal;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Hashtable;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.commo.StandardInterface;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.internal.WLSObjectNameManager;

public class FederatedObjectNameManager extends WLSObjectNameManager {
   MBeanServerConnectionManager connectionManager;
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXDomain");

   public boolean isFiltered(Object var1) {
      return var1 instanceof StandardInterface ? true : super.isFiltered(var1);
   }

   public FederatedObjectNameManager(MBeanServerConnectionManager var1, String var2) {
      super(var2);
      this.connectionManager = var1;
      this.setAddDomainToReadOnly(true);
      this.connectionManager.addCallback(new MBeanServerConnectionManager.MBeanServerConnectionListener() {
         public void connect(String var1, MBeanServerConnection var2) {
         }

         public void disconnect(String var1) {
            Collection var2 = FederatedObjectNameManager.this.getAllObjectNames();
            ObjectName[] var3 = (ObjectName[])((ObjectName[])var2.toArray(new ObjectName[var2.size()]));

            for(int var4 = 0; var4 < var3.length; ++var4) {
               ObjectName var5 = var3[var4];
               String var6 = var5.getKeyProperty("Location");
               if (var6 != null && var6.equals(var1)) {
                  FederatedObjectNameManager.this.unregisterObject(var5);
               }
            }

         }
      });
   }

   public ObjectName buildObjectName(Object var1) {
      ObjectName var2;
      if (Proxy.isProxyClass(var1.getClass())) {
         MBeanServerInvocationHandler var3 = (MBeanServerInvocationHandler)Proxy.getInvocationHandler(var1);
         String var4 = this.connectionManager.lookupServerName(var3._getConnection());
         if (var4 == null) {
            return null;
         }

         ObjectName var5 = var3._getObjectName();
         var2 = this.addLocation(var5, var4);
      } else {
         var2 = super.buildObjectName(var1);
      }

      return var2;
   }

   public ObjectName lookupObjectName(Object var1) {
      if (Proxy.isProxyClass(var1.getClass())) {
         MBeanServerInvocationHandler var2 = (MBeanServerInvocationHandler)Proxy.getInvocationHandler(var1);
         return var2._getObjectName();
      } else {
         return super.lookupObjectName(var1);
      }
   }

   public ObjectName lookupRegisteredObjectName(Object var1) {
      if (Proxy.isProxyClass(var1.getClass())) {
         MBeanServerInvocationHandler var2 = (MBeanServerInvocationHandler)Proxy.getInvocationHandler(var1);
         return var2._getObjectName();
      } else {
         return super.lookupRegisteredObjectName(var1);
      }
   }

   public Object lookupObject(ObjectName var1) {
      Object var2 = super.lookupObject(var1);
      if (var2 == null) {
         String var3 = var1.getKeyProperty("Location");
         if (var3 != null && !var3.equals(this.getDomainName())) {
            MBeanServerConnection var4 = this.connectionManager.lookupMBeanServerConnection(var3);
            Hashtable var5 = var1.getKeyPropertyList();
            var5.remove("Location");

            ObjectName var6;
            try {
               var6 = new ObjectName(var1.getDomain(), var5);
            } catch (MalformedObjectNameException var9) {
               throw new Error(var9);
            }

            try {
               var2 = MBeanServerInvocationHandler.newProxyInstance(var4, var6);
            } catch (Throwable var8) {
               throw new Error(var8);
            }

            if (debug.isDebugEnabled()) {
               debug.debug("Registering on lookupObject for " + var1 + " to " + var6 + " of " + var2.getClass().getName());
            }

            this.registerObject(var1, var2);
         }
      } else if (debug.isDebugEnabled()) {
         debug.debug("lookupObject: returning a " + var2.getClass() + " for " + var1);
      }

      return var2;
   }

   private ObjectName addLocation(ObjectName var1, String var2) {
      Hashtable var3 = var1.getKeyPropertyList();
      var3.put("Location", var2);

      try {
         return new ObjectName(var1.getDomain(), var3);
      } catch (MalformedObjectNameException var5) {
         throw new AssertionError(var5);
      }
   }
}
