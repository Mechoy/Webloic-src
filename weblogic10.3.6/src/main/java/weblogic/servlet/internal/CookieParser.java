package weblogic.servlet.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.http.Cookie;
import weblogic.utils.http.HttpParsing;

public final class CookieParser {
   private String path;
   private String comment;
   private String domain;
   private boolean secure;
   private int version = 0;
   private int maxAge = -1;
   private final char[] buf;
   private final int len;
   private final String bufAsString;
   private final List cookies = new ArrayList();
   private static boolean allowCommasInNetscapeCookie;
   private static boolean stripQuotedValues;
   private static final SimpleDateFormat[] expiresFormats;

   public static Iterator parseCookies(String var0) throws MalformedCookieHeaderException {
      CookieParser var1 = new CookieParser(var0);
      var1.parse();
      return var1.cookies.iterator();
   }

   private CookieParser(String var1) {
      String var2 = var1.trim();
      this.bufAsString = var2.toLowerCase();
      this.buf = var2.toCharArray();
      this.len = this.buf.length;
   }

   private void parse() throws MalformedCookieHeaderException {
      this.getVersion();
      if (this.version == 0) {
         this.parseNetscapeCookie();
      } else {
         this.parseRFC2109Cookie();
      }

   }

   private void parseNetscapeCookie() throws MalformedCookieHeaderException {
      this.path = this.getAttribute("path");
      this.domain = this.getAttribute("domain");
      if (this.indexAttributeName("secure") != -1) {
         this.secure = true;
      }

      this.getExpires();
      String var1 = null;
      String var2 = null;
      char var3 = 0;
      int var4 = 0;
      int var5 = 0;
      boolean var6 = false;

      while(true) {
         while((var5 = this.nextNonWS(var5)) != -1) {
            int var7 = this.nextIndex(';', var5);
            int var8;
            String var9;
            int var13;
            if ((var13 = this.nextIndex('=', var5)) == -1) {
               var8 = var7 > 0 ? var7 : this.len;
               var9 = new String(this.buf, var5, var8 - var5);
               var5 += var9.length() + 1;
               var9 = var9.trim();
               this.addCookie(var9, "");
            } else if (var7 != -1 && var13 > var7) {
               String var14 = new String(this.buf, var5, var7 - var5);
               var5 += var14.length() + 1;
               var14 = var14.trim();
               this.addCookie(var14, "");
            } else {
               if (allowCommasInNetscapeCookie) {
                  var8 = this.nextIndex(';', ',', var13);
               } else {
                  var8 = this.nextIndex(';', var13);
               }

               if (var8 == -1) {
                  var8 = this.len;
               }

               var9 = new String(this.buf, var5, var13 - var5);

               for(int var10 = var5; var10 < var13; ++var10) {
                  if (HttpParsing.isNetscapeSpecial(this.buf[var10])) {
                     var1 = new String(this.buf);
                     var2 = var9;
                     var3 = this.buf[var10];
                     var4 = var10;
                  }
               }

               ++var13;
               boolean var11 = false;
               String var15 = new String(this.buf, var13, var8 - var13);
               if (var13 < this.len && this.buf[var13] == '"' && var8 - 1 > var13 && this.buf[var8 - 1] == '"') {
                  var11 = true;
               }

               if (!var9.equalsIgnoreCase("expires") && !var11) {
                  for(int var12 = var13; var12 < var8; ++var12) {
                     if (HttpParsing.isNetscapeSpecial(this.buf[var12])) {
                        var1 = new String(this.buf);
                        var2 = var9;
                        var3 = this.buf[var12];
                        var4 = var12;
                     }
                  }
               }

               if (var9 != var2) {
                  this.addCookie(var9, this.unquote(var15));
               }

               for(var5 = var8 + 1; var5 < this.len && HttpParsing.isSpace(this.buf[var5]); ++var5) {
               }
            }
         }

         if (var1 != null) {
            throw new MalformedCookieHeaderException("In cookie '" + var1 + "' character '" + var3 + "' at position " + var4 + " is illegal");
         }

         return;
      }
   }

   private void getExpires() throws MalformedCookieHeaderException {
      String var1 = this.getAttribute("Expires", true);
      if (var1 != null) {
         try {
            Date var2 = null;
            int var3 = 0;

            label49:
            while(var3 < expiresFormats.length) {
               try {
                  int var4 = 0;

                  while(true) {
                     if (var4 >= 3) {
                        break label49;
                     }

                     try {
                        var2 = expiresFormats[var3].parse(var1);
                        break label49;
                     } catch (ParseException var7) {
                        throw var7;
                     } catch (Exception var8) {
                        ++var4;
                     }
                  }
               } catch (ParseException var9) {
                  if (var3 == expiresFormats.length - 1) {
                     throw var9;
                  }

                  ++var3;
               }
            }

            if (var2 == null) {
               throw new ParseException("Null date returned from parser", 0);
            } else {
               long var11 = var2.getTime();
               long var5 = (new Date()).getTime();
               if (var5 < var11) {
                  this.maxAge = (int)((var11 - var5) / 1000L);
               } else {
                  this.maxAge = -1;
               }

            }
         } catch (ParseException var10) {
            throw new MalformedCookieHeaderException("Expires header '" + var1 + "' in cookie '" + new String(this.buf) + "' is invalid, nested " + var10.toString());
         }
      }
   }

   private void parseRFC2109Cookie() throws MalformedCookieHeaderException {
      this.comment = this.getAttribute("$Comment");
      this.path = this.getAttribute("$Path");
      this.domain = this.getAttribute("$Domain");
      if (this.indexAttributeName("$Secure") != -1 || this.indexAttributeName("Secure") != -1) {
         this.secure = true;
      }

      this.getMaxAge();
      int var1 = 0;
      MalformedCookieHeaderException var2 = null;
      boolean var3 = true;

      while((var1 = this.nextNonWS(var1)) != -1) {
         String var4 = null;
         String var5 = null;
         int var6 = this.nextIndex('=', var1);
         int var7 = this.nextIndex(';', ',', var1);
         if (var6 >= 0 && (var7 == -1 || var7 >= var6)) {
            var4 = new String(this.buf, var1, var6 - var1);

            try {
               ++var6;
               var5 = this.getValueString(var4, var6, true);
            } catch (MalformedCookieHeaderException var9) {
               var2 = var9;
               var3 = false;
            }

            var1 += var4.length();
            var1 += 2;
            if (var5 != null) {
               var1 += var5.length();
            }

            if (var6 < this.len && this.buf[var6] == '"') {
               var1 += 2;
            }

            var4 = var4.trim();
         } else {
            int var8;
            if (var7 != -1 && var7 < var6) {
               var8 = var7;
            } else if (var6 >= 0) {
               var8 = var6;
            } else {
               var8 = this.len;
            }

            var4 = (new String(this.buf, var1, var8 - var1)).trim();
            var1 = var8 + 1;
         }

         if (!var4.equalsIgnoreCase("$Version") && !var4.equalsIgnoreCase("$Path") && !var4.equalsIgnoreCase("$Domain")) {
            if (var3) {
               this.addCookie(var4, var5);
            }

            var3 = true;
         }
      }

      if (var2 != null) {
         throw var2;
      }
   }

   private void getMaxAge() throws MalformedCookieHeaderException {
      String var1 = this.getAttribute("Max-Age");
      if (var1 != null) {
         try {
            this.maxAge = Integer.parseInt(var1);
         } catch (NumberFormatException var3) {
            throw new MalformedCookieHeaderException("Max-Age header '" + var1 + "' in cookie '" + new String(this.buf) + "' is invalid, nested " + var3);
         }
      }

   }

   private String getAttribute(String var1) throws MalformedCookieHeaderException {
      return this.getAttribute(var1, false);
   }

   private String getAttribute(String var1, boolean var2) throws MalformedCookieHeaderException {
      int var3 = this.indexAttributeName(var1);
      if (var3 == -1) {
         return null;
      } else {
         var3 += var1.length();
         int var4 = this.nextIndex('=', var3);
         int var5;
         if (this.version > 0) {
            var5 = this.nextIndex(';', ',', var3);
         } else {
            var5 = this.nextIndex(';', var3);
         }

         if (var4 >= 0 && (var5 == -1 || var5 >= var4)) {
            ++var4;
            return this.getValueString(var1, var4, var2);
         } else {
            return null;
         }
      }
   }

   private String getValueString(String var1, int var2, boolean var3) throws MalformedCookieHeaderException {
      if (var2 >= this.len) {
         return "";
      } else {
         int var4;
         if (this.version > 0 && this.buf[var2] == '"') {
            ++var2;
            if ((var4 = this.nextIndex('"', var2)) == -1) {
               throw new MalformedCookieHeaderException("In cookie '" + new String(this.buf) + "' quote at position " + (var2 - 1) + " is unbalanced");
            } else {
               return new String(this.buf, var2, var4 - var2);
            }
         } else {
            if (allowCommasInNetscapeCookie) {
               var4 = this.nextIndex(';', ',', var2);
            } else {
               var4 = this.nextIndex(';', var2);
            }

            if (var4 == -1) {
               var4 = this.len;
            }

            if (this.version == 0 && !var3) {
               for(int var5 = var2; var5 < var4; ++var5) {
                  if (!"expires".equalsIgnoreCase(var1) && HttpParsing.isWS(this.buf[var5])) {
                     throw new MalformedCookieHeaderException("In cookie '" + new String(this.buf) + "' the value of '" + var1 + "' may not contain white-space");
                  }
               }
            }

            return new String(this.buf, var2, var4 - var2);
         }
      }
   }

   private int nextNonWS(int var1) {
      while(var1 < this.len) {
         if (!HttpParsing.isWS(this.buf[var1])) {
            return var1;
         }

         ++var1;
      }

      return -1;
   }

   private int nextIndex(char var1, int var2) {
      for(int var3 = var2; var3 < this.len; ++var3) {
         if (var1 == this.buf[var3]) {
            return var3;
         }
      }

      return -1;
   }

   private int nextIndex(char var1, char var2, int var3) {
      for(int var4 = var3; var4 < this.len; ++var4) {
         if (var1 == this.buf[var4] || var2 == this.buf[var4]) {
            return var4;
         }
      }

      return -1;
   }

   private int indexString(int var1, String var2) {
      return this.bufAsString.indexOf(var2.toLowerCase(), var1);
   }

   private int indexAttributeName(String var1) {
      int var2 = -1;

      while(true) {
         do {
            ++var2;
            if ((var2 = this.indexString(var2, var1)) == -1) {
               return -1;
            }
         } while(var2 > 0 && !HttpParsing.isWS(this.buf[var2 - 1]));

         if (var2 + var1.length() == this.len) {
            return var2;
         }

         for(int var3 = var2 + var1.length(); var3 < this.len; ++var3) {
            if (this.version > 0) {
               if (this.buf[var3] == '=' || this.buf[var3] == ';' || this.buf[var3] == ',') {
                  return var2;
               }
            } else if (this.buf[var3] == '=' || this.buf[var3] == ';') {
               return var2;
            }

            if (!HttpParsing.isWS(this.buf[var3])) {
               break;
            }
         }
      }
   }

   private void addCookie(String var1, String var2) {
      try {
         Cookie var3 = new Cookie(var1, var2);
         var3.setVersion(this.version);
         var3.setSecure(this.secure);
         if (this.path != null) {
            var3.setPath(this.path);
         }

         if (this.comment != null) {
            var3.setComment(this.comment);
         }

         if (this.domain != null) {
            var3.setDomain(this.domain);
         }

         var3.setMaxAge(this.maxAge);
         this.cookies.add(var3);
      } catch (IllegalArgumentException var4) {
      }

   }

   public static String formatCookie(Cookie var0, boolean var1) {
      StringBuilder var2 = new StringBuilder(85);
      var2.append(var0.getName());
      switch (var0.getVersion()) {
         case 0:
            formatNetscapeCookie(var2, var0);
            break;
         case 1:
         default:
            formatRFC2109Cookie(var0, var2);
      }

      if (var1) {
         var2.append("; HttpOnly");
      }

      return var2.toString();
   }

   private static void formatNetscapeCookie(StringBuilder var0, Cookie var1) {
      var0.append('=');
      var0.append(var1.getValue());
      String var2 = var1.getDomain();
      if (var2 != null) {
         var0.append("; domain=").append(var2);
      }

      int var3 = var1.getMaxAge();
      if (var3 >= 0) {
         Date var4;
         if (var3 == 0) {
            var4 = new Date(3600000L);
         } else {
            long var5 = (long)var3 * 1000L;
            var4 = new Date(System.currentTimeMillis() + var5);
         }

         int var9 = 0;

         while(var9 < 3) {
            try {
               String var6 = expiresFormats[0].format(var4);
               var0.append("; expires=").append(var6);
               break;
            } catch (Exception var7) {
               ++var9;
            }
         }
      }

      String var8 = var1.getPath();
      if (var8 != null) {
         var0.append("; path=").append(var8);
      }

      if (var1.getSecure()) {
         var0.append("; secure");
      }

   }

   private static void formatRFC2109Cookie(Cookie var0, StringBuilder var1) {
      String var2 = var0.getValue();
      if (var2 != null) {
         var1.append('=');
         appendValue(var1, var2);
      }

      String var3 = var0.getComment();
      if (var3 != null) {
         appendValue(var1.append("; Comment="), var3);
      }

      var1.append("; Version=1");
      String var4 = var0.getDomain();
      if (var4 != null) {
         appendValue(var1.append("; Domain="), var4);
      }

      String var5 = var0.getPath();
      if (var5 != null) {
         appendValue(var1.append("; Path="), var5);
      }

      int var6 = var0.getMaxAge();
      if (var6 >= 0) {
         var1.append("; Max-Age=").append(var6);
      }

      if (var0.getSecure()) {
         var1.append("; Secure");
      }

   }

   private void getVersion() {
      this.version = 0;
      int var1 = this.indexAttributeName("$Version");
      if (var1 != -1) {
         int var2 = this.nextIndex('=', var1);
         if (var2 != -1 && var2 != this.len - 1) {
            ++var2;

            try {
               while(HttpParsing.isWS(this.buf[var2])) {
                  ++var2;
                  if (var2 == this.len) {
                     return;
                  }
               }

               if (this.buf[var2] == '"') {
                  ++var2;
                  if (var2 == this.len) {
                     return;
                  }
               }
            } catch (ArrayIndexOutOfBoundsException var6) {
               return;
            }

            int var3;
            for(var3 = var2; var2 < this.len && HttpParsing.isDigit(this.buf[var2]); ++var2) {
            }

            if (var2 != var3) {
               if (var2 - var3 == 1) {
                  this.version = this.buf[var3] - 48;
               } else {
                  try {
                     this.version = Integer.parseInt(new String(this.buf, var3, var2 - var3));
                  } catch (Exception var5) {
                  }

               }
            }
         }
      }
   }

   private static void appendValue(StringBuilder var0, String var1) {
      if (!HttpParsing.isTokenClean(var1) && !HttpParsing.isQuoted(var1)) {
         var0.append("\"").append(var1).append("\"");
      } else {
         var0.append(var1);
      }

   }

   private String unquote(String var1) {
      return HttpParsing.isQuoted(var1) && stripQuotedValues ? var1.trim().substring(1, var1.length() - 1) : var1;
   }

   static {
      String var0 = System.getProperty("weblogic.allowCommasInNetscapeCookie");
      if (var0 != null && "true".equalsIgnoreCase(var0)) {
         allowCommasInNetscapeCookie = true;
      }

      stripQuotedValues = Boolean.getBoolean("weblogic.cookies.stripQuotedValues");
      expiresFormats = new SimpleDateFormat[]{new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zz", Locale.US), new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zz", Locale.US), new SimpleDateFormat("EEEE, dd-MMM-yyyy HH:mm:ss zz", Locale.US), new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss zz", Locale.US)};

      for(int var1 = 0; var1 < expiresFormats.length; ++var1) {
         expiresFormats[var1].setTimeZone(TimeZone.getTimeZone("GMT"));
      }

   }
}
