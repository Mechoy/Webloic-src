package weblogic.wsee.ws;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.ExceptionMappingBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.JavaXmlTypeMappingBean;
import weblogic.j2ee.descriptor.ServiceEndpointInterfaceMappingBean;
import weblogic.j2ee.descriptor.ServiceInterfaceMappingBean;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

class NoMappingHelper {
   static boolean isNoMappingCase(JavaWsdlMappingBean var0) {
      ExceptionMappingBean[] var1 = var0.getExceptionMappings();
      JavaXmlTypeMappingBean[] var2 = var0.getJavaXmlTypeMappings();
      ServiceInterfaceMappingBean[] var3 = var0.getServiceInterfaceMappings();
      ServiceEndpointInterfaceMappingBean[] var4 = var0.getServiceEndpointInterfaceMappings();
      return var1.length == 0 && var2.length == 0 && var3.length == 0 && var4.length == 0;
   }

   static QName[] getXmlTypes(WsdlDefinitions var0) throws WsException {
      HashSet var1 = new HashSet();
      WsdlPort var2 = getWsdlPort(var0);
      checkBinding(var2.getBinding());
      Iterator var3 = var2.getPortType().getOperations().values().iterator();

      while(var3.hasNext()) {
         WsdlOperation var4 = (WsdlOperation)var3.next();
         if (var4.getInput() != null) {
            fillParamTypes(var4.getInput(), var1);
         }

         if (var4.getOutput() != null) {
            fillParamTypes(var4.getOutput(), var1);
         }
      }

      return (QName[])((QName[])var1.toArray(new QName[var1.size()]));
   }

   private static void checkBinding(WsdlBinding var0) throws WsException {
   }

   private static void fillParamTypes(WsdlMessage var0, Set var1) throws WsException {
      Iterator var2 = var0.getParts().values().iterator();

      while(var2.hasNext()) {
         WsdlPart var3 = (WsdlPart)var2.next();
         QName var4 = var3.getType();
         if (var4 == null) {
            throw new WsException("Wsdl message " + var0.getName() + " part" + " " + var3.getName() + " uses a xml element type " + " A full feature JAXRPC mapping file is required.");
         }

         var1.add(var4);
      }

   }

   private static WsdlPort getWsdlPort(WsdlDefinitions var0) throws WsException {
      Iterator var1 = var0.getServices().values().iterator();
      if (!var1.hasNext()) {
         throw new WsException("Wsdl doesn't have any service defined.  A full feature JAXRPC mapping file is required.");
      } else {
         WsdlService var2 = (WsdlService)var1.next();
         if (var1.hasNext()) {
            throw new WsException("Wsdl doesn't have more than one service defined.  A full feature JAXRPC mapping file is required.");
         } else {
            var1 = var2.getPorts().values().iterator();
            if (!var1.hasNext()) {
               throw new WsException("Wsdl service " + var2.getName() + " doesn't have any port defined. " + "A full feature JAXRPC mapping file is required.");
            } else {
               WsdlPort var3 = (WsdlPort)var1.next();
               if (var1.hasNext()) {
                  throw new WsException("Wsdl service " + var2.getName() + " has more than one port defined. " + "A full feature JAXRPC mapping file is required.");
               } else {
                  return var3;
               }
            }
         }
      }
   }
}
