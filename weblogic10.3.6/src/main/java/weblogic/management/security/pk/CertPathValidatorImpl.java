package weblogic.management.security.pk;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;

public class CertPathValidatorImpl extends CertPathProviderImpl {
   public CertPathValidatorImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected CertPathValidatorImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
