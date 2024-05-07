package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.jaxws.spi.ClientInstancePool;
import weblogic.wsee.util.Verbose;

public final class WseeClientPortRuntimeData extends WseeBasePortRuntimeData {
   private static final boolean verbose = Verbose.isVerbose(WseeClientPortRuntimeData.class);
   String _clientId;

   WseeClientPortRuntimeData(String var1, String var2) throws ManagementException {
      super(var1, var2);
      if (verbose) {
         Verbose.log((Object)("WseeClientPortRuntimeData[" + var1 + "]"));
      }

   }

   public void setParentData(WseeClientRuntimeData var1) {
      super.setParentData(var1);
      this._clientId = var1.getClientId();
   }

   private ClientInstancePool.PoolStats getPoolStats() {
      ClientIdentityRegistry.ClientInfo var1 = ClientIdentityRegistry.getRequiredClientInfo(this._clientId);
      ClientInstancePool var2 = var1.getFirstClientInstancePool();
      return var2 != null ? var2.getStats() : new ClientInstancePool.PoolStats();
   }

   public int getPoolCapacity() {
      return this.getPoolStats().getCapacity();
   }

   public int getPoolFreeCount() {
      return this.getPoolStats().getFreeCount();
   }

   public int getPoolTakenCount() {
      return this.getPoolStats().getTakenCount();
   }

   public int getPoolTotalPooledClientTakeCount() {
      return this.getPoolStats().getPooledClientTakeCount();
   }

   public int getPoolTotalConversationalClientTakeCount() {
      return this.getPoolStats().getConversationalClientTakeCount();
   }

   public int getPoolTotalSimpleClientCreateCount() {
      return this.getPoolStats().getSimpleClientCreateCount();
   }
}
