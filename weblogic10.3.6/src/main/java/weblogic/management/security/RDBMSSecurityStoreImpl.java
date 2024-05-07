package weblogic.management.security;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import weblogic.descriptor.DescriptorBean;

public class RDBMSSecurityStoreImpl extends BaseMBeanImpl {
   public RDBMSSecurityStoreImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   public RealmMBean getRealm() {
      DescriptorBean var1 = this.getRDBMSSecurityStore().getParentBean();
      return var1 instanceof RealmMBean ? (RealmMBean)var1 : null;
   }

   public String getCompatibilityObjectName() {
      String var1 = "Security:Name=";
      return var1 + this.getRealm().getName() + this.getRDBMSSecurityStore().getName();
   }

   private RDBMSSecurityStoreMBean getRDBMSSecurityStore() {
      try {
         return (RDBMSSecurityStoreMBean)this.getProxy();
      } catch (MBeanException var2) {
         throw new AssertionError(var2);
      }
   }
}
