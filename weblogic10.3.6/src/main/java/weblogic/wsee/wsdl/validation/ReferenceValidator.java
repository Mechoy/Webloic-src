package weblogic.wsee.wsdl.validation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.PolicyReferenceWsdlExtension;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;

public class ReferenceValidator implements WsdlValidator {
   private WsdlPolicySubject policySubject;

   public void validateDefinitions(WsdlDefinitions var1) throws WsdlValidationException {
      this.policySubject = new WsdlPolicySubject(var1);
   }

   public void validateService(WsdlService var1) throws WsdlValidationException {
   }

   public void validatePort(WsdlPort var1) throws WsdlValidationException {
      if (var1.getBinding() == null) {
         throw new WsdlValidationException("Unable to find binding for port '" + var1.getName() + "'");
      } else {
         this.validatePolicyURIs(var1.getPolicyUris());
         this.validatePolicyReference(var1);
      }
   }

   public void validateBinding(WsdlBinding var1) throws WsdlValidationException {
      if (var1.getPortType() == null) {
         throw new WsdlValidationException("Unable to find port type for binding '" + var1.getName() + "'");
      } else {
         this.validatePolicyURIs(var1.getPolicyUris());
         this.validatePolicyReference(var1);
      }
   }

   public void validateBindingOperation(WsdlBindingOperation var1) throws WsdlValidationException {
      QName var2 = var1.getName();
      WsdlPortType var3 = var1.getBinding().getPortType();
      WsdlOperation var4 = (WsdlOperation)var3.getOperations().get(var2);
      if (var4 == null) {
         throw new WsdlValidationException("The operation '" + var1.getName() + "' defined in the binding '" + var1.getBinding().getName() + "' cannot be found in the portType '" + var3.getName() + "'");
      } else if (var4.getInput() == null && var1.getInput() != null) {
         throw new WsdlValidationException("The operation '" + var1.getName() + "' has an input message defined in the binding '" + var1.getBinding().getName() + "' but not in the portType'" + var3.getName() + "'");
      } else if (var4.getOutput() == null && var1.getOutput() != null) {
         throw new WsdlValidationException("The operation '" + var1.getName() + "' has an output message defined in the binding '" + var1.getBinding().getName() + "' but not in the portType'" + var3.getName() + "'");
      } else if (var4.getInput() != null && var1.getInput() == null) {
         throw new WsdlValidationException("The operation '" + var1.getName() + "' has an input message defined in the portType'" + var3.getName() + "' but not in the binding '" + var1.getBinding().getName() + "'");
      } else if (var4.getOutput() != null && var1.getOutput() == null) {
         throw new WsdlValidationException("The operation '" + var1.getName() + "' has an output message defined in the portType'" + var3.getName() + "' but not in the binding '" + var1.getBinding().getName() + "'");
      }
   }

   public void validateBindingMessage(WsdlBindingMessage var1) throws WsdlValidationException {
      WsdlBindingOperation var2 = var1.getBindingOperation();
      QName var3 = var2.getName();
      WsdlPortType var4 = var2.getBinding().getPortType();

      try {
         if (var1.getMessage() == null) {
            throw new WsdlValidationException("One of the message [" + var1.getName() + "] for the operation '" + var3 + "' defined in the portType '" + var4.getName() + "' cannot be found in the WSDL");
         }
      } catch (WsdlException var7) {
         throw new WsdlValidationException("One of the message [" + var1.getName() + "] for the operation '" + var3 + "' defined in the portType '" + var4.getName() + "' cannot be found in the WSDL");
      }

      this.validatePolicyURIs(var2.getPolicyUris());
      this.validatePolicyReference(var2);
      WsdlBindingMessage var5 = var2.getInput();
      if (var5 != null) {
         this.validatePolicyURIs(var5.getPolicyUris());
         this.validatePolicyReference(var5);
      }

      WsdlBindingMessage var6 = var2.getOutput();
      if (var6 != null) {
         this.validatePolicyURIs(var6.getPolicyUris());
         this.validatePolicyReference(var6);
      }

   }

   public void validatePortType(WsdlPortType var1) throws WsdlValidationException {
      this.validatePolicyURIs(var1.getPolicyUris());
   }

   public void validateOperation(WsdlOperation var1) throws WsdlValidationException {
      this.validatePolicyURIs(var1.getPolicyUris());
      this.validatePolicyReference(var1);
      this.validatePolicyURIs(var1.getInputPolicyUris());
      this.validatePolicyURIs(var1.getOutputPolicyUris());
   }

   public void validateMessage(WsdlMessage var1) throws WsdlValidationException {
      this.validatePolicyURIs(var1.getPolicyUris());
      this.validatePolicyReference(var1);
   }

   public void validatePart(WsdlPart var1) throws WsdlValidationException {
   }

   private void validatePolicyURIs(PolicyURIs var1) throws WsdlValidationException {
      if (var1 != null) {
         URI[] var2 = var1.getURIs();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            URI var5 = var2[var4];
            if (!this.policySubject.policyExists(var5)) {
               throw new WsdlValidationException("Can not resolve policy #" + var5.getFragment());
            }
         }
      }

   }

   private void validatePolicyReference(WsdlExtensible var1) throws WsdlValidationException {
      PolicyReferenceWsdlExtension var2 = (PolicyReferenceWsdlExtension)var1.getExtension("PolicyReference");
      if (var2 != null) {
         ArrayList var3 = var2.getURIs();

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            URI var5 = (URI)var3.get(var4);
            if (!this.policySubject.policyExists(var5)) {
               Set var6 = this.policySubject.getPolicies().keySet();
               String var7 = "Can not resolve policy #" + var5.getFragment() + ".";
               if (var6.isEmpty()) {
                  var7 = var7 + " No policies are attached to the wsdl.";
               } else {
                  Iterator var8 = var6.iterator();

                  String var9;
                  for(var9 = "Existing policies are: "; var8.hasNext(); var9 = var9 + (String)var8.next() + " ") {
                  }

                  var7 = var7 + " Existing policies are: " + var9 + ".";
               }

               throw new WsdlValidationException(var7);
            }
         }
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
