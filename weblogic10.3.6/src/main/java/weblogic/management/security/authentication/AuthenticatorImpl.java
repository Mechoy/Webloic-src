package weblogic.management.security.authentication;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;

public class AuthenticatorImpl extends AuthenticationProviderImpl {
   public AuthenticatorImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected AuthenticatorImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
