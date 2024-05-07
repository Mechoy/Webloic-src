package weblogic.wsee.jaxws;

import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.ServletDeployInfo;
import weblogic.wsee.ws.WsException;

public class JAXWSWebAppServlet extends JAXWSDeployedServlet {
   DeployInfo loadDeployInfo() throws WsException {
      ServletDeployInfo var1 = ServletDeployInfo.load(this);
      return var1;
   }

   private WebAppServletContext getWebAppServletContext() {
      return (WebAppServletContext)this.getServletContext();
   }
}
