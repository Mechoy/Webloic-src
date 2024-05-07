package weblogic.diagnostics.image.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class JVMRuntimeBeanImpl extends AbstractDescriptorBean implements JVMRuntimeBean, Serializable {
   private String _BootClassPath;
   private boolean _BootClassPathSupported;
   private String _ClassPath;
   private long _CurrentThreadCpuTime;
   private boolean _CurrentThreadCpuTimeSupported;
   private long _CurrentThreadUserTime;
   private int _DaemonThreadCount;
   private long _HeapMemoryCommittedBytes;
   private long _HeapMemoryInitBytes;
   private long _HeapMemoryMaxBytes;
   private long _HeapMemoryUsedBytes;
   private String _LibraryPath;
   private int _LoadedClassCount;
   private String _ManagementSpecVersion;
   private long _NonHeapMemoryCommittedBytes;
   private long _NonHeapMemoryInitBytes;
   private long _NonHeapMemoryMaxBytes;
   private long _NonHeapMemoryUsedBytes;
   private String _OSArch;
   private int _OSAvailableProcessors;
   private String _OSName;
   private String _OSVersion;
   private int _ObjectPendingFinalizationCount;
   private int _PeakThreadCount;
   private String _RunningJVMName;
   private String _SpecName;
   private String _SpecVendor;
   private String _SpecVersion;
   private long _StartTime;
   private boolean _ThreadContentionMonitoringEnabled;
   private boolean _ThreadContentionMonitoringSupported;
   private int _ThreadCount;
   private boolean _ThreadCpuTimeEnabled;
   private boolean _ThreadCpuTimeSupported;
   private String _ThreadDump;
   private String _ThreadRequestExecutionDetails;
   private long _TotalLoadedClassCount;
   private long _TotalStartedThreadCount;
   private long _UnloadedClassCount;
   private long _Uptime;
   private String _VmName;
   private String _VmVendor;
   private String _VmVersion;
   private static SchemaHelper2 _schemaHelper;

   public JVMRuntimeBeanImpl() {
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public JVMRuntimeBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public void setObjectPendingFinalizationCount(int var1) {
      int var2 = this._ObjectPendingFinalizationCount;
      this._ObjectPendingFinalizationCount = var1;
      this._postSet(0, var2, var1);
   }

   public int getObjectPendingFinalizationCount() {
      return this._ObjectPendingFinalizationCount;
   }

   public boolean isObjectPendingFinalizationCountSet() {
      return this._isSet(0);
   }

   public void setHeapMemoryUsedBytes(long var1) {
      long var3 = this._HeapMemoryUsedBytes;
      this._HeapMemoryUsedBytes = var1;
      this._postSet(1, var3, var1);
   }

   public long getHeapMemoryUsedBytes() {
      return this._HeapMemoryUsedBytes;
   }

   public boolean isHeapMemoryUsedBytesSet() {
      return this._isSet(1);
   }

   public void setHeapMemoryMaxBytes(long var1) {
      long var3 = this._HeapMemoryMaxBytes;
      this._HeapMemoryMaxBytes = var1;
      this._postSet(2, var3, var1);
   }

   public long getHeapMemoryMaxBytes() {
      return this._HeapMemoryMaxBytes;
   }

   public boolean isHeapMemoryMaxBytesSet() {
      return this._isSet(2);
   }

   public void setHeapMemoryInitBytes(long var1) {
      long var3 = this._HeapMemoryInitBytes;
      this._HeapMemoryInitBytes = var1;
      this._postSet(3, var3, var1);
   }

   public long getHeapMemoryInitBytes() {
      return this._HeapMemoryInitBytes;
   }

   public boolean isHeapMemoryInitBytesSet() {
      return this._isSet(3);
   }

   public void setHeapMemoryCommittedBytes(long var1) {
      long var3 = this._HeapMemoryCommittedBytes;
      this._HeapMemoryCommittedBytes = var1;
      this._postSet(4, var3, var1);
   }

   public long getHeapMemoryCommittedBytes() {
      return this._HeapMemoryCommittedBytes;
   }

   public boolean isHeapMemoryCommittedBytesSet() {
      return this._isSet(4);
   }

   public void setNonHeapMemoryUsedBytes(long var1) {
      long var3 = this._NonHeapMemoryUsedBytes;
      this._NonHeapMemoryUsedBytes = var1;
      this._postSet(5, var3, var1);
   }

   public long getNonHeapMemoryUsedBytes() {
      return this._NonHeapMemoryUsedBytes;
   }

   public boolean isNonHeapMemoryUsedBytesSet() {
      return this._isSet(5);
   }

   public void setNonHeapMemoryMaxBytes(long var1) {
      long var3 = this._NonHeapMemoryMaxBytes;
      this._NonHeapMemoryMaxBytes = var1;
      this._postSet(6, var3, var1);
   }

   public long getNonHeapMemoryMaxBytes() {
      return this._NonHeapMemoryMaxBytes;
   }

   public boolean isNonHeapMemoryMaxBytesSet() {
      return this._isSet(6);
   }

   public void setNonHeapMemoryInitBytes(long var1) {
      long var3 = this._NonHeapMemoryInitBytes;
      this._NonHeapMemoryInitBytes = var1;
      this._postSet(7, var3, var1);
   }

   public long getNonHeapMemoryInitBytes() {
      return this._NonHeapMemoryInitBytes;
   }

   public boolean isNonHeapMemoryInitBytesSet() {
      return this._isSet(7);
   }

   public void setNonHeapMemoryCommittedBytes(long var1) {
      long var3 = this._NonHeapMemoryCommittedBytes;
      this._NonHeapMemoryCommittedBytes = var1;
      this._postSet(8, var3, var1);
   }

   public long getNonHeapMemoryCommittedBytes() {
      return this._NonHeapMemoryCommittedBytes;
   }

   public boolean isNonHeapMemoryCommittedBytesSet() {
      return this._isSet(8);
   }

   public void setThreadCount(int var1) {
      int var2 = this._ThreadCount;
      this._ThreadCount = var1;
      this._postSet(9, var2, var1);
   }

   public int getThreadCount() {
      return this._ThreadCount;
   }

   public boolean isThreadCountSet() {
      return this._isSet(9);
   }

   public void setPeakThreadCount(int var1) {
      int var2 = this._PeakThreadCount;
      this._PeakThreadCount = var1;
      this._postSet(10, var2, var1);
   }

   public int getPeakThreadCount() {
      return this._PeakThreadCount;
   }

   public boolean isPeakThreadCountSet() {
      return this._isSet(10);
   }

   public void setTotalStartedThreadCount(long var1) {
      long var3 = this._TotalStartedThreadCount;
      this._TotalStartedThreadCount = var1;
      this._postSet(11, var3, var1);
   }

   public long getTotalStartedThreadCount() {
      return this._TotalStartedThreadCount;
   }

   public boolean isTotalStartedThreadCountSet() {
      return this._isSet(11);
   }

   public void setDaemonThreadCount(int var1) {
      int var2 = this._DaemonThreadCount;
      this._DaemonThreadCount = var1;
      this._postSet(12, var2, var1);
   }

   public int getDaemonThreadCount() {
      return this._DaemonThreadCount;
   }

   public boolean isDaemonThreadCountSet() {
      return this._isSet(12);
   }

   public void setThreadContentionMonitoringSupported(boolean var1) {
      boolean var2 = this._ThreadContentionMonitoringSupported;
      this._ThreadContentionMonitoringSupported = var1;
      this._postSet(13, var2, var1);
   }

   public boolean isThreadContentionMonitoringSupported() {
      return this._ThreadContentionMonitoringSupported;
   }

   public boolean isThreadContentionMonitoringSupportedSet() {
      return this._isSet(13);
   }

   public void setThreadContentionMonitoringEnabled(boolean var1) {
      boolean var2 = this._ThreadContentionMonitoringEnabled;
      this._ThreadContentionMonitoringEnabled = var1;
      this._postSet(14, var2, var1);
   }

   public boolean isThreadContentionMonitoringEnabled() {
      return this._ThreadContentionMonitoringEnabled;
   }

   public boolean isThreadContentionMonitoringEnabledSet() {
      return this._isSet(14);
   }

   public void setCurrentThreadCpuTime(long var1) {
      long var3 = this._CurrentThreadCpuTime;
      this._CurrentThreadCpuTime = var1;
      this._postSet(15, var3, var1);
   }

   public long getCurrentThreadCpuTime() {
      return this._CurrentThreadCpuTime;
   }

   public boolean isCurrentThreadCpuTimeSet() {
      return this._isSet(15);
   }

   public void setCurrentThreadUserTime(long var1) {
      long var3 = this._CurrentThreadUserTime;
      this._CurrentThreadUserTime = var1;
      this._postSet(16, var3, var1);
   }

   public long getCurrentThreadUserTime() {
      return this._CurrentThreadUserTime;
   }

   public boolean isCurrentThreadUserTimeSet() {
      return this._isSet(16);
   }

   public void setThreadCpuTimeSupported(boolean var1) {
      boolean var2 = this._ThreadCpuTimeSupported;
      this._ThreadCpuTimeSupported = var1;
      this._postSet(17, var2, var1);
   }

   public boolean isThreadCpuTimeSupported() {
      return this._ThreadCpuTimeSupported;
   }

   public boolean isThreadCpuTimeSupportedSet() {
      return this._isSet(17);
   }

   public void setCurrentThreadCpuTimeSupported(boolean var1) {
      boolean var2 = this._CurrentThreadCpuTimeSupported;
      this._CurrentThreadCpuTimeSupported = var1;
      this._postSet(18, var2, var1);
   }

   public boolean isCurrentThreadCpuTimeSupported() {
      return this._CurrentThreadCpuTimeSupported;
   }

   public boolean isCurrentThreadCpuTimeSupportedSet() {
      return this._isSet(18);
   }

   public void setThreadCpuTimeEnabled(boolean var1) {
      boolean var2 = this._ThreadCpuTimeEnabled;
      this._ThreadCpuTimeEnabled = var1;
      this._postSet(19, var2, var1);
   }

   public boolean isThreadCpuTimeEnabled() {
      return this._ThreadCpuTimeEnabled;
   }

   public boolean isThreadCpuTimeEnabledSet() {
      return this._isSet(19);
   }

   public void setRunningJVMName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RunningJVMName;
      this._RunningJVMName = var1;
      this._postSet(20, var2, var1);
   }

   public String getRunningJVMName() {
      return this._RunningJVMName;
   }

   public boolean isRunningJVMNameSet() {
      return this._isSet(20);
   }

   public void setManagementSpecVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ManagementSpecVersion;
      this._ManagementSpecVersion = var1;
      this._postSet(21, var2, var1);
   }

   public String getManagementSpecVersion() {
      return this._ManagementSpecVersion;
   }

   public boolean isManagementSpecVersionSet() {
      return this._isSet(21);
   }

   public void setVmName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._VmName;
      this._VmName = var1;
      this._postSet(22, var2, var1);
   }

   public String getVmName() {
      return this._VmName;
   }

   public boolean isVmNameSet() {
      return this._isSet(22);
   }

   public void setVmVendor(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._VmVendor;
      this._VmVendor = var1;
      this._postSet(23, var2, var1);
   }

   public String getVmVendor() {
      return this._VmVendor;
   }

   public boolean isVmVendorSet() {
      return this._isSet(23);
   }

   public void setVmVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._VmVersion;
      this._VmVersion = var1;
      this._postSet(24, var2, var1);
   }

   public String getVmVersion() {
      return this._VmVersion;
   }

   public boolean isVmVersionSet() {
      return this._isSet(24);
   }

   public void setSpecName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SpecName;
      this._SpecName = var1;
      this._postSet(25, var2, var1);
   }

   public String getSpecName() {
      return this._SpecName;
   }

   public boolean isSpecNameSet() {
      return this._isSet(25);
   }

   public void setSpecVendor(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SpecVendor;
      this._SpecVendor = var1;
      this._postSet(26, var2, var1);
   }

   public String getSpecVendor() {
      return this._SpecVendor;
   }

   public boolean isSpecVendorSet() {
      return this._isSet(26);
   }

   public void setSpecVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SpecVersion;
      this._SpecVersion = var1;
      this._postSet(27, var2, var1);
   }

   public String getSpecVersion() {
      return this._SpecVersion;
   }

   public boolean isSpecVersionSet() {
      return this._isSet(27);
   }

   public void setClassPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ClassPath;
      this._ClassPath = var1;
      this._postSet(28, var2, var1);
   }

   public String getClassPath() {
      return this._ClassPath;
   }

   public boolean isClassPathSet() {
      return this._isSet(28);
   }

   public void setLibraryPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._LibraryPath;
      this._LibraryPath = var1;
      this._postSet(29, var2, var1);
   }

   public String getLibraryPath() {
      return this._LibraryPath;
   }

   public boolean isLibraryPathSet() {
      return this._isSet(29);
   }

   public void setBootClassPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._BootClassPath;
      this._BootClassPath = var1;
      this._postSet(30, var2, var1);
   }

   public String getBootClassPath() {
      return this._BootClassPath;
   }

   public boolean isBootClassPathSet() {
      return this._isSet(30);
   }

   public void setUptime(long var1) {
      long var3 = this._Uptime;
      this._Uptime = var1;
      this._postSet(31, var3, var1);
   }

   public long getUptime() {
      return this._Uptime;
   }

   public boolean isUptimeSet() {
      return this._isSet(31);
   }

   public void setStartTime(long var1) {
      long var3 = this._StartTime;
      this._StartTime = var1;
      this._postSet(32, var3, var1);
   }

   public long getStartTime() {
      return this._StartTime;
   }

   public boolean isStartTimeSet() {
      return this._isSet(32);
   }

   public void setBootClassPathSupported(boolean var1) {
      boolean var2 = this._BootClassPathSupported;
      this._BootClassPathSupported = var1;
      this._postSet(33, var2, var1);
   }

   public boolean isBootClassPathSupported() {
      return this._BootClassPathSupported;
   }

   public boolean isBootClassPathSupportedSet() {
      return this._isSet(33);
   }

   public void setOSName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OSName;
      this._OSName = var1;
      this._postSet(34, var2, var1);
   }

   public String getOSName() {
      return this._OSName;
   }

   public boolean isOSNameSet() {
      return this._isSet(34);
   }

   public void setOSVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OSVersion;
      this._OSVersion = var1;
      this._postSet(35, var2, var1);
   }

   public String getOSVersion() {
      return this._OSVersion;
   }

   public boolean isOSVersionSet() {
      return this._isSet(35);
   }

   public void setOSArch(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OSArch;
      this._OSArch = var1;
      this._postSet(36, var2, var1);
   }

   public String getOSArch() {
      return this._OSArch;
   }

   public boolean isOSArchSet() {
      return this._isSet(36);
   }

   public void setOSAvailableProcessors(int var1) {
      int var2 = this._OSAvailableProcessors;
      this._OSAvailableProcessors = var1;
      this._postSet(37, var2, var1);
   }

   public int getOSAvailableProcessors() {
      return this._OSAvailableProcessors;
   }

   public boolean isOSAvailableProcessorsSet() {
      return this._isSet(37);
   }

   public void setLoadedClassCount(int var1) {
      int var2 = this._LoadedClassCount;
      this._LoadedClassCount = var1;
      this._postSet(38, var2, var1);
   }

   public int getLoadedClassCount() {
      return this._LoadedClassCount;
   }

   public boolean isLoadedClassCountSet() {
      return this._isSet(38);
   }

   public void setTotalLoadedClassCount(long var1) {
      long var3 = this._TotalLoadedClassCount;
      this._TotalLoadedClassCount = var1;
      this._postSet(39, var3, var1);
   }

   public long getTotalLoadedClassCount() {
      return this._TotalLoadedClassCount;
   }

   public boolean isTotalLoadedClassCountSet() {
      return this._isSet(39);
   }

   public void setUnloadedClassCount(long var1) {
      long var3 = this._UnloadedClassCount;
      this._UnloadedClassCount = var1;
      this._postSet(40, var3, var1);
   }

   public long getUnloadedClassCount() {
      return this._UnloadedClassCount;
   }

   public boolean isUnloadedClassCountSet() {
      return this._isSet(40);
   }

   public String getThreadDump() {
      return this._ThreadDump;
   }

   public boolean isThreadDumpSet() {
      return this._isSet(41);
   }

   public void setThreadDump(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ThreadDump;
      this._ThreadDump = var1;
      this._postSet(41, var2, var1);
   }

   public String getThreadRequestExecutionDetails() {
      return this._ThreadRequestExecutionDetails;
   }

   public boolean isThreadRequestExecutionDetailsSet() {
      return this._isSet(42);
   }

   public void setThreadRequestExecutionDetails(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ThreadRequestExecutionDetails;
      this._ThreadRequestExecutionDetails = var1;
      this._postSet(42, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 30;
      }

      try {
         switch (var1) {
            case 30:
               this._BootClassPath = null;
               if (var2) {
                  break;
               }
            case 28:
               this._ClassPath = null;
               if (var2) {
                  break;
               }
            case 15:
               this._CurrentThreadCpuTime = 0L;
               if (var2) {
                  break;
               }
            case 16:
               this._CurrentThreadUserTime = 0L;
               if (var2) {
                  break;
               }
            case 12:
               this._DaemonThreadCount = 0;
               if (var2) {
                  break;
               }
            case 4:
               this._HeapMemoryCommittedBytes = 0L;
               if (var2) {
                  break;
               }
            case 3:
               this._HeapMemoryInitBytes = 0L;
               if (var2) {
                  break;
               }
            case 2:
               this._HeapMemoryMaxBytes = 0L;
               if (var2) {
                  break;
               }
            case 1:
               this._HeapMemoryUsedBytes = 0L;
               if (var2) {
                  break;
               }
            case 29:
               this._LibraryPath = null;
               if (var2) {
                  break;
               }
            case 38:
               this._LoadedClassCount = 0;
               if (var2) {
                  break;
               }
            case 21:
               this._ManagementSpecVersion = null;
               if (var2) {
                  break;
               }
            case 8:
               this._NonHeapMemoryCommittedBytes = 0L;
               if (var2) {
                  break;
               }
            case 7:
               this._NonHeapMemoryInitBytes = 0L;
               if (var2) {
                  break;
               }
            case 6:
               this._NonHeapMemoryMaxBytes = 0L;
               if (var2) {
                  break;
               }
            case 5:
               this._NonHeapMemoryUsedBytes = 0L;
               if (var2) {
                  break;
               }
            case 36:
               this._OSArch = null;
               if (var2) {
                  break;
               }
            case 37:
               this._OSAvailableProcessors = 0;
               if (var2) {
                  break;
               }
            case 34:
               this._OSName = null;
               if (var2) {
                  break;
               }
            case 35:
               this._OSVersion = null;
               if (var2) {
                  break;
               }
            case 0:
               this._ObjectPendingFinalizationCount = 0;
               if (var2) {
                  break;
               }
            case 10:
               this._PeakThreadCount = 0;
               if (var2) {
                  break;
               }
            case 20:
               this._RunningJVMName = null;
               if (var2) {
                  break;
               }
            case 25:
               this._SpecName = null;
               if (var2) {
                  break;
               }
            case 26:
               this._SpecVendor = null;
               if (var2) {
                  break;
               }
            case 27:
               this._SpecVersion = null;
               if (var2) {
                  break;
               }
            case 32:
               this._StartTime = 0L;
               if (var2) {
                  break;
               }
            case 9:
               this._ThreadCount = 0;
               if (var2) {
                  break;
               }
            case 41:
               this._ThreadDump = null;
               if (var2) {
                  break;
               }
            case 42:
               this._ThreadRequestExecutionDetails = null;
               if (var2) {
                  break;
               }
            case 39:
               this._TotalLoadedClassCount = 0L;
               if (var2) {
                  break;
               }
            case 11:
               this._TotalStartedThreadCount = 0L;
               if (var2) {
                  break;
               }
            case 40:
               this._UnloadedClassCount = 0L;
               if (var2) {
                  break;
               }
            case 31:
               this._Uptime = 0L;
               if (var2) {
                  break;
               }
            case 22:
               this._VmName = null;
               if (var2) {
                  break;
               }
            case 23:
               this._VmVendor = null;
               if (var2) {
                  break;
               }
            case 24:
               this._VmVersion = null;
               if (var2) {
                  break;
               }
            case 33:
               this._BootClassPathSupported = false;
               if (var2) {
                  break;
               }
            case 18:
               this._CurrentThreadCpuTimeSupported = false;
               if (var2) {
                  break;
               }
            case 14:
               this._ThreadContentionMonitoringEnabled = false;
               if (var2) {
                  break;
               }
            case 13:
               this._ThreadContentionMonitoringSupported = false;
               if (var2) {
                  break;
               }
            case 19:
               this._ThreadCpuTimeEnabled = false;
               if (var2) {
                  break;
               }
            case 17:
               this._ThreadCpuTimeSupported = false;
               if (var2) {
                  break;
               }
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image/1.0/weblogic-diagnostics-image.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("uptime")) {
                  return 31;
               }
               break;
            case 7:
               if (var1.equals("os-arch")) {
                  return 36;
               }

               if (var1.equals("os-name")) {
                  return 34;
               }

               if (var1.equals("vm-name")) {
                  return 22;
               }
            case 8:
            case 13:
            case 14:
            case 28:
            case 29:
            case 30:
            case 34:
            case 35:
            case 37:
            default:
               break;
            case 9:
               if (var1.equals("spec-name")) {
                  return 25;
               }

               if (var1.equals("vm-vendor")) {
                  return 23;
               }
               break;
            case 10:
               if (var1.equals("class-path")) {
                  return 28;
               }

               if (var1.equals("os-version")) {
                  return 35;
               }

               if (var1.equals("start-time")) {
                  return 32;
               }

               if (var1.equals("vm-version")) {
                  return 24;
               }
               break;
            case 11:
               if (var1.equals("spec-vendor")) {
                  return 26;
               }

               if (var1.equals("thread-dump")) {
                  return 41;
               }
               break;
            case 12:
               if (var1.equals("library-path")) {
                  return 29;
               }

               if (var1.equals("spec-version")) {
                  return 27;
               }

               if (var1.equals("thread-count")) {
                  return 9;
               }
               break;
            case 15:
               if (var1.equals("boot-class-path")) {
                  return 30;
               }
               break;
            case 16:
               if (var1.equals("running-jvm-name")) {
                  return 20;
               }
               break;
            case 17:
               if (var1.equals("peak-thread-count")) {
                  return 10;
               }
               break;
            case 18:
               if (var1.equals("loaded-class-count")) {
                  return 38;
               }
               break;
            case 19:
               if (var1.equals("daemon-thread-count")) {
                  return 12;
               }
               break;
            case 20:
               if (var1.equals("unloaded-class-count")) {
                  return 40;
               }
               break;
            case 21:
               if (var1.equals("heap-memory-max-bytes")) {
                  return 2;
               }
               break;
            case 22:
               if (var1.equals("heap-memory-init-bytes")) {
                  return 3;
               }

               if (var1.equals("heap-memory-used-bytes")) {
                  return 1;
               }
               break;
            case 23:
               if (var1.equals("current-thread-cpu-time")) {
                  return 15;
               }

               if (var1.equals("management-spec-version")) {
                  return 21;
               }

               if (var1.equals("os-available-processors")) {
                  return 37;
               }

               if (var1.equals("thread-cpu-time-enabled")) {
                  return 19;
               }
               break;
            case 24:
               if (var1.equals("current-thread-user-time")) {
                  return 16;
               }

               if (var1.equals("total-loaded-class-count")) {
                  return 39;
               }
               break;
            case 25:
               if (var1.equals("non-heap-memory-max-bytes")) {
                  return 6;
               }

               if (var1.equals("boot-class-path-supported")) {
                  return 33;
               }

               if (var1.equals("thread-cpu-time-supported")) {
                  return 17;
               }
               break;
            case 26:
               if (var1.equals("non-heap-memory-init-bytes")) {
                  return 7;
               }

               if (var1.equals("non-heap-memory-used-bytes")) {
                  return 5;
               }

               if (var1.equals("total-started-thread-count")) {
                  return 11;
               }
               break;
            case 27:
               if (var1.equals("heap-memory-committed-bytes")) {
                  return 4;
               }
               break;
            case 31:
               if (var1.equals("non-heap-memory-committed-bytes")) {
                  return 8;
               }
               break;
            case 32:
               if (var1.equals("thread-request-execution-details")) {
                  return 42;
               }
               break;
            case 33:
               if (var1.equals("object-pending-finalization-count")) {
                  return 0;
               }

               if (var1.equals("current-thread-cpu-time-supported")) {
                  return 18;
               }
               break;
            case 36:
               if (var1.equals("thread-contention-monitoring-enabled")) {
                  return 14;
               }
               break;
            case 38:
               if (var1.equals("thread-contention-monitoring-supported")) {
                  return 13;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getRootElementName() {
         return "jvm-runtime";
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "object-pending-finalization-count";
            case 1:
               return "heap-memory-used-bytes";
            case 2:
               return "heap-memory-max-bytes";
            case 3:
               return "heap-memory-init-bytes";
            case 4:
               return "heap-memory-committed-bytes";
            case 5:
               return "non-heap-memory-used-bytes";
            case 6:
               return "non-heap-memory-max-bytes";
            case 7:
               return "non-heap-memory-init-bytes";
            case 8:
               return "non-heap-memory-committed-bytes";
            case 9:
               return "thread-count";
            case 10:
               return "peak-thread-count";
            case 11:
               return "total-started-thread-count";
            case 12:
               return "daemon-thread-count";
            case 13:
               return "thread-contention-monitoring-supported";
            case 14:
               return "thread-contention-monitoring-enabled";
            case 15:
               return "current-thread-cpu-time";
            case 16:
               return "current-thread-user-time";
            case 17:
               return "thread-cpu-time-supported";
            case 18:
               return "current-thread-cpu-time-supported";
            case 19:
               return "thread-cpu-time-enabled";
            case 20:
               return "running-jvm-name";
            case 21:
               return "management-spec-version";
            case 22:
               return "vm-name";
            case 23:
               return "vm-vendor";
            case 24:
               return "vm-version";
            case 25:
               return "spec-name";
            case 26:
               return "spec-vendor";
            case 27:
               return "spec-version";
            case 28:
               return "class-path";
            case 29:
               return "library-path";
            case 30:
               return "boot-class-path";
            case 31:
               return "uptime";
            case 32:
               return "start-time";
            case 33:
               return "boot-class-path-supported";
            case 34:
               return "os-name";
            case 35:
               return "os-version";
            case 36:
               return "os-arch";
            case 37:
               return "os-available-processors";
            case 38:
               return "loaded-class-count";
            case 39:
               return "total-loaded-class-count";
            case 40:
               return "unloaded-class-count";
            case 41:
               return "thread-dump";
            case 42:
               return "thread-request-execution-details";
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private JVMRuntimeBeanImpl bean;

      protected Helper(JVMRuntimeBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "ObjectPendingFinalizationCount";
            case 1:
               return "HeapMemoryUsedBytes";
            case 2:
               return "HeapMemoryMaxBytes";
            case 3:
               return "HeapMemoryInitBytes";
            case 4:
               return "HeapMemoryCommittedBytes";
            case 5:
               return "NonHeapMemoryUsedBytes";
            case 6:
               return "NonHeapMemoryMaxBytes";
            case 7:
               return "NonHeapMemoryInitBytes";
            case 8:
               return "NonHeapMemoryCommittedBytes";
            case 9:
               return "ThreadCount";
            case 10:
               return "PeakThreadCount";
            case 11:
               return "TotalStartedThreadCount";
            case 12:
               return "DaemonThreadCount";
            case 13:
               return "ThreadContentionMonitoringSupported";
            case 14:
               return "ThreadContentionMonitoringEnabled";
            case 15:
               return "CurrentThreadCpuTime";
            case 16:
               return "CurrentThreadUserTime";
            case 17:
               return "ThreadCpuTimeSupported";
            case 18:
               return "CurrentThreadCpuTimeSupported";
            case 19:
               return "ThreadCpuTimeEnabled";
            case 20:
               return "RunningJVMName";
            case 21:
               return "ManagementSpecVersion";
            case 22:
               return "VmName";
            case 23:
               return "VmVendor";
            case 24:
               return "VmVersion";
            case 25:
               return "SpecName";
            case 26:
               return "SpecVendor";
            case 27:
               return "SpecVersion";
            case 28:
               return "ClassPath";
            case 29:
               return "LibraryPath";
            case 30:
               return "BootClassPath";
            case 31:
               return "Uptime";
            case 32:
               return "StartTime";
            case 33:
               return "BootClassPathSupported";
            case 34:
               return "OSName";
            case 35:
               return "OSVersion";
            case 36:
               return "OSArch";
            case 37:
               return "OSAvailableProcessors";
            case 38:
               return "LoadedClassCount";
            case 39:
               return "TotalLoadedClassCount";
            case 40:
               return "UnloadedClassCount";
            case 41:
               return "ThreadDump";
            case 42:
               return "ThreadRequestExecutionDetails";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BootClassPath")) {
            return 30;
         } else if (var1.equals("ClassPath")) {
            return 28;
         } else if (var1.equals("CurrentThreadCpuTime")) {
            return 15;
         } else if (var1.equals("CurrentThreadUserTime")) {
            return 16;
         } else if (var1.equals("DaemonThreadCount")) {
            return 12;
         } else if (var1.equals("HeapMemoryCommittedBytes")) {
            return 4;
         } else if (var1.equals("HeapMemoryInitBytes")) {
            return 3;
         } else if (var1.equals("HeapMemoryMaxBytes")) {
            return 2;
         } else if (var1.equals("HeapMemoryUsedBytes")) {
            return 1;
         } else if (var1.equals("LibraryPath")) {
            return 29;
         } else if (var1.equals("LoadedClassCount")) {
            return 38;
         } else if (var1.equals("ManagementSpecVersion")) {
            return 21;
         } else if (var1.equals("NonHeapMemoryCommittedBytes")) {
            return 8;
         } else if (var1.equals("NonHeapMemoryInitBytes")) {
            return 7;
         } else if (var1.equals("NonHeapMemoryMaxBytes")) {
            return 6;
         } else if (var1.equals("NonHeapMemoryUsedBytes")) {
            return 5;
         } else if (var1.equals("OSArch")) {
            return 36;
         } else if (var1.equals("OSAvailableProcessors")) {
            return 37;
         } else if (var1.equals("OSName")) {
            return 34;
         } else if (var1.equals("OSVersion")) {
            return 35;
         } else if (var1.equals("ObjectPendingFinalizationCount")) {
            return 0;
         } else if (var1.equals("PeakThreadCount")) {
            return 10;
         } else if (var1.equals("RunningJVMName")) {
            return 20;
         } else if (var1.equals("SpecName")) {
            return 25;
         } else if (var1.equals("SpecVendor")) {
            return 26;
         } else if (var1.equals("SpecVersion")) {
            return 27;
         } else if (var1.equals("StartTime")) {
            return 32;
         } else if (var1.equals("ThreadCount")) {
            return 9;
         } else if (var1.equals("ThreadDump")) {
            return 41;
         } else if (var1.equals("ThreadRequestExecutionDetails")) {
            return 42;
         } else if (var1.equals("TotalLoadedClassCount")) {
            return 39;
         } else if (var1.equals("TotalStartedThreadCount")) {
            return 11;
         } else if (var1.equals("UnloadedClassCount")) {
            return 40;
         } else if (var1.equals("Uptime")) {
            return 31;
         } else if (var1.equals("VmName")) {
            return 22;
         } else if (var1.equals("VmVendor")) {
            return 23;
         } else if (var1.equals("VmVersion")) {
            return 24;
         } else if (var1.equals("BootClassPathSupported")) {
            return 33;
         } else if (var1.equals("CurrentThreadCpuTimeSupported")) {
            return 18;
         } else if (var1.equals("ThreadContentionMonitoringEnabled")) {
            return 14;
         } else if (var1.equals("ThreadContentionMonitoringSupported")) {
            return 13;
         } else if (var1.equals("ThreadCpuTimeEnabled")) {
            return 19;
         } else {
            return var1.equals("ThreadCpuTimeSupported") ? 17 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isBootClassPathSet()) {
               var2.append("BootClassPath");
               var2.append(String.valueOf(this.bean.getBootClassPath()));
            }

            if (this.bean.isClassPathSet()) {
               var2.append("ClassPath");
               var2.append(String.valueOf(this.bean.getClassPath()));
            }

            if (this.bean.isCurrentThreadCpuTimeSet()) {
               var2.append("CurrentThreadCpuTime");
               var2.append(String.valueOf(this.bean.getCurrentThreadCpuTime()));
            }

            if (this.bean.isCurrentThreadUserTimeSet()) {
               var2.append("CurrentThreadUserTime");
               var2.append(String.valueOf(this.bean.getCurrentThreadUserTime()));
            }

            if (this.bean.isDaemonThreadCountSet()) {
               var2.append("DaemonThreadCount");
               var2.append(String.valueOf(this.bean.getDaemonThreadCount()));
            }

            if (this.bean.isHeapMemoryCommittedBytesSet()) {
               var2.append("HeapMemoryCommittedBytes");
               var2.append(String.valueOf(this.bean.getHeapMemoryCommittedBytes()));
            }

            if (this.bean.isHeapMemoryInitBytesSet()) {
               var2.append("HeapMemoryInitBytes");
               var2.append(String.valueOf(this.bean.getHeapMemoryInitBytes()));
            }

            if (this.bean.isHeapMemoryMaxBytesSet()) {
               var2.append("HeapMemoryMaxBytes");
               var2.append(String.valueOf(this.bean.getHeapMemoryMaxBytes()));
            }

            if (this.bean.isHeapMemoryUsedBytesSet()) {
               var2.append("HeapMemoryUsedBytes");
               var2.append(String.valueOf(this.bean.getHeapMemoryUsedBytes()));
            }

            if (this.bean.isLibraryPathSet()) {
               var2.append("LibraryPath");
               var2.append(String.valueOf(this.bean.getLibraryPath()));
            }

            if (this.bean.isLoadedClassCountSet()) {
               var2.append("LoadedClassCount");
               var2.append(String.valueOf(this.bean.getLoadedClassCount()));
            }

            if (this.bean.isManagementSpecVersionSet()) {
               var2.append("ManagementSpecVersion");
               var2.append(String.valueOf(this.bean.getManagementSpecVersion()));
            }

            if (this.bean.isNonHeapMemoryCommittedBytesSet()) {
               var2.append("NonHeapMemoryCommittedBytes");
               var2.append(String.valueOf(this.bean.getNonHeapMemoryCommittedBytes()));
            }

            if (this.bean.isNonHeapMemoryInitBytesSet()) {
               var2.append("NonHeapMemoryInitBytes");
               var2.append(String.valueOf(this.bean.getNonHeapMemoryInitBytes()));
            }

            if (this.bean.isNonHeapMemoryMaxBytesSet()) {
               var2.append("NonHeapMemoryMaxBytes");
               var2.append(String.valueOf(this.bean.getNonHeapMemoryMaxBytes()));
            }

            if (this.bean.isNonHeapMemoryUsedBytesSet()) {
               var2.append("NonHeapMemoryUsedBytes");
               var2.append(String.valueOf(this.bean.getNonHeapMemoryUsedBytes()));
            }

            if (this.bean.isOSArchSet()) {
               var2.append("OSArch");
               var2.append(String.valueOf(this.bean.getOSArch()));
            }

            if (this.bean.isOSAvailableProcessorsSet()) {
               var2.append("OSAvailableProcessors");
               var2.append(String.valueOf(this.bean.getOSAvailableProcessors()));
            }

            if (this.bean.isOSNameSet()) {
               var2.append("OSName");
               var2.append(String.valueOf(this.bean.getOSName()));
            }

            if (this.bean.isOSVersionSet()) {
               var2.append("OSVersion");
               var2.append(String.valueOf(this.bean.getOSVersion()));
            }

            if (this.bean.isObjectPendingFinalizationCountSet()) {
               var2.append("ObjectPendingFinalizationCount");
               var2.append(String.valueOf(this.bean.getObjectPendingFinalizationCount()));
            }

            if (this.bean.isPeakThreadCountSet()) {
               var2.append("PeakThreadCount");
               var2.append(String.valueOf(this.bean.getPeakThreadCount()));
            }

            if (this.bean.isRunningJVMNameSet()) {
               var2.append("RunningJVMName");
               var2.append(String.valueOf(this.bean.getRunningJVMName()));
            }

            if (this.bean.isSpecNameSet()) {
               var2.append("SpecName");
               var2.append(String.valueOf(this.bean.getSpecName()));
            }

            if (this.bean.isSpecVendorSet()) {
               var2.append("SpecVendor");
               var2.append(String.valueOf(this.bean.getSpecVendor()));
            }

            if (this.bean.isSpecVersionSet()) {
               var2.append("SpecVersion");
               var2.append(String.valueOf(this.bean.getSpecVersion()));
            }

            if (this.bean.isStartTimeSet()) {
               var2.append("StartTime");
               var2.append(String.valueOf(this.bean.getStartTime()));
            }

            if (this.bean.isThreadCountSet()) {
               var2.append("ThreadCount");
               var2.append(String.valueOf(this.bean.getThreadCount()));
            }

            if (this.bean.isThreadDumpSet()) {
               var2.append("ThreadDump");
               var2.append(String.valueOf(this.bean.getThreadDump()));
            }

            if (this.bean.isThreadRequestExecutionDetailsSet()) {
               var2.append("ThreadRequestExecutionDetails");
               var2.append(String.valueOf(this.bean.getThreadRequestExecutionDetails()));
            }

            if (this.bean.isTotalLoadedClassCountSet()) {
               var2.append("TotalLoadedClassCount");
               var2.append(String.valueOf(this.bean.getTotalLoadedClassCount()));
            }

            if (this.bean.isTotalStartedThreadCountSet()) {
               var2.append("TotalStartedThreadCount");
               var2.append(String.valueOf(this.bean.getTotalStartedThreadCount()));
            }

            if (this.bean.isUnloadedClassCountSet()) {
               var2.append("UnloadedClassCount");
               var2.append(String.valueOf(this.bean.getUnloadedClassCount()));
            }

            if (this.bean.isUptimeSet()) {
               var2.append("Uptime");
               var2.append(String.valueOf(this.bean.getUptime()));
            }

            if (this.bean.isVmNameSet()) {
               var2.append("VmName");
               var2.append(String.valueOf(this.bean.getVmName()));
            }

            if (this.bean.isVmVendorSet()) {
               var2.append("VmVendor");
               var2.append(String.valueOf(this.bean.getVmVendor()));
            }

            if (this.bean.isVmVersionSet()) {
               var2.append("VmVersion");
               var2.append(String.valueOf(this.bean.getVmVersion()));
            }

            if (this.bean.isBootClassPathSupportedSet()) {
               var2.append("BootClassPathSupported");
               var2.append(String.valueOf(this.bean.isBootClassPathSupported()));
            }

            if (this.bean.isCurrentThreadCpuTimeSupportedSet()) {
               var2.append("CurrentThreadCpuTimeSupported");
               var2.append(String.valueOf(this.bean.isCurrentThreadCpuTimeSupported()));
            }

            if (this.bean.isThreadContentionMonitoringEnabledSet()) {
               var2.append("ThreadContentionMonitoringEnabled");
               var2.append(String.valueOf(this.bean.isThreadContentionMonitoringEnabled()));
            }

            if (this.bean.isThreadContentionMonitoringSupportedSet()) {
               var2.append("ThreadContentionMonitoringSupported");
               var2.append(String.valueOf(this.bean.isThreadContentionMonitoringSupported()));
            }

            if (this.bean.isThreadCpuTimeEnabledSet()) {
               var2.append("ThreadCpuTimeEnabled");
               var2.append(String.valueOf(this.bean.isThreadCpuTimeEnabled()));
            }

            if (this.bean.isThreadCpuTimeSupportedSet()) {
               var2.append("ThreadCpuTimeSupported");
               var2.append(String.valueOf(this.bean.isThreadCpuTimeSupported()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            JVMRuntimeBeanImpl var2 = (JVMRuntimeBeanImpl)var1;
            this.computeDiff("BootClassPath", this.bean.getBootClassPath(), var2.getBootClassPath(), false);
            this.computeDiff("ClassPath", this.bean.getClassPath(), var2.getClassPath(), false);
            this.computeDiff("CurrentThreadCpuTime", this.bean.getCurrentThreadCpuTime(), var2.getCurrentThreadCpuTime(), false);
            this.computeDiff("CurrentThreadUserTime", this.bean.getCurrentThreadUserTime(), var2.getCurrentThreadUserTime(), false);
            this.computeDiff("DaemonThreadCount", this.bean.getDaemonThreadCount(), var2.getDaemonThreadCount(), false);
            this.computeDiff("HeapMemoryCommittedBytes", this.bean.getHeapMemoryCommittedBytes(), var2.getHeapMemoryCommittedBytes(), false);
            this.computeDiff("HeapMemoryInitBytes", this.bean.getHeapMemoryInitBytes(), var2.getHeapMemoryInitBytes(), false);
            this.computeDiff("HeapMemoryMaxBytes", this.bean.getHeapMemoryMaxBytes(), var2.getHeapMemoryMaxBytes(), false);
            this.computeDiff("HeapMemoryUsedBytes", this.bean.getHeapMemoryUsedBytes(), var2.getHeapMemoryUsedBytes(), false);
            this.computeDiff("LibraryPath", this.bean.getLibraryPath(), var2.getLibraryPath(), false);
            this.computeDiff("LoadedClassCount", this.bean.getLoadedClassCount(), var2.getLoadedClassCount(), false);
            this.computeDiff("ManagementSpecVersion", this.bean.getManagementSpecVersion(), var2.getManagementSpecVersion(), false);
            this.computeDiff("NonHeapMemoryCommittedBytes", this.bean.getNonHeapMemoryCommittedBytes(), var2.getNonHeapMemoryCommittedBytes(), false);
            this.computeDiff("NonHeapMemoryInitBytes", this.bean.getNonHeapMemoryInitBytes(), var2.getNonHeapMemoryInitBytes(), false);
            this.computeDiff("NonHeapMemoryMaxBytes", this.bean.getNonHeapMemoryMaxBytes(), var2.getNonHeapMemoryMaxBytes(), false);
            this.computeDiff("NonHeapMemoryUsedBytes", this.bean.getNonHeapMemoryUsedBytes(), var2.getNonHeapMemoryUsedBytes(), false);
            this.computeDiff("OSArch", this.bean.getOSArch(), var2.getOSArch(), false);
            this.computeDiff("OSAvailableProcessors", this.bean.getOSAvailableProcessors(), var2.getOSAvailableProcessors(), false);
            this.computeDiff("OSName", this.bean.getOSName(), var2.getOSName(), false);
            this.computeDiff("OSVersion", this.bean.getOSVersion(), var2.getOSVersion(), false);
            this.computeDiff("ObjectPendingFinalizationCount", this.bean.getObjectPendingFinalizationCount(), var2.getObjectPendingFinalizationCount(), false);
            this.computeDiff("PeakThreadCount", this.bean.getPeakThreadCount(), var2.getPeakThreadCount(), false);
            this.computeDiff("RunningJVMName", this.bean.getRunningJVMName(), var2.getRunningJVMName(), false);
            this.computeDiff("SpecName", this.bean.getSpecName(), var2.getSpecName(), false);
            this.computeDiff("SpecVendor", this.bean.getSpecVendor(), var2.getSpecVendor(), false);
            this.computeDiff("SpecVersion", this.bean.getSpecVersion(), var2.getSpecVersion(), false);
            this.computeDiff("StartTime", this.bean.getStartTime(), var2.getStartTime(), false);
            this.computeDiff("ThreadCount", this.bean.getThreadCount(), var2.getThreadCount(), false);
            this.computeDiff("ThreadDump", this.bean.getThreadDump(), var2.getThreadDump(), false);
            this.computeDiff("ThreadRequestExecutionDetails", this.bean.getThreadRequestExecutionDetails(), var2.getThreadRequestExecutionDetails(), false);
            this.computeDiff("TotalLoadedClassCount", this.bean.getTotalLoadedClassCount(), var2.getTotalLoadedClassCount(), false);
            this.computeDiff("TotalStartedThreadCount", this.bean.getTotalStartedThreadCount(), var2.getTotalStartedThreadCount(), false);
            this.computeDiff("UnloadedClassCount", this.bean.getUnloadedClassCount(), var2.getUnloadedClassCount(), false);
            this.computeDiff("Uptime", this.bean.getUptime(), var2.getUptime(), false);
            this.computeDiff("VmName", this.bean.getVmName(), var2.getVmName(), false);
            this.computeDiff("VmVendor", this.bean.getVmVendor(), var2.getVmVendor(), false);
            this.computeDiff("VmVersion", this.bean.getVmVersion(), var2.getVmVersion(), false);
            this.computeDiff("BootClassPathSupported", this.bean.isBootClassPathSupported(), var2.isBootClassPathSupported(), false);
            this.computeDiff("CurrentThreadCpuTimeSupported", this.bean.isCurrentThreadCpuTimeSupported(), var2.isCurrentThreadCpuTimeSupported(), false);
            this.computeDiff("ThreadContentionMonitoringEnabled", this.bean.isThreadContentionMonitoringEnabled(), var2.isThreadContentionMonitoringEnabled(), false);
            this.computeDiff("ThreadContentionMonitoringSupported", this.bean.isThreadContentionMonitoringSupported(), var2.isThreadContentionMonitoringSupported(), false);
            this.computeDiff("ThreadCpuTimeEnabled", this.bean.isThreadCpuTimeEnabled(), var2.isThreadCpuTimeEnabled(), false);
            this.computeDiff("ThreadCpuTimeSupported", this.bean.isThreadCpuTimeSupported(), var2.isThreadCpuTimeSupported(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JVMRuntimeBeanImpl var3 = (JVMRuntimeBeanImpl)var1.getSourceBean();
            JVMRuntimeBeanImpl var4 = (JVMRuntimeBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BootClassPath")) {
                  var3.setBootClassPath(var4.getBootClassPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("ClassPath")) {
                  var3.setClassPath(var4.getClassPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("CurrentThreadCpuTime")) {
                  var3.setCurrentThreadCpuTime(var4.getCurrentThreadCpuTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("CurrentThreadUserTime")) {
                  var3.setCurrentThreadUserTime(var4.getCurrentThreadUserTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("DaemonThreadCount")) {
                  var3.setDaemonThreadCount(var4.getDaemonThreadCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("HeapMemoryCommittedBytes")) {
                  var3.setHeapMemoryCommittedBytes(var4.getHeapMemoryCommittedBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("HeapMemoryInitBytes")) {
                  var3.setHeapMemoryInitBytes(var4.getHeapMemoryInitBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("HeapMemoryMaxBytes")) {
                  var3.setHeapMemoryMaxBytes(var4.getHeapMemoryMaxBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("HeapMemoryUsedBytes")) {
                  var3.setHeapMemoryUsedBytes(var4.getHeapMemoryUsedBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("LibraryPath")) {
                  var3.setLibraryPath(var4.getLibraryPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("LoadedClassCount")) {
                  var3.setLoadedClassCount(var4.getLoadedClassCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 38);
               } else if (var5.equals("ManagementSpecVersion")) {
                  var3.setManagementSpecVersion(var4.getManagementSpecVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("NonHeapMemoryCommittedBytes")) {
                  var3.setNonHeapMemoryCommittedBytes(var4.getNonHeapMemoryCommittedBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("NonHeapMemoryInitBytes")) {
                  var3.setNonHeapMemoryInitBytes(var4.getNonHeapMemoryInitBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("NonHeapMemoryMaxBytes")) {
                  var3.setNonHeapMemoryMaxBytes(var4.getNonHeapMemoryMaxBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 6);
               } else if (var5.equals("NonHeapMemoryUsedBytes")) {
                  var3.setNonHeapMemoryUsedBytes(var4.getNonHeapMemoryUsedBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 5);
               } else if (var5.equals("OSArch")) {
                  var3.setOSArch(var4.getOSArch());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 36);
               } else if (var5.equals("OSAvailableProcessors")) {
                  var3.setOSAvailableProcessors(var4.getOSAvailableProcessors());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 37);
               } else if (var5.equals("OSName")) {
                  var3.setOSName(var4.getOSName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 34);
               } else if (var5.equals("OSVersion")) {
                  var3.setOSVersion(var4.getOSVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 35);
               } else if (var5.equals("ObjectPendingFinalizationCount")) {
                  var3.setObjectPendingFinalizationCount(var4.getObjectPendingFinalizationCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
               } else if (var5.equals("PeakThreadCount")) {
                  var3.setPeakThreadCount(var4.getPeakThreadCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("RunningJVMName")) {
                  var3.setRunningJVMName(var4.getRunningJVMName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("SpecName")) {
                  var3.setSpecName(var4.getSpecName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("SpecVendor")) {
                  var3.setSpecVendor(var4.getSpecVendor());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("SpecVersion")) {
                  var3.setSpecVersion(var4.getSpecVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("StartTime")) {
                  var3.setStartTime(var4.getStartTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 32);
               } else if (var5.equals("ThreadCount")) {
                  var3.setThreadCount(var4.getThreadCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ThreadDump")) {
                  var3.setThreadDump(var4.getThreadDump());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 41);
               } else if (var5.equals("ThreadRequestExecutionDetails")) {
                  var3.setThreadRequestExecutionDetails(var4.getThreadRequestExecutionDetails());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 42);
               } else if (var5.equals("TotalLoadedClassCount")) {
                  var3.setTotalLoadedClassCount(var4.getTotalLoadedClassCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 39);
               } else if (var5.equals("TotalStartedThreadCount")) {
                  var3.setTotalStartedThreadCount(var4.getTotalStartedThreadCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("UnloadedClassCount")) {
                  var3.setUnloadedClassCount(var4.getUnloadedClassCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 40);
               } else if (var5.equals("Uptime")) {
                  var3.setUptime(var4.getUptime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (var5.equals("VmName")) {
                  var3.setVmName(var4.getVmName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("VmVendor")) {
                  var3.setVmVendor(var4.getVmVendor());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("VmVersion")) {
                  var3.setVmVersion(var4.getVmVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("BootClassPathSupported")) {
                  var3.setBootClassPathSupported(var4.isBootClassPathSupported());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (var5.equals("CurrentThreadCpuTimeSupported")) {
                  var3.setCurrentThreadCpuTimeSupported(var4.isCurrentThreadCpuTimeSupported());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("ThreadContentionMonitoringEnabled")) {
                  var3.setThreadContentionMonitoringEnabled(var4.isThreadContentionMonitoringEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("ThreadContentionMonitoringSupported")) {
                  var3.setThreadContentionMonitoringSupported(var4.isThreadContentionMonitoringSupported());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("ThreadCpuTimeEnabled")) {
                  var3.setThreadCpuTimeEnabled(var4.isThreadCpuTimeEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("ThreadCpuTimeSupported")) {
                  var3.setThreadCpuTimeSupported(var4.isThreadCpuTimeSupported());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else {
                  super.applyPropertyUpdate(var1, var2);
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            JVMRuntimeBeanImpl var5 = (JVMRuntimeBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("BootClassPath")) && this.bean.isBootClassPathSet()) {
               var5.setBootClassPath(this.bean.getBootClassPath());
            }

            if ((var3 == null || !var3.contains("ClassPath")) && this.bean.isClassPathSet()) {
               var5.setClassPath(this.bean.getClassPath());
            }

            if ((var3 == null || !var3.contains("CurrentThreadCpuTime")) && this.bean.isCurrentThreadCpuTimeSet()) {
               var5.setCurrentThreadCpuTime(this.bean.getCurrentThreadCpuTime());
            }

            if ((var3 == null || !var3.contains("CurrentThreadUserTime")) && this.bean.isCurrentThreadUserTimeSet()) {
               var5.setCurrentThreadUserTime(this.bean.getCurrentThreadUserTime());
            }

            if ((var3 == null || !var3.contains("DaemonThreadCount")) && this.bean.isDaemonThreadCountSet()) {
               var5.setDaemonThreadCount(this.bean.getDaemonThreadCount());
            }

            if ((var3 == null || !var3.contains("HeapMemoryCommittedBytes")) && this.bean.isHeapMemoryCommittedBytesSet()) {
               var5.setHeapMemoryCommittedBytes(this.bean.getHeapMemoryCommittedBytes());
            }

            if ((var3 == null || !var3.contains("HeapMemoryInitBytes")) && this.bean.isHeapMemoryInitBytesSet()) {
               var5.setHeapMemoryInitBytes(this.bean.getHeapMemoryInitBytes());
            }

            if ((var3 == null || !var3.contains("HeapMemoryMaxBytes")) && this.bean.isHeapMemoryMaxBytesSet()) {
               var5.setHeapMemoryMaxBytes(this.bean.getHeapMemoryMaxBytes());
            }

            if ((var3 == null || !var3.contains("HeapMemoryUsedBytes")) && this.bean.isHeapMemoryUsedBytesSet()) {
               var5.setHeapMemoryUsedBytes(this.bean.getHeapMemoryUsedBytes());
            }

            if ((var3 == null || !var3.contains("LibraryPath")) && this.bean.isLibraryPathSet()) {
               var5.setLibraryPath(this.bean.getLibraryPath());
            }

            if ((var3 == null || !var3.contains("LoadedClassCount")) && this.bean.isLoadedClassCountSet()) {
               var5.setLoadedClassCount(this.bean.getLoadedClassCount());
            }

            if ((var3 == null || !var3.contains("ManagementSpecVersion")) && this.bean.isManagementSpecVersionSet()) {
               var5.setManagementSpecVersion(this.bean.getManagementSpecVersion());
            }

            if ((var3 == null || !var3.contains("NonHeapMemoryCommittedBytes")) && this.bean.isNonHeapMemoryCommittedBytesSet()) {
               var5.setNonHeapMemoryCommittedBytes(this.bean.getNonHeapMemoryCommittedBytes());
            }

            if ((var3 == null || !var3.contains("NonHeapMemoryInitBytes")) && this.bean.isNonHeapMemoryInitBytesSet()) {
               var5.setNonHeapMemoryInitBytes(this.bean.getNonHeapMemoryInitBytes());
            }

            if ((var3 == null || !var3.contains("NonHeapMemoryMaxBytes")) && this.bean.isNonHeapMemoryMaxBytesSet()) {
               var5.setNonHeapMemoryMaxBytes(this.bean.getNonHeapMemoryMaxBytes());
            }

            if ((var3 == null || !var3.contains("NonHeapMemoryUsedBytes")) && this.bean.isNonHeapMemoryUsedBytesSet()) {
               var5.setNonHeapMemoryUsedBytes(this.bean.getNonHeapMemoryUsedBytes());
            }

            if ((var3 == null || !var3.contains("OSArch")) && this.bean.isOSArchSet()) {
               var5.setOSArch(this.bean.getOSArch());
            }

            if ((var3 == null || !var3.contains("OSAvailableProcessors")) && this.bean.isOSAvailableProcessorsSet()) {
               var5.setOSAvailableProcessors(this.bean.getOSAvailableProcessors());
            }

            if ((var3 == null || !var3.contains("OSName")) && this.bean.isOSNameSet()) {
               var5.setOSName(this.bean.getOSName());
            }

            if ((var3 == null || !var3.contains("OSVersion")) && this.bean.isOSVersionSet()) {
               var5.setOSVersion(this.bean.getOSVersion());
            }

            if ((var3 == null || !var3.contains("ObjectPendingFinalizationCount")) && this.bean.isObjectPendingFinalizationCountSet()) {
               var5.setObjectPendingFinalizationCount(this.bean.getObjectPendingFinalizationCount());
            }

            if ((var3 == null || !var3.contains("PeakThreadCount")) && this.bean.isPeakThreadCountSet()) {
               var5.setPeakThreadCount(this.bean.getPeakThreadCount());
            }

            if ((var3 == null || !var3.contains("RunningJVMName")) && this.bean.isRunningJVMNameSet()) {
               var5.setRunningJVMName(this.bean.getRunningJVMName());
            }

            if ((var3 == null || !var3.contains("SpecName")) && this.bean.isSpecNameSet()) {
               var5.setSpecName(this.bean.getSpecName());
            }

            if ((var3 == null || !var3.contains("SpecVendor")) && this.bean.isSpecVendorSet()) {
               var5.setSpecVendor(this.bean.getSpecVendor());
            }

            if ((var3 == null || !var3.contains("SpecVersion")) && this.bean.isSpecVersionSet()) {
               var5.setSpecVersion(this.bean.getSpecVersion());
            }

            if ((var3 == null || !var3.contains("StartTime")) && this.bean.isStartTimeSet()) {
               var5.setStartTime(this.bean.getStartTime());
            }

            if ((var3 == null || !var3.contains("ThreadCount")) && this.bean.isThreadCountSet()) {
               var5.setThreadCount(this.bean.getThreadCount());
            }

            if ((var3 == null || !var3.contains("ThreadDump")) && this.bean.isThreadDumpSet()) {
               var5.setThreadDump(this.bean.getThreadDump());
            }

            if ((var3 == null || !var3.contains("ThreadRequestExecutionDetails")) && this.bean.isThreadRequestExecutionDetailsSet()) {
               var5.setThreadRequestExecutionDetails(this.bean.getThreadRequestExecutionDetails());
            }

            if ((var3 == null || !var3.contains("TotalLoadedClassCount")) && this.bean.isTotalLoadedClassCountSet()) {
               var5.setTotalLoadedClassCount(this.bean.getTotalLoadedClassCount());
            }

            if ((var3 == null || !var3.contains("TotalStartedThreadCount")) && this.bean.isTotalStartedThreadCountSet()) {
               var5.setTotalStartedThreadCount(this.bean.getTotalStartedThreadCount());
            }

            if ((var3 == null || !var3.contains("UnloadedClassCount")) && this.bean.isUnloadedClassCountSet()) {
               var5.setUnloadedClassCount(this.bean.getUnloadedClassCount());
            }

            if ((var3 == null || !var3.contains("Uptime")) && this.bean.isUptimeSet()) {
               var5.setUptime(this.bean.getUptime());
            }

            if ((var3 == null || !var3.contains("VmName")) && this.bean.isVmNameSet()) {
               var5.setVmName(this.bean.getVmName());
            }

            if ((var3 == null || !var3.contains("VmVendor")) && this.bean.isVmVendorSet()) {
               var5.setVmVendor(this.bean.getVmVendor());
            }

            if ((var3 == null || !var3.contains("VmVersion")) && this.bean.isVmVersionSet()) {
               var5.setVmVersion(this.bean.getVmVersion());
            }

            if ((var3 == null || !var3.contains("BootClassPathSupported")) && this.bean.isBootClassPathSupportedSet()) {
               var5.setBootClassPathSupported(this.bean.isBootClassPathSupported());
            }

            if ((var3 == null || !var3.contains("CurrentThreadCpuTimeSupported")) && this.bean.isCurrentThreadCpuTimeSupportedSet()) {
               var5.setCurrentThreadCpuTimeSupported(this.bean.isCurrentThreadCpuTimeSupported());
            }

            if ((var3 == null || !var3.contains("ThreadContentionMonitoringEnabled")) && this.bean.isThreadContentionMonitoringEnabledSet()) {
               var5.setThreadContentionMonitoringEnabled(this.bean.isThreadContentionMonitoringEnabled());
            }

            if ((var3 == null || !var3.contains("ThreadContentionMonitoringSupported")) && this.bean.isThreadContentionMonitoringSupportedSet()) {
               var5.setThreadContentionMonitoringSupported(this.bean.isThreadContentionMonitoringSupported());
            }

            if ((var3 == null || !var3.contains("ThreadCpuTimeEnabled")) && this.bean.isThreadCpuTimeEnabledSet()) {
               var5.setThreadCpuTimeEnabled(this.bean.isThreadCpuTimeEnabled());
            }

            if ((var3 == null || !var3.contains("ThreadCpuTimeSupported")) && this.bean.isThreadCpuTimeSupportedSet()) {
               var5.setThreadCpuTimeSupported(this.bean.isThreadCpuTimeSupported());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
      }
   }
}
