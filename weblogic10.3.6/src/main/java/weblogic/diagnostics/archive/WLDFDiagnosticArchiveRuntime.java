package weblogic.diagnostics.archive;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WLDFArchiveRuntimeMBean;

public class WLDFDiagnosticArchiveRuntime extends DiagnosticArchiveRuntime implements WLDFArchiveRuntimeMBean {
   public static final long NANOS_PER_MILLI = 1000000L;

   public WLDFDiagnosticArchiveRuntime(DataArchive var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }
}
