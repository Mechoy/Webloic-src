package weblogic.upgrade.simple;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;

public class SimplestPlugInEver extends AbstractPlugIn {
   public SimplestPlugInEver(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void execute() throws PlugInException {
      String var1 = "This is working ... (I will do i18n later)";
      String var2 = "This is working (2)... (I will do i18n later)";
      PlugInMessageObservation var3 = new PlugInMessageObservation(this.getName());
      var3.setMessage(var1);
      this.updateObservers(var3);

      try {
         Thread.sleep(2000L);
      } catch (Exception var5) {
      }

      this.updateObservers(var3.setMessage(var2));
   }
}
