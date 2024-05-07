package weblogic.diagnostics.archive;

import weblogic.management.ManagementException;
import weblogic.management.runtime.WLDFDataRetirementTaskRuntimeMBean;

public class WLDFDataRetirementByAgeTaskImpl extends DataRetirementByAgeTaskImpl implements WLDFDataRetirementTaskRuntimeMBean {
   public WLDFDataRetirementByAgeTaskImpl(EditableDataArchive var1, long var2) throws ManagementException {
      super(var1, var2);
   }
}
