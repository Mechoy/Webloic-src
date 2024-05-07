package weblogic.upgrade.domain.directoryselection;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.Choice;
import com.bea.plateng.plugin.ia.DefaultChoiceInputAdapter;
import com.bea.plateng.plugin.ia.DefaultCompositeInputAdapter;
import com.bea.plateng.plugin.ia.DefaultTextInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class DomainInfoVerificationPlugIn extends AbstractPlugIn {
   public DomainInfoVerificationPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      DomainMBean var2 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      DefaultCompositeInputAdapter var3 = (DefaultCompositeInputAdapter)this._adapter;
      DefaultChoiceInputAdapter var4 = (DefaultChoiceInputAdapter)var3.getInputAdapter("ChoiceIA");
      var4.setGroupDescription(UpgradeHelper.i18n("DomainInfoVerificationPlugIn.ChoiceIA.description.text"));
      var4.setMultipleSelectionPermitted(false);
      Choice[] var5 = new Choice[6];
      String[] var6 = new String[]{"6.1", "7.0", "8.1", "9.0", "9.1", "9.2"};
      boolean var7 = false;

      for(int var8 = 0; var8 < var6.length; ++var8) {
         var5[var8] = new Choice(var6[var8], false);
         var5[var8].setPrompt(var6[var8]);
         if (var2.getConfigurationVersion() != null && var2.getConfigurationVersion().startsWith(var6[var8]) || "8.1".equals(var6[var8]) && !var7) {
            var5[var8].setSelected(true);
            var7 = true;
         }
      }

      var4.setChoices(var5);
      DefaultTextInputAdapter var9 = (DefaultTextInputAdapter)var3.getInputAdapter("TextIA");
      var9.setRequired(true);
      var9.setDescription(UpgradeHelper.i18n("DomainInfoVerificationPlugIn.TextIA.input.description.text"));
      var9.setValue(var2.getName());
      var9.setPrompt(UpgradeHelper.i18n("DomainInfoVerificationPlugIn.TextIA.input.prompt.text"));
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      ValidationStatus var2 = new ValidationStatus(true);
      DefaultCompositeInputAdapter var3 = (DefaultCompositeInputAdapter)this._adapter;
      DefaultChoiceInputAdapter var4 = (DefaultChoiceInputAdapter)var3.getInputAdapter("ChoiceIA");
      Choice var5 = var4.getSelectedChoices()[0];
      String var6 = var5.getId();
      DefaultTextInputAdapter var7 = (DefaultTextInputAdapter)var3.getInputAdapter("TextIA");
      String var8 = var7.getValue();
      if (var8 == null || var8.trim().length() == 0) {
         var2.setErrorMessage(UpgradeHelper.i18n(""));
         var2.setValid(false);
      }

      return var2;
   }

   public void execute() throws PlugInException {
      try {
         DomainMBean var1 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
         DefaultCompositeInputAdapter var2 = (DefaultCompositeInputAdapter)this._adapter;
         DefaultChoiceInputAdapter var3 = (DefaultChoiceInputAdapter)var2.getInputAdapter("ChoiceIA");
         Choice var4 = var3.getSelectedChoices()[0];
         String var5 = var4.getId();
         DefaultTextInputAdapter var6 = (DefaultTextInputAdapter)var2.getInputAdapter("TextIA");
         String var7 = var6.getValue();
         var1.setName(var7);
         UpgradeHelper.log(this, UpgradeHelper.i18n("DomainInfoVerificationPlugIn.execute.domain_name_set", (Object)var7));
         var1.setConfigurationVersion(var5);
         UpgradeHelper.log(this, UpgradeHelper.i18n("DomainInfoVerificationPlugIn.execute.weblogic_version_set", (Object)var5));
         UpgradeHelper.resetLocalServerNames(this, this._context);
      } catch (Exception var8) {
         throw new PlugInException(this.getName(), var8.getMessage(), var8);
      }
   }
}
