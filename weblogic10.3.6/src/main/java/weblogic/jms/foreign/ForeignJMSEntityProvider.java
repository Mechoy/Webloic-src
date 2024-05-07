package weblogic.jms.foreign;

import java.util.List;
import javax.naming.Context;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.jms.common.EntityName;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.jms.module.JMSModuleManagedEntityProvider;
import weblogic.management.configuration.DomainMBean;

public class ForeignJMSEntityProvider implements JMSModuleManagedEntityProvider {
   public JMSModuleManagedEntity createEntity(ApplicationContext var1, EntityName var2, Context var3, JMSBean var4, NamedEntityBean var5, List var6, DomainMBean var7) throws ModuleException {
      ForeignServerBean var8 = (ForeignServerBean)var5;
      return new ForeignJMSImpl(var8, var2.getFullyQualifiedModuleName());
   }
}
