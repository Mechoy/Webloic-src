package weblogic.wsee.jaxws.framework;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.PropertyNamevalueBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.wsee.WseePersistLogger;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.spi.WLSServiceDelegate;
import weblogic.wsee.ws.WsPort;

public class ConfigUtil {
   @Nullable
   public static Map<String, Object> getServiceRefPropsForClient(@Nullable ClientTubeAssemblerContext var0) {
      if (var0 == null) {
         return null;
      } else {
         WLSServiceDelegate var1 = null;
         if (var0.getService() instanceof WLSServiceDelegate) {
            var1 = (WLSServiceDelegate)var0.getService();
         }

         QName var2 = var0.getPortName();
         String var3 = var2 == null ? null : var2.getLocalPart();
         return getServiceRefPropsForClient(var1, var3);
      }
   }

   @Nullable
   public static Map<String, Object> getServiceRefPropsForClient(@Nullable WLSServiceDelegate var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         HashMap var2 = new HashMap();
         ServiceReferenceDescriptionBean var3 = var0.getServiceReferenceDescription();
         if (var3 != null) {
            PortInfoBean[] var4 = var3.getPortInfos();
            if (var4 != null) {
               PortInfoBean[] var5 = var4;
               int var6 = var4.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  PortInfoBean var8 = var5[var7];
                  if (var8.getPortName().equals(var1)) {
                     PropertyNamevalueBean[] var9 = var8.getStubProperties();
                     if (var9 != null) {
                        PropertyNamevalueBean[] var10 = var9;
                        int var11 = var9.length;

                        for(int var12 = 0; var12 < var11; ++var12) {
                           PropertyNamevalueBean var13 = var10[var12];
                           var2.put(var13.getName(), var13.getValue());
                        }
                     }
                  }
               }
            }
         }

         return var2;
      }
   }

   public static Map<String, Object> getServiceRefProps(@NotNull PortInfoBean var0) {
      HashMap var1 = new HashMap();
      PropertyNamevalueBean[] var2 = var0.getStubProperties();
      if (var2 != null) {
         PropertyNamevalueBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PropertyNamevalueBean var6 = var3[var5];
            var1.put(var6.getName(), var6.getValue());
         }
      }

      return var1;
   }

   public static PortInfoBean getPortInfoBeanForClient(@Nullable ClientTubeAssemblerContext var0) {
      if (var0 == null) {
         return null;
      } else {
         WLSServiceDelegate var1 = null;
         if (var0.getService() instanceof WLSServiceDelegate) {
            var1 = (WLSServiceDelegate)var0.getService();
         }

         WSDLPort var2 = var0.getWsdlModel();
         if (var1 != null && var2 != null) {
            String var3 = var2.getName().getLocalPart();
            return getPortInfoBeanForPort(var1, var3);
         } else {
            return null;
         }
      }
   }

   public static PortInfoBean getPortInfoBeanForPort(WLSServiceDelegate var0, String var1) {
      ServiceReferenceDescriptionBean var2 = var0.getServiceReferenceDescription();
      if (var2 != null) {
         PortInfoBean[] var3 = var2.getPortInfos();
         if (var3 != null) {
            PortInfoBean[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               PortInfoBean var7 = var4[var6];
               if (var7.getPortName().equals(var1)) {
                  return var7;
               }
            }
         }
      }

      return null;
   }

   @Nullable
   public static PortComponentBean getPortComponentBeanForService(@NotNull ServerTubeAssemblerContext var0) {
      EnvironmentFactory var1 = JAXRPCEnvironmentFeature.getFactory(var0.getEndpoint());
      WSDLPort var2 = var1.getPort();
      if (var2 != null) {
         QName var3 = var2.getName();
         EnvironmentFactory.SingletonService var4 = var1.getService();
         WsPort var5 = var4.getPort(var3.getLocalPart());
         PortComponentBean var6 = var5.getPortComponent();
         return var6;
      } else {
         return null;
      }
   }

   public static WebServiceLogicalStoreMBean getLogicalStoreMBean(String var0) {
      WebServiceMBean var1 = WebServiceMBeanFactory.getInstance();
      WebServiceLogicalStoreMBean[] var2 = var1.getWebServicePersistence().getWebServiceLogicalStores();
      WebServiceLogicalStoreMBean[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WebServiceLogicalStoreMBean var6 = var3[var5];
         if (var6.getName().equals(var0)) {
            return var6;
         }
      }

      throw new IllegalArgumentException(WseePersistLogger.logLogicalStoreNotFoundLoggable(var0).getMessage());
   }

   public static File getStandaloneClientStoreDir() {
      String var0 = System.getProperty("weblogic.wsee.persistence.webservice-client.dir", System.getProperty("user.dir", System.getProperty("user.home", System.getProperty("java.io.tmpdir"))));
      return new File(var0);
   }
}
