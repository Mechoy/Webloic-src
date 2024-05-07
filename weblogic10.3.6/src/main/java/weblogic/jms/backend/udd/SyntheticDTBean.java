package weblogic.jms.backend.udd;

import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.MulticastParamsBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;

public class SyntheticDTBean extends SyntheticDDBean implements UniformDistributedTopicBean {
   SyntheticDTBean(UDDEntity var1) {
      super(var1);
   }

   public DistributedDestinationMemberBean[] getDistributedTopicMembers() {
      return (DistributedDestinationMemberBean[])((DistributedDestinationMemberBean[])this.members.toArray(new DistributedDestinationMemberBean[0]));
   }

   public DistributedDestinationMemberBean createDistributedTopicMember(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public void destroyDistributedTopicMember(DistributedDestinationMemberBean var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public DistributedDestinationMemberBean lookupDistributedTopicMember(String var1) {
      return null;
   }

   public String getForwardingPolicy() {
      return ((UniformDistributedTopicBean)this.udd.getUDestBean()).getForwardingPolicy();
   }

   public void setForwardingPolicy(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public MulticastParamsBean getMulticast() {
      return ((UniformDistributedTopicBean)this.udd.getUDestBean()).getMulticast();
   }
}
