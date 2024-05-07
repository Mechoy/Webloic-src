package weblogic.upgrade.jms;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.PlugInProgressObservation;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.jms.JMSException;
import javax.management.InvalidAttributeValueException;
import weblogic.jms.backend.BEDurableSubscriptionStore;
import weblogic.jms.store.JMSObjectHandler;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.FileStoreMBean;
import weblogic.management.configuration.GenericFileStoreMBean;
import weblogic.management.configuration.JDBCStoreMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.internal.PersistenceImpl;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreRecord;
import weblogic.store.PersistentStoreTransaction;
import weblogic.store.RuntimeHandler;
import weblogic.store.StoreWritePolicy;
import weblogic.store.admin.FileAdminHandler;
import weblogic.store.admin.JDBCAdminHandler;
import weblogic.store.io.file.StoreDir;
import weblogic.store.xa.PersistentStoreManagerXA;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class StoreUpgradePlugIn extends AbstractPlugIn {
   private static final String NEW_FILE_NAME_PREFIX = "NEW";
   private static final String OLD_FILE_NAME_PREFIX = "OLD";
   private static final String UPGRADE_CONNECTION_NAME = "weblogic.upgrade.jms";
   private static final String UPGRADE_START_INDICATOR = "9.1 UpgradeInProgress";
   private static final String UPGRADE_DONE_INDICATOR = "9.1 UpgradeComplete";
   private DomainMBean domainBean;
   private String[] serverNames;
   private ClearOrEncryptedService encryptionService;
   private PlugInProgressObservation progress;
   private PersistentStoreXA currentNewStore;

   public StoreUpgradePlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      try {
         super.prepare(var1);
         UpgradeHelper.setupWLSClientLogger(this);
         this.domainBean = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
         if (this.domainBean == null) {
            throw this.createException("StoreUpgradePlugIn.exc.NoBeanTree");
         }

         File var2 = (File)var1.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
         if (var2 == null) {
            throw this.createException("StoreUpgradePlugIn.exc.NoDomainDir");
         }

         EncryptionService var3 = SerializedSystemIni.getEncryptionService(var2.getPath());
         this.encryptionService = new ClearOrEncryptedService(var3);
         this.serverNames = (String[])((String[])var1.get(DomainPlugInConstants.SERVER_NAMES_KEY));
         if (this.serverNames == null) {
            throw new PlugInException(this.getName(), "The list of server names was not found");
         }

         JMSServerMBean[] var4 = this.domainBean.getJMSServers();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getStore() != null) {
               throw this.createException("StoreUpgradePlugIn.exc.NotUpgraded", (Object)var4[var5].getName());
            }
         }
      } finally {
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

   }

   private boolean targetsMatch(JMSServerMBean var1) {
      TargetMBean[] var2 = var1.getTargets();
      if (var2 != null && var2.length == 1) {
         for(int var3 = 0; var3 < this.serverNames.length; ++var3) {
            if (var2[0].getServerNames().contains(this.serverNames[var3])) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void execute() throws PlugInException {
      try {
         UpgradeHelper.setupWLSClientLogger(this);
         this.setProgress(0);
         JMSServerMBean[] var1 = this.domainBean.getJMSServers();
         if (var1 != null && var1.length != 0) {
            int var2 = 100 / var1.length;

            for(int var3 = 0; var3 < var1.length; ++var3) {
               JMSServerMBean var4 = var1[var3];
               if (this.targetsMatch(var4)) {
                  this.upgradeServer(var4, var2);
                  this.setProgress(var2 * (var3 + 1));
               }
            }

            this.setProgress(100);
            return;
         }

         this.updateStatus("StoreUpgradePlugIn.msg.NoServersToUpgrade");
         this.setProgress(100);
      } finally {
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

   }

   private void upgradeServer(JMSServerMBean var1, int var2) throws PlugInException {
      this.updateStatus("StoreUpgradePlugIn.msg.UpgradingServer", var1.getName());
      PersistentStoreMBean var3 = var1.getPersistentStore();
      if (var3 == null) {
         this.updateStatus("StoreUpgradePlugIn.msg.NoStore");
      } else {
         StoreReader var4 = null;
         boolean var5 = false;

         try {
            this.updateStatus("StoreUpgradePlugIn.msg.OpeningOldStore");
            var4 = this.openOldStore(var3);
            if (!var4.requiresUpgrade()) {
               this.updateStatus("StoreUpgradePlugIn.msg.NothingToUpgrade");
               return;
            }

            if (var4.alreadyUpgraded()) {
               this.updateStatus("StoreUpgradePlugIn.msg.AlreadyUpgraded", var1.getName());
               return;
            }

            this.updateStatus("StoreUpgradePlugIn.msg.OpeningNewStore");
            this.currentNewStore = this.openNewStore(var3);
            this.updateStatus("StoreUpgradePlugIn.msg.UpgradingData");
            this.updateStatus("StoreUpgradePlugIn.msg.UpgradingDataWait");
            this.performUpgrade(var1.getName(), var3, var4, this.currentNewStore, var2);
            this.updateStatus("StoreUpgradePlugIn.msg.UpgradingDataDone");
            var5 = true;
         } catch (PlugInException var70) {
            throw var70;
         } catch (Throwable var71) {
            var71.printStackTrace(System.err);
            throw new RuntimeException(var71);
         } finally {
            this.updateStatus("StoreUpgradePlugIn.msg.ClosingStores");
            if (var4 != null) {
               var4.close();
            }

            if (this.currentNewStore != null) {
               try {
                  this.currentNewStore.close();
               } catch (PersistentStoreException var68) {
               } finally {
                  this.currentNewStore = null;
               }
            }

         }

         if (var5 && var3 instanceof FileStoreMBean) {
            this.updateStatus("StoreUpgradePlugIn.msg.RenamingFiles");
            this.renameFileStoreFiles((FileStoreMBean)var3);
         }

      }
   }

   private StoreReader openReader(PersistentStoreMBean var1) throws JMSException {
      if (var1 instanceof GenericFileStoreMBean) {
         return new FileStoreReader((GenericFileStoreMBean)var1, new UpgradeIOBypass());
      } else if (var1 instanceof JDBCStoreMBean) {
         return new JDBCStoreReader((JDBCStoreMBean)var1, new UpgradeIOBypass(), this.encryptionService);
      } else {
         throw new AssertionError("Invalid store type detected");
      }
   }

   private StoreReader openOldStore(PersistentStoreMBean var1) throws PlugInException {
      try {
         return this.openReader(var1);
      } catch (JMSException var3) {
         throw this.createException("StoreUpgradePlugIn.exc.ErrorOpeningOldStore", (Throwable)var3);
      }
   }

   private PersistentStoreXA openNewStore(PersistentStoreMBean var1) throws PlugInException {
      String var2 = "NEW" + var1.getName();

      try {
         PersistentStoreXA var3;
         if (var1 instanceof GenericFileStoreMBean) {
            String var4 = ((GenericFileStoreMBean)var1).getDirectory();
            var4 = FileAdminHandler.canonicalizeDirectoryName(var4);
            var3 = PersistentStoreManagerXA.makeXAStore(var2, var4, (String)null, true, (RuntimeHandler)null);
         } else {
            if (!(var1 instanceof JDBCStoreMBean)) {
               throw new AssertionError("Invalid store type detected");
            }

            var3 = JDBCAdminHandler.makeStore(var2, (String)null, (JDBCStoreMBean)var1, this.encryptionService, (RuntimeHandler)null);
         }

         HashMap var6 = new HashMap();
         var6.put("SynchronousWritePolicy", StoreWritePolicy.DISABLED);
         var3.open(var6);
         return var3;
      } catch (PersistentStoreException var5) {
         throw this.createException("StoreUpgradePlugIn.exc.ErrorOpeningNewStore", (Throwable)var5);
      }
   }

   private void performUpgrade(String var1, PersistentStoreMBean var2, StoreReader var3, PersistentStoreXA var4, int var5) throws PlugInException {
      try {
         PersistentStoreConnection var6 = var4.createConnection("weblogic.upgrade.jms");
         PersistentStoreConnection.Cursor var7 = var6.createCursor(0);
         PersistentStoreRecord var8;
         if ((var8 = var7.next()) != null) {
            String var9 = (String)var8.getData();
            if (var9.equals("9.1 UpgradeComplete")) {
               this.updateStatus("StoreUpgradePlugIn.msg.AlreadyUpgraded", var1);
               return;
            }

            if (var9.equals("9.1 UpgradeInProgress")) {
               try {
                  this.updateStatus("StoreUpgradePlugIn.msg.ReUpgradingPartiallyUpgraded", var1);
                  this.currentNewStore = null;
                  var4.close();
                  this.cleanupNewStore(var2);
                  var4 = this.openNewStore(var2);
                  this.currentNewStore = var4;
                  var6 = var4.createConnection("weblogic.upgrade.jms");
               } catch (PersistentStoreException var17) {
                  throw this.createException("StoreUpgradePlugIn.exc.ReUpgradingError", (Object)var1);
               }
            }
         }

         PersistentStoreTransaction var21 = var4.begin();
         PersistentHandle var10 = var6.create(var21, "9.1 UpgradeInProgress", 0);
         var21.commit();
         PersistenceImpl var11 = new PersistenceImpl(var1, var4, new JMSObjectHandler());
         var11.open();
         BEDurableSubscriptionStore var12 = new BEDurableSubscriptionStore(var1, var4);
         String var13 = "JMS_" + var2.getName();
         JMSStoreUpgradeProcessor var14 = new JMSStoreUpgradeProcessor(this, var1, var13, var3, var4, var11, var12, new UpgradeIOBypass());
         var14.upgrade(var5);

         try {
            var2.setXAResourceName(var13);
         } catch (InvalidAttributeValueException var16) {
         }

         var21 = var4.begin();
         var6.delete(var21, var10, 0);
         var6.create(var21, "9.1 UpgradeComplete", 0);
         var11.close();
         var21.commit();
      } catch (JMSException var18) {
         throw this.createException("StoreUpgradePlugIn.exc.UpgradeError", (Throwable)var18);
      } catch (PersistentStoreException var19) {
         throw this.createException("StoreUpgradePlugIn.exc.UpgradeError", (Throwable)var19);
      } catch (KernelException var20) {
         throw this.createException("StoreUpgradePlugIn.exc.UpgradeError", (Throwable)var20);
      }
   }

   private void renameFileStoreFiles(FileStoreMBean var1) throws PlugInException {
      String var2 = FileAdminHandler.canonicalizeDirectoryName(var1.getDirectory());
      StoreDir var3 = new StoreDir(var2, var1.getName(), "dat");

      try {
         var3.changePrefix("OLD" + var1.getName());
      } catch (IOException var7) {
         throw this.createException("StoreUpgradePlugIn.exc.RenamingError", (Throwable)var7);
      }

      StoreDir var4 = new StoreDir(var2, "NEW" + var1.getName(), "dat");

      try {
         var4.changePrefix(var1.getName());
      } catch (IOException var6) {
         throw this.createException("StoreUpgradePlugIn.exc.RenamingError", (Throwable)var6);
      }
   }

   private void cleanupNewStore(PersistentStoreMBean var1) throws PlugInException {
      if (var1 instanceof GenericFileStoreMBean) {
         this.cleanupNewFileStore((GenericFileStoreMBean)var1);
      } else if (var1 instanceof JDBCStoreMBean) {
         this.cleanupNewJDBCStore((JDBCStoreMBean)var1);
      }

   }

   private void cleanupNewFileStore(GenericFileStoreMBean var1) throws PlugInException {
      try {
         String var2 = var1.getDirectory();
         var2 = FileAdminHandler.canonicalizeDirectoryName(var2);
         String var3 = "NEW" + var1.getName();
         StoreDir var4 = new StoreDir(var2, var3, "dat");
         var4.deleteFiles();
      } catch (IOException var5) {
         throw this.createException("StoreUpgradePlugIn.exc.DeletingPartialUpgradeFilesError", (Throwable)var5);
      }
   }

   private void cleanupNewJDBCStore(JDBCStoreMBean var1) throws PlugInException {
      String var2 = "NEW" + var1.getName();

      try {
         JDBCAdminHandler.deleteStore(var2, var1, this.encryptionService);
      } catch (PersistentStoreException var4) {
         throw this.createException("StoreUpgradePlugIn.exc.DeletingPartialUpgradeDBTableError", (Throwable)var4);
      }
   }

   private void log(String var1) {
      this.updateObservers(new PlugInMessageObservation(this.getName(), var1 + ""));
   }

   private void updateStatus(String var1) {
      this.log(UpgradeHelper.i18n(var1));
   }

   private void updateStatus(String var1, Object var2) {
      this.log(UpgradeHelper.i18n(var1, var2));
   }

   private PlugInException createException(String var1) {
      String var2 = UpgradeHelper.i18n(var1);
      this.log(var2);
      return new PlugInException(this.getName(), var2);
   }

   private PlugInException createException(String var1, Object var2) {
      String var3 = UpgradeHelper.i18n(var1, var2);
      this.log(var3);
      return new PlugInException(this.getName(), var3);
   }

   private PlugInException createException(String var1, Throwable var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append(UpgradeHelper.i18n(var1));

      for(Throwable var4 = var2; var4 != null; var4 = var4.getCause()) {
         var3.append(": ");
         var3.append(var4.toString());
      }

      this.log(var3.toString());
      PlugInException var5 = new PlugInException(this.getName(), var3.toString());
      if (var2 != null) {
         var5.initCause(var2);
      }

      return var5;
   }

   void setProgress(int var1) {
      if (this.progress == null) {
         this.progress = new PlugInProgressObservation(this.getName());
      }

      this.progress.setProgress(var1);
      this.updateObservers(this.progress);
   }

   void incrementProgress(int var1) {
      if (this.progress != null) {
         this.progress.incrementProgress(var1);
         this.updateObservers(this.progress);
      }

   }

   DomainMBean getDomainBean() {
      return this.domainBean;
   }
}
