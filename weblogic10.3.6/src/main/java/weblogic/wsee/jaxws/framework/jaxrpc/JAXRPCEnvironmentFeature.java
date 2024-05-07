package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.jaxws.framework.jaxrpc.client.ClientEnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.server.ServerEnvironmentFactory;

public class JAXRPCEnvironmentFeature extends WebServiceFeature {
   private static String ID = "weblogic.wsee.jaxws.framework.jaxrpc";
   private EnvironmentFactory factory;

   public static EnvironmentFactory getFactory(WSEndpoint<?> var0) {
      WSBinding var1 = var0.getBinding();
      JAXRPCEnvironmentFeature var2 = (JAXRPCEnvironmentFeature)var1.getFeature(JAXRPCEnvironmentFeature.class);
      if (var2 == null) {
         var2 = new JAXRPCEnvironmentFeature(var0);
         ((WebServiceFeatureList)var1.getFeatures()).add(var2);
      }

      return var2.getFactory();
   }

   public static EnvironmentFactory getFactory(ClientTubeAssemblerContext var0) {
      WSBinding var1 = var0.getBinding();
      JAXRPCEnvironmentFeature var2 = (JAXRPCEnvironmentFeature)var1.getFeature(JAXRPCEnvironmentFeature.class);
      if (var2 == null) {
         var2 = new JAXRPCEnvironmentFeature(var0);
         ((WebServiceFeatureList)var1.getFeatures()).add(var2);
      }

      return var2.getFactory();
   }

   public static EnvironmentFactory getFactory(WSBinding var0, WSService var1, WSDLPort var2) {
      JAXRPCEnvironmentFeature var3 = (JAXRPCEnvironmentFeature)var0.getFeature(JAXRPCEnvironmentFeature.class);
      if (var3 == null) {
         var3 = new JAXRPCEnvironmentFeature(var0, var1, var2);
         ((WebServiceFeatureList)var0.getFeatures()).add(var3);
      }

      return var3.getFactory();
   }

   public JAXRPCEnvironmentFeature(WSEndpoint<?> var1) {
      this.factory = new ServerEnvironmentFactory(var1);
   }

   public JAXRPCEnvironmentFeature(ClientTubeAssemblerContext var1) {
      this(var1.getBinding(), var1.getService(), var1.getWsdlModel());
   }

   public JAXRPCEnvironmentFeature(WSBinding var1, WSService var2, WSDLPort var3) {
      this.factory = new ClientEnvironmentFactory(var1, var2, var3);
   }

   public EnvironmentFactory getFactory() {
      return this.factory;
   }

   public void setFactory(EnvironmentFactory var1) {
      this.factory = var1;
   }

   public String getID() {
      return ID;
   }
}
