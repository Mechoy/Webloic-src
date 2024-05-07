package weblogic.wsee.jws;

import java.io.Serializable;
import java.net.URL;

public interface ServiceHandle extends Serializable {
   int SCHEME_DEFAULT = 0;
   int SCHEME_HTTP = 1;
   int SCHEME_JMS = 2;
   int SCHEME_SMTP = 3;
   int SCHEME_FTP = 4;
   int SCHEME_FILE = 5;

   int getScheme();

   String getJNDIBaseName();

   String getURI();

   String getContextURI();

   URL getURL(int var1);

   URL getURL();

   String getConversationID();

   String getControlID();
}
