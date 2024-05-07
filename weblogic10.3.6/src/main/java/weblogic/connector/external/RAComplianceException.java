package weblogic.connector.external;

public class RAComplianceException extends Exception {
   String msg = new String();
   int numMsgs = 0;

   public void addMessage(String var1) {
      ++this.numMsgs;
      if (this.numMsgs == 1) {
         this.msg = var1;
      } else if (this.numMsgs == 2) {
         this.msg = "[1] " + this.msg + "   \n[2] " + var1;
      } else {
         this.msg = this.msg + "   \n[" + this.numMsgs + "] " + var1;
      }

   }

   public String toString() {
      return this.msg;
   }

   public String getMessage() {
      return this.msg;
   }
}
