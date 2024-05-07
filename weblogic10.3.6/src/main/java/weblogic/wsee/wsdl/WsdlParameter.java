package weblogic.wsee.wsdl;

import javax.jws.WebParam;

public interface WsdlParameter {
   WsdlPart getInPart();

   WsdlPart getOutPart();

   WebParam.Mode getMode();

   WsdlPart getPrimaryPart();
}
