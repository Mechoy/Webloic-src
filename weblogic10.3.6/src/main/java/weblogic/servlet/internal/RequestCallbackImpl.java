package weblogic.servlet.internal;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.io.FilenameEncoder;

final class RequestCallbackImpl implements RequestCallback {
   private HttpServletResponse response;
   private HttpServletRequest request;
   private ServletResponseImpl respImpl;
   private static final String JSP_ERROR_SENT_ATTRIBUTE = "wl.jsp.errorSent";

   public RequestCallbackImpl(HttpServletRequest var1, HttpServletResponse var2, ServletResponseImpl var3) {
      this.request = var1;
      this.response = var2;
      this.respImpl = var3;
   }

   private void reportJSPFailure(String var1) throws IOException {
      if (!this.respImpl.isOutputStreamInitialized()) {
         if (this.request.getAttribute("wl.jsp.errorSent") == null) {
            this.request.setAttribute("wl.jsp.errorSent", Boolean.TRUE);
            String var2 = System.getProperty("file.encoding");
            ChunkOutput var3 = null;
            ServletOutputStream var4 = this.respImpl.getOutputStreamNoCheck();
            if (var4 != null && var4 instanceof ServletOutputStreamImpl) {
               var3 = ((ServletOutputStreamImpl)var4).getOutput().getOutput();
            }

            if (var3 != null && var3 instanceof CharChunkOutput) {
               this.respImpl.setHeaderInternal("Content-Type", "text/html");
            } else {
               this.respImpl.setHeaderInternal("Content-Type", "text/html; charset=" + var2);
            }

            PrintWriter var5 = this.response.getWriter();
            HttpServletResponse var10001 = this.response;
            this.response.setStatus(500);
            var5.print(var1);
            var5.flush();
         }
      }
   }

   public void reportJSPTranslationFailure(String var1, String var2) throws IOException {
      this.reportJSPFailure(var2);
   }

   public void reportJSPCompilationFailure(String var1, String var2) throws IOException {
      this.reportJSPFailure(var2);
   }

   public String getIncludeURI() {
      StringBuilder var1 = new StringBuilder(30);
      String var2 = (String)this.request.getAttribute("javax.servlet.include.servlet_path");
      String var3 = (String)this.request.getAttribute("javax.servlet.include.path_info");
      if (var2 == null && var3 == null) {
         var2 = this.request.getServletPath();
         var3 = this.request.getPathInfo();
      }

      if (var2 != null) {
         var1.append(var2);
      }

      if (var3 != null) {
         var1.append(var3);
      }

      String var4 = var1.toString();
      var4 = FilenameEncoder.resolveRelativeURIPath(HttpParsing.unescape(var4));
      return var4;
   }
}
