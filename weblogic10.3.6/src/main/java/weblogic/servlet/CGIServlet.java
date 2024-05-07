package weblogic.servlet;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.version;
import weblogic.servlet.internal.ServletOutputStreamImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.security.Utils;
import weblogic.utils.Executable;
import weblogic.utils.StringUtils;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.io.Chunk;

public final class CGIServlet extends HttpServlet {
   private String[] cgiDir = null;
   private String allCgiDir = null;
   private ArrayList stat_env = null;
   private Hashtable extensionMap = null;
   private boolean debug = false;

   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      String var2 = this.getInitParameter("debug");
      if (var2 != null) {
         this.debug = var2.equals("true");
      }

      this.setCgiDir(((WebAppServletContext)this.getServletContext()).getDocroot(), this.getInitParameter("cgiDir"));
      if (this.debug) {
         this.log("CGI Initialized [root : " + this.allCgiDir + "] [" + "debug : " + this.debug + "]");
      }

      Enumeration var3 = this.getInitParameterNames();
      this.extensionMap = new Hashtable();

      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         String var5 = this.getInitParameter(var4);
         if (var4.startsWith("*.")) {
            this.extensionMap.put(var4, var5);
         }
      }

      Env var6 = Env.getenv();
      this.stat_env = var6.getWholeEnv();
      this.stat_env.add("SERVER_SOFTWARE=WebLogic/" + version.getReleaseBuildVersion());
      this.stat_env.add("GATEWAY_INTERFACE=CGI/1.1");
   }

   private void setCgiDir(String var1, String var2) {
      if (var2 == null) {
         var2 = File.separator + "cgi-bin";
      }

      if (File.separatorChar == '\\') {
         var2 = var2.replace('/', File.separatorChar);
      } else {
         var2 = var2.replace('\\', File.separatorChar);
      }

      File var3 = new File(var1);
      if (!var3.isDirectory() && var1.toUpperCase().endsWith(".WAR")) {
         String var4 = var1.substring(0, var1.lastIndexOf(File.separatorChar)) + File.separatorChar + ((WebAppServletContext)this.getServletContext()).getTempPath();
         if (File.separatorChar == '\\') {
            var4 = var4.replace('/', File.separatorChar);
         } else {
            var4 = var4.replace('\\', File.separatorChar);
         }

         if (!this.extractScripts(var1, var4, var2)) {
            this.log("Could not extract scripts from '" + var1 + "'");
         }

         var1 = var4;
      }

      String[] var11 = StringUtils.splitCompletely(var2, ";");
      int var5 = var11.length;
      this.cgiDir = new String[var5];
      this.allCgiDir = "";

      for(int var6 = 0; var6 < var5; ++var6) {
         if (var11[var6].endsWith("/") || var11[var6].endsWith("\\")) {
            var11[var6] = var11[var6].substring(0, var11[var6].length() - 1);
         }

         File var7 = new File(var11[var6]);
         String var8;
         if (!var7.isAbsolute()) {
            if (var11[var6].charAt(0) == File.separatorChar) {
               var8 = var1 + var11[var6];
            } else {
               var8 = var1 + File.separator + var11[var6];
            }
         } else {
            var8 = var11[var6];
         }

         var7 = new File(var8);
         if (!var7.exists()) {
            this.log("CGI directory: " + var8 + " doesn't exist.");
            this.cgiDir[var6] = null;
         } else if (!var7.isDirectory()) {
            this.log("CGI directory: " + var8 + " isn't a directory.");
            this.cgiDir[var6] = null;
         } else {
            try {
               this.cgiDir[var6] = var7.getCanonicalPath();
               this.allCgiDir = this.allCgiDir + this.cgiDir[var6] + File.pathSeparator;
            } catch (IOException var10) {
               this.cgiDir[var6] = null;
               this.log("CGI directory: " + var8 + " doesn't resolve to a canonical name.");
            }
         }
      }

   }

   public String getCgiDir() {
      return this.allCgiDir;
   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException, ServletException {
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      String var5 = null;
      String var6 = (String)var1.getAttribute("javax.servlet.include.path_info");
      String var7 = (String)var1.getAttribute("javax.servlet.include.servlet_path");
      if (var6 == null) {
         var6 = Utils.encodeXSS(var1.getPathInfo());
      }

      if (var7 == null) {
         var7 = var1.getServletPath();
      }

      String var8 = (var7.length() <= 1 ? "" : var7) + (var6 == null ? "" : var6);
      if (var8 != null && var8.length() != 0) {
         if (!var8.startsWith("/")) {
            var8 = "/" + var8;
         }

         int var9 = var8.indexOf(46);
         if (var9 != -1) {
            int var10 = var8.lastIndexOf("/", var9);
            int var11 = var8.indexOf("/", var9);
            if (var11 != -1) {
               var5 = var8.substring(var10 + 1, var11);
            } else {
               var5 = var8.substring(var10 + 1);
            }
         } else if (var6 == null) {
            try {
               var5 = var8.substring(1, var8.indexOf(47, 1));
            } catch (StringIndexOutOfBoundsException var41) {
               var5 = var8.substring(1, var8.length());
            }
         } else {
            try {
               var5 = var6.substring(1, var6.indexOf(47, 1));
            } catch (StringIndexOutOfBoundsException var40) {
               var5 = var6.substring(1, var6.length());
            }
         }

         String var44 = var8.substring(var8.indexOf(var5) + var5.length()).replace('/', File.separatorChar);
         String var45 = ((WebAppServletContext)this.getServletContext()).getDocroot();
         File var12 = new File(var45);
         if (!var12.isDirectory() && var45.toUpperCase().endsWith(".WAR")) {
            var45 = var45.substring(0, var45.length() - 4);
         }

         String var13 = var45 + var44;
         String var14 = var5;
         if (var5.length() == 0) {
            var2.sendError(404);
         } else {
            if (this.debug) {
               this.log("script name=" + var5);
            }

            CGIServletOutputStream var15 = new CGIServletOutputStream(var2);
            CharArrayWriter var16 = new CharArrayWriter();
            String var17 = Utils.encodeXSS(var1.getQueryString());
            if (var17 == null) {
               var17 = "";
            }

            String var18 = var1.getContentType();
            int var19 = var1.getContentLength();
            var4.add("DOCUMENT_ROOT=" + var45);
            var4.add("SERVER_NAME=" + var1.getServerName());
            var4.add("SERVER_PROTOCOL=" + var1.getProtocol());
            var4.add("SERVER_PORT=" + var1.getServerPort());
            var4.add("REQUEST_METHOD=" + var1.getMethod());
            var4.add("SCRIPT_NAME=" + HttpParsing.unescape(var1.getServletPath() + "/" + var5));
            var4.add("QUERY_STRING=" + var17);
            var4.add("REMOTE_HOST=" + Utils.encodeXSS(var1.getRemoteHost()));
            var4.add("REMOTE_ADDR=" + Utils.encodeXSS(var1.getRemoteAddr()));
            var4.add("REQUEST_URI=" + Utils.encodeXSS(var1.getRequestURI()) + (var1.getQueryString() == null ? "" : "?" + var17));
            if (var44.length() != 0) {
               var4.add("PATH_INFO=" + HttpParsing.unescape(var44).replace(File.separatorChar, '/'));
               var4.add("PATH_TRANSLATED=" + HttpParsing.unescape(var13));
            }

            Enumeration var20 = var1.getHeaderNames();

            while(var20.hasMoreElements()) {
               String var21 = Utils.encodeXSS((String)var20.nextElement());
               if (!var21.toUpperCase(Locale.ENGLISH).startsWith("AUTHORIZATION")) {
                  var4.add("HTTP_" + var21.toUpperCase().replace('-', '_') + "=" + Utils.encodeXSS(var1.getHeader(var21)));
               }
            }

            var4.add("AUTH_TYPE=" + var1.getAuthType());
            var4.add("REMOTE_USER=" + Utils.encodeXSS(var1.getRemoteUser()));
            var4.add("REMOTE_IDENT=");
            var4.add("HTTP_COOKIE=" + Utils.encodeXSS(var1.getHeader("Cookie")));
            var4.add("SERVER_URL=" + Utils.encodeXSS(var1.getScheme()) + "://" + Utils.encodeXSS(var1.getHeader("HOST")) + Utils.encodeXSS(var1.getContextPath()));
            if (var18 != null) {
               var4.add("CONTENT_TYPE=" + var18);
            }

            if (var19 > -1) {
               var4.add("CONTENT_LENGTH=" + Integer.toString(var19));
            }

            int var46 = var5.lastIndexOf(46);
            String var22;
            if (var46 != -1) {
               var22 = var5.substring(var46, var5.length());
               String var23 = (String)this.extensionMap.get("*" + var22);
               if (var23 != null) {
                  var3.add(var23);
               }
            }

            var22 = null;
            File var24 = null;

            int var47;
            for(var47 = 0; var47 < this.cgiDir.length; ++var47) {
               var22 = this.cgiDir[var47] + File.separator + var14;
               File var25 = new File(var22);
               if (var25.exists()) {
                  var24 = new File(this.cgiDir[var47]);
                  break;
               }
            }

            if (var47 == this.cgiDir.length) {
               this.log("Could not find script '" + var14 + "' in '" + this.allCgiDir + "'");
               if (this.debug && var17.startsWith("__cgiinfo")) {
                  this.cgiInfo(var3, var4, var2);
               } else {
                  var2.sendError(404);
               }
            } else {
               var4.add("SCRIPT_FILENAME=" + var22);
               var3.add(var22);
               if (this.debug) {
                  this.log("Script found " + var22);
               }

               if (var17.indexOf("=") < 0) {
                  StringTokenizer var48 = new StringTokenizer(var17, "+");

                  while(var48.hasMoreTokens()) {
                     var3.add(var48.nextToken());
                  }
               }

               if (var17.startsWith("__cgiinfo")) {
                  this.cgiInfo(var3, var4, var2);
               } else {
                  if (this.debug) {
                     this.log("Exec script with args :" + var3);
                  }

                  Executable var49 = new Executable();
                  var49.setStdout(var15);
                  var49.setErrwriter(var16);
                  var49.setPath("");
                  if ("POST".equals(var1.getMethod()) || "PUT".equals(var1.getMethod())) {
                     var49.setStdin(var1.getInputStream());
                  }

                  int var26 = this.stat_env.size();
                  int var27 = var4.size();
                  int var28 = var3.size();
                  String[] var29 = new String[var26 + var27];
                  var47 = 0;

                  int var30;
                  for(var30 = 0; var30 < var26; ++var30) {
                     var29[var47++] = (String)this.stat_env.get(var30);
                  }

                  for(var30 = 0; var30 < var27; ++var30) {
                     var29[var47++] = (String)var4.get(var30);
                  }

                  String[] var50 = new String[var28];

                  for(int var31 = 0; var31 < var28; ++var31) {
                     var50[var31] = (String)var3.get(var31);
                  }

                  try {
                     if (!var49.exec(var50, var29, var24)) {
                        if (this.debug) {
                           try {
                              this.log("Failed to exec CGI script. Return value : " + var49.getExitValue());
                           } catch (Throwable var39) {
                              this.log("Failed to exec CGI script. Process ended abrubtly. No return value available.");
                           }
                        } else {
                           this.log("Failed to exec CGI script:" + var14);
                        }

                        if (!var2.isCommitted()) {
                           var2.sendError(500);
                        }
                     }
                  } catch (Exception var42) {
                     String var32 = var16 != null ? var16.toString() : "";
                     var32 = "CGI script failed: " + var32;
                     this.getServletContext().log(var32, var42);
                     throw new ServletException(var32);
                  } finally {
                     var15.close();
                  }

               }
            }
         }
      } else {
         this.log("Cannot resolve cgi script. Cannot proceed further.");
         var2.sendError(500);
      }
   }

   private void cgiInfo(ArrayList var1, ArrayList var2, HttpServletResponse var3) throws IOException {
      var3.setContentType("text/html");
      var3.setHeader("Pragma", "no-cache");
      String var4 = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Draft//EN\">\n<HTML>\n<HEAD>\n<META NAME=\"GENERATOR\" CONTENT=\"WebLogic Server\">\n</HEAD>\n<BODY>\n<TABLE><TR><TD>Command = ";

      try {
         int var5;
         for(var5 = 0; var5 < var1.size(); ++var5) {
            var4 = var4 + (String)var1.get(var5) + " ";
         }

         var4 = var4 + "</TD></TR>\n";

         for(var5 = 0; var5 < var2.size(); ++var5) {
            var4 = var4 + "<TR><TD>" + (String)var2.get(var5) + "</TD></TR>\n";
         }

         for(var5 = 0; var5 < this.stat_env.size(); ++var5) {
            var4 = var4 + "<TR><TD>" + (String)this.stat_env.get(var5) + "</TD></TR>\n";
         }
      } catch (Exception var6) {
         var4 = var4 + "<TR><TD>" + "got exception " + var6 + "</TD></TR>\n";
      }

      var4 = var4 + "</TABLE>\n\n</BODY>\n</HTML>\n";
      var3.getOutputStream().write(var4.getBytes());
   }

   private boolean extractScripts(String var1, String var2, String var3) {
      byte[] var4 = new byte[4096];
      String[] var5 = StringUtils.splitCompletely(var3, ";");

      try {
         File var7;
         for(int var6 = 0; var6 < var5.length; ++var6) {
            var7 = new File(var5[var6]);
            if (var7.isAbsolute()) {
               var5[var6] = null;
            } else if (!var5[var6].startsWith("/") && !var5[var6].startsWith("\\")) {
               var5[var6] = var5[var6].replace('\\', '/');
            } else {
               var5[var6] = var5[var6].substring(1).replace('\\', '/');
            }
         }

         ZipFile var16 = new ZipFile(var1);
         var7 = new File(var2);
         if (!var7.exists() && !var7.mkdirs()) {
            this.log("Cannot make temp directory '" + var2 + "' to extract scripts");
         }

         Enumeration var8 = var16.entries();

         while(true) {
            ZipEntry var9;
            String var10;
            do {
               do {
                  if (!var8.hasMoreElements()) {
                     return true;
                  }

                  var9 = (ZipEntry)var8.nextElement();
                  var10 = var9.getName();
                  var3 = var3.replace('\\', '/');
               } while(var10.endsWith("/"));
            } while(!this.dirExistsInPath(var10, var5));

            File var11 = new File(var7, var10.replace('/', File.separatorChar));
            (new File(var11.getParent())).mkdirs();
            InputStream var12 = var16.getInputStream(var9);
            FileOutputStream var13 = new FileOutputStream(var11);

            int var14;
            while((var14 = var12.read(var4)) > 0) {
               var13.write(var4, 0, var14);
            }

            var12.close();
            var13.close();
         }
      } catch (Exception var15) {
         this.log("Failure extracting CGI scripts from WAR file " + var2, var15);
         return false;
      }
   }

   private boolean dirExistsInPath(String var1, String[] var2) {
      if (var2 == null) {
         return false;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] != null && var1.startsWith(var2[var3])) {
               return true;
            }
         }

         return false;
      }
   }

   class CGIServletOutputStream extends OutputStream {
      private ServletOutputStream sos = null;
      private HttpServletResponse res;
      private StringBuffer buf;
      private boolean isBody;
      private boolean nphScript = false;
      String locHeader = null;
      private Chunk chunk = Chunk.getChunk();
      private ByteBuffer byteBuffer;

      CGIServletOutputStream(HttpServletResponse var2) {
         this.byteBuffer = ByteBuffer.wrap(this.chunk.buf);
         this.res = var2;
         this.buf = new StringBuffer();
         this.isBody = false;
         this.nphScript = false;
         this.locHeader = null;
         this.byteBuffer.clear();
      }

      private OutputStream getOutputStream() throws IOException {
         if (this.sos == null) {
            this.sos = (ServletOutputStreamImpl)this.res.getOutputStream();
         }

         return this.sos;
      }

      public void close() throws IOException {
         Chunk.releaseChunk(this.chunk);
      }

      public void flush() throws IOException {
         if (this.byteBuffer.position() > 0) {
            this.getOutputStream().write(this.byteBuffer.array(), 0, this.byteBuffer.position());
         }

         this.byteBuffer.clear();
      }

      public void write(int var1) {
         if (this.isBody) {
            if (!this.byteBuffer.hasRemaining()) {
               try {
                  this.flush();
               } catch (IOException var4) {
                  CGIServlet.this.log("CGIServlet failed to write body for the response", var4);
                  return;
               }
            }

            this.byteBuffer.put((byte)var1);
         } else {
            if (var1 == 13) {
               return;
            }

            if (var1 != 10) {
               this.buf.append((char)var1);
            } else if (this.buf.length() == 0) {
               this.isBody = true;

               try {
                  if (this.locHeader != null && !this.nphScript) {
                     this.res.sendRedirect(this.locHeader);
                  }
               } catch (IOException var3) {
                  CGIServlet.this.log("CGIServlet failed to redirect the request. locHeader=" + this.locHeader, var3);
                  return;
               }
            } else {
               this.processHeader();
            }
         }

      }

      private void processHeader() {
         String var1 = this.buf.toString();
         this.buf.setLength(0);
         String[] var2;
         if (var1.startsWith("HTTP/")) {
            this.res.reset();
            this.nphScript = true;
            var2 = StringUtils.splitCompletely(var1, " ");
            if (var2.length < 2 || var2[1] == null) {
               return;
            }

            try {
               this.res.setStatus(Integer.parseInt(var2[1]));
            } catch (NumberFormatException var4) {
               CGIServlet.this.log("CGIServlet failed to set StatusHeader", var4);
               return;
            }
         } else {
            var2 = StringUtils.split(var1, ':');
            if (var2.length >= 2) {
               this.setHeaderElement(var2);
            }
         }

      }

      private void setHeaderElement(String[] var1) {
         var1[0] = var1[0].trim();
         var1[1] = var1[1].trim();
         if ("Content-type".equalsIgnoreCase(var1[0])) {
            this.res.setContentType(var1[1]);
         } else if ("Content-length".equalsIgnoreCase(var1[0])) {
            try {
               this.res.setContentLength(Integer.parseInt(var1[1]));
            } catch (NumberFormatException var5) {
               CGIServlet.this.log("CGIServlet failed to set ContentLength", var5);
            }
         } else if ("Location".equalsIgnoreCase(var1[0])) {
            this.locHeader = new String(var1[1]);
            this.res.setHeader("Location", this.locHeader);
         } else if ("Status".equalsIgnoreCase(var1[0])) {
            String[] var2 = StringUtils.splitCompletely(var1[1], " ");

            try {
               this.res.setStatus(Integer.parseInt(var2[0]));
            } catch (NumberFormatException var4) {
               CGIServlet.this.log("CGIServlet failed to set StatusHeader", var4);
            }
         } else if ("Set-Cookie".equalsIgnoreCase(var1[0])) {
            this.res.addHeader(var1[0], var1[1]);
         } else {
            this.res.setHeader(var1[0], var1[1]);
         }

      }
   }
}
