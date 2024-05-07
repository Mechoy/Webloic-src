package weblogic.upgrade.domain.application;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.Choice;
import com.bea.plateng.plugin.ia.DefaultChoiceInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.util.ArrayList;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class DomainApplicationsPlugIn extends AbstractPlugIn {
   private static String SELECTED_APPS_KEY = DomainApplicationsPlugIn.class.getName() + ".SELECTED_APPS_KEY";

   public DomainApplicationsPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      DomainMBean var2 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      ApplicationMBean[] var3 = var2.getApplications();
      Choice[] var4 = new Choice[var3.length];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         Choice var6 = new Choice(var3[var5].getName());
         var6.setPrompt(var3[var5].getName());
         var4[var5] = var6;
      }

      DefaultChoiceInputAdapter var7 = (DefaultChoiceInputAdapter)this._adapter;
      var7.setChoices(var4);
      var7.setMultipleSelectionPermitted(true);
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      ValidationStatus var2 = super.validateInputAdapter(var1);
      if (var2.isValid()) {
         DefaultChoiceInputAdapter var3 = (DefaultChoiceInputAdapter)this._adapter;
         Choice[] var4 = var3.getSelectedChoices();
         DomainMBean var5 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
         ArrayList var6 = new ArrayList();

         for(int var7 = 0; var7 < var4.length; ++var7) {
            String var8 = var4[var7].getPrompt();
            var6.add(var8);
         }

         this._context.put(SELECTED_APPS_KEY, (String[])((String[])var6.toArray(new String[0])));
      }

      return var2;
   }

   public void execute() throws PlugInException {
      String[] var1 = (String[])((String[])this._context.get(SELECTED_APPS_KEY));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.updateStatus("Updating application: " + var1[var2]);
         this.sleep(200L);
         this.updateStatus("Application Upgrade not yet supported");
      }

   }

   private void updateStatus(String var1) {
      this.updateObservers(new PlugInMessageObservation(this.getName(), var1 + ""));
   }

   private void sleep(long var1) {
      try {
         Thread.sleep(var1);
      } catch (Exception var4) {
      }

   }
}
