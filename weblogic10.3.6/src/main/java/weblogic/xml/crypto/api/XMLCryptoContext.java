package weblogic.xml.crypto.api;

public interface XMLCryptoContext {
   String getBaseURI();

   KeySelector getKeySelector();

   Object getProperty(String var1);

   URIDereferencer getURIDereferencer();

   void setBaseURI(String var1);

   void setKeySelector(KeySelector var1);

   Object setProperty(String var1, Object var2);

   void setURIDereferencer(URIDereferencer var1);
}
