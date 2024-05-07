package weblogic.upgrade.domain.adminserverselection;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.Choice;
import com.bea.plateng.plugin.ia.DefaultChoiceInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.ConfigLogger;
import weblogic.management.provider.PropertyService;
import weblogic.server.ServiceFailureException;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class AdminServerSelectionPlugIn extends AbstractPlugIn implements DomainPlugInConstants {
   private static PropertyService propertyService = null;
   private static final boolean DEBUG = false;

   public AdminServerSelectionPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      DefaultChoiceInputAdapter var2 = (DefaultChoiceInputAdapter)this._adapter;
      DomainMBean var3 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      ServerMBean[] var4 = var3.getServers();
      if (var4 != null && var4.length != 0) {
         Choice[] var5 = new Choice[var4.length];
         HashMap var6 = new HashMap();

         for(int var7 = 0; var7 < var4.length; ++var7) {
            String var8 = var4[var7].getName();
            var5[var7] = new Choice(var8, false);
            var5[var7].setPrompt(var8);
            var6.put(var8, var5[var7]);
         }

         var2.setChoices(var5);
         String var9 = this.getDefaultAdminServerName(var3, var4);
         Choice var10 = (Choice)var6.get(var9);
         var10.setSelected(true);
         this._context.put(DomainPlugInConstants.ADMIN_SERVER_NAME_KEY, var9);
         var2.setMultipleSelectionPermitted(false);
      } else {
         throw new PlugInException(this.getName(), "No servers are defined in this domain");
      }
   }

   public InputAdapter getInputAdapter() {
      DefaultChoiceInputAdapter var1 = (DefaultChoiceInputAdapter)this._adapter;
      Choice[] var2 = (Choice[])var1.getChoices();
      return var2.length == 1 ? null : super.getInputAdapter();
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      DefaultChoiceInputAdapter var2 = (DefaultChoiceInputAdapter)this._adapter;
      Choice var3 = var2.getSelectedChoices()[0];
      String var4 = var3.getId();
      this._context.put(DomainPlugInConstants.ADMIN_SERVER_NAME_KEY, var4);
      return new ValidationStatus(true);
   }

   public void execute() throws PlugInException {
      try {
         String var1 = (String)this._context.get(DomainPlugInConstants.ADMIN_SERVER_NAME_KEY);
         UpgradeHelper.addSummaryMessage(this._context, this.getName(), UpgradeHelper.i18n("AdminServerSelectionPlugIn.execute.admin_server_selected", (Object)var1));
         System.setProperty("weblogic.Name", var1);
         BootStrap.reinit();
         if (propertyService == null) {
            propertyService = new PropertyService();
         }

         propertyService.start();
         DomainMBean var2 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
         propertyService.doPostParseInitialization(var2);
         String[] var3 = (String[])((String[])this._context.get(DomainPlugInConstants.SERVER_NAMES_KEY));
         List var4 = Arrays.asList(var3);
         if (var4.size() != 0 && !var4.contains(var1)) {
            this._context.put(DomainPlugInConstants.HAS_CONFIGURATION_KEY, Boolean.FALSE);
            UpgradeHelper.log(this, UpgradeHelper.i18n("AdminServerSelectionPlugIn.CONF_NOT_WRITTEN_OUT_LATER"));
         } else {
            this._context.put(DomainPlugInConstants.HAS_CONFIGURATION_KEY, Boolean.TRUE);
            UpgradeHelper.log(this, UpgradeHelper.i18n("AdminServerSelectionPlugIn.CONF_WRITTEN_OUT_LATER"));
         }

      } catch (ServiceFailureException var5) {
         throw new PlugInException(this.getName(), "Error starting property service", var5);
      }
   }

   private String getDefaultAdminServerName(DomainMBean var1, ServerMBean[] var2) throws PlugInException {
      String var3 = (String)this._context.get(DomainPlugInConstants.ADMIN_SERVER_NAME_KEY);
      if (var3 == null || var3.length() == 0) {
         var3 = System.getProperty("weblogic.upgrade.adminserver");
      }

      if (var3 == null || var3.length() == 0) {
         var3 = System.getProperty("weblogic.Name");
      }

      if (var3 != null && var3.length() > 0) {
         if (var1.lookupServer(var3) != null) {
            return var3;
         } else {
            throw new PlugInException(this.getName(), ConfigLogger.logServerNameNotFoundLoggable(var3, var1.getName()).getMessage());
         }
      } else if (var2.length == 1) {
         return var2[0].getName();
      } else {
         boolean var4 = false;

         for(int var5 = 0; var5 < var2.length; ++var5) {
            if (var2[var5].getName().equals("myserver")) {
               var4 = true;
               break;
            }
         }

         return var4 ? "myserver" : var2[0].getName();
      }
   }
}
