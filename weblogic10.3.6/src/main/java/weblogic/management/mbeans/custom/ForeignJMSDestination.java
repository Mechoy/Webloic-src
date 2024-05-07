package weblogic.management.mbeans.custom;

import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public class ForeignJMSDestination extends ForeignJNDIObject {
   public ForeignJMSDestination(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(ForeignDestinationBean var1) {
      super.useDelegates(var1);
   }
}
