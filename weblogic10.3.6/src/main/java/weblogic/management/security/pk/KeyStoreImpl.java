package weblogic.management.security.pk;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.management.security.ProviderImpl;

public class KeyStoreImpl extends ProviderImpl {
   public KeyStoreImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected KeyStoreImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }
}
