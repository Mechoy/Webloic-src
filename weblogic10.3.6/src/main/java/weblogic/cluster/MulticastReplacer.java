package weblogic.cluster;

import java.io.IOException;
import weblogic.rmi.cluster.ReplicaList;
import weblogic.rmi.spi.HostID;
import weblogic.utils.io.Replacer;

public final class MulticastReplacer implements Replacer {
   private final HostID hostID;

   public MulticastReplacer(HostID var1) {
      this.hostID = var1;
   }

   public Object replaceObject(Object var1) throws IOException {
      return var1 instanceof ReplicaList ? ((ReplicaList)var1).getListWithRefHostedBy(this.hostID) : UpgradeUtils.getInstance().getInteropReplacer().replaceObject(var1);
   }

   public Object resolveObject(Object var1) throws IOException {
      return UpgradeUtils.getInstance().getInteropReplacer().resolveObject(var1);
   }

   public void insertReplacer(Replacer var1) {
      throw new AssertionError("Should never get called");
   }
}
