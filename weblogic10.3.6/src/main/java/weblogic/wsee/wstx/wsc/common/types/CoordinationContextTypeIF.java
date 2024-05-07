package weblogic.wsee.wstx.wsc.common.types;

import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;

public interface CoordinationContextTypeIF<T extends EndpointReference, E, I, C> {
   BaseIdentifier<I> getIdentifier();

   void setIdentifier(BaseIdentifier<I> var1);

   BaseExpires<E> getExpires();

   void setExpires(BaseExpires<E> var1);

   String getCoordinationType();

   void setCoordinationType(String var1);

   T getRegistrationService();

   void setRegistrationService(T var1);

   Map<QName, String> getOtherAttributes();

   C getDelegate();
}
