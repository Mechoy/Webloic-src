package weblogic.upgrade;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInMessageObservation;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.io.File;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class PluginActionDelegate {
   private AbstractPlugIn plugIn;
   private PlugInMessageObservation msgObservation;
   private PlugInContext context;

   public String getName() {
      return this.getClass().getName();
   }

   public void initPlugIn(AbstractPlugIn var1) {
      this.plugIn = var1;
      this.msgObservation = new PlugInMessageObservation(this.plugIn.getName());
   }

   public void prepare(PlugInContext var1) throws Exception {
      this.context = var1;
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      return new ValidationStatus(true);
   }

   public void execute() throws Exception {
   }

   public void log(Object var1) {
      if (var1 != null) {
         if (this.plugIn != null) {
            this.msgObservation = new PlugInMessageObservation(this.plugIn.getName());
            this.msgObservation.setMessage(var1.toString() + "");
            this.plugIn.updateObservers(this.msgObservation);
         } else {
            System.out.println(var1);
         }
      }

   }

   public File getDomainDirectory() {
      File var1 = null;
      if (this.context != null) {
         var1 = (File)this.context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
      }

      if (var1 == null) {
         String var2 = DomainDir.getRootDir();
         if (var2 == null) {
            var2 = ".";
         }

         var1 = new File(var2);
      }

      return var1;
   }

   public DomainMBean getDomainMBean() {
      DomainMBean var1 = null;
      if (this.context != null) {
         var1 = (DomainMBean)this.context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      }

      return var1;
   }

   public PlugIn getPlugin() {
      return this.plugIn;
   }

   public PlugInContext getPlugInContext() {
      return this.context;
   }
}
