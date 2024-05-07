package weblogic.wsee.connection.transport;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import javax.xml.soap.MimeHeaders;

public interface ServerTransport extends Transport {
   boolean isUserInRole(String var1);

   Principal getUserPrincipal();

   boolean isReliable();

   OutputStream sendGeneralFault(MimeHeaders var1) throws IOException;

   OutputStream sendAuthorizationFault(MimeHeaders var1) throws IOException;

   OutputStream sendAuthorizationRequiredFault(MimeHeaders var1) throws IOException;
}
