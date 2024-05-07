package weblogic.wsee.wsdl.validation;

import java.util.Iterator;
import weblogic.wsee.util.ToStringWriter;
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

public class WsdlValidationRegistry {
   private static final WsdlValidationRegistry instance = new WsdlValidationRegistry();
   private ValidatorChain defaultValidator = this.createDefaultValidator();
   public static final WsdlValidator WSI_VALIDATOR = createWsiValidator();
   public static final WsdlValidator PARTS_SCHEMA_VALIDATOR = createPartsSchemaValidator();

   private WsdlValidationRegistry() {
   }

   public static WsdlValidationRegistry getInstance() {
      return instance;
   }

   public ValidatorChain getDefaultValidator() {
      return this.defaultValidator;
   }

   public void validate(WsdlDefinitions var1) throws WsdlValidationException {
      this.validate(var1, this.createDefaultValidator());
   }

   public void validate(WsdlDefinitions var1, WsdlValidator var2) throws WsdlValidationException {
      var2.validateDefinitions(var1);
      Iterator var3 = var1.getMessages().values().iterator();

      while(var3.hasNext()) {
         this.validateMessage((WsdlMessage)var3.next(), var2);
      }

      var3 = var1.getPortTypes().values().iterator();

      while(var3.hasNext()) {
         this.validatePortType((WsdlPortType)var3.next(), var2);
      }

      var3 = var1.getBindings().values().iterator();

      while(var3.hasNext()) {
         this.validateBinding((WsdlBinding)var3.next(), var2);
      }

      var3 = var1.getServices().values().iterator();

      while(var3.hasNext()) {
         this.validateService((WsdlService)var3.next(), var2);
      }

   }

   private void validatePortType(WsdlPortType var1, WsdlValidator var2) throws WsdlValidationException {
      var2.validatePortType(var1);
      Iterator var3 = var1.getOperations().values().iterator();

      while(var3.hasNext()) {
         var2.validateOperation((WsdlOperation)var3.next());
      }

   }

   private void validateMessage(WsdlMessage var1, WsdlValidator var2) throws WsdlValidationException {
      var2.validateMessage(var1);
      Iterator var3 = var1.getParts().values().iterator();

      while(var3.hasNext()) {
         var2.validatePart((WsdlPart)var3.next());
      }

   }

   private void validateBinding(WsdlBinding var1, WsdlValidator var2) throws WsdlValidationException {
      var2.validateBinding(var1);
      Iterator var3 = var1.getOperations().values().iterator();

      while(var3.hasNext()) {
         WsdlBindingOperation var4 = (WsdlBindingOperation)var3.next();
         this.validateBindingOperation(var4, var2);
      }

   }

   private void validateBindingOperation(WsdlBindingOperation var1, WsdlValidator var2) throws WsdlValidationException {
      var2.validateBindingOperation(var1);
      if (var1.getInput() != null) {
         var2.validateBindingMessage(var1.getInput());
      }

      if (var1.getOutput() != null) {
         var2.validateBindingMessage(var1.getOutput());
      }

      Iterator var3 = var1.getFaults().values().iterator();

      while(var3.hasNext()) {
         WsdlBindingMessage var4 = (WsdlBindingMessage)var3.next();
         var2.validateBindingMessage(var4);
      }

   }

   private void validateService(WsdlService var1, WsdlValidator var2) throws WsdlValidationException {
      var2.validateService(var1);
      Iterator var3 = var1.getPorts().values().iterator();

      while(var3.hasNext()) {
         WsdlPort var4 = (WsdlPort)var3.next();
         var2.validatePort(var4);
      }

   }

   private static WsdlValidator createWsiValidator() {
      return new ValidatorChain();
   }

   private static WsdlValidator createPartsSchemaValidator() {
      ValidatorChain var0 = new ValidatorChain();
      var0.add(new ReferenceValidator());
      var0.add(new PartsAndSchemaValidator());
      return var0;
   }

   private ValidatorChain createDefaultValidator() {
      ValidatorChain var1 = new ValidatorChain();
      var1.add(new ReferenceValidator());
      return var1;
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
