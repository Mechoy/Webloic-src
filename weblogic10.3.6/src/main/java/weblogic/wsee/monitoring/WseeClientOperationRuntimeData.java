package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.wsee.util.Verbose;

public final class WseeClientOperationRuntimeData extends WseeBaseOperationRuntimeData {
   private static final boolean verbose = Verbose.isVerbose(WseeClientOperationRuntimeData.class);

   public static WseeClientOperationRuntimeData createWsProtocolOp(WseeBaseRuntimeData var0) throws ManagementException {
      WseeClientOperationRuntimeData var1 = new WseeClientOperationRuntimeData("Ws-Protocol");
      var1.setParentData(var0);
      return var1;
   }

   WseeClientOperationRuntimeData(String var1) {
      super(var1);
      if (verbose) {
         Verbose.log((Object)("WseeClientOperationRuntimeData[" + var1 + "]"));
      }

   }
}
