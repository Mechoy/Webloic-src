package weblogic.connector.monitoring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RACollectionManager;
import weblogic.connector.common.RAInstanceManager;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ConnectorComponentRuntimeMBean;
import weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean;
import weblogic.management.runtime.ConnectorInboundRuntimeMBean;
import weblogic.management.runtime.ConnectorServiceRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.utils.ErrorCollectionException;

public final class ServiceRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ConnectorServiceRuntimeMBean {
   Set connRuntimes = new HashSet(10);

   public ServiceRuntimeMBeanImpl() throws ManagementException {
      super("ConnectorService");
   }

   private void suspendOrResume(int var1, int var2, Properties var3) throws ErrorCollectionException {
      Debug.service("About to suspend/resume all resource adapters");
      ErrorCollectionException var4 = new ErrorCollectionException();
      Iterator var5 = RACollectionManager.getRAs();
      boolean var6 = false;

      try {
         if (var5 != null) {
            while(var5.hasNext()) {
               try {
                  if (var1 == 1) {
                     ((RAInstanceManager)var5.next()).suspend(var2, var3);
                  } else if (var1 == 2) {
                     ((RAInstanceManager)var5.next()).resume(var2, var3);
                  }
               } catch (Throwable var12) {
                  var4.addError(var12);
                  var6 = true;
               }
            }
         }
      } finally {
         if (var6) {
            throw var4;
         }

      }

   }

   public void suspendAll(Properties var1) throws ErrorCollectionException {
      this.suspendOrResume(1, 7, var1);
   }

   public void resumeAll(Properties var1) throws ErrorCollectionException {
      this.suspendOrResume(2, 7, var1);
   }

   public void suspend(int var1, Properties var2) throws ErrorCollectionException {
      this.suspendOrResume(1, var1, var2);
   }

   public void resume(int var1, Properties var2) throws ErrorCollectionException {
      this.suspendOrResume(2, var1, var2);
   }

   public void suspendAll() throws ErrorCollectionException {
      this.suspendOrResume(1, 7, (Properties)null);
   }

   public void suspend(int var1) throws ErrorCollectionException {
      this.suspendOrResume(1, var1, (Properties)null);
   }

   public void resumeAll() throws ErrorCollectionException {
      this.suspendOrResume(2, 7, (Properties)null);
   }

   public void resume(int var1) throws ErrorCollectionException {
      this.suspendOrResume(2, var1, (Properties)null);
   }

   public int getActiveRACount() {
      int var1 = 0;
      Iterator var3 = this.connRuntimes.iterator();

      while(var3.hasNext()) {
         ConnectorComponentRuntimeMBean var2 = (ConnectorComponentRuntimeMBean)var3.next();
         if (var2.isActiveVersion()) {
            ++var1;
         }
      }

      return var1;
   }

   public int getRACount() {
      return this.connRuntimes.size();
   }

   public ConnectorComponentRuntimeMBean[] getRAs() {
      return (ConnectorComponentRuntimeMBean[])((ConnectorComponentRuntimeMBean[])this.connRuntimes.toArray(new ConnectorComponentRuntimeMBean[this.connRuntimes.size()]));
   }

   public ConnectorComponentRuntimeMBean[] getActiveRAs() {
      Vector var1 = new Vector(10);
      Iterator var3 = this.connRuntimes.iterator();

      while(var3.hasNext()) {
         ConnectorComponentRuntimeMBean var2 = (ConnectorComponentRuntimeMBean)var3.next();
         if (var2.isActiveVersion()) {
            var1.add(var2);
         }
      }

      return (ConnectorComponentRuntimeMBean[])((ConnectorComponentRuntimeMBean[])var1.toArray(new ConnectorComponentRuntimeMBean[var1.size()]));
   }

   public ConnectorComponentRuntimeMBean[] getInactiveRAs() {
      Vector var1 = new Vector(10);
      Iterator var3 = this.connRuntimes.iterator();

      while(var3.hasNext()) {
         ConnectorComponentRuntimeMBean var2 = (ConnectorComponentRuntimeMBean)var3.next();
         if (!var2.isActiveVersion()) {
            var1.add(var2);
         }
      }

      return (ConnectorComponentRuntimeMBean[])((ConnectorComponentRuntimeMBean[])var1.toArray(new ConnectorComponentRuntimeMBean[var1.size()]));
   }

   public ConnectorComponentRuntimeMBean getRA(String var1) {
      ConnectorComponentRuntimeMBean var2 = null;
      Iterator var3 = this.connRuntimes.iterator();
      if (var1 != null && var1.length() > 0) {
         while(var3.hasNext()) {
            var2 = (ConnectorComponentRuntimeMBean)var3.next();
            if (var1.equals(var2.getJndiName())) {
               break;
            }

            var2 = null;
         }
      }

      return var2;
   }

   /** @deprecated */
   public int getConnectionPoolCurrentCount() {
      int var1 = 0;
      ConnectorComponentRuntimeMBean var3 = null;
      Iterator var4 = this.connRuntimes.iterator();

      while(var4.hasNext()) {
         var3 = (ConnectorComponentRuntimeMBean)var4.next();
         if (var3.isActiveVersion()) {
            ConnectorConnectionPoolRuntimeMBean[] var2 = var3.getConnectionPools();
            if (var2 != null) {
               var1 += var2.length;
            }
         }
      }

      return var1;
   }

   /** @deprecated */
   public int getConnectionPoolsTotalCount() {
      return RACollectionManager.getConnectionPoolsTotalCount();
   }

   /** @deprecated */
   public ConnectorConnectionPoolRuntimeMBean[] getConnectionPools() {
      ConnectorComponentRuntimeMBean var2 = null;
      Iterator var3 = this.connRuntimes.iterator();
      Vector var4 = new Vector(10);

      while(true) {
         ConnectorConnectionPoolRuntimeMBean[] var1;
         do {
            do {
               if (!var3.hasNext()) {
                  return (ConnectorConnectionPoolRuntimeMBean[])((ConnectorConnectionPoolRuntimeMBean[])var4.toArray(new ConnectorConnectionPoolRuntimeMBean[var4.size()]));
               }

               var2 = (ConnectorComponentRuntimeMBean)var3.next();
            } while(!var2.isActiveVersion());

            var1 = var2.getConnectionPools();
         } while(var1 == null);

         for(int var5 = 0; var5 < var1.length; ++var5) {
            var4.add(var1[var5]);
         }
      }
   }

   public ConnectorConnectionPoolRuntimeMBean getConnectionPool(String var1) {
      Debug.enter(this, "getConnectionPool( " + var1 + " )");
      ConnectorConnectionPoolRuntimeMBean var2 = null;

      ConnectorConnectionPoolRuntimeMBean var13;
      try {
         ConnectorConnectionPoolRuntimeMBean[] var3 = null;
         ConnectorComponentRuntimeMBean var4 = null;
         Iterator var5 = null;
         boolean var6 = false;
         if (var1 != null && var1.length() > 0) {
            var5 = this.connRuntimes.iterator();

            label134:
            while(true) {
               do {
                  while(true) {
                     if (!var5.hasNext() || var6) {
                        break label134;
                     }

                     var4 = (ConnectorComponentRuntimeMBean)var5.next();
                     if (var4.isActiveVersion()) {
                        var3 = var4.getConnectionPools();
                        break;
                     }

                     if (Debug.getVerbose(this)) {
                        Debug.println(this, "getConnectionPool() found inactive conn component, name = " + var4.getName());
                     }
                  }
               } while(var3 == null);

               String var7 = null;

               for(int var8 = 0; var8 < var3.length && !var6; ++var8) {
                  var7 = var3[var8].getKey();
                  Debug.println(this, ".getConnectionPool() pool[" + var8 + "] = " + var7);
                  if (var1.equals(var7)) {
                     Debug.println(this, ".getConnectionPool() Found pool");
                     var2 = var3[var8];
                     var6 = true;
                  }
               }
            }
         }

         var13 = var2;
      } finally {
         if (Debug.getVerbose(this)) {
            Debug.exit(this, "getConnectionPool( " + var1 + " ) returned " + var2);
         }

      }

      return var13;
   }

   public ConnectorInboundRuntimeMBean[] getInboundConnections(String var1) {
      ConnectorInboundRuntimeMBean[] var2 = null;
      ConnectorComponentRuntimeMBean var3 = null;
      Iterator var4 = this.connRuntimes.iterator();
      boolean var5 = false;
      Vector var6 = new Vector(10);
      if (var1 != null && var1.length() > 0) {
         while(true) {
            do {
               do {
                  if (!var4.hasNext() || var5) {
                     return (ConnectorInboundRuntimeMBean[])((ConnectorInboundRuntimeMBean[])var6.toArray(new ConnectorInboundRuntimeMBean[var6.size()]));
                  }

                  var3 = (ConnectorComponentRuntimeMBean)var4.next();
               } while(!var3.isActiveVersion());

               var2 = var3.getInboundConnections();
            } while(var2 == null);

            for(int var7 = 0; var7 < var2.length; ++var7) {
               if (var1.equals(var2[var7].getMsgListenerType())) {
                  var6.add(var2[var7]);
               }
            }
         }
      } else {
         return (ConnectorInboundRuntimeMBean[])((ConnectorInboundRuntimeMBean[])var6.toArray(new ConnectorInboundRuntimeMBean[var6.size()]));
      }
   }

   public boolean addConnectorRuntime(ConnectorComponentRuntimeMBean var1) {
      return this.connRuntimes.add(var1);
   }

   public boolean removeConnectorRuntime(ConnectorComponentRuntimeMBean var1) {
      return this.connRuntimes.remove(var1);
   }
}
