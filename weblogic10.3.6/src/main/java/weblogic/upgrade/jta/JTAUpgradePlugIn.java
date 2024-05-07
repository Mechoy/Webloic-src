package weblogic.upgrade.jta;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import java.io.File;
import java.io.IOException;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.store.PersistentStoreException;
import weblogic.store.admin.FileAdminHandler;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.transaction.internal.TransactionLogUpgradeHelper;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class JTAUpgradePlugIn extends AbstractPlugIn {
   private DomainMBean domainBean;
   private String[] serverNames;
   private String domainPath;

   public JTAUpgradePlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      try {
         super.prepare(var1);
         UpgradeHelper.setupWLSClientLogger(this);
         this.domainBean = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
         if (this.domainBean == null) {
            throw this.createException("JTAUpgradePlugIn.exc.NoBeanTree");
         }

         File var2 = (File)var1.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
         if (var2 == null) {
            throw this.createException("JTAUpgradePlugIn.exc.NoDomainDir");
         }

         this.domainPath = var2.getPath();
         this.serverNames = (String[])((String[])var1.get(DomainPlugInConstants.SERVER_NAMES_KEY));
         if (this.serverNames == null) {
            throw new PlugInException(this.getName(), "The list of server names was not found");
         }
      } finally {
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

   }

   public void execute() throws PlugInException {
      try {
         UpgradeHelper.setupWLSClientLogger(this);
         ServerMBean[] var1 = this.domainBean.getServers();
         if (var1 != null && var1.length != 0) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               ServerMBean var3 = var1[var2];
               if (this.serverNames == null || this.targetsMatch(var3)) {
                  this.upgradeServer(var3);
               }
            }

            return;
         }

         this.updateStatus("JTAUpgradePlugIn.msg.NoServersToUpgrade");
      } finally {
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

   }

   private void upgradeServer(ServerMBean var1) throws PlugInException {
      this.updateStatus("JTAUpgradePlugIn.msg.UpgradingTLOG", var1.getName());

      try {
         FileAdminHandler var2 = new FileAdminHandler();
         var2.prepareDefaultStore(var1, true);
         var2.activate((DeploymentMBean)null);

         try {
            PersistentStoreXA var3 = var2.getStore();
            TransactionLogUpgradeHelper var4 = new TransactionLogUpgradeHelper();
            var4.attemptUpgrade(var3, var1.getTransactionLogFilePrefix(), var1.getName(), this.domainPath);
         } finally {
            var2.deactivate((DeploymentMBean)null);
            var2.unprepare((DeploymentMBean)null);
         }

      } catch (IOException var12) {
         throw this.createException("JTAUpgradePlugIn.exc.ErrorUpgradingLog", var12);
      } catch (PersistentStoreException var13) {
         throw this.createException("JTAUpgradePlugIn.exc.ErrorUpgradingLog", var13);
      } catch (DeploymentException var14) {
         throw this.createException("JTAUpgradePlugIn.exc.ErrorUpgradingLog", var14);
      } catch (UndeploymentException var15) {
         throw this.createException("JTAUpgradePlugIn.exc.ErrorUpgradingLog", var15);
      }
   }

   private boolean targetsMatch(ServerMBean var1) {
      for(int var2 = 0; var2 < this.serverNames.length; ++var2) {
         if (var1.getName().equals(this.serverNames[var2])) {
            return true;
         }
      }

      return false;
   }

   private void log(String var1) {
      this.updateObservers(new PlugInMessageObservation(this.getName(), var1));
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
}
