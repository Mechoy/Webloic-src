package weblogic.wsee.jaxws;

import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.EJBDeployInfo;
import weblogic.wsee.ws.WsException;

public class JAXWSEjbServlet extends JAXWSDeployedServlet {
   DeployInfo loadDeployInfo() throws WsException {
      return EJBDeployInfo.load(this);
   }
}
