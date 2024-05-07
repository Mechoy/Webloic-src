package weblogic.application.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.ApplicationFileManager;
import weblogic.application.ComponentMBeanFactory;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.application.utils.EarUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.utils.jars.VirtualJarFile;

public final class EarDeploymentFactory extends BaseComponentMBeanFactory implements DeploymentFactory, ComponentMBeanFactory {
   private WebServiceUtils wsUtils = WebServiceUtils.getWebServiceUtils();

   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      return !EarUtils.isEar(var2) ? null : new EarDeployment(var1, var2);
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      return null;
   }

   private void addAppComponents(ApplicationFileManager var1, VirtualJarFile var2, List var3, ApplicationBean var4, ApplicationMBean var5, AppDeploymentMBean var6) throws IOException {
      if (var4 == null) {
         throw new IOException("No META-INF/application.xml was found in the EAR " + var5.getName());
      } else {
         ModuleBean[] var7 = var4.getModules();
         if (var7 != null) {
            Set var8 = this.wsUtils.findWebServices(var5, var1, var2, var7);

            for(int var9 = 0; var9 < var7.length; ++var9) {
               ComponentMBeanFactory.MBeanFactory var10 = null;
               String var11 = null;
               String var12 = null;
               if (var7[var9].getEjb() != null) {
                  var10 = EJB_COMP;
                  var11 = var7[var9].getEjb();
               } else if (var7[var9].getWeb() == null) {
                  if (var7[var9].getConnector() != null) {
                     var11 = var7[var9].getConnector();
                     var10 = CONNECTOR_COMP;
                  }
               } else {
                  var11 = var7[var9].getWeb().getWebUri();
                  var12 = EarUtils.fixAppContextRoot(var7[var9].getWeb().getContextRoot());
                  if (var12 == null || "".equals(var12) || "/".equals(var12)) {
                     var12 = var11;
                  }

                  var10 = var8.contains(var11) ? WEB_SERVICE_COMP : WEB_COMP;
               }

               if (var10 != null) {
                  if (var6 != null) {
                     if (var12 == null) {
                        var12 = var11;
                     }

                     var12 = this.getCompatibilityName(var12, var6);
                  }

                  var3.add(this.findOrCreateComponentMBean(var10, var5, var12, var11));
               }
            }

         }
      }
   }

   protected String getCompatibilityName(String var1, AppDeploymentMBean var2) {
      SubDeploymentMBean var3 = var2.lookupSubDeployment(var1);
      if (var3 == null) {
         return var1;
      } else {
         String var4 = var3.getCompatibilityName();
         return var4 == null ? var1 : var4;
      }
   }

   private void addWLAppComponents(List var1, WeblogicApplicationBean var2, ApplicationMBean var3) {
      if (var2 != null) {
         JDBCConnectionPoolBean[] var4 = var2.getJDBCConnectionPools();
         if (var4 != null && var4.length != 0) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               String var6 = var4[var5].getDataSourceJNDIName();
               var1.add(this.findOrCreateComponentMBean(JDBC_COMP, var3, var6));
            }

         }
      }
   }

   public boolean needsApplicationPathMunging() {
      return false;
   }

   public ComponentMBean[] findOrCreateComponentMBeans(ApplicationMBean var1, File var2, AppDeploymentMBean var3) throws DeploymentException {
      if (!EarUtils.isEar(var2)) {
         return null;
      } else {
         ArrayList var4 = new ArrayList();
         VirtualJarFile var5 = null;

         ComponentMBean[] var9;
         try {
            ApplicationFileManager var6 = ApplicationFileManager.newInstance(var2);
            var5 = var6.getVirtualJarFile();
            boolean var7 = var3 == null ? true : var3.isInternalApp();
            ApplicationDescriptor var8 = new ApplicationDescriptor(var1.getName(), var7, var5, (File)null, (DeploymentPlanBean)null, var2.getName());
            this.addAppComponents(var6, var5, var4, var8.getApplicationDescriptor(), var1, var3);
            this.addWLAppComponents(var4, var8.getWeblogicApplicationDescriptor(), var1);
            var9 = (ComponentMBean[])((ComponentMBean[])var4.toArray(new ComponentMBean[var4.size()]));
         } catch (XMLStreamException var19) {
            throw new DeploymentException(var19.getMessage(), var19);
         } catch (IOException var20) {
            throw new DeploymentException(var20.getMessage(), var20);
         } finally {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (IOException var18) {
               }
            }

         }

         return var9;
      }
   }
}
