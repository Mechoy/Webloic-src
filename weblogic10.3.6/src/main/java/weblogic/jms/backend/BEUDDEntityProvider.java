package weblogic.jms.backend;

import java.util.List;
import javax.naming.Context;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.jms.backend.udd.UDDEntity;
import weblogic.jms.common.EntityName;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.jms.module.JMSModuleManagedEntityProvider;
import weblogic.management.configuration.DomainMBean;

public class BEUDDEntityProvider implements JMSModuleManagedEntityProvider {
   public JMSModuleManagedEntity createEntity(ApplicationContext var1, EntityName var2, Context var3, JMSBean var4, NamedEntityBean var5, List var6, DomainMBean var7) throws ModuleException {
      String var8 = var2.toString();
      return new UDDEntity(var8, var1, var2.getFullyQualifiedModuleName(), var2.getEARModuleName(), var3, var4, var5, var6, var7);
   }
}
