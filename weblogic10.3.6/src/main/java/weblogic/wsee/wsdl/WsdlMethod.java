package weblogic.wsee.wsdl;

import java.util.List;

public interface WsdlMethod {
   WsdlPart getResultPart();

   List<? extends WsdlParameter> getParameters();
}
