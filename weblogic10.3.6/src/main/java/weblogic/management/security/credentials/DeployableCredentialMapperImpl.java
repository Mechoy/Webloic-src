package weblogic.management.security.credentials;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;

public class DeployableCredentialMapperImpl extends CredentialMapperImpl {
   public DeployableCredentialMapperImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected DeployableCredentialMapperImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
