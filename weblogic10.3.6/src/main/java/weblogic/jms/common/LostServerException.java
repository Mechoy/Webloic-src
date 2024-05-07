package weblogic.jms.common;

import weblogic.logging.Loggable;

public final class LostServerException extends JMSException {
   static final long serialVersionUID = -7591145489117685663L;
   boolean replayLastException;

   public LostServerException(String var1) {
      super(var1);
   }

   public LostServerException(Throwable var1) {
      super(var1);
   }

   public LostServerException(Loggable var1) {
      super(var1.getMessage());
   }

   public LostServerException(Loggable var1, Throwable var2) {
      super(var1.getMessage(), var2);
   }

   public LostServerException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public boolean isReplayLastException() {
      return this.replayLastException;
   }

   public void setReplayLastException(boolean var1) {
      this.replayLastException = var1;
   }
}
