package weblogic.management.runtime;

import weblogic.diagnostics.accessor.runtime.EditableArchiveRuntimeMBean;
import weblogic.management.ManagementException;

public interface WLDFEditableArchiveRuntimeMBean extends WLDFArchiveRuntimeMBean, EditableArchiveRuntimeMBean {
   WLDFDataRetirementTaskRuntimeMBean performDataRetirement() throws ManagementException;
}
