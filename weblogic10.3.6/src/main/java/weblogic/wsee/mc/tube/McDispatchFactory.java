package weblogic.wsee.mc.tube;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import javax.xml.ws.Dispatch;

public interface McDispatchFactory {
   <T> Dispatch<T> createDispatch(@NotNull WSEndpointReference var1, @NotNull Class<T> var2) throws Exception;
}
