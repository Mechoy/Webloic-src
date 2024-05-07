package weblogic.deploy.service.internal.transport;

public interface MessageDispatcher {
   void dispatch(DeploymentServiceMessage var1);

   DeploymentServiceMessage blockingDispatch(DeploymentServiceMessage var1);
}
