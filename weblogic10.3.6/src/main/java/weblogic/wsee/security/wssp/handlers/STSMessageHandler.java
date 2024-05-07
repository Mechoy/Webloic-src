package weblogic.wsee.security.wssp.handlers;

import javax.xml.rpc.handler.GenericHandler;

public class STSMessageHandler extends weblogic.wsee.security.wst.internal.STSMessageHandler {
   protected GenericHandler getPolicyHandler() {
      PostWssServerPolicyHandler var1 = new PostWssServerPolicyHandler();
      return var1;
   }
}
