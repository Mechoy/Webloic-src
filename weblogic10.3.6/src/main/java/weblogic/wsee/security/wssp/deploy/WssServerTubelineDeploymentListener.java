package weblogic.wsee.security.wssp.deploy;

import weblogic.wsee.jaxws.framework.jaxrpc.ListenerUsage;
import weblogic.wsee.jaxws.framework.jaxrpc.TubelineDeploymentListener;

public class WssServerTubelineDeploymentListener extends TubelineDeploymentListener {
   public WssServerTubelineDeploymentListener() {
      super(WssServerDeploymentListener.class, ListenerUsage.SERVER_ONLY);
   }
}
