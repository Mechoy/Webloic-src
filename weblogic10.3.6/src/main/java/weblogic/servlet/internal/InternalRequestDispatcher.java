package weblogic.servlet.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.util.HashMap;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletInputStream;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannelManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.security.ServletAuthentication;
import weblogic.servlet.security.internal.WebAppSecurity;
import weblogic.utils.http.HttpRequestParseException;
import weblogic.utils.http.HttpRequestParser;
import weblogic.utils.io.NullInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class InternalRequestDispatcher {
   private static final boolean DEBUG = false;
   private static final String CONTENT_TYPE = "Content-Type";
   private final String host;
   private final String uri;
   private final HashMap headers;
   private String content;
   private String charset;
   private int status;
   private String statusString;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String runAs;

   public InternalRequestDispatcher(String var1) throws IOException {
      this(var1, (String)null);
   }

   public InternalRequestDispatcher(String var1, String var2) throws IOException {
      this(var1, var2, (String)null);
   }

   public InternalRequestDispatcher(String var1, String var2, String var3) throws IOException {
      this.headers = new HashMap();
      this.status = 0;
      this.statusString = null;
      this.runAs = null;
      this.host = var2;
      this.uri = var1;
      this.runAs = var3;
      this.dispatch();
   }

   public int getStatus() {
      return this.status;
   }

   public String getStatusString() {
      return this.statusString;
   }

   public String getContent() {
      return this.content;
   }

   public String getHeader(String var1) {
      return (String)this.headers.get(var1);
   }

   public String getContentType() {
      return this.getHeader("Content-Type");
   }

   public String getCharset() {
      return this.charset;
   }

   protected void dispatch() throws IOException {
      if (this.uri != null && this.uri.startsWith("/")) {
         UnsyncByteArrayOutputStream var1 = new UnsyncByteArrayOutputStream();
         ServletRequestImpl var2 = new ServletRequestImpl((MuxableSocketHTTP)null);
         byte[] var3 = (new String("GET " + this.uri + " " + "HTTP/1.0" + "\r\n\r\n")).getBytes();

         HttpRequestParser var4;
         IOException var6;
         try {
            var4 = new HttpRequestParser(var3, var3.length);
         } catch (HttpRequestParseException var19) {
            var6 = new IOException("Couldn't parse request");
            var6.initCause(var19);
            throw var6;
         }

         var2.initFromRequestParser(var4);
         ServletResponseImpl var5 = new ServletResponseImpl(var2, var1);
         var5.getServletOutputStream().setWriteEnabled(!"HEAD".equals(var4.getMethod()));
         var6 = null;
         HttpServer var21;
         if (this.host != null) {
            var21 = WebService.getVirtualHost(this.host);
         } else {
            var21 = WebService.defaultHttpServer();
         }

         if (var21 == null) {
            throw new IOException("Virtual host not found: " + this.host);
         } else {
            ServletContextManager var7 = var21.getServletContextManager();
            ContextVersionManager var8 = var7.resolveVersionManagerForURI(this.uri);
            WebAppServletContext var9;
            if (var8.isVersioned()) {
               var2.initContextManager(var8);
               var9 = var2.getContext();
            } else {
               var9 = var8.getContext();
               var2.initContext(var9);
            }

            ServletInputStreamImpl var10 = new ServletInputStreamImpl(NullInputStream.getInstance());
            var2.setInputStream((ServletInputStream)var10);
            String var11 = var21.getFrontendHost();
            if (var11 != null) {
               var2.setServerName(var11);
            } else {
               var2.setServerName(var21.getListenAddress());
            }

            int var12 = var21.getFrontendHTTPPort();
            if (var12 != 0) {
               var2.setServerPort(var12);
            } else {
               var2.setServerPort(ServerChannelManager.findLocalServerChannel(ProtocolHandlerHTTP.PROTOCOL_HTTP).getPublicPort());
            }

            var5.initContext(var9);
            var5.getServletOutputStream().setHttpServer(var21);
            var5.setHeaderInternal("Server", HttpServer.SERVER_INFO);
            if (!ManagementService.getRuntimeAccess(kernelId).getServer().isHttpdEnabled()) {
               throw new IOException("HTTP is not enabled on this server!");
            } else if (var9 == null) {
               var5.sendError(404);
               this.status = 404;
               this.statusString = "Not Found";
            } else {
               ServletStubImpl var13 = var9.resolveDirectRequest(var2);
               var2.setServletStub(var13);
               if (this.runAs != null) {
                  AuthenticatedSubject var14 = this.getRunAsSubject(this.runAs, var2, var5, var9);
                  if (var14 != null) {
                     ServletAuthentication.runAs((AuthenticatedSubject)var14, var2);
                  }
               }

               var2.getHttpAccountingInfo().setInvokeTime(System.currentTimeMillis());
               var9.execute(var2, var5);

               try {
                  var5.getServletOutputStream().flush();
               } catch (RuntimeException var18) {
               }

               byte[] var22 = var1.toByteArray();
               var1.close();
               var5.getServletOutputStream().close();
               int var15 = this.parseHttpHeaders(var22);
               this.parseCharset();
               if (var15 != -1) {
                  if (this.getCharset() != null) {
                     try {
                        this.content = new String(var22, var15, var22.length - var15, this.getCharset());
                        return;
                     } catch (UnsupportedEncodingException var20) {
                     }
                  }

                  try {
                     this.content = new String(var22, var15, var22.length - var15, "ISO-8859-1");
                  } catch (UnsupportedEncodingException var17) {
                     this.content = new String(var22, var15, var22.length - var15);
                  }
               }
            }
         }
      } else {
         throw new IOException("URI must be absolute with no protocol, etc: " + this.uri);
      }
   }

   protected int parseHttpHeaders(byte[] var1) throws IOException {
      int var2 = -1;
      String var3 = this.getFirstLine(var1);
      boolean var4 = false;
      int var5 = var1.length;
      byte[] var6 = var1;
      if (var3 == null) {
         return var2;
      } else {
         int var10;
         for(var10 = var3.length(); var6[var10] == 10 || var6[var10] == 13; ++var10) {
         }

         this.parseResponseStatus(var3);

         while(true) {
            String var7 = null;

            int var8;
            for(var8 = var10; var10 < var5 && var6[var10] != 10; ++var10) {
               if (var6[var10] == 13) {
                  ++var10;
                  if (var10 < var5 && var6[var10] == 10) {
                     break;
                  }
               }

               if (var6[var10] == 58) {
                  ++var10;
                  if (var10 >= var5) {
                     throw new IllegalArgumentException("malformed HTTP headers");
                  }

                  var7 = new String(var6, var8, var10 - var8 - 1);
                  if (var6[var10] != 32) {
                     --var10;
                  }
                  break;
               }
            }

            ++var10;
            if (var7 == null) {
               if (var10 <= var5) {
                  var2 = var10;
               } else {
                  var2 = -1;
               }

               return var2;
            }

            var8 = var10;

            String var9;
            for(var9 = null; var10 < var5; ++var10) {
               if (var6[var10] == 10) {
                  var9 = new String(var6, var8, var10 - var8);
                  break;
               }

               if (var6[var10] == 13) {
                  ++var10;
                  if (var10 < var5 && var6[var10] == 10) {
                     var9 = new String(var6, var8, var10 - var8 - 1);
                     break;
                  }
               }
            }

            ++var10;
            if (var9 == null) {
               throw new IllegalArgumentException("malformed HTTP headers");
            }

            this.headers.put(var7, var9);
         }
      }
   }

   protected String getFirstLine(byte[] var1) {
      String var2 = null;
      boolean var3 = false;
      int var4 = var1.length;
      byte[] var5 = var1;
      byte var6 = 0;

      for(int var7 = var6; var7 < var4; ++var7) {
         if (var5[var7] == 10) {
            var2 = new String(var5, var6, var7);
            break;
         }

         if (var5[var7] == 13) {
            ++var7;
            if (var5[var7] == 10 && var7 > 2) {
               var2 = new String(var5, var6, var7 - 1);
            }
            break;
         }
      }

      return var2;
   }

   protected void parseResponseStatus(String var1) {
      var1 = var1.trim();
      int var2 = var1.indexOf(32);
      if (var2 != -1) {
         int var3 = var1.indexOf(32, var2 + 1);
         if (var3 != -1) {
            this.statusString = var1.substring(var3 + 1);
         } else {
            var3 = var1.length();
         }

         try {
            this.status = Integer.parseInt(var1.substring(var2 + 1, var3));
         } catch (NumberFormatException var5) {
         }
      }

   }

   private void parseCharset() {
      String var1 = this.getContentType();
      if (var1 != null) {
         int var2 = var1.indexOf("charset=");
         if (var2 != -1) {
            int var3 = var1.indexOf(";", var2);
            var2 += 8;
            if (var3 == -1) {
               this.charset = var1.substring(var2);
            } else {
               this.charset = var1.substring(var2, var3);
            }
         }

      }
   }

   private AuthenticatedSubject getRunAsSubject(String var1, ServletRequestImpl var2, ServletResponseImpl var3, WebAppServletContext var4) {
      try {
         return this.getPrincipalAuthenticator(var4).impersonateIdentity(var1, WebAppSecurity.getContextHandler(var2, var3));
      } catch (LoginException var6) {
         var4.log((String)"Failed to get runAs subject for the InternalRequestDispatcher", (Throwable)var6);
         throw new IllegalArgumentException(var6.getMessage());
      }
   }

   private PrincipalAuthenticator getPrincipalAuthenticator(WebAppServletContext var1) {
      return (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var1.getSecurityRealmName(), ServiceType.AUTHENTICATION);
   }
}
