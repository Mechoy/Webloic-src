package weblogic.wsee.wstx.wsc.common.types;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;

public interface CreateCoordinationContextType {
   BaseExpires getExpires();

   void setExpires(BaseExpires var1);

   CurrentContextIF getCurrentContext();

   void setCurrentContext(CurrentContextIF var1);

   String getCoordinationType();

   void setCoordinationType(String var1);

   List<Object> getAny();

   Map<QName, String> getOtherAttributes();

   public interface CurrentContextIF<T extends EndpointReference, E, I, C> extends CoordinationContextTypeIF<T, E, I, C> {
      List<Object> getAny();
   }
}
