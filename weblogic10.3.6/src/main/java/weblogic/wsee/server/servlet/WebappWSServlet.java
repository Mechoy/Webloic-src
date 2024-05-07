package weblogic.wsee.server.servlet;

import javax.servlet.ServletException;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.ServletDeployInfo;

public class WebappWSServlet extends BaseWSServlet {
   public DeployInfo loadDeployInfo() throws ServletException {
      return ServletDeployInfo.load(this);
   }
}
