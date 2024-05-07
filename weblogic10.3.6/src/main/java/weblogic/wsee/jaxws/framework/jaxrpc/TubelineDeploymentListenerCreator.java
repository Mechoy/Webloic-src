package weblogic.wsee.jaxws.framework.jaxrpc;

import weblogic.wsee.ws.init.WsDeploymentListener;

public class TubelineDeploymentListenerCreator implements weblogic.wsee.jaxws.tubeline.TubelineDeploymentListenerCreator {
   public weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener create(Class var1) {
      return WsDeploymentListener.class.isAssignableFrom(var1) ? new TubelineDeploymentListener(var1) : null;
   }
}
