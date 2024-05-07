package weblogic.wsee.jaxws.framework;

import com.sun.xml.ws.api.message.Packet;
import java.io.Serializable;
import java.util.Map;

public abstract class InvocationPropertySet implements Serializable {
   private static final long serialVersionUID = 1L;
   protected transient Map<String, Object> _invocationProps;

   public InvocationPropertySet() {
   }

   protected InvocationPropertySet(Packet var1) {
      this._invocationProps = var1.invocationProperties;
   }
}
