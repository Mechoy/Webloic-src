package weblogic.work;

import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.ResponseTimeRequestClassBean;
import weblogic.management.configuration.ResponseTimeRequestClassMBean;

class ResponseTimeRequestClassBeanUpdateListener implements BeanUpdateListener {
   private ResponseTimeRequestClass delegate;

   ResponseTimeRequestClassBeanUpdateListener(ResponseTimeRequestClass var1) {
      this.delegate = var1;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      DescriptorBean var2 = var1.getProposedBean();
      if (var2 instanceof ResponseTimeRequestClassBean) {
         this.delegate.setResponseTime((double)((ResponseTimeRequestClassBean)var2).getGoalMs());
      } else if (var2 instanceof ResponseTimeRequestClassMBean) {
         this.delegate.setResponseTime((double)((ResponseTimeRequestClassMBean)var2).getGoalMs());
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }
}
