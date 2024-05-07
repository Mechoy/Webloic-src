package weblogic.xml.dom;

import java.io.PrintWriter;

public class ChildCountException extends DOMProcessingException {
   public static final int MISSING_CHILD = 1;
   public static final int EXTRA_CHILD = 2;
   private static final String[] errorMsgs = new String[]{"missing child", "extra child"};
   private int errorCode;
   private String parentTagName;
   private String childTagName;
   private int childCount;

   public ChildCountException(int var1, String var2, String var3, int var4) {
      this.errorCode = var1;
      this.parentTagName = var2;
      this.childTagName = var3;
      this.childCount = var4;
   }

   public void writeErrorCondition(PrintWriter var1) {
      var1.println("[Begin ChildCountException:");
      var1.println("\terrorCode: " + this.errorCode);
      var1.println("\tparentTagName: " + this.parentTagName);
      var1.println("\tchildTagName: " + this.childTagName);
      var1.println("\tchildCount: " + this.childCount);
      var1.println("End ChildCountException]");
   }

   public String toString() {
      return super.toString() + ": " + errorMsgs[this.errorCode - 1] + " " + this.childTagName + " in " + this.parentTagName;
   }
}
