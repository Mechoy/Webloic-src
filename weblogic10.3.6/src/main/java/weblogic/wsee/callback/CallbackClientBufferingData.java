package weblogic.wsee.callback;

import java.io.Serializable;

public class CallbackClientBufferingData implements Serializable {
   private int retryCount = 0;
   private String retryDelay = null;
   private String targetURI = null;

   public String getTargetURI() {
      return this.targetURI;
   }

   public void setTargetURI(String var1) {
      this.targetURI = var1;
   }

   public int getRetryCount() {
      return this.retryCount;
   }

   public void setRetryCount(int var1) {
      this.retryCount = var1;
   }

   public String getRetryDelay() {
      return this.retryDelay;
   }

   public void setRetryDelay(String var1) {
      this.retryDelay = var1;
   }
}
