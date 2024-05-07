package weblogic.upgrade.singleton;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.DefaultCompositeInputAdapter;
import com.bea.plateng.plugin.ia.DefaultTextInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import javax.management.InvalidAttributeValueException;
import weblogic.cluster.singleton.ConfigMigrationProcessor;
import weblogic.management.DistributedManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.UpdateException;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;
import weblogic.utils.Debug;

public class MigratableServersJDBCConfigPlugIn extends AbstractPlugIn {
   private static final String MACHINE_CONST = "_Machine";
   private DomainMBean domain;
   private String userNameAsString;
   private String passwordAsString;
   private String urlAsString;
   private String driverNameAsString;
   private String propertiesAsString;
   private boolean domainHasMigratableServers = false;
   private final ArrayList machineNameList = new ArrayList();
   private final ArrayList listenAddressList = new ArrayList();
   private final ArrayList listenPortList = new ArrayList();
   private final ArrayList nmTypeList = new ArrayList();
   private DefaultTextInputAdapter userName;
   private DefaultTextInputAdapter password;
   private DefaultTextInputAdapter url;
   private DefaultTextInputAdapter driverName;
   private DefaultTextInputAdapter properties;
   private final ArrayList nameAdapters = new ArrayList();
   private final ArrayList listenAddrAdapters = new ArrayList();
   private final ArrayList listenPortAdapters = new ArrayList();
   private final ArrayList nmTypeAdapters = new ArrayList();
   private HashSet set = new HashSet();

   public MigratableServersJDBCConfigPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public InputAdapter getInputAdapter() {
      if (!this.domainHasMigratableServers) {
         return null;
      } else {
         try {
            DefaultCompositeInputAdapter var1 = UpgradeHelper.createCompositeIA(this.getName(), "compIA");
            this.userName = UpgradeHelper.createTextIA(this.getName(), "TextIA");
            this.userName.setPrompt(UpgradeHelper.i18n(this.getName() + "." + this.userName.getName() + ".input.prompt.text"));
            var1.add(this.userName);
            this.password = UpgradeHelper.createTextIA(this.getName(), "TextIB");
            this.password.setPrompt(UpgradeHelper.i18n(this.getName() + "." + this.password.getName() + ".input.prompt.text"));
            var1.add(this.password);
            this.url = UpgradeHelper.createTextIA(this.getName(), "TextIC");
            var1.add(this.url);
            this.url.setValue("");
            this.url.setPrompt(UpgradeHelper.i18n(this.getName() + "." + this.url.getName() + ".input.prompt.text"));
            this.driverName = UpgradeHelper.createTextIA(this.getName(), "TextID");
            this.driverName.setValue("weblogic.jdbc.oracle.OracleDriver");
            this.driverName.setPrompt(UpgradeHelper.i18n(this.getName() + "." + this.driverName.getName() + ".input.prompt.text"));
            var1.add(this.driverName);
            this.properties = UpgradeHelper.createTextIA(this.getName(), "TextIE");
            this.properties.setValue("");
            this.properties.setPrompt(UpgradeHelper.i18n(this.getName() + "." + this.properties.getName() + ".input.prompt.text"));
            var1.add(this.properties);
            Debug.assertion(this.domain != null, "Domain cannot be null");
            MigratableTargetMBean[] var2 = this.domain.getMigratableTargets();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               ServerMBean var4 = var2[var3].getUserPreferredServer();
               if (!this.set.contains(var4)) {
                  this.set.add(var4);
                  if (var4.getMachine() == null) {
                     this.promptMachineConfig(var4, var1);
                  }
               }
            }

            return var1;
         } catch (Exception var5) {
            var5.printStackTrace();
            throw new AssertionError("Unexpected exception" + var5);
         }
      }
   }

   private void promptMachineConfig(ServerMBean var1, DefaultCompositeInputAdapter var2) throws Exception {
      DefaultTextInputAdapter var3 = null;
      var3 = UpgradeHelper.createTextIA(this.getName(), var1.getName() + "_Machine");
      var3.setValue(var1.getName() + "Machine");
      var3.setPrompt(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.msg.machinename", (Object)var1.getName()));
      var2.add(var3);
      this.nameAdapters.add(var3);
      DefaultTextInputAdapter var4 = UpgradeHelper.createTextIA(this.getName(), var1.getName() + "_ListenAddress");
      var4.setValue("localhost");
      var4.setPrompt(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.msg.machinelistenaddress", (Object)var1.getName()));
      var2.add(var4);
      this.listenAddrAdapters.add(var4);
      DefaultTextInputAdapter var5 = UpgradeHelper.createTextIA(this.getName(), var1.getName() + "_ListenPort");
      var5.setValue("5556");
      var5.setPrompt(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.msg.machinelistenport", (Object)var1.getName()));
      var2.add(var5);
      this.listenPortAdapters.add(var5);
      DefaultTextInputAdapter var6 = UpgradeHelper.createTextIA(this.getName(), var1.getName() + "_NMType");
      var6.setPrompt(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.msg.nmtype"));
      var2.add(var6);
      this.nmTypeAdapters.add(var6);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      this.domain = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      if (this.domain == null) {
         throw new PlugInException(this.getName(), "Failed to find domain mbean tree");
      } else {
         this.domainHasMigratableServers = doesDomainHaveMigratableServers(this.domain);
      }
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      ValidationStatus var2 = super.validateInputAdapter(var1);
      if (this.domainHasMigratableServers) {
         DefaultCompositeInputAdapter var3 = (DefaultCompositeInputAdapter)var1;
         this.userName = (DefaultTextInputAdapter)var3.getInputAdapter("TextIA");
         this.password = (DefaultTextInputAdapter)var3.getInputAdapter("TextIB");
         this.url = (DefaultTextInputAdapter)var3.getInputAdapter("TextIC");
         this.driverName = (DefaultTextInputAdapter)var3.getInputAdapter("TextID");
         this.properties = (DefaultTextInputAdapter)var3.getInputAdapter("TextIE");
         if (var2.isValid()) {
            this.validateInputValues(var2);
         }
      }

      return var2;
   }

   private boolean areJDBCConfigurationInputValuesValid(ValidationStatus var1) {
      if (this.userNameAsString.length() == 0) {
         var1.setErrorMessage(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.IA.input.username.text"));
         var1.setValid(false);
         return false;
      } else if (this.passwordAsString.length() == 0) {
         var1.setErrorMessage(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.IB.input.password.text"));
         var1.setValid(false);
         return false;
      } else if (this.urlAsString.length() == 0) {
         var1.setErrorMessage(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.IC.input.url.text"));
         var1.setValid(false);
         return false;
      } else if (this.driverNameAsString.length() == 0) {
         var1.setErrorMessage(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.ID.input.driver.text"));
         var1.setValid(false);
         return false;
      } else {
         return true;
      }
   }

   private boolean areMachineAttributesCorrect(ValidationStatus var1) {
      int var2;
      DefaultTextInputAdapter var3;
      for(var2 = 0; var2 < this.nameAdapters.size(); ++var2) {
         var3 = (DefaultTextInputAdapter)this.nameAdapters.get(var2);
         if (var3.getValue().length() == 0) {
            var1.setErrorMessage(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.IE.input.machinename.text"));
            var1.setValid(false);
            return false;
         }

         this.machineNameList.add(var3.getValue());
      }

      for(var2 = 0; var2 < this.listenAddrAdapters.size(); ++var2) {
         var3 = (DefaultTextInputAdapter)this.listenAddrAdapters.get(var2);
         if (var3.getValue().length() == 0) {
            var1.setErrorMessage(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.IF.input.machinelistenaddress.text"));
            var1.setValid(false);
            return false;
         }

         this.listenAddressList.add(var3.getValue());
      }

      for(var2 = 0; var2 < this.listenPortAdapters.size(); ++var2) {
         var3 = (DefaultTextInputAdapter)this.listenPortAdapters.get(var2);
         if (var3.getValue().length() == 0) {
            var1.setErrorMessage(UpgradeHelper.i18n("MigratableServersJDBCConfigPlugIn.IG.input.machinelistenport.text"));
            var1.setValid(false);
            return false;
         }

         this.listenPortList.add(var3.getValue());
      }

      for(var2 = 0; var2 < this.nmTypeAdapters.size(); ++var2) {
         var3 = (DefaultTextInputAdapter)this.nmTypeAdapters.get(var2);
         if (var3.getValue().length() == 0) {
            this.nmTypeList.add("");
         } else {
            this.nmTypeList.add(var3.getValue());
         }
      }

      return true;
   }

   private void validateInputValues(ValidationStatus var1) {
      this.userNameAsString = this.userName.getValue();
      this.passwordAsString = this.password.getValue();
      this.urlAsString = this.url.getValue();
      this.driverNameAsString = this.driverName.getValue();
      this.propertiesAsString = this.properties.getValue();
      if (this.areJDBCConfigurationInputValuesValid(var1)) {
         if (this.areMachineAttributesCorrect(var1)) {
            ;
         }
      }
   }

   public void execute() throws PlugInException {
      if (this.domainHasMigratableServers) {
         ConfigMigrationProcessor var1 = ConfigMigrationProcessor.getInstance();

         try {
            var1.updateConfiguration(this.domain);
            var1.setConnectionPoolProperties(this.userNameAsString, this.passwordAsString, this.urlAsString, this.driverNameAsString, this.propertiesAsString, this.domain);
            int var2 = this.machineNameList.size();

            for(int var3 = 0; var3 < var2; ++var3) {
               String var4 = ((DefaultTextInputAdapter)this.nameAdapters.get(var3)).getName();
               String var5 = var4.substring(0, var4.indexOf("_Machine"));
               var1.createMachine(this.domain, (String)this.machineNameList.get(var3), (String)this.listenAddressList.get(var3), (String)this.listenPortList.get(var3), (String)this.nmTypeList.get(var3), var5);
            }

            var1.setCandidateMachinesForAutomaticMigration(this.domain);
         } catch (InvalidAttributeValueException var6) {
            var6.printStackTrace();
         } catch (UpdateException var7) {
            var7.printStackTrace();
         } catch (DistributedManagementException var8) {
            var8.printStackTrace();
         }

      }
   }

   public static boolean doesDomainHaveMigratableServers(DomainMBean var0) {
      boolean var1 = false;
      MigratableTargetMBean[] var2 = var0.getMigratableTargets();
      if (var2.length > 0) {
         ServerMBean[] var3 = var0.getServers();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (!isDefaultMigratableTarget(var3, var2[var4].getName())) {
               var1 = true;
               break;
            }
         }
      }

      return var1;
   }

   private static boolean isDefaultMigratableTarget(ServerMBean[] var0, String var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         String var3 = var0[var2].getName() + " (migratable)";
         if (var3.equals(var1)) {
            return true;
         }
      }

      return false;
   }
}
