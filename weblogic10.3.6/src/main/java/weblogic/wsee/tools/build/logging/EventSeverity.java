package weblogic.wsee.tools.build.logging;

public final class EventSeverity {
   public static final EventSeverity DEBUG = new EventSeverity("DEBUG");
   public static final EventSeverity INFO = new EventSeverity("INFO");
   public static final EventSeverity WARNING = new EventSeverity("WARNING");
   public static final EventSeverity ERROR = new EventSeverity("ERROR");
   public static final EventSeverity VERBOSE = new EventSeverity("VERBOSE");
   public static final EventSeverity FATAL = new EventSeverity("FATAL");
   private final String myName;

   private EventSeverity(String var1) {
      this.myName = var1;
   }

   public String toString() {
      return this.myName;
   }
}
