package weblogic.servlet.jsp;

public final class JspException extends RuntimeException {
   private static final long serialVersionUID = 5685371183394534746L;
   public int lineNumber;
   public String uri;

   public JspException(String var1, int var2, String var3) {
      this(var1, var2);
      this.uri = var3;
   }

   public JspException(String var1, int var2) {
      super(var1);
      this.lineNumber = var2;
   }

   public JspException(String var1) {
      this(var1, -1);
   }

   public String getShortMessage() {
      return super.getMessage();
   }

   public String getMessage() {
      return "(line " + this.lineNumber + "): " + super.getMessage();
   }
}
