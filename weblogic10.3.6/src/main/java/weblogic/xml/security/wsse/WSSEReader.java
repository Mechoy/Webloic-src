package weblogic.xml.security.wsse;

import weblogic.xml.security.utils.XMLObjectReader;
import weblogic.xml.security.wsse.v200207.BinarySecurityTokenImpl;
import weblogic.xml.security.wsse.v200207.SecurityImpl;
import weblogic.xml.security.wsse.v200207.SecurityTokenReferenceImpl;
import weblogic.xml.security.wsse.v200207.UsernameTokenImpl;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class WSSEReader extends XMLObjectReader implements weblogic.xml.security.wsse.v200207.WSSEConstants {
   private static final WSSEReader singleton = new WSSEReader();

   private WSSEReader() {
      this.register(WSSE_URI, "BinarySecurityToken", 0);
      this.register(WSSE_URI, "UsernameToken", 7);
      this.register(WSSE_URI, "Security", 4);
      this.register(WSSE_URI, "SecurityTokenReference", 5);
   }

   public Object readObjectInternal(int var1, String var2, XMLInputStream var3) throws XMLStreamException {
      switch (var1) {
         case 0:
            return new BinarySecurityTokenImpl(var3, var2);
         case 1:
         case 2:
         case 3:
         case 6:
         default:
            throw new AssertionError("Unknown typecode: " + var1);
         case 4:
            return new SecurityImpl(var3, var2);
         case 5:
            return new SecurityTokenReferenceImpl(var3, var2);
         case 7:
            return new UsernameTokenImpl(var3, var2);
      }
   }

   public static Object read(XMLInputStream var0) throws XMLStreamException {
      return singleton.readObject(var0);
   }

   public static Object read(XMLInputStream var0, int var1) throws XMLStreamException {
      return singleton.readObject(var0, var1);
   }
}
