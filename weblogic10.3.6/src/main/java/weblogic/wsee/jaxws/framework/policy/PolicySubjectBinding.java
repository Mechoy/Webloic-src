package weblogic.wsee.jaxws.framework.policy;

import java.util.List;

public interface PolicySubjectBinding {
   PolicySubjectMetadata getPolicySubjectMetadata();

   List<PolicyReference> getPolicyReferences();
}
