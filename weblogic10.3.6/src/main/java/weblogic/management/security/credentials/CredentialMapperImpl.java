package weblogic.management.security.credentials;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.management.security.ProviderImpl;

public class CredentialMapperImpl extends ProviderImpl {
   public CredentialMapperImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected CredentialMapperImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
