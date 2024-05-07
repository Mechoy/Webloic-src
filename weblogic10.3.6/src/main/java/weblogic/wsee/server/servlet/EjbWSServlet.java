package weblogic.wsee.server.servlet;

import javax.servlet.ServletException;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.EJBDeployInfo;

public class EjbWSServlet extends BaseWSServlet {
   DeployInfo loadDeployInfo() throws ServletException {
      return EJBDeployInfo.load(this);
   }
}
