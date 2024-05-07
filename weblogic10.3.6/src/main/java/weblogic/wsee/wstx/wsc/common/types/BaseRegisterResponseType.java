package weblogic.wsee.wstx.wsc.common.types;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;

public abstract class BaseRegisterResponseType<T extends EndpointReference, K> {
   protected K delegate;

   protected BaseRegisterResponseType(K var1) {
      this.delegate = var1;
   }

   public abstract T getCoordinatorProtocolService();

   public abstract void setCoordinatorProtocolService(T var1);

   public abstract List<Object> getAny();

   public abstract Map<QName, String> getOtherAttributes();

   public K getDelegate() {
      return this.delegate;
   }
}
