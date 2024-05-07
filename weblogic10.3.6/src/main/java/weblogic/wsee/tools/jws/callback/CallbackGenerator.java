package weblogic.wsee.tools.jws.callback;

import com.bea.util.jam.JClass;
import java.util.Iterator;
import javax.xml.namespace.QName;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.bind.TypeFamily;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.ClientGenFactory;
import weblogic.wsee.tools.clientgen.jaxrpc.ClientGenImpl;
import weblogic.wsee.tools.clientgen.jaxrpc.Options;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public class CallbackGenerator extends JAXRPCProcessor {
   private JAXRPCWebServiceInfo webServiceInfo;

   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (!this.moduleInfo.isWsdlOnly()) {
         if (var1.getWebService().getCallbackService() != null) {
            this.webServiceInfo = var1;
            JClass var2 = var1.getWebService().getCallbackService().getJClass();

            try {
               WsdlPartnerLinkType var3 = (WsdlPartnerLinkType)var1.getDefinitions().getExtension("PartnerLinkType");

               assert var3 != null : "Partner link type not found in wsdl";

               QName var4 = var3.getPortTypeName("Callback");
               this.callbackClientgen(var4, var1.getWebService().getJClass(), var2);
            } catch (WsdlException var5) {
               throw new WsBuildException(var5.getMessage(), var5);
            }
         }
      }
   }

   private void callbackClientgen(QName var1, JClass var2, JClass var3) throws WsBuildException {
      WsdlService var4 = this.findCallbackService(var1);
      Options var5 = new Options();
      var5.setServiceName(var4.getName().getLocalPart());
      var5.setTypeFamily(TypeFamily.getTypeFamilyForClass(var3));
      var5.setWriteJavaTypes(false);
      var5.setJaxRPCWrappedArrayStyle(false);
      var5.setGenerateAsyncMethods(false);
      var5.setUseJaxRpcRules(false);
      ClientGenImpl var6 = (ClientGenImpl)ClientGenFactory.newInstance(WebServiceType.JAXRPC);
      var6.setWsdlDefinitions(this.webServiceInfo.getDefinitions());
      var6.setCallbackJClass(var3);
      var6.setWebServiceJClass(var2);
      var6.setDestDir(this.moduleInfo.getOutputDir());
      var6.setPackageName(this.webServiceInfo.getWebService().getPackageName() + ".callbackclient");
      var6.setOptions(var5);
      var6.execute();
   }

   private WsdlService findCallbackService(QName var1) {
      Iterator var2 = this.webServiceInfo.getDefinitions().getServices().values().iterator();

      while(var2.hasNext()) {
         WsdlService var3 = (WsdlService)var2.next();
         Iterator var4 = var3.getPorts().values().iterator();

         while(var4.hasNext()) {
            WsdlPort var5 = (WsdlPort)var4.next();
            if (var5.getPortType().getName().equals(var1)) {
               return var3;
            }
         }
      }

      throw new AssertionError("Service with PortType " + var1 + " is " + "not found in wsdl.");
   }
}
