package weblogic.management.security.pk;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;

public class CertPathBuilderValidatorImpl extends CertPathBuilderImpl {
   private CertPathValidatorImpl validatorImpl;

   public CertPathBuilderValidatorImpl(ModelMBean var1) throws MBeanException {
      super(var1);
      this.validatorImpl = new CertPathValidatorImpl(var1);
   }

   protected CertPathBuilderValidatorImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
      this.validatorImpl = new CertPathValidatorImpl(var1);
   }
}
