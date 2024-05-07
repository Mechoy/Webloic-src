package weblogic.upgrade.upgradesecurityproviders;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.DefaultFileSelectionInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.Home;
import weblogic.management.security.internal.MigrateOldProviders;
import weblogic.management.security.internal.SecurityProviderUpgradeTextTextFormatter;
import weblogic.upgrade.UpgradeHelper;

public class UpgradeProviderPlugin extends AbstractPlugIn {
   public UpgradeProviderPlugin(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      Home.getHome();
      String var2 = Home.getPath();
      File var3 = null;
      if ("SecurityProviderUpgradeStepOne".equals(this.getName())) {
         var3 = (File)this._context.get("BEA_HOME");
         if (var3 == null) {
            var3 = new File(".");
         }
      } else {
         var3 = new File(var2);
         var3 = new File(var3, "lib/mbeantypes");
      }

      ((DefaultFileSelectionInputAdapter)this._adapter).setSelectionMode(1);
      ((DefaultFileSelectionInputAdapter)this._adapter).setMultipleSelection(false);
      ((DefaultFileSelectionInputAdapter)this._adapter).setSelectedFileNames(new String[]{var3.getPath()});
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      ValidationStatus var2 = super.validateInputAdapter(var1);
      File[] var3 = ((DefaultFileSelectionInputAdapter)var1).getSelectedFiles();
      return var2;
   }

   public void execute() throws PlugInException {
      try {
         UpgradeHelper.setupWLSClientLogger(this);
         File[] var1 = ((DefaultFileSelectionInputAdapter)this._adapter).getSelectedFiles();
         File var2 = var1[0];
         String var3 = this.getName();
         if ("SecurityProviderUpgradeStepOne".equals(var3)) {
            this._context.put("old.bea.home", var2.getAbsolutePath());
         } else {
            this._context.put("new.bea.home", var2.getAbsolutePath());
            this.upgrade();
         }
      } finally {
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

   }

   private void upgrade() {
      PlugInMessageObservation var1 = null;
      Object var2 = new ArrayList();

      try {
         UpgradeHelper.clearSummaryMessages(this._context, this.getName());
         var1 = new PlugInMessageObservation(this.getName());
         SecurityProviderUpgradeTextTextFormatter var3 = new SecurityProviderUpgradeTextTextFormatter();
         this.log(var3.started());
         var2 = MigrateOldProviders.run(new String[]{(String)this._context.get("old.bea.home"), (String)this._context.get("new.bea.home")});
      } catch (Exception var7) {
         MigrateOldProviders.cleanup();
         var7.printStackTrace();
      } finally {
         MigrateOldProviders.cleanup();
      }

      this.updateObservers(var1);
      Iterator var9 = ((List)var2).iterator();

      String var4;
      for(var4 = ""; var9.hasNext(); var4 = var4 + (String)var9.next() + "\n") {
      }

      UpgradeHelper.addSummaryMessage(this._context, this.getName(), var4);
   }

   public void log(String var1) {
      this.updateObservers(new PlugInMessageObservation(this.getName(), var1));
   }
}
