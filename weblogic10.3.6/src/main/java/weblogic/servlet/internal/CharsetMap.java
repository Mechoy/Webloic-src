package weblogic.servlet.internal;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;
import weblogic.j2ee.descriptor.wl.CharsetMappingBean;
import weblogic.utils.StringUtils;

public final class CharsetMap {
   private Map userMap = null;

   public CharsetMap(Map var1) {
      this.userMap = var1;
   }

   public String getJavaCharset(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = null;
         if (this.userMap != null) {
            var2 = (String)this.userMap.get(var1);
         }

         if (var2 == null) {
            var2 = weblogic.utils.CharsetMap.getJavaFromIANA(var1);
         }

         return var2 == null ? var1 : var2;
      }
   }

   public void addMapping(CharsetMappingBean var1) {
      String var2 = var1.getIanaCharsetName();
      String var3 = var1.getJavaCharsetName();
      if (this.userMap == null) {
         this.userMap = new Hashtable();
      }

      this.userMap.put(var2, var3);
   }

   public InputStreamReader makeI18NReader(String var1, InputStream var2) throws UnsupportedEncodingException {
      if (var1 == null) {
         return new InputStreamReader(var2);
      } else {
         try {
            return new InputStreamReader(var2, this.getJavaCharset(var1));
         } catch (IllegalArgumentException var4) {
            throw new UnsupportedEncodingException(this.getEncodingErr(var1));
         } catch (UnsupportedEncodingException var5) {
            throw new UnsupportedEncodingException(this.getEncodingErr(var1));
         }
      }
   }

   private String getEncodingErr(String var1) {
      String var2 = var1.equals(this.getJavaCharset(var1)) ? "Charset: '" + var1 + "' not recognized, and there is no alias for it in the " + "WebServerMBean" : "Neither charset: '" + var1 + "' nor the WebServerMBean alias for it: '" + this.getJavaCharset(var1) + "' identifies a supported Java charset.";
      return var2;
   }

   static boolean isCharsetAllowedForType(String var0) {
      if (var0 == null) {
         return false;
      } else {
         return StringUtils.indexOfIgnoreCase("application/octet-stream", var0) <= -1;
      }
   }
}
