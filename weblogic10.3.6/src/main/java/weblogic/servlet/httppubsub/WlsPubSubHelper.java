package weblogic.servlet.httppubsub;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.SessionData;

public class WlsPubSubHelper implements PubSubHelper {
   public Object getAuthSubject(HttpSession var1) {
      SessionData var2 = (SessionData)var1;
      if (var2 == null) {
         return null;
      } else {
         AuthenticatedSubject var3 = (AuthenticatedSubject)var2.getInternalAttribute("weblogic.authuser");
         return var3;
      }
   }

   public Object getWeblogicWebAppBean(ServletContext var1) {
      WebAppServletContext var2 = (WebAppServletContext)var1;
      return var2.getWebAppModule().getWlWebAppBean();
   }

   public String getApplicationName(ServletContext var1) {
      WebAppServletContext var2 = (WebAppServletContext)var1;
      return var2.getApplicationId();
   }

   public String getContextPath(ServletContext var1) {
      WebAppServletContext var2 = (WebAppServletContext)var1;
      return var2.getContextPath();
   }

   public String getSecurityModel(ServletContext var1) {
      WebAppServletContext var2 = (WebAppServletContext)var1;
      if (var2.getApplicationContext() != null) {
         AppDeploymentMBean var3 = var2.getApplicationContext().getAppDeploymentMBean();
         if (var3 != null) {
            return var3.getSecurityDDModel();
         }
      }

      return "DDOnly";
   }
}
