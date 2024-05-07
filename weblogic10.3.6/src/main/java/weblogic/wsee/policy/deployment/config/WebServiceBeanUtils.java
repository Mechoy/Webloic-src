package weblogic.wsee.policy.deployment.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.xml.namespace.QName;
import weblogic.deploy.api.model.WebLogicDDBeanRoot;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanDConfig;
import weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;

public class WebServiceBeanUtils {
   public static final String DIRECTION_BOTH = "both";
   public static final String DIRECTION_INBOUND = "inbound";
   public static final String DIRECTION_OUTBOUND = "outbound";
   public static final String POLICY_TYPE_WLS = "wls";
   public static final String POLICY_TYPE_OWSM = "owsm";
   public static final String POLICY_TYPE_UNKNOWN = "unknown";
   private static WebservicePolicyRefBean policyRefBean;

   public static String getServiceName(WseeV2RuntimeMBean var0) {
      String var1 = var0.getServiceName();
      int var2 = var1.indexOf(33);
      if (var2 > 0) {
         var1 = var1.substring(var2 + 1);
      }

      return var1;
   }

   public static String[] getServletMappingUris(WseeV2RuntimeMBean var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         StringTokenizer var2 = new StringTokenizer(var0.getURI(), ";");

         while(var2.hasMoreTokens()) {
            String var3 = var2.nextToken();
            var1.add(var3);
         }

         return (String[])((String[])var1.toArray(new String[var1.size()]));
      }
   }

   public static WebAppBean getWebAppBean(DeploymentConfigurationHelper var0, String var1) throws PolicyManagementException {
      WebAppBean var2 = null;

      try {
         WebLogicDeployableObject var3 = var0.getDeployableObject(var1);
         DDBeanRoot var4 = var3.getDDBeanRoot("WEB-INF/web.xml");
         if (var4 != null) {
            WebLogicDDBeanRoot var5 = (WebLogicDDBeanRoot)var4;
            var2 = (WebAppBean)var5.getDescriptorBean();
         }
      } catch (FileNotFoundException var6) {
      } catch (DDBeanCreateException var7) {
         throw new PolicyManagementException(var7);
      } catch (IOException var8) {
         throw new PolicyManagementException(var8);
      }

      return var2;
   }

   public static EjbJarBean getEjbBean(DeploymentConfigurationHelper var0, String var1) throws PolicyManagementException {
      EjbJarBean var2 = null;

      try {
         WebLogicDeployableObject var3 = var0.getDeployableObject(var1);
         DDBeanRoot var4 = var3.getDDBeanRoot("META-INF/ejb-jar.xml");
         WebLogicDDBeanRoot var5 = (WebLogicDDBeanRoot)var4;
         var2 = (EjbJarBean)var5.getDescriptorBean();
      } catch (FileNotFoundException var6) {
      } catch (DDBeanCreateException var7) {
         throw new PolicyManagementException(var7);
      } catch (IOException var8) {
         throw new PolicyManagementException(var8);
      }

      return var2;
   }

   public static String getServiceClassName(String var0, WebservicesBean var1, QName var2, boolean var3, WebAppBean var4, EjbJarBean var5) throws PolicyManagementException {
      String var6 = getLinkName(var1, var0, var2, var3);
      String var7 = null;
      if (var6 != null) {
         if (var3) {
            var7 = getClassNameFromWebApp(var6, var4);
         } else {
            var7 = getClassNameFromEJB(var6, var5);
         }

         if (var7 == null) {
            throw new PolicyManagementException("Could not get the class name");
         } else {
            return var7;
         }
      } else {
         throw new PolicyManagementException("Could not find the link name");
      }
   }

   private static String getClassNameFromEJB(String var0, EjbJarBean var1) {
      String var2 = null;
      EnterpriseBeansBean var3 = var1.getEnterpriseBeans();
      SessionBeanBean[] var4 = var3.getSessions();

      for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
         if (var0.equals(var4[var5].getEjbName())) {
            var2 = var4[var5].getEjbClass();
            break;
         }
      }

      if (var2 == null) {
         EntityBeanBean[] var7 = var3.getEntities();

         for(int var6 = 0; var7 != null && var6 < var7.length; ++var6) {
            if (var0.equals(var7[var6].getEjbName())) {
               var2 = var7[var6].getEjbClass();
               break;
            }
         }
      }

      return var2;
   }

   private static String getClassNameFromWebApp(String var0, WebAppBean var1) {
      String var2 = null;
      ServletBean[] var3 = var1.getServlets();

      for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
         if (var0.equals(var3[var4].getServletName())) {
            var2 = var3[var4].getServletClass();
            break;
         }
      }

      return var2;
   }

   public static WebservicesBean getWebservicesBean(DeploymentConfigurationHelper var0, String var1, boolean var2) throws PolicyManagementException {
      try {
         WebservicesBean var3 = null;
         WebLogicDeployableObject var4 = var0.getDeployableObject(var1);
         DDBeanRoot var5 = null;
         if (var2) {
            var5 = var4.getDDBeanRoot("WEB-INF/webservices.xml");
         } else {
            var5 = var4.getDDBeanRoot("META-INF/webservices.xml");
         }

         if (var5 != null) {
            WebLogicDDBeanRoot var6 = (WebLogicDDBeanRoot)var5;
            var3 = (WebservicesBean)var6.getDescriptorBean();
         }

         return var3;
      } catch (Exception var7) {
         throw new PolicyManagementException(var7);
      }
   }

   public static WeblogicWebservicesBean getWeblogicWebservicesBean(DeploymentConfigurationHelper var0, String var1, boolean var2) throws PolicyManagementException {
      try {
         WebLogicDeployableObject var3 = var0.getDeployableObject(var1);
         DConfigBeanRoot var4 = var0.getDeploymentConfiguration().getDConfigBeanRoot(var3.getDDBeanRoot());
         WeblogicWebservicesBean var5 = null;
         DDBeanRoot var6 = null;
         if (var2) {
            var6 = var3.getDDBeanRoot("WEB-INF/webservices.xml");
         } else {
            var6 = var3.getDDBeanRoot("META-INF/webservices.xml");
         }

         if (var6 != null) {
            WeblogicWebservicesBeanDConfig var7 = (WeblogicWebservicesBeanDConfig)var4.getDConfigBean(var6);
            if (var7 != null) {
               var5 = (WeblogicWebservicesBean)var7.getDescriptorBean();
            }
         }

         return var5;
      } catch (Exception var8) {
         throw new PolicyManagementException(var8);
      }
   }

   public static WebserviceDescriptionBean getWeblogicWebserviceDescriptionBean(DeploymentConfigurationHelper var0, String var1, boolean var2, String var3) throws ConfigurationException, PolicyManagementException {
      WeblogicWebservicesBean var4 = getWeblogicWebservicesBean(var0, var1, var2);
      if (var4 != null) {
         WebserviceDescriptionBean[] var5 = var4.getWebserviceDescriptions();

         for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
            if (var5[var6].getWebserviceDescriptionName().equals(var3)) {
               return var5[var6];
            }
         }
      }

      return null;
   }

   public static String getWebServiceDescriptionName(DeploymentConfigurationHelper var0, String var1, boolean var2, String var3) throws PolicyManagementException, ConfigurationException {
      WebserviceDescriptionBean var4 = getWeblogicWebserviceDescriptionBean(var0, var1, var2, var3);
      return var4 != null ? var4.getWebserviceDescriptionName() : null;
   }

   public static String getLinkName(WebservicesBean var0, String var1, QName var2, boolean var3) {
      weblogic.j2ee.descriptor.WebserviceDescriptionBean[] var4 = var0.getWebserviceDescriptions();
      String var7 = null;

      for(int var8 = 0; var4 != null && var8 < var4.length; ++var8) {
         if (var1.equals(var4[var8].getWebserviceDescriptionName())) {
            PortComponentBean[] var5 = var4[var8].getPortComponents();

            for(int var9 = 0; var5 != null && var9 < var5.length; ++var9) {
               if (var2.getLocalPart().equals(var5[var9].getWsdlPort().getLocalPart())) {
                  ServiceImplBeanBean var6 = var5[var9].getServiceImplBean();
                  if (var6 != null) {
                     if (var3) {
                        var7 = var6.getServletLink();
                     } else {
                        var7 = var6.getEjbLink();
                     }

                     return var7;
                  }

                  return var7;
               }
            }

            return var7;
         }
      }

      return var7;
   }

   public static Class loadServiceClass(DeploymentConfigurationHelper var0, String var1, String var2) throws PolicyManagementException {
      try {
         WebLogicDeployableObject var3 = var0.getDeployableObject(var1);
         return var3.getClassFromScope(var2);
      } catch (Exception var4) {
         throw new PolicyManagementException(var4);
      }
   }
}
