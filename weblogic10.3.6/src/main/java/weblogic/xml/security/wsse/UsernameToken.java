package weblogic.xml.security.wsse;

import java.security.NoSuchAlgorithmException;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.UserInfo;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public interface UsernameToken extends Token {
   String getUsername();

   String getPassword();

   String getPasswordDigest();

   String getPasswordType();

   void setId(String var1) throws SecurityProcessingException;

   boolean verifyDigest(String var1) throws NoSuchAlgorithmException;

   void toXML(XMLOutputStream var1) throws XMLStreamException;

   UserInfo getUserInfo();

   void setGenerateNonce(boolean var1);

   String getNonce();

   long getCreatedTimeInMillis();
}
