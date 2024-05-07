package weblogic.diagnostics.watch;

import java.io.OutputStream;
import java.util.Date;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.diagnostics.image.descriptor.WatchAlarmStateBean;
import weblogic.diagnostics.image.descriptor.WatchImageSourceBean;
import weblogic.management.ManagementException;

class WatchSource implements ImageSource {
   private WatchImageSourceBean root;
   private boolean timeoutRequested;

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      DescriptorManager var2 = new DescriptorManager();
      Descriptor var3 = var2.createDescriptorRoot(WatchImageSourceBean.class);
      this.root = (WatchImageSourceBean)var3.getRootBean();

      try {
         this.writeWatchAlarms();
         var2.writeDescriptorBeanAsXML((DescriptorBean)this.root, var1);
      } catch (Exception var5) {
         throw new ImageSourceCreationException(var5);
      }
   }

   public void timeoutImageCreation() {
      this.timeoutRequested = true;
   }

   private void writeWatchAlarms() throws ManagementException {
      Watch[] var1 = WatchManager.getInstance().getActiveAlarmWatches();

      for(int var2 = 0; var1 != null && var2 < var1.length && !this.timeoutRequested; ++var2) {
         Watch var3 = var1[var2];
         WatchAlarmStateBean var4 = this.root.createWatchAlarmState();
         var4.setWatchName(var3.getWatchName());
         if (var3.getAlarmType() == 2) {
            var4.setAlarmResetType("AutomaticReset");
            var4.setAlarmResetPeriod((new Date(var3.getResetTime())).toString());
         } else {
            var4.setAlarmResetType("ManualReset");
         }
      }

   }
}
