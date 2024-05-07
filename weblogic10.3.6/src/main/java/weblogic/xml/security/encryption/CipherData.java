package weblogic.xml.security.encryption;

import java.io.InputStream;
import java.io.OutputStream;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class CipherData implements XMLEncConstants {
   public abstract InputStream getInputStream();

   public abstract byte[] getCipherBytes();

   public abstract OutputStream getOutputStream();

   public abstract void clear();

   public abstract void setCipherBytes(byte[] var1);

   static CipherData fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      XMLEvent var2 = StreamUtils.getElement(var0, var1, "CipherData");
      if (var2 == null) {
         return null;
      } else {
         CipherData var3 = CipherValue.fromXML(var0, var1);
         StreamUtils.closeScope(var0, var1, "CipherData");
         return var3;
      }
   }

   public abstract void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException;
}
