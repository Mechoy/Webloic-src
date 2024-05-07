package weblogic.wsee.wsdl.builder;

import java.util.List;
import weblogic.wsee.wsdl.WsdlMethod;

public interface WsdlMethodBuilder extends WsdlMethod {
   void addWsdlParameter(WsdlParameterBuilder var1);

   void setResultPart(WsdlPartBuilder var1);

   WsdlPartBuilder getResultPart();

   List<WsdlParameterBuilder> getParameters();
}
