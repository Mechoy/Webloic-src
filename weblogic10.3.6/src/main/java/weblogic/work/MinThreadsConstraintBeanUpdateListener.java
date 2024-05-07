package weblogic.work;

import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.MinThreadsConstraintBean;
import weblogic.management.configuration.MinThreadsConstraintMBean;

public class MinThreadsConstraintBeanUpdateListener implements BeanUpdateListener {
   private final MinThreadsConstraint delegate;

   MinThreadsConstraintBeanUpdateListener(MinThreadsConstraint var1) {
      this.delegate = var1;
   }

   public void prepareUpdate(BeanUpdateEvent var1) {
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      DescriptorBean var2 = var1.getProposedBean();
      if (var2 instanceof MinThreadsConstraintMBean) {
         this.delegate.setCount(((MinThreadsConstraintMBean)var2).getCount());
      } else if (var2 instanceof MinThreadsConstraintBean) {
         this.delegate.setCount(((MinThreadsConstraintBean)var2).getCount());
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }
}
