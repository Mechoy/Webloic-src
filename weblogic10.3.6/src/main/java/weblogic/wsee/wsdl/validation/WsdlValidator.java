package weblogic.wsee.wsdl.validation;

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

public interface WsdlValidator {
   void validateDefinitions(WsdlDefinitions var1) throws WsdlValidationException;

   void validateService(WsdlService var1) throws WsdlValidationException;

   void validatePort(WsdlPort var1) throws WsdlValidationException;

   void validateBinding(WsdlBinding var1) throws WsdlValidationException;

   void validateBindingOperation(WsdlBindingOperation var1) throws WsdlValidationException;

   void validateBindingMessage(WsdlBindingMessage var1) throws WsdlValidationException;

   void validatePortType(WsdlPortType var1) throws WsdlValidationException;

   void validateOperation(WsdlOperation var1) throws WsdlValidationException;

   void validateMessage(WsdlMessage var1) throws WsdlValidationException;

   void validatePart(WsdlPart var1) throws WsdlValidationException;
}
