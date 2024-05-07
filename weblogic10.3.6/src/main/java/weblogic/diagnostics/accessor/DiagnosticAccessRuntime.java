package weblogic.diagnostics.accessor;

import weblogic.diagnostics.accessor.runtime.DataAccessRuntimeMBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WLDFAccessRuntimeMBean;
import weblogic.management.runtime.WLDFDataAccessRuntimeMBean;

public class DiagnosticAccessRuntime extends AccessRuntime implements WLDFAccessRuntimeMBean, AccessorConstants {
   private static final DebugLogger ACCESSOR_DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticAccessor");

   public static DiagnosticAccessRuntime getInstance() throws ManagementException {
      try {
         return (DiagnosticAccessRuntime)AccessRuntime.getAccessorInstance();
      } catch (ClassCastException var1) {
         throw new ManagementException(var1);
      }
   }

   DiagnosticAccessRuntime(AccessorMBeanFactory var1, AccessorConfigurationProvider var2, AccessorSecurityProvider var3, RuntimeMBean var4) throws ManagementException {
      super(var1, var2, var3, var4);
   }

   public WLDFDataAccessRuntimeMBean lookupWLDFDataAccessRuntime(String var1) throws ManagementException {
      return this.lookupWLDFDataAccessRuntime(var1, (ColumnInfo[])null);
   }

   public WLDFDataAccessRuntimeMBean lookupWLDFDataAccessRuntime(String var1, ColumnInfo[] var2) throws ManagementException {
      try {
         return (WLDFDataAccessRuntimeMBean)super.lookupDataAccessRuntime(var1, var2);
      } catch (ClassCastException var4) {
         throw new ManagementException(var4);
      }
   }

   public WLDFDataAccessRuntimeMBean[] getWLDFDataAccessRuntimes() throws ManagementException {
      try {
         DataAccessRuntimeMBean[] var1 = super.getDataAccessRuntimes();
         int var2 = var1 != null ? var1.length : 0;
         WLDFDataAccessRuntimeMBean[] var3 = new WLDFDataAccessRuntimeMBean[var2];
         System.arraycopy(var1, 0, var3, 0, var2);
         return var3;
      } catch (ClassCastException var4) {
         throw new ManagementException(var4);
      }
   }
}
