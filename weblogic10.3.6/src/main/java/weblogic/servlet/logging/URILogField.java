package weblogic.servlet.logging;

import java.nio.ByteBuffer;
import weblogic.utils.http.HttpReasonPhraseCoder;

public final class URILogField implements LogField {
   private int type;

   URILogField(String var1, String var2) {
      if ("uri".equals(var2)) {
         this.type = 9;
      } else if ("uri-stem".equals(var2)) {
         this.type = 10;
      } else if ("uri-query".equals(var2)) {
         this.type = 11;
      } else if ("method".equals(var2)) {
         this.type = 8;
      } else if ("status".equals(var2)) {
         this.type = 7;
      } else if ("comment".equals(var2)) {
         this.type = 12;
      } else {
         this.type = 0;
      }

   }

   public void logField(HttpAccountingInfo var1, FormatStringBuffer var2) {
      switch (this.type) {
         case 0:
            var2.appendValueOrDash((String)null);
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         default:
            break;
         case 7:
            var2.appendValueOrDash(String.valueOf(var1.getResponseStatusCode()));
            break;
         case 8:
            var2.appendValueOrDash(var1.getMethod());
            break;
         case 9:
            ByteBuffer var3 = var1.getURIAsBytes();
            int var4 = var3.position();
            int var5 = var3.limit() - var3.position();
            if (var3 != null) {
               var2.append(var3.array(), var4, var5);
            } else {
               var2.append("-");
            }
            break;
         case 10:
            if (var1.getRequestURI() != null) {
               var2.append(var1.getRequestURI());
            } else {
               var2.append("-");
            }
            break;
         case 11:
            var2.appendValueOrDash(var1.getQueryString());
            break;
         case 12:
            var2.appendQuotedValueOrDash(HttpReasonPhraseCoder.getReasonPhrase(var1.getResponseStatusCode()));
      }

   }
}
