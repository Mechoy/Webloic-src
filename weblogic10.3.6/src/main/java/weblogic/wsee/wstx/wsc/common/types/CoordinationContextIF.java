package weblogic.wsee.wstx.wsc.common.types;

import com.sun.xml.bind.api.JAXBRIContext;
import java.util.List;
import javax.xml.ws.EndpointReference;

public interface CoordinationContextIF<T extends EndpointReference, E, I, C> extends CoordinationContextTypeIF<T, E, I, C> {
   List<Object> getAny();

   JAXBRIContext getJAXBRIContext();
}
