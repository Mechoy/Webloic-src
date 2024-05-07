package weblogic.wsee.jaxws.framework.policy;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.ws.api.model.JavaMethod;
import com.sun.xml.ws.api.wsdl.writer.WSDLGenExtnContext;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.AddressingFeature;
import weblogic.jws.Policy;
import weblogic.jws.Policy.Direction;
import weblogic.wsee.util.Verbose;

public class WSDLGeneratorExtension extends com.sun.xml.ws.api.wsdl.writer.WSDLGeneratorExtension {
   private static boolean verbose = Verbose.isVerbose(WSDLGeneratorExtension.class);
   private WSDLGenExtnContext ctxt;
   private PolicyMap policyMap;

   public void addBindingOperationExtension(TypedXmlWriter var1, JavaMethod var2) {
      this.policyMap.addApplicablePolicyReferences(new QName(this.ctxt.getModel().getTargetNamespace(), var2.getOperationName()), Direction.both, var1);
   }

   public void addBindingOperationInputExtension(TypedXmlWriter var1, JavaMethod var2) {
      this.policyMap.addApplicablePolicyReferences(new QName(this.ctxt.getModel().getTargetNamespace(), var2.getOperationName()), Direction.inbound, var1);
   }

   public void addBindingOperationOutputExtension(TypedXmlWriter var1, JavaMethod var2) {
      this.policyMap.addApplicablePolicyReferences(new QName(this.ctxt.getModel().getTargetNamespace(), var2.getOperationName()), Direction.outbound, var1);
   }

   public void addBindingExtension(TypedXmlWriter var1) {
      this.policyMap.addApplicablePolicyReferences(this.ctxt.getModel().getPortName(), (Policy.Direction)null, (TypedXmlWriter)var1);
   }

   public void end(WSDLGenExtnContext var1) {
      if (!this.policyMap.isEmpty()) {
         ((WebServiceFeatureList)var1.getBinding().getFeatures()).add(new AddressingFeature());
      }

      super.end(var1);
      if (verbose) {
         Verbose.log((Object)"WSDLGeneratorExtension.end()");
      }

   }

   public void start(WSDLGenExtnContext var1) {
      super.start(var1);
      if (verbose) {
         Verbose.banner("WSDLGeneratorExtension.start()");
      }

      this.ctxt = var1;
      this.policyMap = new PolicyMap(var1.getContainer(), var1.getModel(), var1.getBinding(), var1.getModel().getPortName(), var1.getEndpointClass());
      this.policyMap.addDefinitionsExtension(var1.getRoot());
   }
}
