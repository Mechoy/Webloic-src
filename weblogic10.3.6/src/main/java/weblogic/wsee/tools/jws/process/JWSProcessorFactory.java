package weblogic.wsee.tools.jws.process;

import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.JWSProcessor;
import weblogic.wsee.tools.jws.UpgradedJwsProcessor;
import weblogic.wsee.tools.jws.WebServiceWriter;
import weblogic.wsee.tools.jws.callback.CallbackGenerator;
import weblogic.wsee.tools.jws.callback.CallbackReceiveProcessor;
import weblogic.wsee.tools.jws.conversation.ConversationProcessor;
import weblogic.wsee.tools.jws.jaxws.JAXWSProcessor;
import weblogic.wsee.tools.jws.mapping.JaxrpcMappingBuilder;
import weblogic.wsee.tools.jws.policy.PolicyProcessor;
import weblogic.wsee.tools.jws.policy.WebServicePolicyProcessor;
import weblogic.wsee.tools.jws.sei.EndpointInterface;
import weblogic.wsee.tools.jws.type.Java2SchemaProcessor;
import weblogic.wsee.tools.jws.war.WebAppProcessor;
import weblogic.wsee.tools.jws.war.WeblogicWeb;
import weblogic.wsee.tools.jws.webservices.WebServicesProcessor;
import weblogic.wsee.tools.jws.wlsdd.WlsWebservices;
import weblogic.wsee.tools.jws.wsdl.PolicyAnnotator;
import weblogic.wsee.tools.jws.wsdl.WsdlBuilder;
import weblogic.xml.schema.binding.util.ClassUtil;

public class JWSProcessorFactory {
   public static JWSProcessor createProcessor() throws WsBuildException {
      CompositeProcessor var0 = new CompositeProcessor();
      var0.addProcessor(new Java2SchemaProcessor());
      var0.addProcessor(new WsdlBuilder());
      var0.addProcessor(new CallbackReceiveProcessor());
      var0.addProcessor(new ConversationProcessor());
      var0.addProcessor(new UpgradedJwsProcessor());
      var0.addProcessor(new PolicyAnnotator());
      var0.addProcessor(new WebServicesProcessor());
      var0.addProcessor(getEndpointInterfaceProcessor());
      var0.addProcessor(new JaxrpcMappingBuilder());
      var0.addProcessor(new WebAppProcessor());
      var0.addProcessor(new WeblogicWeb());
      var0.addProcessor(new WlsWebservices());
      var0.addProcessor(new PolicyProcessor());
      var0.addProcessor(new WebServicePolicyProcessor());
      var0.addProcessor(new WebServiceWriter());
      var0.addProcessor(new CallbackGenerator());
      var0.addProcessor(new JAXWSProcessor());
      return var0;
   }

   private static JWSProcessor getEndpointInterfaceProcessor() throws WsBuildException {
      String var0 = EndpointInterface.class.getName() + "Script";

      try {
         EndpointInterface var1 = (EndpointInterface)ClassUtil.newInstance(var0);
         return var1;
      } catch (ClassUtil.ClassUtilException var2) {
         throw new WsBuildException(var0 + " not found");
      }
   }
}
