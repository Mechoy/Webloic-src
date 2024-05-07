package weblogic.wsee.tools.jws.wlsdd;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import java.util.Iterator;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.j2ee.descriptor.wl.WebserviceAddressBean;
import weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WebserviceSecurityBean;
import weblogic.j2ee.descriptor.wl.WsdlBean;
import weblogic.jws.StreamAttachments;
import weblogic.jws.WSDL;
import weblogic.jws.security.UserDataConstraint;
import weblogic.jws.security.WssConfiguration;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.util.JamUtil;

public class WlsWebservices extends JAXRPCProcessor {
   private static final String TRANSPORT_GUARANTEE_INTEGRAL = "INTEGRAL";
   private static final String TRANSPORT_GUARANTEE_CONFIDENTIAL = "CONFIDENTIAL";

   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (!this.moduleInfo.isWsdlOnly()) {
         WeblogicWebservicesBean var2 = this.getWeblogicWebservicesBean(this.moduleInfo);
         fillWebServices(var1.getWebService(), var2);
      }
   }

   private WeblogicWebservicesBean getWeblogicWebservicesBean(ModuleInfo var1) {
      WeblogicWebservicesBean var2 = var1.getWeblogicWebservicesBean();
      if (var2 == null) {
         EditableDescriptorManager var3 = new EditableDescriptorManager();
         var2 = (WeblogicWebservicesBean)var3.createDescriptorRoot(WeblogicWebservicesBean.class).getRootBean();
         var1.setWeblogicWebservicesBean(var2);
      }

      return var2;
   }

   private static void fillWebServices(WebServiceSEIDecl var0, WeblogicWebservicesBean var1) throws WsBuildException {
      WebserviceDescriptionBean var2 = var1.createWebserviceDescription();
      fillWebservice(var0, var2);
      fillWebserviceSecurity(var0, var1);
   }

   private static void fillWebserviceSecurity(WebServiceDecl var0, WeblogicWebservicesBean var1) throws WsBuildException {
      JAnnotation var2 = var0.getJClass().getAnnotation(WssConfiguration.class);
      if (var2 != null) {
         JAnnotationValue var3 = var2.getValue("value");
         if (var3 != null) {
            String var4 = var3.asString();
            if (var4 != null) {
               if (var1.getWebserviceSecurity() != null) {
                  if (var4.equals(var1.getWebserviceSecurity().getMbeanName())) {
                     return;
                  }

                  throw new WsBuildException("Webservices in a module cannot point to more than one WebserviceSecurity MBean");
               }

               WebserviceSecurityBean var5 = var1.createWebserviceSecurity();
               var5.setMbeanName(var4);
            }
         }
      }

   }

   private static void fillWebservice(WebServiceSEIDecl var0, WebserviceDescriptionBean var1) {
      Iterator var2 = var0.getDDPorts();

      while(var2.hasNext()) {
         PortDecl var3 = (PortDecl)var2.next();
         PortComponentBean var4 = var1.createPortComponent();
         JAnnotation var5 = var0.getJClass().getAnnotation(WSDL.class);
         if (var5 != null) {
            boolean var6 = JamUtil.getAnnotationBooleanValue(var5, "exposed", true);
            if (!var6) {
               WsdlBean var7 = var4.createWsdl();
               var7.setExposed(var6);
            }
         }

         var1.setWebserviceDescriptionName(var0.getJClass().getQualifiedName());
         var1.setWebserviceType(var0.getType().toString());
         fillPortComponent(var0, var3, var4);
      }

   }

   private static void fillPortComponent(WebServiceSEIDecl var0, PortDecl var1, PortComponentBean var2) {
      UserDataConstraint.Transport var3 = var0.getWebServiceSecurityDecl().getTransport();
      if (var3 == UserDataConstraint.Transport.INTEGRAL) {
         var2.setTransportGuarantee("INTEGRAL");
      } else if (var3 == UserDataConstraint.Transport.CONFIDENTIAL) {
         var2.setTransportGuarantee("CONFIDENTIAL");
      }

      var2.setPortComponentName(var1.getPortName());
      WebserviceAddressBean var4 = var2.createServiceEndpointAddress();
      var4.setWebserviceContextpath(var1.getContextPath());
      var4.setWebserviceServiceuri(var1.getServiceUri());
      JAnnotation var5 = var0.getJClass().getAnnotation(StreamAttachments.class);
      if (var5 != null) {
         var2.setStreamAttachments(true);
      }

   }
}
