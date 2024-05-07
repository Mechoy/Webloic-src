package weblogic.diagnostics.harvester.internal;

import java.io.IOException;
import java.security.AccessController;
import javax.management.MBeanException;
import javax.management.Notification;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.harvester.HarvesterException;
import weblogic.diagnostics.harvester.HarvesterRuntimeException;
import weblogic.diagnostics.harvester.LogSupport;
import weblogic.management.ManagementException;
import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WLDFHarvesterRuntimeMBean;
import weblogic.management.runtime.WLDFRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class HarvesterRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WLDFHarvesterRuntimeMBean {
   private final DebugLogger dbg = DebugSupport.getDebugLogger();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static HarvesterRuntimeMBeanImpl singleton = null;
   private MetricArchiver archiver;
   int notificationSequence = 0;
   NotificationGenerator notificationGenerator;

   static final HarvesterRuntimeMBeanImpl getInstance(MetricArchiver var0) throws ManagementException {
      if (singleton == null) {
         WLDFRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getWLDFRuntime();
         singleton = new HarvesterRuntimeMBeanImpl(var0, var1);
      }

      return singleton;
   }

   private HarvesterRuntimeMBeanImpl(MetricArchiver var1, WLDFRuntimeMBean var2) throws ManagementException {
      super("WLDFHarvesterRuntime", var2);
      var2.setWLDFHarvesterRuntime(this);
   }

   public long getTotalSamplingCycles() {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? (long)var1.getTotalSamplingCycles() : 0L;
   }

   public long getMinimumSamplingTime() {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getMinimumSamplingTime() : 0L;
   }

   public long getMaximumSamplingTime() {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getMaximumSamplingTime() : 0L;
   }

   public long getTotalSamplingTime() {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getTotalSamplingTime() : 0L;
   }

   public long getAverageSamplingTime() {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? this.getArchiver().getAverageSamplingTime() : 0L;
   }

   public long getTotalDataSampleCount() throws HarvesterException.HarvestingNotEnabled {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? this.getArchiver().getTotalConfiguredDataSampleCount() : 0L;
   }

   public long getTotalImplicitDataSampleCount() throws HarvesterException.HarvestingNotEnabled {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getTotalImplicitDataSampleCount() : 0L;
   }

   public String[] getConfiguredNamespaces() throws HarvesterException.HarvestingNotEnabled {
      String[] var1 = this.getArchiver().getConfiguredNamespaces();
      return var1;
   }

   public String getDefaultNamespace() throws HarvesterException.HarvestingNotEnabled {
      String var1 = this.getArchiver().getDefaultNamespace();
      return var1;
   }

   public long getCurrentDataSampleCount() throws HarvesterException.HarvestingNotEnabled {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? (long)var1.getCurrentConfiguredDataSampleCount() : 0L;
   }

   public long getCurrentImplicitDataSampleCount() throws HarvesterException.HarvestingNotEnabled {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getCurrentImplicitDataSampleCount() : 0L;
   }

   public long getCurrentSnapshotStartTime() throws HarvesterException.HarvestingNotEnabled {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getCurrentSnapshotStartTime() : -1L;
   }

   public long getCurrentSnapshotElapsedTime() throws HarvesterException.HarvestingNotEnabled {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getCurrentSnapshotElapsedTime() : 0L;
   }

   public long getSamplePeriod() throws HarvesterException.HarvestingNotEnabled {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getSamplePeriod() : 0L;
   }

   public String getHarvestableType(String var1) throws HarvesterException.HarvestableInstancesNotFoundException, HarvesterException.AmbiguousInstanceName {
      String var2 = this.getArchiver().getHarvestableType(var1);
      return var2;
   }

   public String[] getCurrentlyHarvestedAttributes(String var1) throws HarvesterException.AmbiguousTypeName, HarvesterException.HarvestingNotEnabled, HarvesterException.MissingConfigurationType {
      String[] var2 = new String[0];
      MetricArchiver var3 = this.getArchiver();
      if (var3.isEnabled()) {
         var2 = var3.getCurrentlyHarvestedAttributes(var1);
      }

      return var2;
   }

   public String[] getCurrentlyHarvestedInstances(String var1) throws HarvesterException.MissingConfigurationType, HarvesterException.HarvestingNotEnabled {
      String[] var2 = new String[0];
      MetricArchiver var3 = this.getArchiver();
      if (var3.isEnabled()) {
         var2 = var3.getCurrentlyHarvestedInstances(var1);
      }

      return var2;
   }

   public String[][] getHarvestableAttributes(String var1) throws HarvesterException.AmbiguousTypeName, HarvesterException.HarvestableTypesNotFoundException, HarvesterException.TypeNotHarvestable {
      String[][] var2 = new String[0][];

      try {
         var2 = this.getArchiver().getHarvestableAttributes(var1);
         return var2;
      } catch (IOException var4) {
         throw new HarvesterRuntimeException(var4);
      }
   }

   public String[][] getHarvestableAttributesForInstance(String var1) throws HarvesterException.AmbiguousTypeName, HarvesterException.HarvestableTypesNotFoundException, HarvesterException.TypeNotHarvestable {
      String[][] var2 = (String[][])null;
      String var3 = null;

      try {
         var3 = this.getArchiver().getHarvestableType(var1);
         if (var3 != null) {
            var2 = this.getHarvestableAttributes(var3);
         }
      } catch (HarvesterException.HarvestableInstancesNotFoundException var5) {
         if (this.dbg.isDebugEnabled()) {
            this.dbg.debug("No harvestable instances found for derived type name " + var3, var5);
         }
      } catch (HarvesterException.AmbiguousInstanceName var6) {
         if (this.dbg.isDebugEnabled()) {
            this.dbg.debug("Ambiguous instance name: " + var1, var6);
         }
      }

      return var2;
   }

   public String[] getKnownHarvestableTypes() {
      String[] var1 = this.getArchiver().getKnownHarvestableTypes();
      return var1;
   }

   public String[] getKnownHarvestableTypes(String var1) {
      String[] var2 = this.getArchiver().getKnownHarvestableTypes(var1);
      return var2;
   }

   public String[] getKnownHarvestableInstances(String var1) throws HarvesterException.AmbiguousTypeName, HarvesterException.HarvestableTypesNotFoundException, HarvesterException.TypeNotHarvestable {
      String[] var2 = this.getArchiver().getKnownHarvestableInstances(var1);
      return var2;
   }

   public String[] getKnownHarvestableInstances(String var1, String var2) throws HarvesterException.HarvestableTypesNotFoundException, HarvesterException.AmbiguousTypeName, HarvesterException.TypeNotHarvestable {
      String[] var3 = this.getArchiver().getKnownHarvestableInstances(var1, var2);
      return var3;
   }

   public long getTotalSamplingTimeOutlierCount() {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.getTotalSamplingTimeOutlierCount() : 0L;
   }

   public boolean isCurrentSampleTimeAnOutlier() {
      MetricArchiver var1 = this.getArchiver();
      return var1.isEnabled() ? var1.isCurrentSampleTimeAnOutlier() : false;
   }

   public float getOutlierDetectionFactor() {
      MetricArchiver var1 = this.getArchiver();
      return var1 != null ? var1.getOutlierDetectionFactor() : 0.0F;
   }

   private MetricArchiver getArchiver() {
      if (this.archiver == null) {
         this.archiver = MetricArchiver.getInstance();
      }

      return this.archiver;
   }

   void harvestCycleOccurred(long var1) {
      ++this.notificationSequence;

      try {
         if (this.notificationGenerator != null) {
            if (this.dbg.isDebugEnabled()) {
               this.dbg.debug("Harvester runtime MBean is sending notification. ");
            }

            this.notificationGenerator.sendNotification(new Notification("weblogic.diagnostics.harvester.cycleCompleted", this.notificationGenerator.getObjectName(), (long)this.notificationSequence, var1));
         }
      } catch (MBeanException var4) {
         LogSupport.logUnexpectedException("Harvest cycle notification failed with exception.", var4);
      }

   }

   void setNotificationGenerator(NotificationGenerator var1) {
      this.notificationGenerator = var1;
   }
}
