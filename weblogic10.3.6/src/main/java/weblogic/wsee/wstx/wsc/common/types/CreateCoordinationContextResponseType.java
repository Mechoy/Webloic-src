package weblogic.wsee.wstx.wsc.common.types;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

public interface CreateCoordinationContextResponseType {
   CoordinationContextIF getCoordinationContext();

   void setCoordinationContext(CoordinationContextIF var1);

   List<Object> getAny();

   Map<QName, String> getOtherAttributes();
}
