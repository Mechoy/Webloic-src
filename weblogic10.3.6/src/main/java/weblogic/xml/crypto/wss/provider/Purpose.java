package weblogic.xml.crypto.wss.provider;

import java.util.HashMap;
import java.util.Map;
import weblogic.xml.crypto.api.KeySelector;

public class Purpose {
   private String purpose;
   public static Purpose DECRYPT = new Purpose("decrypt");
   public static Purpose ENCRYPT = new Purpose("encrypt");
   public static Purpose ENCRYPT_RESPONSE = new Purpose("encrypt_response");
   public static Purpose SIGN = new Purpose("sign");
   public static Purpose VERIFY = new Purpose("verify");
   public static Purpose IDENTITY = new Purpose("identity");
   private static Map conversionMap = new HashMap();

   private Purpose(String var1) {
      this.purpose = var1;
   }

   public String toString() {
      return this.purpose;
   }

   public static Purpose convert(KeySelector.Purpose var0) {
      return (Purpose)conversionMap.get(var0);
   }

   static {
      conversionMap.put(KeySelector.Purpose.ENCRYPT, ENCRYPT);
      conversionMap.put(KeySelector.Purpose.DECRYPT, DECRYPT);
      conversionMap.put(KeySelector.Purpose.SIGN, SIGN);
      conversionMap.put(KeySelector.Purpose.VERIFY, VERIFY);
   }
}
