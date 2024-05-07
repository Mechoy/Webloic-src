package weblogic.jms.frontend;

import java.util.List;
import javax.naming.Context;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.jms.JMSService;
import weblogic.jms.common.EntityName;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.jms.module.JMSModuleManagedEntityProvider;
import weblogic.management.configuration.DomainMBean;

public class JmsConnectionFactoryEntityProvider implements JMSModuleManagedEntityProvider {
   public JMSModuleManagedEntity createEntity(ApplicationContext var1, EntityName var2, Context var3, JMSBean var4, NamedEntityBean var5, List var6, DomainMBean var7) throws ModuleException {
      if ("WebLogic_Debug_CON_fail_init".equals(var5.getName())) {
         throw new ModuleException("DEBUG: A connection factory with name WebLogic_Debug_CON_fail_init will force the initializer to fail");
      } else {
         JMSConnectionFactoryBean var8 = (JMSConnectionFactoryBean)var5;
         FEConnectionFactory var9 = null;
         var9 = new FEConnectionFactory(JMSService.getJMSService().getFrontEnd(), var8, var2.getEARModuleName(), var2.getFullyQualifiedModuleName(), var3);
         return var9;
      }
   }
}
