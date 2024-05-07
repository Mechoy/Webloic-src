package weblogic.messaging.dispatcher;

import weblogic.kernel.KernelStatus;

public class WLSMessagingEnvironmentImpl extends MessagingEnvironment {
   public boolean isServer() {
      return KernelStatus.isServer();
   }
}
