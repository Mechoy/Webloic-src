package weblogic.management.security.pk;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.management.security.ProviderImpl;

public class CertPathProviderImpl extends ProviderImpl {
   public CertPathProviderImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected CertPathProviderImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
