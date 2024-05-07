package weblogic.work;

import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.CapacityBean;
import weblogic.management.configuration.CapacityMBean;

class OverloadManagerBeanUpdateListener implements BeanUpdateListener {
   private OverloadManager delegate;

   OverloadManagerBeanUpdateListener(OverloadManager var1) {
      this.delegate = var1;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      DescriptorBean var2 = var1.getProposedBean();
      if (var2 instanceof CapacityMBean) {
         this.delegate.setCapacity(((CapacityMBean)var2).getCount());
      } else if (var2 instanceof CapacityBean) {
         this.delegate.setCapacity(((CapacityBean)var2).getCount());
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }
}
