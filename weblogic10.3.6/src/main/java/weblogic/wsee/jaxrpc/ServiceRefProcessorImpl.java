package weblogic.wsee.jaxrpc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import weblogic.deployment.ServiceRefProcessor;
import weblogic.deployment.ServiceRefProcessorException;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.J2EEUtils;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.PortComponentRefBean;
import weblogic.j2ee.descriptor.PortMappingBean;
import weblogic.j2ee.descriptor.ServiceInterfaceMappingBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.wsee.deploy.ClientDeployInfo;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.wsdl.WsdlPort;

public class ServiceRefProcessorImpl implements ServiceRefProcessor {
   private static final boolean verbose = Verbose.isVerbose(ServiceRefProcessorImpl.class);
   private static final boolean DEBUG = false;
   private static String IMPL_POSTFIX = "_WEBLOGIC_WEBSERVICE_IMPL";
   private String implJndiName;
   private String proxyJndiName;
   private ServiceImpl serviceImpl;
   private Object serviceProxy;
   private ClientDeployInfo info;

   public ServiceRefProcessorImpl(ServiceRefBean var1, ServiceReferenceDescriptionBean var2, ServletContext var3) throws WsException {
      this.proxyJndiName = var1.getServiceRefName();
      this.implJndiName = this.proxyJndiName + IMPL_POSTFIX;
      this.info = new ClientDeployInfo();
      String var4 = null;
      if (var1.getWsdlFile() == null) {
         this.checkNoWsdlCase(var1);
      } else {
         var4 = this.getWsdlUrl(var1, var2, var3);
         this.loadJaxrpcMappingFile(var1, var3);
      }

      this.info.setServiceRef(var1);
      this.info.setWlServiceRef(var2);

      try {
         this.serviceImpl = new ServiceImpl(this.info, var4);
         this.serviceImpl._setAllowHandlerChain(false);
      } catch (ServiceException var6) {
         throw new WsException(var6.getMessage(), var6);
      }

      this.serviceProxy = this.createServiceProxy();
   }

   private void loadJaxrpcMappingFile(ServiceRefBean var1, ServletContext var2) throws WsException {
      ClassLoader var3 = Thread.currentThread().getContextClassLoader();
      if (var1.getJaxrpcMappingFile() == null) {
         throw new WsException("<jaxrpc-mapping-file> should be specified if <wsdl-file> is declared.");
      } else {
         InputStream var4 = null;
         if (var2 != null) {
            var4 = var2.getResourceAsStream(var1.getJaxrpcMappingFile());
         } else {
            var4 = var3.getResourceAsStream(var1.getJaxrpcMappingFile());
         }

         if (var4 == null) {
            throw new WsException("Can't find jaxrpc mapping file \"" + var1.getJaxrpcMappingFile() + "\"");
         } else {
            try {
               DescriptorManager var5 = new DescriptorManager();
               this.info.setMappingdd((JavaWsdlMappingBean)var5.createDescriptor(var4).getRootBean());
            } catch (IOException var13) {
               throw new WsException("Failed to parse jaxrpc mapping file \"" + var1.getJaxrpcMappingFile() + "\"" + var13, var13);
            } finally {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (IOException var12) {
                  }
               }

            }

         }
      }
   }

   private void checkNoWsdlCase(ServiceRefBean var1) throws WsException {
      if (var1.getJaxrpcMappingFile() != null) {
         throw new WsException("<jaxrpc-mapping-file> should not be specified if no <wsdl-file> is declared.");
      } else if (var1.getServiceQname() != null) {
         throw new WsException("<service-qname> should not be specified if no <wsdl-file> is declared.");
      }
   }

   private String getWsdlUrl(ServiceRefBean var1, ServiceReferenceDescriptionBean var2, ServletContext var3) throws WsException {
      ClassLoader var4 = Thread.currentThread().getContextClassLoader();
      URL var5 = null;
      if (verbose) {
         if (var2 == null) {
            Verbose.log((Object)"Weblogic description bean is null");
         } else {
            Verbose.log((Object)("Got weblogic description bean:" + var2));
         }
      }

      if (var2 != null && var2.getWsdlUrl() != null) {
         var5 = getResource(var2.getWsdlUrl(), var3);
      } else {
         if (var3 != null) {
            var5 = this.creatURLFromServletContext(var1, var3);
         } else {
            var5 = var4.getResource(var1.getWsdlFile());
         }

         if (var5 == null) {
            throw new WsException("Can't find wsdl file \"" + var1.getWsdlFile() + "\"");
         }
      }

      return var5.toString();
   }

   private URL creatURLFromServletContext(ServiceRefBean var1, ServletContext var2) throws WsException {
      try {
         URL var3;
         if (var1.getWsdlFile().startsWith("/")) {
            var3 = var2.getResource(var1.getWsdlFile());
         } else {
            var3 = var2.getResource("/" + var1.getWsdlFile());
         }

         return var3;
      } catch (MalformedURLException var5) {
         throw new WsException("Can't find wsdl file \"" + var1.getWsdlFile() + "\"" + var5, var5);
      }
   }

   private static URL getResource(String var0, ServletContext var1) throws WsException {
      try {
         URL var2 = null;
         if (var0 != null && var0.startsWith("/") && var1 != null) {
            var2 = var1.getResource(var0);
         }

         if (var2 == null) {
            var2 = Thread.currentThread().getContextClassLoader().getResource(var0);
         }

         if (var2 == null) {
            var2 = new URL(var0);
         }

         return var2;
      } catch (Exception var3) {
         throw new WsException("Failed to interpret the given WSDL URL as a web resource, class loader resource, or absolute URL: " + var3.toString(), var3);
      }
   }

   public void bindServiceRef(Context var1, Context var2, String var3) throws NamingException, ServiceRefProcessorException {
      var2.bind(this.implJndiName, this.serviceImpl);
      var2.bind(this.proxyJndiName, this.serviceProxy);
      if (verbose) {
         Verbose.log((Object)("Bound service-ref under name " + this.proxyJndiName));
      }

      ServiceRefBean var4 = this.info.getServiceRef();
      PortComponentRefBean[] var5 = var4.getPortComponentRefs();
      if (var5.length != 0) {
         try {
            this.processComponentLink(var1, var5, var3);
         } catch (WsException var7) {
            throw new ServiceRefProcessorException(var7.getMessage(), var7);
         }
      }

   }

   private void processComponentLink(Context var1, PortComponentRefBean[] var2, String var3) throws WsException {
      HashMap var4 = new HashMap();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         PortComponentRefBean var6 = var2[var5];
         String var7 = var6.getPortComponentLink();
         if (var7 != null) {
            try {
               Context var8 = null;

               try {
                  var8 = (Context)var1.lookup("app/wsee");
               } catch (NamingException var11) {
                  throw new WsException("Failed to resolve port component link, do you have any webservices defined in your application? " + var11, var11);
               }

               String var9 = J2EEUtils.getAppScopedLinkPath(var7, var3, var8);
               if (var9 == null) {
                  throw new WsException("Failed to resolve port component link " + var7);
               }

               WsdlPort var10 = (WsdlPort)var1.lookup("app/wsee/" + var9);
               if (verbose) {
                  Verbose.log((Object)("Got wsdl port for port-component-link " + var7 + ":" + var10));
               }

               var4.put(var6.getServiceEndpointInterface(), var10);
            } catch (NamingException var12) {
               throw new WsException("Failed to resolve port component link, " + var7 + var12, var12);
            }
         }
      }

      this.serviceImpl.setPortComponentLinks(var4);
   }

   public void unbindServiceRef(Context var1) throws NamingException {
      var1.unbind(this.implJndiName);
      var1.unbind(this.proxyJndiName);
   }

   private Object createServiceProxy() throws WsException {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      ServiceRefBean var2 = this.info.getServiceRef();
      String var3 = var2.getServiceInterface();
      Class var4 = null;

      try {
         var4 = var1.loadClass(var3);
      } catch (ClassNotFoundException var8) {
         throw new WsException("Failed to service interface class " + var8, var8);
      }

      ClassLoader var5 = var4.getClassLoader();
      Class[] var6 = new Class[]{var4};
      JndiServiceImpl var7 = new JndiServiceImpl(this.implJndiName, this.buildPortNameMap());
      return Proxy.newProxyInstance(var5, var6, var7);
   }

   private Map buildPortNameMap() {
      HashMap var1 = new HashMap();
      JavaWsdlMappingBean var2 = this.info.getMappingdd();
      QName var3 = this.info.getServiceRef().getServiceQname();
      if (var2 == null) {
         return Collections.EMPTY_MAP;
      } else {
         ServiceInterfaceMappingBean[] var4 = var2.getServiceInterfaceMappings();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               ServiceInterfaceMappingBean var6 = var4[var5];
               if (var3 == null || var3.equals(var6.getWsdlServiceName())) {
                  this.addPortNames(var6, var1);
                  break;
               }
            }
         }

         return var1;
      }
   }

   private void addPortNames(ServiceInterfaceMappingBean var1, Map var2) {
      PortMappingBean[] var3 = var1.getPortMappings();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         PortMappingBean var5 = var3[var4];
         var2.put(var5.getJavaPortName(), var5.getPortName());
      }

   }
}
