package weblogic.management.mbeans.custom;

import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public final class JMSQueue extends JMSDestination {
   private static final long serialVersionUID = 6209479225384067069L;
   private long _creationTime = 1L;

   public JMSQueue(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setCreationTime(long var1) {
      this._creationTime = var1;
   }

   public long getCreationTime() {
      return this._creationTime;
   }

   public void useDelegates(DomainMBean var1, JMSBean var2, QueueBean var3) {
      super.useDelegates(var1, var2, var3);
   }
}
