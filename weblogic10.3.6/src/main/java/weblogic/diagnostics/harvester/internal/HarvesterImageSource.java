package weblogic.diagnostics.harvester.internal;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.harvester.HarvesterDataSample;
import weblogic.diagnostics.harvester.LogSupport;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.diagnostics.image.descriptor.HarvesterImageSourceBean;
import weblogic.diagnostics.utils.DateUtils;

class HarvesterImageSource implements ImageSource {
   private HarvesterImageSourceBean root;
   private boolean timeoutRequested;
   private static final long NANOS_PER_MILLI = 1000000L;

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      DescriptorManager var2 = new DescriptorManager();
      Descriptor var3 = var2.createDescriptorRoot(HarvesterImageSourceBean.class);
      this.root = (HarvesterImageSourceBean)var3.getRootBean();
      this.writeHarvesterSamples();

      try {
         var2.writeDescriptorBeanAsXML((DescriptorBean)this.root, var1);
      } catch (Exception var5) {
         throw new ImageSourceCreationException(var5);
      }
   }

   public void timeoutImageCreation() {
      this.timeoutRequested = true;
   }

   private void writeHarvesterSamples() {
      try {
         MetricArchiver var1 = MetricArchiver.getInstance();
         HarvesterSnapshot var2 = var1.getCurrentSnapshot();
         if (var2 == null) {
            return;
         }

         long var3 = var2.getSnapshotStartTimeMillis();
         var3 *= 1000000L;
         this.root.setHarvesterCycleStartTime(DateUtils.nanoDateToString(var3));
         long var5 = var2.getSnapshotElapsedTimeNanos();
         this.root.setHarvesterCycleDurationNanos(var5);
         Collection var7 = var2.getHarvesterDataSamples();
         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            HarvesterDataSample var9 = (HarvesterDataSample)var8.next();
            this.root.addHarvesterSample(var9.toStringLong());
         }
      } catch (Exception var10) {
         LogSupport.logUnexpectedException("Error in HarvesterImageSource.", var10);
      }

   }
}
