package weblogic.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.servlet.internal.WarSource;
import weblogic.utils.http.QueryParams;

public final class ServerSideIncludeServlet extends FileServlet {
   private static final boolean DEBUG = false;

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      try {
         WarSource var3 = this.findSource(var1, var2);
         if (var3 != null) {
            if (var3.length() == 0L) {
               var2.setContentLength(0);
            } else {
               InputStream var4 = var3.getInputStream();

               try {
                  ServletOutputStream var5 = var2.getOutputStream();
                  PushbackInputStream var6 = new PushbackInputStream(var4);

                  while(true) {
                     while(true) {
                        while(true) {
                           int var7 = var6.read();
                           if (var7 == 60) {
                              var7 = var6.read();
                              String var8;
                              if (var7 == 115 || var7 == 83) {
                                 var6.unread(var7);
                                 var8 = this.parseTagName(var6);
                                 if (var8.equalsIgnoreCase("servlet")) {
                                    this.handleServletTag(var1, var2, var6, var5);
                                 } else {
                                    var5.print("<");
                                    var5.print(var8);
                                 }
                                 continue;
                              }

                              if (var7 == 33) {
                                 var7 = var6.read();
                                 if (var7 == 45) {
                                    var7 = var6.read();
                                    if (var7 == 45) {
                                       var7 = var6.read();
                                       if (var7 == 35) {
                                          var8 = this.parseTagName(var6);
                                          if (var8.equalsIgnoreCase("include")) {
                                             try {
                                                this.handleIncludeTag(var6, var1, var2);
                                             } catch (Exception var14) {
                                                this.log("ServerSideIncludeServlet failed to handle include tag", var14);
                                                var2.sendError(404);
                                             }
                                          } else {
                                             var5.print("<!--#");
                                             var5.print(var8);
                                          }
                                          continue;
                                       }

                                       var5.print("<!--");
                                    } else {
                                       var5.print("<!-");
                                    }
                                 } else {
                                    var5.print("<!");
                                 }
                              } else {
                                 if (var7 != -1) {
                                    var6.unread(var7);
                                 }

                                 var7 = 60;
                              }
                           }

                           if (var7 == -1) {
                              return;
                           }

                           var5.write(var7);
                        }
                     }
                  }
               } finally {
                  var4.close();
               }
            }
         }
      } catch (FileNotFoundException var16) {
         var2.sendError(404);
      }
   }

   protected void handleServletTag(HttpServletRequest var1, HttpServletResponse var2, PushbackInputStream var3, ServletOutputStream var4) throws IOException {
      Map var5 = this.parseTagParameters(var3);
      QueryParams var6 = new QueryParams();
      var3.read();

      while(true) {
         int var7 = var3.read();
         if (var7 == 60) {
            var7 = var3.read();
            String var8;
            if (var7 == 47) {
               var8 = this.parseTagName(var3);
               if ("servlet".equalsIgnoreCase(var8)) {
                  var3.read();
                  break;
               }

               var4.print("</");
               var4.print(var8);
            } else {
               if (var7 != -1) {
                  var3.unread(var7);
               }

               var8 = this.parseTagName(var3);
               if (var8.equalsIgnoreCase("param")) {
                  this.parseParamTag(var3, var6);
               } else {
                  var4.print("<");
                  var4.print(var8);
               }
            }
         } else {
            if (var7 == -1) {
               break;
            }

            var4.write(var7);
         }
      }

      String var10 = (String)var5.get("name");
      if (var10 != null && !var10.equals("")) {
         try {
            RequestDispatcher var11 = this.getRD(var10, var1);
            if (var11 == null) {
               if (DEBUG_URL_RES.isDebugEnabled()) {
                  DEBUG_URL_RES.debug(this.context.getLogContext() + ": Couldn't find servlet for name: " + var10);
               }

            } else {
               var11.include(var1, var2);
            }
         } catch (Exception var9) {
            if (DEBUG_URL_RES.isDebugEnabled()) {
               DEBUG_URL_RES.debug(this.context.getLogContext() + ": Error initializing servlet", var9);
            }

         }
      } else {
         if (DEBUG_URL_RES.isDebugEnabled()) {
            DEBUG_URL_RES.debug(this.context.getLogContext() + ": Couldn't find servlet parameter: name");
         }

      }
   }

   private void consumeToTagEnd(InputStream var1) throws IOException {
      int var2;
      do {
         var2 = var1.read();
      } while(var2 != 62 && var2 != -1);

   }

   private String readRValue(PushbackInputStream var1) throws IOException {
      char var3 = 0;
      StringBuilder var4 = new StringBuilder();
      this.skipWhiteSpace(var1);
      int var2 = var1.read();
      if (var2 != 39 && var2 != 34) {
         if (var2 == -1) {
            return "";
         }

         var4.append((char)var2);
      } else {
         var3 = (char)var2;
      }

      while(true) {
         var2 = var1.read();
         if (var2 == -1 || var2 == var3 && var2 != 0) {
            break;
         }

         if (var2 == 32) {
            if (var3 == 0) {
               var1.unread(var2);
               break;
            }

            var4.append((char)var2);
         } else {
            if (var2 == 62) {
               var1.unread(var2);
               break;
            }

            if (var2 <= 32 || var2 >= 127) {
               break;
            }

            var4.append((char)var2);
         }
      }

      return var4.toString();
   }

   private boolean parseParamTag(PushbackInputStream var1, Map var2) throws IOException {
      boolean var3 = false;

      while(true) {
         this.skipWhiteSpace(var1);
         boolean var4 = false;
         String var5 = this.parseTagName(var1);
         if ("".equals(var5)) {
            break;
         }

         int var6;
         if ("name".equalsIgnoreCase(var5)) {
            var4 = true;
            var3 = true;
            this.skipWhiteSpace(var1);
            var6 = var1.read();
            if (var6 == -1 || var6 == 62) {
               break;
            }

            if (var6 != 61) {
               this.consumeToTagEnd(var1);
               break;
            }

            var5 = this.readRValue(var1);
            if ("".equalsIgnoreCase(var5)) {
               break;
            }
         } else {
            this.skipWhiteSpace(var1);
            var6 = var1.read();
            if (var6 != 61) {
               this.consumeToTagEnd(var1);
               break;
            }
         }

         this.skipWhiteSpace(var1);
         String var8 = this.parseTagName(var1);
         if (var4) {
            this.skipWhiteSpace(var1);
            int var7 = var1.read();
            if (var7 == -1 || var7 == 62) {
               break;
            }

            if (var7 != 61) {
               this.consumeToTagEnd(var1);
               break;
            }

            var8 = this.readRValue(var1);
         }

         this.consumeToTagEnd(var1);
         var2.put(var5, var8);
      }

      return var3;
   }

   protected void handleIncludeTag(PushbackInputStream var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      RequestDispatcher var4 = null;
      Map var5 = this.parseTagParameters(var1);
      String var6;
      if ((var6 = (String)var5.get("virtual")) != null) {
         if (var6.charAt(0) != '/') {
            var6 = "/" + var6;
         }

         if ((var4 = this.getRD(var6, var2)) == null) {
            throw new FileNotFoundException("Failed find SSI included file: " + var6);
         } else {
            try {
               var4.include(var2, var3);
            } catch (Exception var9) {
            }

         }
      } else if ((var6 = (String)var5.get("file")) != null) {
         var4 = this.getRD(var6, var2);

         try {
            var4.include(var2, var3);
         } catch (Exception var10) {
         }

      } else {
         Iterator var7 = var5.keySet().iterator();
         String var8 = (String)var5.get(var7.next());
         if (DEBUG_URL_RES.isDebugEnabled()) {
            if (var8 != null) {
               DEBUG_URL_RES.debug(this.context.getLogContext() + ": Syntax of include tag containing " + var8 + " is incorrect.  Tag format is: <!--#include virtual=\"somefile.txt\">");
            } else {
               DEBUG_URL_RES.debug(this.context.getLogContext() + ": Syntax of include tag incorrect.  Tag format is: <!--#include virtual=\"somefile.txt\">");
            }
         }

      }
   }

   private String parseTagName(PushbackInputStream var1) throws IOException {
      StringBuilder var2 = new StringBuilder();

      int var3;
      for(var3 = var1.read(); var3 != -1 && (Character.isLetterOrDigit((char)var3) || (char)var3 == '_'); var3 = var1.read()) {
         var2.append((char)var3);
      }

      if (var3 != -1) {
         var1.unread(var3);
      }

      return var2.toString();
   }

   private Map parseTagParameters(PushbackInputStream var1) throws IOException {
      QueryParams var2 = new QueryParams();
      this.skipWhiteSpace(var1);

      String var6;
      while((var6 = this.parseTagName(var1)) != null && !"".equals(var6)) {
         this.skipWhiteSpace(var1);
         String var7 = "";
         if (var1.read() == 61) {
            this.skipWhiteSpace(var1);
            int var3 = var1.read();
            boolean var4;
            int var5;
            if (var3 != 34 && var3 != 39) {
               var4 = false;
               var5 = -1;
            } else {
               var4 = true;
               var5 = var3;
               var3 = var1.read();
            }

            StringBuilder var8;
            for(var8 = new StringBuilder(); var3 > 0; var3 = var1.read()) {
               if (var4) {
                  if (var3 == 10 || var3 == 13) {
                     throw new IOException("End of line reached within quoted value");
                  }

                  if (var3 == var5) {
                     break;
                  }
               } else if (var3 == 32 || var3 == 9 || var3 == 10 || var3 == 13 || var3 == 62) {
                  var1.unread(var3);
                  break;
               }

               var8.append((char)var3);
            }

            if (var3 == 0) {
               throw new IOException("End of file reached within SSI tag");
            }

            this.skipWhiteSpace(var1);
            var7 = var8.toString();
         }

         var6 = var6.toLowerCase(Locale.ENGLISH);
         var2.put(var6, var7);
         this.skipWhiteSpace(var1);
      }

      this.consumeToTagEnd(var1);
      return var2;
   }

   private void skipWhiteSpace(PushbackInputStream var1) throws IOException {
      int var2;
      do {
         var2 = var1.read();
      } while(var2 != -1 && Character.isWhitespace((char)var2));

      if (var2 != -1) {
         var1.unread(var2);
      }

   }

   private RequestDispatcher getRD(String var1, HttpServletRequest var2) {
      if (var1.charAt(0) != '/') {
         String var3 = var2.getRequestURI();
         int var4 = var3.lastIndexOf(47);
         if (var4 == -1) {
            var1 = '/' + var1;
         } else if (var4 == var3.length() - 1) {
            var1 = var3 + var1;
         } else {
            String var5 = var2.getContextPath();
            if (var3.startsWith(var5)) {
               var1 = var3.substring(var5.length(), var4 + 1) + var1;
            } else {
               var1 = var3.substring(0, var4 + 1) + var1;
            }
         }
      }

      RequestDispatcher var6 = this.context.getRequestDispatcher(var1);
      return var6;
   }
}
