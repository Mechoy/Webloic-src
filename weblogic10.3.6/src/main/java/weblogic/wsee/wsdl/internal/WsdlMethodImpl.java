package weblogic.wsee.wsdl.internal;

import java.util.ArrayList;
import java.util.List;
import weblogic.wsee.wsdl.builder.WsdlMethodBuilder;
import weblogic.wsee.wsdl.builder.WsdlParameterBuilder;
import weblogic.wsee.wsdl.builder.WsdlPartBuilder;

public class WsdlMethodImpl implements WsdlMethodBuilder {
   private List<WsdlParameterBuilder> wsdlParameters = new ArrayList();
   private WsdlPartBuilder resultPart = null;

   public void addWsdlParameter(WsdlParameterBuilder var1) {
      this.wsdlParameters.add(var1);
   }

   public void setResultPart(WsdlPartBuilder var1) {
      this.resultPart = var1;
   }

   public WsdlPartBuilder getResultPart() {
      return this.resultPart;
   }

   public List<WsdlParameterBuilder> getParameters() {
      return this.wsdlParameters;
   }
}
