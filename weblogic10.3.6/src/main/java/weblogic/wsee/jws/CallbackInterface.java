package weblogic.wsee.jws;

import java.io.Serializable;
import java.net.URL;
import org.w3c.dom.Element;
import weblogic.jws.Exclude;

@Exclude
public interface CallbackInterface extends Serializable {
   long serialVersionUID = 1L;

   String getConversationID();

   /** @deprecated */
   void setEndPoint(URL var1);

   /** @deprecated */
   URL getEndPoint();

   void setEndpointAddress(String var1);

   String getEndpointAddress();

   void setUsername(String var1);

   void setPassword(String var1);

   String getUsername();

   String getPassword();

   /** @deprecated */
   void setProtocol(Protocol var1);

   /** @deprecated */
   Protocol getProtocol();

   void setTimeout(int var1);

   int getTimeout();

   Element[] getInputHeaders();

   void setOutputHeaders(Element[] var1);

   void setClientCert(String var1, String var2);

   void setKeystore(String var1, String var2);

   void setKeystore(String var1, String var2, String var3);

   void setTruststore(String var1, String var2);

   void setTruststore(String var1, String var2, String var3);

   void reset();

   void useClientKeySSL(boolean var1);
}
