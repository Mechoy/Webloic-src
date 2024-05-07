package weblogic.management.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.internal.DeploymentHandler;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.internal.DeploymentHandlerHome;

public class GenericServiceManager implements DeploymentHandler {
   private static final GenericServiceManager myself = new GenericServiceManager();
   private boolean initialized;
   private final List handlerList = new ArrayList();

   private GenericServiceManager() {
   }

   public static GenericServiceManager getManager() {
      return myself;
   }

   public GenericManagedService register(Class var1, Class var2, boolean var3) {
      if (!DeploymentMBean.class.isAssignableFrom(var1)) {
         throw new IllegalArgumentException("The class " + var1.getName() + " does not implement DeploymentMBean");
      } else if (!GenericAdminHandler.class.isAssignableFrom(var2)) {
         throw new IllegalArgumentException("The class " + var2.getName() + " does not implement GenericAdminHandler");
      } else {
         GenericManagedService var4 = new GenericManagedService(var1, var2, var3);
         synchronized(this) {
            this.handlerList.add(var4);
            if (!this.initialized) {
               DeploymentHandlerHome.addDeploymentHandler(this);
               this.initialized = true;
            }

            return var4;
         }
      }
   }

   private synchronized GenericManagedService findService(DeploymentMBean var1) {
      Iterator var2 = this.handlerList.iterator();

      GenericManagedService var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (GenericManagedService)var2.next();
      } while(!var3.getMBeanClass().isAssignableFrom(var1.getClass()));

      return var3;
   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      GenericManagedService var3 = this.findService(var1);
      if (var3 != null) {
         var3.prepareDeployment(var1);
      }

   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      GenericManagedService var3 = this.findService(var1);
      if (var3 != null) {
         var3.activateDeployment(var1);
      }

   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      GenericManagedService var3 = this.findService(var1);
      if (var3 != null) {
         var3.deactivateDeployment(var1);
      }

   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      GenericManagedService var3 = this.findService(var1);
      if (var3 != null) {
         var3.unprepareDeployment(var1);
      }

   }
}
