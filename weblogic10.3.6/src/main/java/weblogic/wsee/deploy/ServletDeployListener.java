package weblogic.wsee.deploy;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import weblogic.management.DeploymentException;
import weblogic.servlet.WebLogicServletContextListener;
import weblogic.servlet.internal.WebAppServletContext;

public class ServletDeployListener implements WebLogicServletContextListener, ServletContextListener {
   private WSEEModule module = null;

   public void contextPrepared(ServletContextEvent var1) {
      WebAppServletContext var2 = (WebAppServletContext)var1.getServletContext();
      this.module = new WSEEWebModule(var2);

      try {
         AppDeploymentExtensionFactory.INSTANCE.addModule(var2.getApplicationContext(), this.module);
      } catch (DeploymentException var4) {
         throw new RuntimeException(var4);
      }
   }

   public void contextInitialized(ServletContextEvent var1) {
   }

   public void contextDestroyed(ServletContextEvent var1) {
      if (this.module != null) {
         WebAppServletContext var2 = (WebAppServletContext)var1.getServletContext();

         try {
            AppDeploymentExtensionFactory.INSTANCE.removeModule(var2.getApplicationContext(), this.module);
         } catch (DeploymentException var4) {
            throw new RuntimeException(var4);
         }

         this.module = null;
      }

   }
}
