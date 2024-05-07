package weblogic.upgrade.domain.directoryselection;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.ia.Choice;
import com.bea.plateng.plugin.ia.DefaultChoiceInputAdapter;
import java.util.ArrayList;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class OptionalGroupsSelectionPlugIn extends AbstractPlugIn {
   public OptionalGroupsSelectionPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      new PlugInMessageObservation(this.getName());
      this.updateObservers(new PlugInMessageObservation(this.getName(), UpgradeHelper.i18n("OptionalGroupsSelectionPlugIn.prepare.prepare_to_select_options")));
      ArrayList var3 = new ArrayList();
      Choice var4 = null;
      var4 = new Choice("DOMAIN_DIRECTORY_BACKUP_SELECTED_VALUE");
      var4.setPrompt(UpgradeHelper.i18n("OptionalGroupsSelectionPlugIn.IA.DOMAIN_DIRECTORY_BACKUP_SELECTED_VALUE.prompt"));
      var4.setDescription(UpgradeHelper.i18n("OptionalGroupsSelectionPlugIn.IA.DOMAIN_DIRECTORY_BACKUP_SELECTED_VALUE.description"));
      var4.setSelected(true);
      var3.add(var4);
      var4 = new Choice("DOMAIN_DIRECTORY_BACKUP_LOG_FILES_INCLUDED_SELECTED_VALUE");
      var4.setPrompt(UpgradeHelper.i18n("OptionalGroupsSelectionPlugIn.IA.DOMAIN_DIRECTORY_BACKUP_LOG_FILES_INCLUDED_SELECTED_VALUE.prompt"));
      var4.setDescription(UpgradeHelper.i18n("OptionalGroupsSelectionPlugIn.IA.DOMAIN_DIRECTORY_BACKUP_LOG_FILES_INCLUDED_SELECTED_VALUE.description"));
      var4.setSelected(true);
      var3.add(var4);
      var4 = new Choice("SKIP_BACKWARDS_COMPATIBILITY_FLAGS_SELECTED_VALUE");
      var4.setPrompt(UpgradeHelper.i18n("OptionalGroupsSelectionPlugIn.IA.SKIP_BACKWARDS_COMPATIBILITY_FLAGS_SELECTED_VALUE.prompt"));
      var4.setDescription(UpgradeHelper.i18n("OptionalGroupsSelectionPlugIn.IA.SKIP_BACKWARDS_COMPATIBILITY_FLAGS_SELECTED_VALUE.description"));
      var4.setSelected(false);
      var3.add(var4);
      DefaultChoiceInputAdapter var5 = (DefaultChoiceInputAdapter)this._adapter;
      var5.setChoices((Choice[])((Choice[])var3.toArray(new Choice[0])));
      var5.setMultipleSelectionPermitted(true);
   }

   public void execute() throws PlugInException {
      DefaultChoiceInputAdapter var1 = (DefaultChoiceInputAdapter)this._adapter;
      Choice[] var2 = var1.getSelectedChoices();
      String[] var3 = new String[var2.length];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3[var4] = var2[var4].getId();
      }

      this._context.put(DomainPlugInConstants.OPTIONAL_GROUPS_KEY, var3);
   }
}
