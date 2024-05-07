package weblogic.wsee.wstx.wsc.common.types;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;

public abstract class BaseRegisterType<T extends EndpointReference, K> {
   protected K delegate;

   protected BaseRegisterType(K var1) {
      this.delegate = var1;
   }

   public K getDelegate() {
      return this.delegate;
   }

   public abstract String getProtocolIdentifier();

   public abstract void setProtocolIdentifier(String var1);

   public abstract T getParticipantProtocolService();

   public abstract void setParticipantProtocolService(T var1);

   public abstract List<Object> getAny();

   public abstract Map<QName, String> getOtherAttributes();

   public abstract boolean isDurable();

   public abstract boolean isVolatile();
}
