package weblogic.management.provider.internal;

import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class BeanInfoAccessService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      BeanInfoAccess var1 = ManagementService.getBeanInfoAccess();
      if (var1 == null) {
         var1 = BeanInfoAccessSingleton.getInstance();
         ManagementService.initializeBeanInfo(var1);
      }
   }
}
