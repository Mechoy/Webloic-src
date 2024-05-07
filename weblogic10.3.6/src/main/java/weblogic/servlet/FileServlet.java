package weblogic.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.CollationKey;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.platform.VM;
import weblogic.servlet.internal.ByteRangeHandler;
import weblogic.servlet.internal.ServletOutputStreamImpl;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WarSource;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.socket.WeblogicSocket;
import weblogic.utils.CharsetMap;
import weblogic.utils.classloaders.Source;
import weblogic.utils.io.StreamUtils;

public class FileServlet extends HttpServlet {
   private static final String REQUEST_URI_INCLUDE = "javax.servlet.include.request_uri";
   private static final String SERVLET_PATH_INCLUDE = "javax.servlet.include.servlet_path";
   private static final String PATH_INFO_INCLUDE = "javax.servlet.include.path_info";
   private static final String METHOD_GET = "GET";
   private static final String METHOD_HEAD = "HEAD";
   private static final String METHOD_DELETE = "DELETE";
   private static final String METHOD_OPTIONS = "OPTIONS";
   private static final String METHOD_POST = "POST";
   private static final String METHOD_PUT = "PUT";
   private static final String METHOD_TRACE = "TRACE";
   private static final int FORMAT_SPACE = 5;
   public static final String SORTBY_NAME = "NAME";
   public static final String SORTBY_LAST_MODIFIED = "LAST_MODIFIED";
   public static final String SORTBY_SIZE = "SIZE";
   private static final SimpleDateFormat DIRECTORY_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy kk:mm");
   public static final DebugLogger DEBUG_URL_RES = DebugLogger.getDebugLogger("DebugURLResolution");
   public static final long DEFAULT_MIN_NATIVE = 4096L;
   private long minNative = 4096L;
   private boolean nativeOK = false;
   protected WebAppServletContext context;
   protected String defaultFilename;

   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      this.context = (WebAppServletContext)var1.getServletContext();
      this.defaultFilename = this.context.getInitParameter("defaultFilename");
      if (this.defaultFilename == null) {
         this.defaultFilename = var1.getInitParameter("defaultFilename");
      }

      if ("".equals(this.defaultFilename)) {
         this.defaultFilename = null;
      }

      this.nativeOK = this.initNative(var1);
   }

   private synchronized boolean initNative(ServletConfig var1) {
      String var2;
      if (!this.context.getConfigManager().isNativeIOEnabled()) {
         var2 = this.context.getInitParameter("weblogic.http.nativeIOEnabled");
         if (var2 == null) {
            var2 = var1.getInitParameter("nativeIOEnabled");
         }

         if (!"true".equalsIgnoreCase(var2)) {
            if (DEBUG_URL_RES.isDebugEnabled()) {
               DEBUG_URL_RES.debug(this.context.getLogContext() + ": using standard I/O");
            }

            return false;
         }
      }

      var2 = this.context.getInitParameter("weblogic.http.minimumNativeFileSize");
      if (var2 == null) {
         var2 = var1.getInitParameter("minimumNativeFileSize");
      }

      try {
         if (var2 != null && !"".equals(var2)) {
            this.minNative = Long.parseLong(var2);
         } else {
            this.minNative = this.context.getConfigManager().getMinimumNativeFileSize();
         }
      } catch (NumberFormatException var5) {
         if (DEBUG_URL_RES.isDebugEnabled()) {
            DEBUG_URL_RES.debug(this.context.getLogContext() + ": Invalid setting for minimumNativeFileSize: " + var2);
         }

         this.minNative = this.context.getConfigManager().getMinimumNativeFileSize();
      }

      try {
         System.loadLibrary("fastfile");
      } catch (UnsatisfiedLinkError var4) {
         if (DEBUG_URL_RES.isDebugEnabled()) {
            DEBUG_URL_RES.debug(this.context.getLogContext() + ": Could not load library for native I/O", var4);
         }

         HTTPLogger.logFailedToLoadNativeIOLibrary(this.context.getLogContext(), var4);
         return false;
      }

      if (DEBUG_URL_RES.isDebugEnabled()) {
         DEBUG_URL_RES.debug(this.context.getLogContext() + ": Native I/O library loaded");
      }

      return true;
   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getMethod();
      if (!var3.equals("GET") && !var3.equals("HEAD") && !var3.equals("POST")) {
         if (var3.equals("DELETE")) {
            this.doDelete(var1, var2);
         } else if (var3.equals("OPTIONS")) {
            var2.setHeader("Allow", "GET, HEAD, OPTIONS, POST");
         } else if (var3.equals("PUT")) {
            this.doPut(var1, var2);
         } else if (var3.equals("TRACE")) {
            this.doTrace(var1, var2);
         } else {
            var2.sendError(501);
         }
      } else {
         this.doGetHeadPost(var1, var2);
      }

   }

   private void doGetHeadPost(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      WarSource var3 = this.findSource(var1, var2);
      if (var3 != null) {
         if (var3.length() == 0L) {
            var2.setContentLength(0);
         } else if (var1.getAttribute("javax.servlet.error.request_uri") != null || this.isModified(var1, var2, var3)) {
            if (isRangeRequest(var1)) {
               this.sendRangeData(var3, var1, var2);
            } else {
               String var4 = var3.getContentType(this.context);
               if (var4 != null) {
                  var2.setContentType(var4);
               }

               long var5 = var3.length();
               if (var5 != -1L) {
                  var2.setContentLength((int)var5);
               }

               if (!var1.getMethod().equals("HEAD")) {
                  if (this.nativeOK && this.isNativeOK(var3, var1, var2)) {
                     if (DEBUG_URL_RES.isDebugEnabled()) {
                        DEBUG_URL_RES.debug(this.context.getLogContext() + ": Sending file using native I/O");
                     }

                     this.sendFileNative(var3.getFileName(), ServletRequestImpl.getOriginalRequest(var1), var5);
                  } else {
                     if (DEBUG_URL_RES.isDebugEnabled()) {
                        DEBUG_URL_RES.debug(this.context.getLogContext() + ": Sending file using standard I/O");
                     }

                     boolean var7 = var1.getAttribute("javax.servlet.include.request_uri") != null;
                     this.sendFile(var3, var2, "HTTP/1.1".equals(var1.getProtocol()), var7);
                  }

               }
            }
         }
      }
   }

   private static boolean isRangeRequest(HttpServletRequest var0) {
      if (!"HTTP/1.1".equals(var0.getProtocol())) {
         return false;
      } else if (!var0.getMethod().equals("GET")) {
         return false;
      } else {
         return var0.getHeader("Range") != null;
      }
   }

   private boolean isNativeOK(WarSource var1, HttpServletRequest var2, HttpServletResponse var3) {
      return !var2.isSecure() && var1.length() > this.minNative && var1.isFile() && var2.getAttribute("javax.servlet.include.request_uri") == null && var3 instanceof ServletResponseImpl && !((ServletResponseImpl)var3).isInternalDispatch();
   }

   protected WarSource findSource(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      String var3 = getURI(var1);
      if (DEBUG_URL_RES.isDebugEnabled()) {
         DEBUG_URL_RES.debug(this.context.getLogContext() + ": looking for file: '" + var3 + "'");
      }

      WarSource var4 = this.getSource(var3);
      if (var4 == null) {
         var2.sendError(404);
         return null;
      } else {
         char var5 = var3.length() == 0 ? 0 : var3.charAt(var3.length() - 1);
         if (var4.isDirectory()) {
            if (var5 != '/') {
               var2.sendRedirect(var1.getRequestURL().append('/').toString());
            } else {
               if (this.defaultFilename != null) {
                  WarSource var6 = this.getSource(var3 + this.defaultFilename);
                  if (var6 != null) {
                     return var6;
                  }
               }

               if (this.context.getConfigManager().isIndexDirectoryEnabled()) {
                  Enumeration var7 = this.context.getResourceFinder(var3).getSources(var3);
                  this.produceDirectoryListing(var7, var1, var2);
               } else {
                  var2.sendError(403);
               }
            }

            return null;
         } else if (var5 != '\\' && var5 != '/') {
            return var4;
         } else {
            var2.sendError(404);
            return null;
         }
      }
   }

   private static String getURI(HttpServletRequest var0) {
      String var1 = (String)var0.getAttribute("javax.servlet.include.servlet_path");
      String var2 = (String)var0.getAttribute("javax.servlet.include.path_info");
      if (var1 == null && var2 == null) {
         var1 = var0.getServletPath();
         var2 = var0.getPathInfo();
      }

      String var3 = null;
      if (var1 != null) {
         var3 = var1;
         if (var2 != null) {
            var3 = var1 + var2;
         }
      } else if (var2 != null) {
         var3 = var2;
      }

      if (var3 == null) {
         var3 = "";
      }

      return var3;
   }

   private WarSource getSource(String var1) {
      return this.context.getResourceAsSourceWithMDS(var1);
   }

   private boolean isModified(HttpServletRequest var1, HttpServletResponse var2, WarSource var3) throws IOException {
      if (var1.getAttribute("javax.servlet.include.request_uri") != null) {
         return true;
      } else {
         long var4 = var3.lastModified();
         var4 -= var4 % 1000L;
         if (var4 == 0L) {
            if (DEBUG_URL_RES.isDebugEnabled()) {
               DEBUG_URL_RES.debug(this.context.getLogContext() + ": Couldn't find resource for URI: " + var1.getRequestURI() + " - sending 404");
            }

            var2.sendError(404);
            return false;
         } else {
            long var6 = -1L;

            try {
               var6 = var1.getDateHeader("If-Modified-Since");
            } catch (IllegalArgumentException var9) {
            }

            if (var6 >= var4) {
               if (DEBUG_URL_RES.isDebugEnabled()) {
                  DEBUG_URL_RES.debug(this.context.getLogContext() + ": Source: " + var3 + " Last-Modified: " + var4 + " If-Modified-Since : " + var6 + " - sending 302");
               }

               var2.setStatus(304);
               return false;
            } else {
               var2.setHeader("Last-Modified", var3.getLastModifiedAsString());
               return true;
            }
         }
      }
   }

   private void sendRangeData(WarSource var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException {
      String var4 = var1.getContentType(this.context);
      var3.addHeader("Accept-Ranges", "bytes");
      ByteRangeHandler var5 = ByteRangeHandler.makeInstance(var1, var2, var4);
      if (var5 != null) {
         var5.sendRangeData(var3);
      }

   }

   private void sendFile(Source var1, HttpServletResponse var2, boolean var3, boolean var4) throws IOException {
      InputStream var5 = var1.getInputStream();

      try {
         if (var5 == null) {
            var2.sendError(404);
            return;
         }

         if (var3) {
            var2.addHeader("Accept-Ranges", "bytes");
         }

         ServletOutputStream var6 = var2.getOutputStream();

         try {
            ((ServletOutputStreamImpl)var6).writeStream(var5);
         } catch (ClassCastException var13) {
            StreamUtils.writeTo(var5, var6);
         }
      } catch (IllegalStateException var14) {
         if (!(var2 instanceof ServletResponseWrapper) && !var4) {
            throw var14;
         }

         this.sendFileUsingWriter(var1, var2);
      } finally {
         if (var5 != null) {
            var5.close();
         }

      }

   }

   private void sendFileUsingWriter(Source var1, HttpServletResponse var2) throws IOException {
      InputStream var3 = null;
      InputStreamReader var4 = null;
      int var5 = var2.getBufferSize();

      try {
         PrintWriter var6 = var2.getWriter();
         var3 = var1.getInputStream();
         if (var3 == null) {
            var2.sendError(404);
            return;
         }

         String var7 = var2.getCharacterEncoding();
         if (var7 == null) {
            var7 = ((WebAppServletContext)this.getServletContext()).getConfigManager().getDefaultEncoding();
         }

         var4 = new InputStreamReader(var3, var7);
         if (var5 == -1) {
            var5 = 4096;
         }

         char[] var8 = new char[var5];
         boolean var9 = false;

         int var14;
         while((var14 = var4.read(var8)) > 0) {
            var6.write(var8, 0, var14);
         }
      } finally {
         if (var4 != null) {
            var4.close();
         }

         if (var3 != null) {
            var3.close();
         }

      }

   }

   private void sendFileNative(String var1, ServletRequestImpl var2, long var3) throws IOException {
      ServletResponseImpl var5 = var2.getResponse();
      ServletOutputStreamImpl var6 = (ServletOutputStreamImpl)var5.getOutputStream();
      var6.setNativeControlsPipe(true);
      Socket var7 = var2.getConnection().getSocket();
      if (var7 instanceof WeblogicSocket) {
         var7 = ((WeblogicSocket)var7).getSocket();
      }

      int var8 = var2.getConnection().getSocketFD();
      if (var8 == -1) {
         var8 = VM.getVM().getFD(var7);
         var2.getConnection().setSocketFD(var8);
      }

      transmitFile(var1, var3, var8);
      var6.setNativeControlsPipe(false);
   }

   private void produceDirectoryListing(Enumeration var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException {
      String var4 = CharsetMap.getIANAFromJava(System.getProperty("file.encoding"));
      String var5 = "text/html";
      if (var4 != null) {
         var5 = var5 + "; charset=" + var4;
      }

      var3.setContentType(var5);
      ServletOutputStream var6 = var3.getOutputStream();
      String var7 = ServletRequestImpl.getResolvedURI(var2);
      var6.println("<HTML>\n<HEAD>\n<TITLE>Index of ");
      var6.println(var7);
      var6.println("</TITLE>\n</HEAD>\n<BODY>");
      var6.println("<H1>Index of ");
      var6.println(var7);
      var6.println("</H1><BR>");
      var6.println("<PRE>");
      var6.print("<A HREF=\"?sortby=NAME\">");
      outStr("Name", var6, 29, true);
      var6.print("<A HREF=\"?sortby=LAST_MODIFIED\">");
      outStr("Last Modified", var6, 24, true);
      var6.print("<A HREF=\"?sortby=SIZE\">");
      outStr("Size", var6, 10, true);
      var6.println("<HR>");
      if (!"/".equals(var7) && var7.length() > 2) {
         int var8 = var7.lastIndexOf(47, var7.length() - 2);
         if (var8 >= 0) {
            String var9 = var7.substring(0, var8 + 1);
            var6.println("<A HREF=\"" + var9 + "\">Parent Directory</A>");
         }
      }

      Object var17 = null;

      while(true) {
         while(var1.hasMoreElements()) {
            WarSource var18 = (WarSource)var1.nextElement();
            List var10 = Arrays.asList(var18.listSources());
            if (!var1.hasMoreElements() && var17 == null) {
               var17 = var10;
            } else {
               if (var17 == null) {
                  var17 = new ArrayList();
               }

               ((List)var17).addAll(var10);
            }
         }

         if (((List)var17).size() == 0) {
            var6.println("</PRE></BODY></HTML>");
            return;
         }

         TypeComparator var19 = new TypeComparator(this.getComparator(var2));
         Collections.sort((List)var17, var19);
         Iterator var20 = ((List)var17).iterator();

         while(true) {
            while(true) {
               if (!var20.hasNext()) {
                  var6.println("</PRE></BODY></HTML>");
                  return;
               }

               WarSource var11 = (WarSource)var20.next();
               String var12 = var11.getName();
               Date var13 = new Date(var11.lastModified());
               if (var11.isDirectory()) {
                  if ("WEB-INF".equals(var12) || "META-INF".equals(var12)) {
                     continue;
                  }

                  var6.print("<A HREF=\"" + var12 + "/\">");
                  outStr(var12, var6, 29, true);
                  outStr(DIRECTORY_DATE_FORMAT.format(var13), var6, 24, false);
                  outStr("&lt;DIR&gt;", var6, 10, false);
                  break;
               }

               var6.print("<A HREF=\"" + var12 + "\">");
               outStr(var12, var6, 29, true);
               outStr(DIRECTORY_DATE_FORMAT.format(var13), var6, 24, false);
               long var14 = var11.length();
               String var16;
               if (var14 >= 1024L) {
                  var16 = String.valueOf(var14 / 1024L) + 'k';
               } else {
                  var16 = String.valueOf(var14);
               }

               outStr(var16, var6, 10, false);
               break;
            }

            var6.println();
         }
      }
   }

   private Comparator getComparator(HttpServletRequest var1) {
      String var2 = var1.getParameter("sortby");
      if ("LAST_MODIFIED".equals(var2)) {
         return new LastModifiedComparator();
      } else if ("SIZE".equals(var2)) {
         return new SizeComparator();
      } else if ("NAME".equals(var2)) {
         return new NameComparator();
      } else {
         var2 = this.context.getConfigManager().getIndexDirectorySortBy();
         if ("LAST_MODIFIED".equals(var2)) {
            return new LastModifiedComparator();
         } else {
            return (Comparator)("SIZE".equals(var2) ? new SizeComparator() : new NameComparator());
         }
      }
   }

   private static void outStr(String var0, ServletOutputStream var1, int var2, boolean var3) throws IOException {
      int var4;
      for(var4 = 0; var4 < var0.length() && var4 < var2; ++var4) {
         var1.print(var0.charAt(var4));
      }

      if (var3) {
         var1.print("</A>");
      }

      while(var4 < var2) {
         var1.write(32);
         ++var4;
      }

   }

   private static native void transmitFile(String var0, long var1, int var3) throws IOException;

   private static final class TypeComparator implements Comparator {
      private final Comparator tieBreaker;

      TypeComparator(Comparator var1) {
         this.tieBreaker = var1;
      }

      public int compare(Object var1, Object var2) {
         WarSource var3 = (WarSource)var1;
         WarSource var4 = (WarSource)var2;
         boolean var5 = var3.isDirectory();
         boolean var6 = var4.isDirectory();
         if (var5 == var6) {
            return this.tieBreaker.compare(var3, var4);
         } else {
            return var5 ? -1 : 1;
         }
      }
   }

   private static final class SizeComparator extends NameComparator {
      private SizeComparator() {
         super(null);
      }

      public int compare(Object var1, Object var2) {
         WarSource var3 = (WarSource)var1;
         WarSource var4 = (WarSource)var2;
         if (var3.isDirectory()) {
            return super.compare(var3, var4);
         } else {
            long var5 = var3.length() - var4.length();
            if (var5 < 0L) {
               return -1;
            } else {
               return var5 > 0L ? 1 : super.compare(var3, var4);
            }
         }
      }

      // $FF: synthetic method
      SizeComparator(Object var1) {
         this();
      }
   }

   private static final class LastModifiedComparator extends NameComparator {
      private LastModifiedComparator() {
         super(null);
      }

      public int compare(Object var1, Object var2) {
         Source var3 = (Source)var1;
         Source var4 = (Source)var2;
         long var5 = var3.lastModified() - var4.lastModified();
         if (var5 < 0L) {
            return -1;
         } else {
            return var5 > 0L ? 1 : super.compare(var3, var4);
         }
      }

      // $FF: synthetic method
      LastModifiedComparator(Object var1) {
         this();
      }
   }

   private static class NameComparator implements Comparator {
      private final Collator collator;

      private NameComparator() {
         this.collator = Collator.getInstance();
      }

      public int compare(Object var1, Object var2) {
         String var3 = ((WarSource)var1).getName();
         String var4 = ((WarSource)var2).getName();
         CollationKey var5 = this.collator.getCollationKey(var3);
         CollationKey var6 = this.collator.getCollationKey(var4);
         return var5.compareTo(var6);
      }

      // $FF: synthetic method
      NameComparator(Object var1) {
         this();
      }
   }
}
