package weblogic.work;

import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.MaxThreadsConstraintBean;
import weblogic.management.configuration.MaxThreadsConstraintMBean;

public class MaxThreadsConstraintBeanUpdateListener implements BeanUpdateListener {
   private final MaxThreadsConstraint delegate;

   MaxThreadsConstraintBeanUpdateListener(MaxThreadsConstraint var1) {
      this.delegate = var1;
   }

   public final void prepareUpdate(BeanUpdateEvent var1) {
   }

   public final void activateUpdate(BeanUpdateEvent var1) {
      DescriptorBean var2 = var1.getProposedBean();
      if (var2 instanceof MaxThreadsConstraintMBean) {
         this.delegate.setCountInternal(((MaxThreadsConstraintMBean)var2).getCount());
      } else if (var2 instanceof MaxThreadsConstraintBean) {
         this.delegate.setCountInternal(((MaxThreadsConstraintBean)var2).getCount());
      }

   }

   public final void rollbackUpdate(BeanUpdateEvent var1) {
   }
}
