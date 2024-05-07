package weblogic.servlet.internal;

import java.util.Locale;
import weblogic.utils.StringUtils;

final class URLMatchHelper {
   private static final boolean WIN_32;
   private final String pattern;
   private final ServletStubImpl servletStub;
   private final String servletPath;
   private final String extension;
   private final boolean defaultServlet;
   private final boolean exactPattern;
   private final boolean fileOrJspServlet;
   private final boolean isJspClassServlet;

   URLMatchHelper(String var1, ServletStubImpl var2) {
      this.pattern = var1;
      this.servletStub = var2;
      String var3 = this.servletStub.getClassName();
      this.fileOrJspServlet = "weblogic.servlet.FileServlet".equals(var3) || "weblogic.servlet.JSPServlet".equals(var3) || "weblogic.servlet.JavelinJSPServlet".equals(var3) || "weblogic.servlet.JavelinxJSPServlet".equals(var3);
      this.isJspClassServlet = "weblogic.servlet.JSPClassServlet".equals(var3);
      if (this.pattern.startsWith("*.") && this.pattern.length() > 2) {
         this.servletPath = null;
         this.extension = this.pattern.substring(1);
         this.defaultServlet = false;
         this.exactPattern = false;
      } else if (this.pattern.equals("/")) {
         this.servletPath = null;
         this.extension = null;
         this.defaultServlet = true;
         this.exactPattern = true;
      } else if (this.pattern.equals("/*")) {
         this.servletPath = "";
         this.extension = null;
         this.defaultServlet = true;
         this.exactPattern = false;
      } else if (this.pattern.endsWith("/*")) {
         this.servletPath = this.pattern.substring(0, this.pattern.length() - 2);
         this.extension = null;
         this.defaultServlet = false;
         this.exactPattern = false;
      } else {
         this.servletPath = this.pattern;
         this.extension = null;
         this.defaultServlet = false;
         this.exactPattern = true;
      }

   }

   String getPattern() {
      return this.pattern;
   }

   ServletStubImpl getServletStub() {
      return this.servletStub;
   }

   boolean isDefaultServlet() {
      return this.defaultServlet;
   }

   boolean isIndexServlet() {
      return !this.defaultServlet && !this.fileOrJspServlet;
   }

   boolean isFileOrJspServlet() {
      return this.fileOrJspServlet || this.isJspClassServlet;
   }

   String getServletPath(String var1) {
      if (this.exactPattern) {
         return var1;
      } else if (this.defaultServlet) {
         return "";
      } else if (this.extension == null) {
         return this.servletPath;
      } else {
         int var2;
         if (WIN_32) {
            var2 = StringUtils.lastIndexOfIgnoreCase(var1, this.extension);
         } else {
            var2 = var1.lastIndexOf(this.extension);
         }

         int var3 = var1.indexOf(47, var2);
         return var3 < 0 ? var1 : var1.substring(0, var3);
      }
   }

   static {
      WIN_32 = System.getProperty("os.name", "unknown").toLowerCase(Locale.ENGLISH).indexOf("windows") >= 0;
   }
}
