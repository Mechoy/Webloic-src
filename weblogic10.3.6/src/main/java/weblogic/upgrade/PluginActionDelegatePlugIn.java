package weblogic.upgrade;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.util.ArrayList;
import weblogic.management.provider.ConfigurationProcessor;

public class PluginActionDelegatePlugIn extends AbstractPlugIn {
   private PlugInMessageObservation msgObservation;
   private PluginActionDelegate[] updaters;
   private String[] ARR = new String[0];

   public PluginActionDelegatePlugIn(PlugInDefinition var1, String[] var2) throws PlugInException {
      super(var1);

      try {
         this.ARR = var2;
         this.msgObservation = new PlugInMessageObservation(this.getName());
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < this.ARR.length; ++var4) {
            Class var5 = Class.forName(this.ARR[var4]);
            Object var6 = var5.newInstance();
            if (var6 instanceof ConfigurationProcessor) {
               final ConfigurationProcessor var7 = (ConfigurationProcessor)var6;
               PluginActionDelegate var8 = new PluginActionDelegate() {
                  public void execute() throws Exception {
                     var7.updateConfiguration(this.getDomainMBean());
                  }

                  public String getName() {
                     return var7.getClass().getName();
                  }
               };
               var3.add(var8);
            } else {
               if (!(var6 instanceof PluginActionDelegate)) {
                  throw new PlugInException(this.getName(), "Object must be a ConfigurationProcessor or PluginActionDelegate, not: " + var6.getClass().getName());
               }

               var3.add(var6);
            }
         }

         this.updaters = (PluginActionDelegate[])((PluginActionDelegate[])var3.toArray(new PluginActionDelegate[0]));
      } catch (PlugInException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new PlugInException(this.getName(), "Constructor Exception: " + var10, var10);
      }
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      try {
         super.prepare(var1);
         UpgradeHelper.setupWLSClientLogger(this);

         for(int var2 = 0; var2 < this.updaters.length; ++var2) {
            try {
               this.updaters[var2].initPlugIn(this);
               this.updaters[var2].prepare(var1);
            } catch (Exception var7) {
               throw new PlugInException(this.getName(), "Prepare Exception: " + var7, var7);
            }
         }
      } finally {
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      ValidationStatus var2 = super.validateInputAdapter(var1);
      ValidationStatus var3 = null;
      if (var2.isValid()) {
         for(int var4 = 0; var4 < this.updaters.length; ++var4) {
            var3 = this.updaters[var4].validateInputAdapter(var1);
            if (!var3.isValid()) {
               var2 = var3;
               break;
            }
         }
      }

      return var2;
   }

   public void execute() throws PlugInException {
      try {
         UpgradeHelper.setupWLSClientLogger(this);

         for(int var1 = 0; var1 < this.updaters.length; ++var1) {
            try {
               this.updaters[var1].execute();
            } catch (Throwable var6) {
               var6.printStackTrace();
               throw new PlugInException(this.getName(), "Exception: " + var6, var6);
            }
         }
      } finally {
         UpgradeHelper.setupWLSClientLogger((AbstractPlugIn)null);
      }

   }

   public synchronized void log(Object var1) {
      if (var1 != null) {
         this.msgObservation = new PlugInMessageObservation(this.getName());
         this.msgObservation.setMessage(var1.toString() + "");
         this.updateObservers(this.msgObservation);
      }

   }
}
