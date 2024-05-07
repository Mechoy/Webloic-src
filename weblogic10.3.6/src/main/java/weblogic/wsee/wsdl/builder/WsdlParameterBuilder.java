package weblogic.wsee.wsdl.builder;

import weblogic.wsee.wsdl.WsdlParameter;

public interface WsdlParameterBuilder extends WsdlParameter {
   WsdlPartBuilder getInPart();

   WsdlPartBuilder getOutPart();

   WsdlPartBuilder getPrimaryPart();
}
