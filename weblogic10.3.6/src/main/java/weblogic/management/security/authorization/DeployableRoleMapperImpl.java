package weblogic.management.security.authorization;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;

public class DeployableRoleMapperImpl extends RoleMapperImpl {
   public DeployableRoleMapperImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected DeployableRoleMapperImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
