package weblogic.management.security.authentication;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.management.security.ProviderImpl;

public class AuthenticationProviderImpl extends ProviderImpl {
   public AuthenticationProviderImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected AuthenticationProviderImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
