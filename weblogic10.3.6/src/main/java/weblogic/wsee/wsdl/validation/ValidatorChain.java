package weblogic.wsee.wsdl.validation;

import java.util.ArrayList;
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

public final class ValidatorChain implements WsdlValidator {
   private ArrayList chain = new ArrayList();

   public void add(WsdlValidator var1) {
      this.checkValidator(var1);
      this.chain.add(var1);
   }

   public void add(int var1, WsdlValidator var2) {
      this.checkValidator(var2);
      this.chain.add(var1, var2);
   }

   public int size() {
      return this.chain.size();
   }

   public void validateDefinitions(WsdlDefinitions var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validateDefinitions(var1);
      }

   }

   public void validateService(WsdlService var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validateService(var1);
      }

   }

   public void validatePort(WsdlPort var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validatePort(var1);
      }

   }

   public void validateBinding(WsdlBinding var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validateBinding(var1);
      }

   }

   public void validateBindingOperation(WsdlBindingOperation var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validateBindingOperation(var1);
      }

   }

   public void validateBindingMessage(WsdlBindingMessage var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validateBindingMessage(var1);
      }

   }

   public void validatePortType(WsdlPortType var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validatePortType(var1);
      }

   }

   public void validateOperation(WsdlOperation var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validateOperation(var1);
      }

   }

   public void validateMessage(WsdlMessage var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validateMessage(var1);
      }

   }

   public void validatePart(WsdlPart var1) throws WsdlValidationException {
      Iterator var2 = this.chain.iterator();

      while(var2.hasNext()) {
         WsdlValidator var3 = (WsdlValidator)var2.next();
         var3.validatePart(var1);
      }

   }

   private void checkValidator(WsdlValidator var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("validator can not be null");
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeArray("chain", this.chain.iterator());
      var1.end();
   }
}
