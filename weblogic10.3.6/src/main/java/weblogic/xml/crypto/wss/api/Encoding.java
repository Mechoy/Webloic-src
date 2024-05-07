package weblogic.xml.crypto.wss.api;

public interface Encoding {
   String encode(byte[] var1);

   byte[] decode(String var1);

   String getURI();
}
