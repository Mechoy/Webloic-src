package weblogic.wsee.connection.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.soap.MimeHeaders;

public interface Transport {
   String getName();

   String getServiceURI();

   String getEndpointAddress();

   OutputStream send(MimeHeaders var1) throws IOException;

   InputStream receive(MimeHeaders var1) throws IOException;

   void confirmOneway() throws IOException;
}
