package weblogic.jms.backend.udd;

import weblogic.j2ee.descriptor.wl.MulticastParamsBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;

public class SyntheticTopicBean extends SyntheticDestinationBean implements TopicBean {
   public SyntheticTopicBean(UDDEntity var1, String var2) {
      super(var1, var2);
   }

   public MulticastParamsBean getMulticast() {
      return ((UniformDistributedTopicBean)this.udd.getUDestBean()).getMulticast();
   }
}
