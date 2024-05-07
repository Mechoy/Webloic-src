package weblogic.servlet.jsp;

import java.io.IOException;
import weblogic.utils.PlatformConstants;

public class CompilationException extends IOException {
   static final long serialVersionUID = -1069773868125433531L;
   private String errors;
   private String html;
   private String jspURI;

   public CompilationException(String var1, String var2, String var3, String var4, Throwable var5) {
      super(var1 + PlatformConstants.EOL + var3);
      this.initCause(var5);
      this.errors = var3;
      this.html = var4;
      this.jspURI = var2;
   }

   public String getErrorText() {
      return this.errors;
   }

   public String getJavaFileName() {
      return this.jspURI;
   }

   public String toHtml() {
      return this.html;
   }
}
