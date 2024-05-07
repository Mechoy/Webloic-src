package weblogic.wsee.security.wssp.deploy;

import weblogic.wsee.jaxws.framework.jaxrpc.ListenerUsage;
import weblogic.wsee.jaxws.framework.jaxrpc.TubelineDeploymentListener;

public class WssClientTubelineDeploymentListener extends TubelineDeploymentListener {
   public WssClientTubelineDeploymentListener() {
      super(WssClientDeploymentListener.class, ListenerUsage.CLIENT_ONLY);
   }
}
