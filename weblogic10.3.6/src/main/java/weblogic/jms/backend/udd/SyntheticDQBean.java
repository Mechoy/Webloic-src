package weblogic.jms.backend.udd;

import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedQueueBean;

public class SyntheticDQBean extends SyntheticDDBean implements UniformDistributedQueueBean {
   SyntheticDQBean(UDDEntity var1) {
      super(var1);
   }

   public DistributedDestinationMemberBean[] getDistributedQueueMembers() {
      return (DistributedDestinationMemberBean[])((DistributedDestinationMemberBean[])this.members.toArray(new DistributedDestinationMemberBean[0]));
   }

   public DistributedDestinationMemberBean createDistributedQueueMember(String var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public void destroyDistributedQueueMember(DistributedDestinationMemberBean var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public int getForwardDelay() {
      return ((UniformDistributedQueueBean)this.udd.getUDestBean()).getForwardDelay();
   }

   public void setForwardDelay(int var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }

   public DistributedDestinationMemberBean lookupDistributedQueueMember(String var1) {
      return null;
   }

   public boolean getResetDeliveryCountOnForward() {
      return ((UniformDistributedQueueBean)this.udd.getUDestBean()).getResetDeliveryCountOnForward();
   }

   public void setResetDeliveryCountOnForward(boolean var1) {
      throw new AssertionError("Don't want to modify fake bean");
   }
}
