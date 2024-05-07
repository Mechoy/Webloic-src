package weblogic.store.admin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.FileStoreMBean;
import weblogic.management.configuration.JDBCStoreMBean;
import weblogic.management.configuration.JMSFileStoreMBean;
import weblogic.management.configuration.JMSJDBCStoreMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSStoreMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.mbeans.custom.JMSFileStore;
import weblogic.management.mbeans.custom.JMSJDBCStore;
import weblogic.management.provider.AccessCallback;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.store.common.StoreDebug;
import weblogic.utils.ArrayUtils;

public class StoreCompatibilityUpgrader implements ConfigurationProcessor, PropertyChangeListener, ArrayUtils.DiffHandler, AccessCallback {
   private DomainMBean domainBean;

   public void shutdown() {
      this.domainBean.removePropertyChangeListener(this);
   }

   public void accessed(DomainMBean var1) {
      this.domainBean = var1;
      this.updateConfiguration(var1);
      var1.addPropertyChangeListener(this);
      JMSServerMBean[] var2 = var1.getJMSServers();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         var2[var3].addPropertyChangeListener(new ServerListener(var2[var3]));
      }

   }

   public void updateConfiguration(DomainMBean var1) {
      FileStoreMBean[] var3 = var1.getFileStores();

      int var2;
      for(var2 = 0; var3 != null && var2 < var3.length; ++var2) {
         createJMSFileStoreDelegate(var1, var3[var2]);
      }

      JDBCStoreMBean[] var4 = var1.getJDBCStores();

      for(var2 = 0; var4 != null && var2 < var4.length; ++var2) {
         createJMSJDBCStoreDelegate(var1, var4[var2]);
      }

      JMSServerMBean[] var5 = var1.getJMSServers();

      for(var2 = 0; var5 != null && var2 < var5.length; ++var2) {
         setPersistentStoreAttribute(var1, var5[var2], var5[var2].getPersistentStore());
         setPagingDirectoryAttribute(var1, var5[var2], var5[var2].getPagingDirectory());
      }

   }

   private static void createJMSFileStoreDelegate(DomainMBean var0, FileStoreMBean var1) {
      if (var0.lookupJMSFileStore(var1.getName()) == null) {
         JMSFileStoreMBean var2 = var0.createJMSFileStore(var1.getName());
         var2.setDelegatedBean(var1);
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Created a new JMSFileStoreMBean named " + var1.getName());
         }
      }

   }

   private static void createJMSJDBCStoreDelegate(DomainMBean var0, JDBCStoreMBean var1) {
      if (var0.lookupJMSJDBCStore(var1.getName()) == null) {
         JMSJDBCStoreMBean var2 = var0.createJMSJDBCStore(var1.getName());
         var2.setDelegatedBean(var1);
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Created a new JMSJDBCStoreMBean named " + var1.getName());
         }
      }

   }

   private static void createFileStoreDelegate(DomainMBean var0, JMSFileStoreMBean var1) {
      if (var0.lookupFileStore(var1.getName()) == null) {
         FileStoreMBean var2 = var0.createFileStore(var1.getName());
         JMSFileStore.copy(var1, var2);
         var1.setDelegatedBean(var2);
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Created a new FileStoreMBean named " + var1.getName());
         }
      }

   }

   private static void createJDBCStoreDelegate(DomainMBean var0, JMSJDBCStoreMBean var1) {
      if (var0.lookupJDBCStore(var1.getName()) == null) {
         JDBCStoreMBean var2 = var0.createJDBCStore(var1.getName());
         JMSJDBCStore.copy(var1, var2);
         var1.setDelegatedBean(var2);
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Created a new JDBCStoreMBean named " + var1.getName());
         }
      }

   }

   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      if (var2.equals("JDBCStores") || var2.equals("FileStores") || var2.equals("JMSJDBCStores") || var2.equals("JMSFileStores") || var2.equals("JMSServers")) {
         ArrayUtils.computeDiff((Object[])((Object[])var1.getOldValue()), (Object[])((Object[])var1.getNewValue()), this);
      }

   }

   public void addObject(Object var1) {
      if (var1 instanceof JMSFileStoreMBean) {
         createFileStoreDelegate(this.domainBean, (JMSFileStoreMBean)var1);
      } else if (var1 instanceof JMSJDBCStoreMBean) {
         createJDBCStoreDelegate(this.domainBean, (JMSJDBCStoreMBean)var1);
      } else if (var1 instanceof FileStoreMBean) {
         createJMSFileStoreDelegate(this.domainBean, (FileStoreMBean)var1);
      } else if (var1 instanceof JDBCStoreMBean) {
         createJMSJDBCStoreDelegate(this.domainBean, (JDBCStoreMBean)var1);
      } else if (var1 instanceof JMSServerMBean) {
         JMSServerMBean var2 = (JMSServerMBean)var1;
         var2.addPropertyChangeListener(new ServerListener(var2));
      }

   }

   public void removeObject(Object var1) {
      if (var1 instanceof JMSFileStoreMBean) {
         FileStoreMBean var2 = this.domainBean.lookupFileStore(((JMSFileStoreMBean)var1).getName());
         if (var2 != null) {
            this.domainBean.destroyFileStore(var2);
         }
      } else if (var1 instanceof JMSJDBCStoreMBean) {
         JDBCStoreMBean var3 = this.domainBean.lookupJDBCStore(((JMSJDBCStoreMBean)var1).getName());
         if (var3 != null) {
            this.domainBean.destroyJDBCStore(var3);
         }
      } else if (var1 instanceof FileStoreMBean) {
         JMSFileStoreMBean var4 = this.domainBean.lookupJMSFileStore(((FileStoreMBean)var1).getName());
         if (var4 != null) {
            this.domainBean.destroyJMSFileStore(var4);
         }
      } else if (var1 instanceof JDBCStoreMBean) {
         JMSJDBCStoreMBean var5 = this.domainBean.lookupJMSJDBCStore(((JDBCStoreMBean)var1).getName());
         if (var5 != null) {
            this.domainBean.destroyJMSJDBCStore(var5);
         }
      }

   }

   private static void updateStoreAndTargets(DomainMBean var0, JMSServerMBean var1, JMSStoreMBean var2, TargetMBean[] var3) {
      try {
         PersistentStoreMBean var4 = var1.getPersistentStore();
         if (var2 != null && !areTargetsNull(var3) && (var4 == null || !var4.getName().equals(var2.getName()))) {
            Object var5;
            if (var2 instanceof JMSFileStoreMBean) {
               var5 = var0.lookupFileStore(var2.getName());
            } else {
               var5 = var0.lookupJDBCStore(var2.getName());
            }

            if (var5 != null) {
               if (areTargetsNull(((PersistentStoreMBean)var5).getTargets())) {
                  ((PersistentStoreMBean)var5).setTargets(var1.getTargets());
               }

               var1.setPersistentStore((PersistentStoreMBean)var5);
               if (StoreDebug.storeAdmin.isDebugEnabled()) {
                  StoreDebug.storeAdmin.debug("JMSServerMBean " + var1.getName() + ": Set PersistentStore to " + ((PersistentStoreMBean)var5).getName());
               }
            }
         }
      } catch (InvalidAttributeValueException var6) {
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Error setting PersistentStore", var6);
         }
      } catch (DistributedManagementException var7) {
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Error setting PersistentStore", var7);
         }
      }

   }

   private static void setPersistentStoreAttribute(DomainMBean var0, JMSServerMBean var1, PersistentStoreMBean var2) {
      try {
         JMSStoreMBean var3 = var1.getStore();
         if (var2 == null && var3 != null) {
            var1.setStore((JMSStoreMBean)null);
            if (StoreDebug.storeAdmin.isDebugEnabled()) {
               StoreDebug.storeAdmin.debug("JMSServerMBean " + var1.getName() + ": Set Store to null");
            }
         } else if (var2 != null && (var3 == null || !var3.getName().equals(var2.getName()))) {
            Object var4;
            if (var2 instanceof FileStoreMBean) {
               var4 = var0.lookupJMSFileStore(var2.getName());
            } else {
               var4 = var0.lookupJMSJDBCStore(var2.getName());
            }

            if (var4 != null) {
               var1.setStore((JMSStoreMBean)var4);
               if (StoreDebug.storeAdmin.isDebugEnabled()) {
                  StoreDebug.storeAdmin.debug("JMSServerMBean " + var1.getName() + ": Set Store to " + ((JMSStoreMBean)var4).getName());
               }
            }
         }
      } catch (InvalidAttributeValueException var5) {
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Error setting Store", var5);
         }
      }

   }

   private static void setPagingDirectoryAttribute(DomainMBean var0, JMSServerMBean var1, String var2) {
      try {
         JMSStoreMBean var3 = var1.getPagingStore();
         if (isStringEmpty(var2) && var3 != null) {
            var1.setPagingStore((JMSStoreMBean)null);
            if (var3 instanceof JMSFileStoreMBean) {
               var0.destroyJMSFileStore((JMSFileStoreMBean)var3);
            }

            if (StoreDebug.storeAdmin.isDebugEnabled()) {
               StoreDebug.storeAdmin.debug("JMSServer " + var1.getName() + " set PagingDirectory to null and deleted paging store");
            }
         } else if (!isStringEmpty(var2) && var3 == null) {
            JMSFileStoreMBean var4 = var0.createJMSFileStore(var1.getName() + "PagingStore");
            var4.setSynchronousWritePolicy("Disabled");
            var4.setDelegatedJMSServer(var1);
            var1.setPagingStore(var4);
            if (StoreDebug.storeAdmin.isDebugEnabled()) {
               StoreDebug.storeAdmin.debug("JMSServer " + var1.getName() + " created a JMSFileStore and set it to the PagingStore attribute");
            }
         }
      } catch (InvalidAttributeValueException var5) {
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Error setting PagingStore attribute", var5);
         }
      } catch (DistributedManagementException var6) {
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Error setting PagingStore attribute", var6);
         }
      }

   }

   private static void setPagingStoreAttribute(DomainMBean var0, JMSServerMBean var1, JMSStoreMBean var2) {
      try {
         String var3 = var1.getPagingDirectory();
         if (var2 == null && !isStringEmpty(var3)) {
            var1.setPagingDirectory((String)null);
            if (StoreDebug.storeAdmin.isDebugEnabled()) {
               StoreDebug.storeAdmin.debug("JMSServer " + var1.getName() + " setting PagingDirectory to null");
            }
         } else if (var2 != null && var2 instanceof JMSFileStoreMBean && isStringEmpty(var3)) {
            JMSFileStoreMBean var4 = (JMSFileStoreMBean)var2;

            try {
               if (var4.getDirectory() != null) {
                  var1.setPagingDirectory(var4.getDirectory());
               }
            } catch (InvalidAttributeValueException var6) {
            }

            var4.setDelegatedJMSServer(var1);
            if (StoreDebug.storeAdmin.isDebugEnabled()) {
               StoreDebug.storeAdmin.debug("JMSServer " + var1.getName() + " setting PagingDirectory from PagingStore parameters");
            }
         }
      } catch (InvalidAttributeValueException var7) {
         if (StoreDebug.storeAdmin.isDebugEnabled()) {
            StoreDebug.storeAdmin.debug("Error setting PagingDirectory attribute", var7);
         }
      }

   }

   private static boolean isStringEmpty(String var0) {
      return var0 == null || var0.length() == 0;
   }

   private static boolean areTargetsNull(TargetMBean[] var0) {
      return var0 == null || var0.length == 0;
   }

   private final class ServerListener implements PropertyChangeListener {
      private JMSServerMBean server;

      ServerListener(JMSServerMBean var2) {
         this.server = var2;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (var2.equals("Targets")) {
            StoreCompatibilityUpgrader.updateStoreAndTargets(StoreCompatibilityUpgrader.this.domainBean, this.server, this.server.getStore(), (TargetMBean[])((TargetMBean[])var1.getNewValue()));
         }

      }
   }
}
