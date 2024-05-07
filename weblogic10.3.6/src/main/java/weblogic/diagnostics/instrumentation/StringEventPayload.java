package weblogic.diagnostics.instrumentation;

public class StringEventPayload implements EventPayload {
   static final long serialVersionUID = -665469141712332428L;
   protected String payload;

   public StringEventPayload(String var1) {
      this.payload = var1;
   }

   public String getPayload() {
      return this.payload;
   }

   public String toString() {
      return this.payload;
   }
}
