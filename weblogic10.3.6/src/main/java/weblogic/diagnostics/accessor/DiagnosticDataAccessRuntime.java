package weblogic.diagnostics.accessor;

import weblogic.management.ManagementException;
import weblogic.management.runtime.WLDFAccessRuntimeMBean;
import weblogic.management.runtime.WLDFDataAccessRuntimeMBean;

public class DiagnosticDataAccessRuntime extends DataAccessRuntime implements WLDFDataAccessRuntimeMBean, AccessorConstants {
   DiagnosticDataAccessRuntime(String var1, WLDFAccessRuntimeMBean var2, DiagnosticDataAccessService var3) throws ManagementException {
      super(var1, var2, var3);
   }
}
