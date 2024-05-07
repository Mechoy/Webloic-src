package weblogic.diagnostics.archive;

import weblogic.diagnostics.archive.dbstore.JdbcDataArchive;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WLDFDataRetirementTaskRuntimeMBean;
import weblogic.management.runtime.WLDFDbstoreArchiveRuntimeMBean;

public class WLDFDiagnosticDbstoreArchiveRuntime extends DiagnosticDbstoreArchiveRuntime implements WLDFDbstoreArchiveRuntimeMBean {
   public WLDFDiagnosticDbstoreArchiveRuntime(JdbcDataArchive var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public WLDFDataRetirementTaskRuntimeMBean performDataRetirement() throws ManagementException {
      WLDFDataRetirementTaskRuntimeMBean var1 = null;

      try {
         var1 = (WLDFDataRetirementTaskRuntimeMBean)super.performRetirement();
         return var1;
      } catch (Exception var3) {
         throw new ManagementException(var3);
      }
   }
}
