package weblogic.wsee.policy.deployment;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class WseePolicySubjectManagerService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      try {
         WseePolicySubjectManagerRuntimeMBeanImpl.initialize();
      } catch (Exception var2) {
         throw new ServiceFailureException(var2);
      }
   }
}
