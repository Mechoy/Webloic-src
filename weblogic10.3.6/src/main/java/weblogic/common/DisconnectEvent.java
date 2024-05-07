package weblogic.common;

public final class DisconnectEvent {
   Object source;
   String reason;

   DisconnectEvent(Object var1, String var2) {
      this.source = var1;
      this.reason = var2;
   }

   public Object getSource() {
      return this.source;
   }

   public String getReason() {
      return this.reason;
   }

   public String toString() {
      return this.getClass().getName() + "[source=" + this.source + ", reason=" + this.reason + "]";
   }
}
