package weblogic.xml.crypto.wss.nonce;

import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.TimestampHandler;
import weblogic.xml.crypto.wss.api.NonceValidator;

public class NonceValidatorFactory {
   private static final boolean debug = false;
   private static NonceValidator singletonValidator = null;

   private NonceValidatorFactory() {
   }

   public static NonceValidator getInstance(String var0, TimestampHandler var1) {
      if (singletonValidator == null) {
         if (null != var0 && var0.length() > 0) {
            if ("none".equalsIgnoreCase(var0)) {
               singletonValidator = new DisableNonceValidatorImpl();
               return singletonValidator;
            }

            String var2 = var0;
            int var3 = var0.indexOf(";");
            if (var3 != -1) {
               var2 = var0.substring(0, var3);
               var0 = var0.substring(var3 + 1);
            }

            try {
               Class var4 = Class.forName(var2.trim());
               NonceValidator var5 = (NonceValidator)var4.newInstance();
               var5.init(var0, var1);
               singletonValidator = var5;
            } catch (ClassNotFoundException var6) {
               LogUtils.logWss("Unable to find class name =[" + var2 + "]");
            } catch (Exception var7) {
               LogUtils.logWss("Nonce Validator init error, config String =[" + var0 + " path name =[" + var2 + "]");
            }
         }

         if (null == singletonValidator) {
            singletonValidator = new NonceValidatorImpl();
            singletonValidator.init(var0, var1);
         }
      } else {
         singletonValidator.setExpirationTime(var1.getMessageAge());
      }

      return singletonValidator;
   }

   public static NonceValidator getInstance() {
      return singletonValidator != null ? singletonValidator : getInstance(60);
   }

   public static NonceValidator getInstance(int var0) {
      if (singletonValidator == null) {
         singletonValidator = new NonceValidatorImpl(var0);
      } else {
         singletonValidator.setExpirationTime(var0);
      }

      return singletonValidator;
   }
}
