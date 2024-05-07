package weblogic.application;

import java.io.File;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.ConnectorComponentMBean;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.management.configuration.JDBCPoolComponentMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.configuration.WebServiceComponentMBean;

public interface ComponentMBeanFactory {
   MBeanFactory EJB_COMP = new MBeanFactory() {
      public ComponentMBean newCompMBean(ApplicationMBean var1, String var2) {
         return var1.createEJBComponent(var2);
      }

      public Class getComponentMBeanType() {
         return EJBComponentMBean.class;
      }
   };
   MBeanFactory WEB_COMP = new MBeanFactory() {
      public ComponentMBean newCompMBean(ApplicationMBean var1, String var2) {
         return var1.createWebAppComponent(var2);
      }

      public Class getComponentMBeanType() {
         return WebAppComponentMBean.class;
      }
   };
   MBeanFactory WEB_SERVICE_COMP = new MBeanFactory() {
      public ComponentMBean newCompMBean(ApplicationMBean var1, String var2) {
         return var1.createWebServiceComponent(var2);
      }

      public Class getComponentMBeanType() {
         return WebServiceComponentMBean.class;
      }
   };
   MBeanFactory CONNECTOR_COMP = new MBeanFactory() {
      public ComponentMBean newCompMBean(ApplicationMBean var1, String var2) {
         return var1.createConnectorComponent(var2);
      }

      public Class getComponentMBeanType() {
         return ConnectorComponentMBean.class;
      }
   };
   MBeanFactory JDBC_COMP = new MBeanFactory() {
      public ComponentMBean newCompMBean(ApplicationMBean var1, String var2) {
         return var1.createJDBCPoolComponent(var2);
      }

      public Class getComponentMBeanType() {
         return JDBCPoolComponentMBean.class;
      }
   };
   ComponentMBeanFactory DEFAULT_FACTORY = new ComponentMBeanFactory() {
      public ComponentMBean[] findOrCreateComponentMBeans(ApplicationMBean var1, File var2) {
         return new ComponentMBean[0];
      }

      public ComponentMBean[] findOrCreateComponentMBeans(ApplicationMBean var1, File var2, AppDeploymentMBean var3) {
         return new ComponentMBean[0];
      }

      public boolean needsApplicationPathMunging() {
         return false;
      }
   };

   ComponentMBean[] findOrCreateComponentMBeans(ApplicationMBean var1, File var2, AppDeploymentMBean var3) throws DeploymentException;

   boolean needsApplicationPathMunging();

   public abstract static class MBeanFactory {
      public abstract ComponentMBean newCompMBean(ApplicationMBean var1, String var2);

      public abstract Class getComponentMBeanType();
   }
}
