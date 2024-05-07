package weblogic.management.security.authorization;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.management.security.ProviderImpl;

public class RoleMapperImpl extends ProviderImpl {
   public RoleMapperImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected RoleMapperImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
