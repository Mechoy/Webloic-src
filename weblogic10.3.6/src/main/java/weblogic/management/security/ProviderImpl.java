package weblogic.management.security;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.descriptor.DescriptorBean;

public class ProviderImpl extends BaseMBeanImpl {
   public ProviderImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected ProviderImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }

   private ProviderMBean getProvider() {
      try {
         return (ProviderMBean)this.getProxy();
      } catch (MBeanException var2) {
         throw new AssertionError(var2);
      }
   }

   public RealmMBean getRealm() {
      DescriptorBean var1 = this.getProvider().getParentBean();
      return var1 instanceof RealmMBean ? (RealmMBean)var1 : null;
   }

   public String getCompatibilityObjectName() {
      String var1 = "Security:Name=";
      String var2 = var1 + this.getRealm().getName() + this.getProvider().getName();
      return var2;
   }
}
