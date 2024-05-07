package weblogic.wsee.wsdl.internal;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import weblogic.wsee.wsdl.builder.WsdlParameterBuilder;
import weblogic.wsee.wsdl.builder.WsdlPartBuilder;

public class WsdlParameterImpl implements WsdlParameterBuilder {
   private WsdlPartBuilder inPart = null;
   private WsdlPartBuilder outPart = null;
   private WebParam.Mode mode = null;

   public WsdlParameterImpl(WsdlPartBuilder var1, WsdlPartBuilder var2) {
      if (var1 == null && var2 == null) {
         throw new IllegalArgumentException("both in and out can not be null");
      } else {
         this.inPart = var1;
         this.outPart = var2;
         this.mode = Mode.IN;
         if (var1 == null && var2 != null) {
            this.mode = Mode.OUT;
         } else if (var1 != null && var2 != null) {
            this.mode = Mode.INOUT;
         }

      }
   }

   public WsdlPartBuilder getInPart() {
      return this.inPart;
   }

   public WsdlPartBuilder getOutPart() {
      return this.outPart;
   }

   public WebParam.Mode getMode() {
      return this.mode;
   }

   public WsdlPartBuilder getPrimaryPart() {
      return this.mode == Mode.OUT ? this.outPart : this.inPart;
   }
}
