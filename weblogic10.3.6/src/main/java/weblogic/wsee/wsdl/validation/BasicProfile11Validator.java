package weblogic.wsee.wsdl.validation;

import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.soap11.SoapBody;

public class BasicProfile11Validator implements WsdlValidator {
   private static final boolean verbose = Verbose.isVerbose(BasicProfile11Validator.class);

   public void validateDefinitions(WsdlDefinitions var1) throws WsdlValidationException {
   }

   public void validateService(WsdlService var1) throws WsdlValidationException {
   }

   public void validatePort(WsdlPort var1) throws WsdlValidationException {
   }

   public void validateBinding(WsdlBinding var1) throws WsdlValidationException {
   }

   public void validateBindingOperation(WsdlBindingOperation var1) throws WsdlValidationException {
   }

   public void validateBindingMessage(WsdlBindingMessage var1) throws WsdlValidationException {
      SoapBody var2 = SoapBody.narrow(var1);
      if (var2 != null) {
         if (!"literal".equals(var2.getUse())) {
            throw new BasicProfile11ValidationException(new BasicProfile11ErrorMessage("R2706", "Value of 'use' attribute in binding message '" + var1.getName() + "' in binding operation '" + var1.getBindingOperation().getName() + "' is not 'literal'"));
         }
      }
   }

   public void validatePortType(WsdlPortType var1) throws WsdlValidationException {
   }

   public void validateOperation(WsdlOperation var1) throws WsdlValidationException {
   }

   public void validateMessage(WsdlMessage var1) throws WsdlValidationException {
   }

   public void validatePart(WsdlPart var1) throws WsdlValidationException {
   }
}
