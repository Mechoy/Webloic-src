package weblogic.management.security.pk;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;

public class CertPathBuilderImpl extends CertPathProviderImpl {
   public CertPathBuilderImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected CertPathBuilderImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
