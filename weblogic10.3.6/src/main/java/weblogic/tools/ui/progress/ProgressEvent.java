package weblogic.tools.ui.progress;

public class ProgressEvent {
   private String message;
   private int messageType;
   private int percentageComplete;
   public static final int WARN = 0;
   public static final int NORM = 1;
   public static final int ERR = 2;
   public static final int DONE_SUCCESS = 3;
   public static final int DONE_FAILURE = 4;

   public ProgressEvent() {
      this.messageType = 1;
   }

   public ProgressEvent(String var1, int var2) {
      this.message = var1;
      this.messageType = var2;
   }

   public String getMessage() {
      return this.message;
   }

   public int getMessageType() {
      return this.messageType;
   }

   public int getPercentageComplete() {
      return this.percentageComplete;
   }

   public void setEventInfo(String var1, int var2) {
      this.messageType = var2;
      this.message = var1;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public void setPercentageComplete(int var1) {
      this.percentageComplete = var1;
   }
}
