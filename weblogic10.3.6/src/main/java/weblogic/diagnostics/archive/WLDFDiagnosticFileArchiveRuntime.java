package weblogic.diagnostics.archive;

import weblogic.diagnostics.archive.filestore.FileDataArchive;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WLDFFileArchiveRuntimeMBean;

public class WLDFDiagnosticFileArchiveRuntime extends DiagnosticFileArchiveRuntime implements WLDFFileArchiveRuntimeMBean {
   public WLDFDiagnosticFileArchiveRuntime(FileDataArchive var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }
}
