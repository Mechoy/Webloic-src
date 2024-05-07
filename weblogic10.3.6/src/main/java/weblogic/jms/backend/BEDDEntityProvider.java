package weblogic.jms.backend;

import java.util.List;
import javax.naming.Context;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.DistributedDestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.jms.common.EntityName;
import weblogic.jms.dd.DistributedDestination;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.jms.module.JMSModuleManagedEntityProvider;
import weblogic.management.configuration.DomainMBean;

public class BEDDEntityProvider implements JMSModuleManagedEntityProvider {
   public JMSModuleManagedEntity createEntity(ApplicationContext var1, EntityName var2, Context var3, JMSBean var4, NamedEntityBean var5, List var6, DomainMBean var7) throws ModuleException {
      String var8 = var2.toString();
      DistributedDestinationBean var9 = (DistributedDestinationBean)var5;
      DistributedDestination var10 = new DistributedDestination(var8, var4, var9, var1.getAppDeploymentMBean(), var2.getEARModuleName(), var2.getFullyQualifiedModuleName(), var1);
      return var10;
   }
}
