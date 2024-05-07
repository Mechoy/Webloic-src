package weblogic.wsee.jaxws.framework.policy.advertisementimpl;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.server.DocumentAddressResolver;
import com.sun.xml.ws.api.server.PortAddressResolver;
import com.sun.xml.ws.api.server.SDDocument;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.transport.http.WSHTTPConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import org.xml.sax.InputSource;

public interface AdvertisementHelper {
   boolean hasPolicyAdvertisementFilter();

   boolean handleAdvertisementRequest(SDDocument var1, WSEndpoint<?> var2, WSHTTPConnection var3, PortAddressResolver var4, DocumentAddressResolver var5) throws IOException;

   Object readWSDL(@NotNull URL var1, InputSource var2) throws IOException;

   void writeWSDL(@NotNull Object var1, OutputStream var2) throws IOException;

   String getDocumentBaseUriFromWSDL(Object var1);
}
