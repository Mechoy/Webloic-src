package weblogic.xml.security.wsu;

import java.util.Calendar;
import javax.xml.soap.SOAPElement;
import weblogic.xml.security.wsu.v200207.TimestampImpl;

public class WSUFactory {
   private static final WSUFactory theOne = new WSUFactory();

   public static final WSUFactory getInstance(String var0) {
      if (var0.equals(weblogic.xml.security.wsu.v200207.WSUConstants.WSU_URI)) {
         return theOne;
      } else {
         throw new WSUFactoryException("Unsupported WSU namespace: " + var0);
      }
   }

   public Timestamp createTimestamp() {
      return new TimestampImpl();
   }

   public Timestamp createTimestamp(long var1) {
      TimestampImpl var3 = new TimestampImpl();
      var3.setExpires(var1);
      return var3;
   }

   public Timestamp createTimestamp(Calendar var1) {
      TimestampImpl var2 = new TimestampImpl(var1);
      return var2;
   }

   public Timestamp createTimestamp(Calendar var1, Calendar var2) {
      Timestamp var3 = this.createTimestamp(var1);
      var3.setExpires(var2);
      return var3;
   }

   public Timestamp createTimestamp(SOAPElement var1) {
      TimestampImpl var2 = new TimestampImpl(var1);
      return var2;
   }
}
