package weblogic.xml.security.wsse;

import weblogic.xml.security.keyinfo.KeyResolver;
import weblogic.xml.stream.XMLInputStream;

/** @deprecated */
public interface SecureInputStream extends XMLInputStream {
   Security getSecurityElement();

   KeyResolver getKeyResolver();

   void setKeyResolver(KeyResolver var1);
}
