package weblogic.deploy.service.internal.transport.http;

import java.rmi.RemoteException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.internal.transport.DeploymentServiceMessage;
import weblogic.deploy.service.internal.transport.MessageDispatcher;
import weblogic.deploy.service.internal.transport.MessageReceiver;

public class HTTPMessageReceiver implements MessageReceiver {
   private static final HTTPMessageReceiver SINGLETON = new HTTPMessageReceiver();
   private MessageDispatcher dispatcher = null;

   public HTTPMessageReceiver() {
      Debug.serviceHttpDebug("Created HTTPMessageReceiver");
   }

   public static MessageReceiver getMessageReceiver() {
      return SINGLETON;
   }

   public void setDispatcher(MessageDispatcher var1) {
      if (this.dispatcher == null) {
         this.dispatcher = var1;
      }

   }

   public void receiveMessage(DeploymentServiceMessage var1) throws Exception {
      this.dispatcher.dispatch(var1);
   }

   public DeploymentServiceMessage receiveSynchronousMessage(DeploymentServiceMessage var1) throws RemoteException {
      DeploymentServiceMessage var2 = this.dispatcher.blockingDispatch(var1);
      return var2;
   }
}
