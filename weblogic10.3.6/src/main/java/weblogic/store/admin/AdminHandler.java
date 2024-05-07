package weblogic.store.admin;

import java.util.HashMap;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.utils.GenericAdminHandler;
import weblogic.management.utils.GenericBeanListener;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.store.StoreLogger;
import weblogic.store.common.StoreDebug;
import weblogic.store.xa.PersistentStoreXA;

public abstract class AdminHandler implements GenericAdminHandler {
   public static final String DEFAULT_STORE_NAME_PREFIX = "_WLS_";
   protected PersistentStoreXA store;
   protected HashMap config;
   protected String name;
   protected String logicalName;
   protected String overrideResourceName;
   protected MigratableTargetMBean migratableTarget;
   protected boolean handlerOpened;
   protected boolean storeOpened;
   protected boolean defaultStore;
   private GenericBeanListener listener;
   private static final HashMap changeableAttributes = new HashMap();

   public void prepare(DeploymentMBean var1) throws DeploymentException {
      this.name = var1.getName();
      this.defaultStore = false;
      PersistentStoreMBean var2 = (PersistentStoreMBean)var1;
      this.logicalName = var2.getLogicalName();
      this.overrideResourceName = var2.getXAResourceName();
      if (isEmptyString(this.overrideResourceName)) {
         this.overrideResourceName = null;
      }

      this.listener = new GenericBeanListener(var1, this, changeableAttributes);
      if (this.name.startsWith("_WLS_")) {
         throw new DeploymentException("Invalid store name: " + this.name);
      } else {
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Preparing PersistentStore \"" + this.name + "\" from MBean \"" + var1 + "\"");
         }

         if (PersistentStoreManager.getManager().storeExists(this.name)) {
            throw new DeploymentException("The persistent store " + this.name + " already exists");
         } else {
            TargetMBean[] var3 = var1.getTargets();
            if (var3 != null) {
               if (var3.length != 1) {
                  throw new DeploymentException("The store may only have one target");
               }

               if (var3[0] instanceof ClusterMBean) {
                  throw new DeploymentException("Store may not be targetted to the cluster");
               }
            }

         }
      }
   }

   String getName() {
      return this.name;
   }

   public PersistentStoreXA getStore() {
      return this.store;
   }

   public void activate(DeploymentMBean var1) throws DeploymentException {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Opening the persistent store " + this.name);
      }

      try {
         this.store.open(this.config);
      } catch (PersistentStoreException var3) {
         StoreLogger.logStoreDeploymentFailed(this.name, var3.toString(), var3);
         throw new DeploymentException(var3);
      }

      PersistentStoreManager var2 = PersistentStoreManager.getManager();
      if (this.defaultStore) {
         if (var2.getDefaultStore() != null) {
            throw new AssertionError("Multiple default stores configured");
         }

         var2.setDefaultStore(this.store);
      } else {
         var2.addStore(this.name, this.store);
         if (this.logicalName != null) {
            var2.addStoreByLogicalName(this.logicalName, this.store);
         }
      }

   }

   public void deactivate(DeploymentMBean var1) throws UndeploymentException {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Closing the persistent store " + this.name);
      }

      PersistentStoreManager var2 = PersistentStoreManager.getManager();
      if (this.defaultStore) {
         var2.setDefaultStore((PersistentStore)null);
      } else {
         var2.removeStore(this.name);
         if (this.logicalName != null) {
            var2.removeStoreByLogicalName(this.logicalName);
         }
      }

      try {
         this.store.close();
      } catch (PersistentStoreException var4) {
         StoreLogger.logStoreShutdownFailed(this.name, var4.toString(), var4);
         throw new UndeploymentException(var4);
      }
   }

   public void unprepare(DeploymentMBean var1) {
      if (this.listener != null) {
         this.listener.close();
         this.listener = null;
      }

   }

   public void setLogicalName(String var1) {
      PersistentStoreManager var2 = PersistentStoreManager.getManager();
      if (this.logicalName != null) {
         var2.removeStoreByLogicalName(this.logicalName);
      }

      this.logicalName = var1;
      if (this.logicalName != null && this.store != null) {
         var2.addStoreByLogicalName(this.logicalName, this.store);
      }

   }

   protected static boolean isEmptyString(String var0) {
      return var0 == null || var0.length() == 0;
   }

   protected static boolean isEmptyBytes(byte[] var0) {
      return var0 == null || var0.length == 0;
   }

   static {
      changeableAttributes.put("LogicalName", String.class);
   }
}
