package weblogic.diagnostics.archive;

import weblogic.diagnostics.archive.wlstore.PersistentStoreDataArchive;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WLDFDataRetirementTaskRuntimeMBean;
import weblogic.management.runtime.WLDFWlstoreArchiveRuntimeMBean;

public class WLDFDiagnosticWlstoreArchiveRuntime extends DiagnosticWlstoreArchiveRuntime implements WLDFWlstoreArchiveRuntimeMBean {
   public WLDFDiagnosticWlstoreArchiveRuntime(PersistentStoreDataArchive var1, RuntimeMBean var2) throws ManagementException {
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
