package weblogic.xml.crypto.wss;

import java.util.List;
import weblogic.xml.crypto.wss.api.Timestamp;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public interface WSSecurityInfo {
   List getSecurityTokens();

   List getSecurityTokens(String var1);

   List getEncryptions();

   List getEncryptions(SecurityToken var1);

   List getSignatures();

   List getSignatures(SecurityToken var1);

   Timestamp getTimestamp();
}
