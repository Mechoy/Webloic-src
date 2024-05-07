package weblogic.work;

import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.FairShareRequestClassBean;
import weblogic.management.configuration.FairShareRequestClassMBean;

class FairShareRequestClassBeanUpdateListener implements BeanUpdateListener {
   private final FairShareRequestClass delegate;

   FairShareRequestClassBeanUpdateListener(FairShareRequestClass var1) {
      this.delegate = var1;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      DescriptorBean var2 = var1.getProposedBean();
      if (var2 instanceof FairShareRequestClassBean) {
         this.delegate.setShare(((FairShareRequestClassBean)var2).getFairShare());
      } else if (var2 instanceof FairShareRequestClassMBean) {
         this.delegate.setShare(((FairShareRequestClassMBean)var2).getFairShare());
      }
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }
}
