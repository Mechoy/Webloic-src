package weblogic.management.security.audit;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.management.security.ProviderImpl;

public class AuditorImpl extends ProviderImpl {
   public AuditorImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected AuditorImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
