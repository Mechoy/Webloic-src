package weblogic.wsee.tools.jws.webservices;

import java.util.Iterator;
import javax.xml.namespace.QName;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;
import weblogic.j2ee.descriptor.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;

public class WebServicesProcessor extends JAXRPCProcessor {
   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (!this.moduleInfo.isWsdlOnly()) {
         WebservicesBean var2 = this.getWebservicesBean(this.moduleInfo);
         this.fillWebServices(var1.getWebService(), var2);
      }
   }

   private WebservicesBean getWebservicesBean(ModuleInfo var1) {
      WebservicesBean var2 = var1.getWebServicesBean();
      if (var2 == null) {
         EditableDescriptorManager var3 = new EditableDescriptorManager();
         var2 = (WebservicesBean)var3.createDescriptorRoot(WebservicesBean.class).getRootBean();
         var2.setVersion("1.2");
         var1.setWebServicesBean(var2);
      }

      return var2;
   }

   private void fillWebServices(WebServiceSEIDecl var1, WebservicesBean var2) {
      WebserviceDescriptionBean var3 = var2.createWebserviceDescription();
      var3.setWebserviceDescriptionName(var1.getJClass().getQualifiedName());
      var3.setWsdlFile(getWsdlFile(var1));
      var3.setJaxrpcMappingFile(getRoot(var1) + '/' + var1.getArtifactName() + ".xml");

      PortComponentBean var6;
      for(Iterator var4 = var1.getDDPorts(); var4.hasNext(); var1.getHandlerChainDecl().populatePort(var6, (EnvEntryBean[])null)) {
         PortDecl var5 = (PortDecl)var4.next();
         var6 = var3.createPortComponent();
         var6.setPortComponentName(var5.getPortName());
         var6.setWsdlPort(new QName(var1.getTargetNamespace(), var5.getPortName()));
         var6.setServiceEndpointInterface(var1.getEndpointInterfaceName());
         ServiceImplBeanBean var7 = var6.createServiceImplBean();
         if (var1.isEjb()) {
            var7.setEjbLink(var1.getDeployedName());
         } else {
            var7.setServletLink(var1.getDeployedName() + var5.getProtocol());
         }
      }

   }

   private static String getRoot(WebServiceDecl var0) {
      return var0.isEjb() ? "META-INF" : "WEB-INF";
   }

   private static String getWsdlFile(WebServiceSEIDecl var0) {
      if (var0.getCowReader() == null) {
         return getRoot(var0) + "/" + var0.getWsdlFile();
      } else {
         String var1 = var0.getWsdlLocation();
         if (var0.isEjb() && var1.charAt(0) == '/') {
            var1 = var1.substring(1);
         }

         return var1;
      }
   }
}
