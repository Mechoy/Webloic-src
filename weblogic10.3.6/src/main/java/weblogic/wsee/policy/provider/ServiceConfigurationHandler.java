package weblogic.wsee.policy.provider;

import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.ws.init.WsDeploymentContext;

public interface ServiceConfigurationHandler {
   void process(WsDeploymentContext var1) throws PolicyException;
}
