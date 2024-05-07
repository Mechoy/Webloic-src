package weblogic.management.security.authorization;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;

public class DeployableAuthorizerImpl extends AuthorizerImpl {
   public DeployableAuthorizerImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected DeployableAuthorizerImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
