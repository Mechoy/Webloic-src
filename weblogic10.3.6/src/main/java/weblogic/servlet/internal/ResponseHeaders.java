package weblogic.servlet.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import weblogic.utils.StringUtils;
import weblogic.utils.collections.ArrayMap;
import weblogic.utils.http.BytesToString;
import weblogic.utils.http.HttpRequestParser;
import weblogic.utils.io.ChunkedDataOutputStream;
import weblogic.utils.string.ThreadLocalDateFormat;

final class ResponseHeaders {
   private static final String SYSTEM_ENCODING = System.getProperty("file.encoding");
   public static final String CACHE_CONTROL = "Cache-Control";
   private static final String CONNECTION = "Connection";
   public static final String DATE = "Date";
   private static final String PRAGMA = "Pragma";
   private static final String TRANSFER_ENCODING = "Transfer-Encoding";
   private static final String ACCEPT_RANGES = "Accept-Ranges";
   public static final String LOCATION = "Location";
   public static final String SERVER = "Server";
   private static final String CONTENT_LENGTH = "Content-Length";
   private static final String CONTENT_TYPE = "Content-Type";
   private static final String EXPIRES = "Expires";
   private static final String LAST_MODIFIED = "Last-Modified";
   public static final String SET_COOKIE = "Set-Cookie";
   private static final String CONTENT_DISPOSITION = "Content-Disposition";
   private static final String WL_RESULT = "WL-Result";
   public static final String P3P = "P3P";
   public static final String X_POWERED_BY = "X-Powered-By";
   private static final String NO_CACHE = "no-cache";
   private static final String EQUAL = "=";
   private static final String QUOTE = "\"";
   private static final String COMMA = ",";
   private static final String NO_CACHE_SET_COOKIE = "no-cache=\"Set-Cookie\"";
   private static final byte[] CRLF = new byte[]{13, 10};
   private static final byte[] COLON_SPACE = new byte[]{58, 32};
   private static final byte[][] standardHeadersAsBytes = new byte[][]{getValueBytes("Cache-Control"), getValueBytes("Connection"), getValueBytes("Date"), getValueBytes("Pragma"), getValueBytes("Transfer-Encoding"), getValueBytes("Accept-Ranges"), getValueBytes("Location"), getValueBytes("Server"), getValueBytes("Content-Length"), getValueBytes("Content-Type"), getValueBytes("Expires"), getValueBytes("Last-Modified"), getValueBytes("Content-Disposition"), getValueBytes("WL-Result")};
   private static final int CACHE_CONTROL_OFFSET = 0;
   private static final int CONNECTION_OFFSET = 1;
   private static final int DATE_OFFSET = 2;
   private static final int PRAGMA_OFFSET = 3;
   private static final int TRANSFER_ENCODING_OFFSET = 4;
   private static final int ACCEPT_RANGES_OFFSET = 5;
   private static final int LOCATION_OFFSET = 6;
   private static final int SERVER_OFFSET = 7;
   private static final int CONTENT_LENGTH_OFFSET = 8;
   private static final int CONTENT_TYPE_OFFSET = 9;
   private static final int EXPIRES_OFFSET = 10;
   private static final int LAST_MODIFIED_OFFSET = 11;
   private static final int CONTENT_DISPOSITION_OFFSET = 12;
   private static final int WL_RESULT_OFFSET = 13;
   private static final int NUM_HEADERS = 14;
   private final Object[] values = new Object[14];
   private ArrayMap genericHeaders;
   private final ServletResponseImpl response;
   private String encoding;

   private static byte[] getValueBytes(String var0) {
      try {
         return (var0 + ": ").getBytes("ISO-8859-1");
      } catch (UnsupportedEncodingException var2) {
         return (var0 + ": ").getBytes();
      }
   }

   public ResponseHeaders(ServletResponseImpl var1) {
      this.response = var1;
      this.setEncoding(SYSTEM_ENCODING);
   }

   private static boolean eq(String var0, String var1, int var2) {
      return var0 == var1 || var1.regionMatches(true, 0, var0, 0, var2);
   }

   private static int getHeaderOffset(String var0) {
      switch (var0.length()) {
         case 4:
            if (eq(var0, "Date", 4)) {
               return 2;
            }
         case 5:
         case 11:
         case 15:
         case 16:
         case 18:
         default:
            break;
         case 6:
            if (eq(var0, "Server", 6)) {
               return 7;
            }

            if (eq(var0, "Pragma", 6)) {
               return 3;
            }
            break;
         case 7:
            if (eq(var0, "Expires", 7)) {
               return 10;
            }
            break;
         case 8:
            if (eq(var0, "Location", 8)) {
               return 6;
            }
            break;
         case 9:
            if (eq(var0, "WL-Result", 9)) {
               return 13;
            }
            break;
         case 10:
            if (eq(var0, "Connection", 10)) {
               return 1;
            }
            break;
         case 12:
            if (eq(var0, "Content-Type", 12)) {
               return 9;
            }
            break;
         case 13:
            if (eq(var0, "Last-Modified", 13)) {
               return 11;
            }

            if (eq(var0, "Accept-Ranges", 13)) {
               return 5;
            }

            if (eq(var0, "Cache-Control", 13)) {
               return 0;
            }
            break;
         case 14:
            if (eq(var0, "Content-Length", 14)) {
               return 8;
            }
            break;
         case 17:
            if (eq(var0, "Transfer-Encoding", 17)) {
               return 4;
            }
         case 19:
            if (eq(var0, "Content-Disposition", 19)) {
               return 12;
            }
      }

      return -1;
   }

   void addHeader(String var1, String var2) {
      int var3 = getHeaderOffset(var1);
      Object var4;
      if (var3 > -1) {
         var4 = this.values[var3];
         if (this.isDuplicateStdHeaderAllowed(var3)) {
            this.values[var3] = this.mergeHeaderValue(var4, var2);
         } else {
            this.values[var3] = var2;
         }

      } else {
         if (this.genericHeaders == null) {
            this.genericHeaders = new ArrayMap(8);
         }

         var4 = this.genericHeaders.get(var1);
         this.genericHeaders.put(var1, this.mergeHeaderValue(var4, var2));
      }
   }

   private Object mergeHeaderValue(Object var1, String var2) {
      if (var1 == null) {
         return var2;
      } else {
         String[] var3;
         if (var1 instanceof String) {
            var3 = new String[]{(String)var1, var2};
            return var3;
         } else {
            var3 = (String[])((String[])var1);
            String[] var4 = new String[var3.length + 1];
            System.arraycopy(var3, 0, var4, 0, var3.length);
            var4[var3.length] = var2;
            return var4;
         }
      }
   }

   private boolean isDuplicateStdHeaderAllowed(int var1) {
      switch (var1) {
         case 0:
         case 3:
         case 5:
            return true;
         default:
            return false;
      }
   }

   void setHeader(String var1, String var2) {
      int var3 = getHeaderOffset(var1);
      if (var3 > -1) {
         this.values[var3] = var2;
      } else {
         if (this.genericHeaders == null) {
            this.genericHeaders = new ArrayMap();
         }

         this.genericHeaders.put(var1, var2);
      }
   }

   String getHeader(String var1) {
      int var2 = getHeaderOffset(var1);
      if (var2 > -1) {
         return this.getHeaderValue(var2);
      } else if (this.genericHeaders == null) {
         return null;
      } else {
         Object var3 = this.genericHeaders.get(var1);
         if (var3 == null) {
            return null;
         } else {
            return var3 instanceof String ? (String)var3 : ((String[])((String[])var3))[0];
         }
      }
   }

   private String getHeaderValue(int var1) {
      Object var2 = this.values[var1];
      if (var2 == null) {
         return null;
      } else {
         return var2 instanceof String[] ? ((String[])((String[])var2))[0] : var2.toString();
      }
   }

   void unsetHeader(String var1) {
      int var2 = getHeaderOffset(var1);
      if (var2 > -1) {
         this.values[var2] = null;
      } else {
         if (this.genericHeaders != null) {
            this.genericHeaders.remove(var1);
         }

      }
   }

   boolean containsHeader(String var1) {
      int var2 = getHeaderOffset(var1);
      if (var2 > -1) {
         return this.values[var2] != null;
      } else {
         return this.genericHeaders == null ? false : this.genericHeaders.containsKey(var1);
      }
   }

   void setIntHeader(String var1, int var2) {
      this.setHeader(var1, Integer.toString(var2));
   }

   void setDateHeader(String var1, long var2) {
      this.setHeader(var1, getDateString(var2));
   }

   void addIntHeader(String var1, int var2) {
      this.addHeader(var1, Integer.toString(var2));
   }

   void addDateHeader(String var1, long var2) {
      this.addHeader(var1, getDateString(var2));
   }

   void setContentLength(int var1) {
      this.values[8] = Integer.toString(var1);
   }

   void setContentType(String var1) {
      this.values[9] = var1;
   }

   void setDate(long var1) {
      this.values[2] = ThreadLocalDateFormat.getInstance().getDate(var1);
   }

   void setServer(String var1) {
      this.values[7] = var1;
   }

   boolean getKeepAlive() {
      return "Keep-Alive".equalsIgnoreCase(this.getHeaderValue(1));
   }

   void setConnection(String var1) {
      this.values[1] = var1;
   }

   void disableCacheControlForCookie() {
      Object var1 = this.values[0];
      if (var1 == null) {
         this.values[0] = "no-cache=\"Set-Cookie\"";
      } else {
         String var2 = null;
         if (var1 instanceof String[]) {
            var2 = StringUtils.join((String[])((String[])var1), ",");
         } else {
            var2 = (String)var1;
         }

         String var3 = var2.toLowerCase();
         if (var3.indexOf("no-cache") < 0) {
            this.values[0] = var1 + "," + "no-cache=\"Set-Cookie\"";
         } else {
            String var4 = "no-cache=\"";
            int var5 = var3.indexOf(var4);
            if (var5 >= 0) {
               int var6 = var5 + var4.length();
               int var7 = var3.indexOf("\"", var6);
               String var8 = var2.substring(var6, var7);
               StringBuilder var9 = new StringBuilder();
               var9.append(var2.substring(0, var6)).append("Set-Cookie").append(",").append(var8).append(var2.substring(var7));
               this.values[0] = var9.toString();
            }

         }
      }
   }

   int writeHeaders(ServletOutputStreamImpl var1, String var2) throws IOException {
      ChunkedDataOutputStream var3 = new ChunkedDataOutputStream() {
         protected void getBytes(String var1, int var2, int var3, byte[] var4, int var5) {
            StringUtils.getBytes(var1, var2, var3, var4, var5);
         }
      };
      var3.writeBytes(var2);

      int var4;
      for(var4 = 0; var4 < 14; ++var4) {
         Object var5 = this.values[var4];
         if (var5 != null) {
            if (this.isDuplicateStdHeaderAllowed(var4)) {
               this.writeStandardDuplicateHeader(var3, var4, var5);
            } else {
               var3.write(standardHeadersAsBytes[var4]);
               if (var4 == 6) {
                  var3.write(((String)var5).getBytes(HttpRequestParser.getURIDecodeEncoding()));
                  var3.write(CRLF);
               } else if (var4 == 12) {
                  this.writeContentDisposition(var3, "Content-Disposition", var5);
               } else {
                  var3.writeBytes((String)var5);
                  var3.write(CRLF);
               }
            }
         }
      }

      if (this.genericHeaders != null) {
         Iterator var7 = this.genericHeaders.entrySet().iterator();

         while(var7.hasNext()) {
            Map.Entry var8 = (Map.Entry)var7.next();
            String var6 = (String)var8.getKey();
            var3.writeBytes(var6);
            var3.write(COLON_SPACE);
            writeHeaderValue(var3, var6, var8.getValue());
         }
      }

      var3.write(CRLF);
      var4 = var3.getSize();
      var1.writeHeader(var3);
      return var4;
   }

   private void writeStandardDuplicateHeader(ChunkedDataOutputStream var1, int var2, Object var3) throws IOException {
      if (!(var3 instanceof String[])) {
         var1.write(standardHeadersAsBytes[var2]);
         var1.writeBytes(var3.toString());
         var1.write(CRLF);
      } else {
         String[] var4 = (String[])((String[])var3);

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var1.write(standardHeadersAsBytes[var2]);
            var1.writeBytes(var4[var5]);
            var1.write(CRLF);
         }

      }
   }

   private void writeContentDisposition(ChunkedDataOutputStream var1, String var2, Object var3) throws IOException, UnsupportedEncodingException {
      HttpServer var4;
      if (this.response.getContext() == null) {
         var4 = WebService.defaultHttpServer();
      } else {
         var4 = this.response.getContext().getServer();
      }

      if (var4.getMBean().isUseHeaderEncoding()) {
         if (var3 instanceof String) {
            this.writeEncodedString(var1, (String)var3);
            var1.write(CRLF);
         } else {
            String[] var5 = (String[])((String[])var3);

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var6 > 0) {
                  var1.writeBytes(var2);
                  var1.write(COLON_SPACE);
               }

               this.writeEncodedString(var1, var5[var6]);
               var1.write(CRLF);
            }
         }
      } else {
         writeHeaderValue(var1, var2, var3);
      }

   }

   private static void writeHeaderValue(ChunkedDataOutputStream var0, String var1, Object var2) throws IOException {
      if (var2 == null) {
         var0.write(CRLF);
      } else if (var2 instanceof String) {
         var0.writeBytes((String)var2);
         var0.write(CRLF);
      } else {
         String[] var3 = (String[])((String[])var2);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var4 > 0) {
               var0.writeBytes(var1);
               var0.write(COLON_SPACE);
            }

            var0.writeBytes(var3[var4]);
            var0.write(CRLF);
         }
      }

   }

   public void setEncoding(String var1) {
      if (BytesToString.is8BitUnicodeSubset(var1)) {
         this.encoding = null;
      } else {
         this.encoding = var1;
      }

   }

   private void writeEncodedString(ChunkedDataOutputStream var1, String var2) throws UnsupportedEncodingException, IOException {
      if (this.encoding == null) {
         var1.writeBytes(var2);
      } else {
         var1.write(var2.getBytes(this.encoding));
      }

   }

   static String getDateString(long var0) {
      return ThreadLocalDateFormat.getInstance().getDate(var0);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(512);

      for(int var2 = 0; var2 < 14; ++var2) {
         if (this.values[var2] != null) {
            var1.append(new String(standardHeadersAsBytes[var2])).append(": ").append(this.values[var2]).append('\n');
         }
      }

      if (this.genericHeaders != null) {
         Iterator var4 = this.genericHeaders.keySet().iterator();

         while(var4.hasNext()) {
            String var3 = (String)var4.next();
            var1.append(var3).append(": ").append(this.genericHeaders.get(var3)).append('\n');
         }
      }

      return var1.toString();
   }
}
