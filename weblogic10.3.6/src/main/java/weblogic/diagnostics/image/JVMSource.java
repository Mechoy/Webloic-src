package weblogic.diagnostics.image;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.security.AccessController;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.context.DiagnosticContextManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.image.descriptor.JVMRuntimeBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JVMRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.PlatformConstants;
import weblogic.work.ExecuteThread;
import weblogic.work.RequestManager;
import weblogic.work.WorkAdapter;

class JVMSource implements ImageSource {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticImage");
   private JVMRuntimeBean root;
   private RuntimeMXBean mxRuntimeBean = ManagementFactory.getRuntimeMXBean();
   private ThreadMXBean mxThreadBean = ManagementFactory.getThreadMXBean();
   private OperatingSystemMXBean mxOSBean = ManagementFactory.getOperatingSystemMXBean();
   private MemoryMXBean mxMemoryBean = ManagementFactory.getMemoryMXBean();
   private ClassLoadingMXBean mxClassLoadingBean = ManagementFactory.getClassLoadingMXBean();
   private boolean timeoutRequested;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      DescriptorManager var2 = new DescriptorManager();
      Descriptor var3 = var2.createDescriptorRoot(JVMRuntimeBean.class);
      this.root = (JVMRuntimeBean)var3.getRootBean();
      this.writeOSMxBean();
      this.writeClassLoadingMxBean();
      this.writeRuntimeMxBean();
      this.writeMemoryMxBean();
      this.writeThreadMxBean();
      this.writeThreadDump();
      this.writeThreadRequestExecutionDetails();

      try {
         var2.writeDescriptorBeanAsXML((DescriptorBean)this.root, var1);
      } catch (IOException var5) {
         throw new ImageSourceCreationException(var5);
      }
   }

   private void writeThreadRequestExecutionDetails() {
      RequestManager var1 = RequestManager.getInstance();
      ExecuteThread[] var2 = var1.getAllThreads();
      StringBuffer var3 = new StringBuffer();
      ExecuteThread[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ExecuteThread var7 = var4[var6];
         var3.append("Thread name:");
         var3.append(var7.getName());
         long var8 = var7.getId();
         var3.append(", ThreadID: ");
         var3.append(var8);
         String var10 = DiagnosticContextManager.getDiagnosticContextId(var8);
         var3.append(", ECID: ");
         var3.append(var10);
         var3.append(", Work:");
         WorkAdapter var11 = var7.getCurrentWork();
         if (var11 != null) {
            var3.append('{');
            var3.append(var11);
            var3.append('}');
         } else {
            var3.append(" No work currently allocated to thread.");
         }

         var3.append(PlatformConstants.EOL);
      }

      this.root.setThreadRequestExecutionDetails(var3.toString());
   }

   public void timeoutImageCreation() {
      this.timeoutRequested = true;
   }

   private void writeOSMxBean() {
      this.root.setOSName(this.mxOSBean.getName());
      this.root.setOSVersion(this.mxOSBean.getVersion());
      this.root.setOSArch(this.mxOSBean.getArch());
      this.root.setOSAvailableProcessors(this.mxOSBean.getAvailableProcessors());
   }

   private void writeClassLoadingMxBean() {
      this.root.setLoadedClassCount(this.mxClassLoadingBean.getLoadedClassCount());
      this.root.setTotalLoadedClassCount(this.mxClassLoadingBean.getTotalLoadedClassCount());
      this.root.setUnloadedClassCount(this.mxClassLoadingBean.getUnloadedClassCount());
   }

   private void writeRuntimeMxBean() {
      this.root.setRunningJVMName(this.mxRuntimeBean.getName());
      this.root.setManagementSpecVersion(this.mxRuntimeBean.getManagementSpecVersion());
      this.root.setVmName(this.mxRuntimeBean.getVmName());
      this.root.setVmVendor(this.mxRuntimeBean.getVmVendor());
      this.root.setVmVersion(this.mxRuntimeBean.getVmVersion());
      this.root.setSpecName(this.mxRuntimeBean.getSpecName());
      this.root.setSpecVendor(this.mxRuntimeBean.getSpecVendor());
      this.root.setSpecVersion(this.mxRuntimeBean.getSpecVersion());
      this.root.setClassPath(this.mxRuntimeBean.getClassPath());
      this.root.setLibraryPath(this.mxRuntimeBean.getLibraryPath());
      this.root.setBootClassPath(this.mxRuntimeBean.getBootClassPath());
      this.root.setUptime(this.mxRuntimeBean.getUptime());
      this.root.setStartTime(this.mxRuntimeBean.getStartTime());
      this.root.setBootClassPathSupported(this.mxRuntimeBean.isBootClassPathSupported());
   }

   private void writeMemoryMxBean() {
      this.root.setObjectPendingFinalizationCount(this.mxMemoryBean.getObjectPendingFinalizationCount());
      this.root.setHeapMemoryInitBytes(this.mxMemoryBean.getHeapMemoryUsage().getInit());
      this.root.setHeapMemoryUsedBytes(this.mxMemoryBean.getHeapMemoryUsage().getUsed());
      this.root.setHeapMemoryCommittedBytes(this.mxMemoryBean.getHeapMemoryUsage().getCommitted());
      this.root.setHeapMemoryMaxBytes(this.mxMemoryBean.getHeapMemoryUsage().getMax());
      this.root.setNonHeapMemoryInitBytes(this.mxMemoryBean.getNonHeapMemoryUsage().getInit());
      this.root.setNonHeapMemoryUsedBytes(this.mxMemoryBean.getNonHeapMemoryUsage().getUsed());
      this.root.setNonHeapMemoryCommittedBytes(this.mxMemoryBean.getNonHeapMemoryUsage().getCommitted());
      this.root.setNonHeapMemoryMaxBytes(this.mxMemoryBean.getNonHeapMemoryUsage().getMax());
   }

   private void writeThreadMxBean() {
      this.root.setThreadCount(this.mxThreadBean.getThreadCount());
      this.root.setPeakThreadCount(this.mxThreadBean.getPeakThreadCount());
      this.root.setTotalStartedThreadCount(this.mxThreadBean.getTotalStartedThreadCount());
      this.root.setDaemonThreadCount(this.mxThreadBean.getDaemonThreadCount());
      this.root.setThreadContentionMonitoringSupported(this.mxThreadBean.isThreadContentionMonitoringSupported());
      this.root.setThreadContentionMonitoringEnabled(this.mxThreadBean.isThreadContentionMonitoringEnabled());
      this.root.setCurrentThreadCpuTime(this.mxThreadBean.getCurrentThreadCpuTime());
      this.root.setCurrentThreadUserTime(this.mxThreadBean.getCurrentThreadUserTime());
      this.root.setThreadCpuTimeSupported(this.mxThreadBean.isThreadCpuTimeSupported());
      this.root.setThreadCpuTimeEnabled(this.mxThreadBean.isThreadCpuTimeEnabled());
      this.root.setCurrentThreadCpuTimeSupported(this.mxThreadBean.isCurrentThreadCpuTimeSupported());
   }

   private void writeThreadDump() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("JVMSource Adding ThreadDump using JVMRuntime MBean");
      }

      JVMRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getJVMRuntime();
      this.root.setThreadDump(var1.getThreadStackDump());
   }
}
