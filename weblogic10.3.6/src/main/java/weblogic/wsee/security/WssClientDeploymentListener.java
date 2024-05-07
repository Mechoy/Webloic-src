package weblogic.wsee.security;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;

public class WssClientDeploymentListener extends WssDeploymentListener {
   protected void insertHandler(HandlerList var1) throws HandlerException {
      if (!var1.contains("SECURITY_HANDLER")) {
         HandlerInfo var2 = this.getHandlerInfo();
         ArrayList var3 = this.getPrecedingHandlers();
         ArrayList var4 = this.getFollowingHandlers();
         var1.lenientInsert("SECURITY_HANDLER", var2, var4, var3);
      }
   }

   protected void removeHandler(HandlerList var1) {
      var1.remove("SECURITY_HANDLER");
   }

   protected ArrayList getPrecedingHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("CONNECTION_HANDLER");
      return var1;
   }

   protected ArrayList getFollowingHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("POLICY_CLIENT_RT_HANDLER");
      var1.add("RELIABILITY_HANDLER");
      var1.add("CODEC_HANDLER");
      var1.add("JAX_RPC_CHAIN_HANDLER");
      return var1;
   }

   protected HandlerInfo getHandlerInfo() {
      HashMap var1 = new HashMap();
      return new HandlerInfo(WssClientHandler.class, var1, (QName[])null);
   }
}
