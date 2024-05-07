package weblogic.wsee.tools.logging;

import java.util.HashMap;
import java.util.Map;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;

public class BuildListenerLogger implements BuildListener {
   private static final Map<Integer, EventLevel> PRIORITIES = new HashMap();
   private final Logger logger;

   public BuildListenerLogger(Logger var1) {
      this.logger = var1;
   }

   public void buildStarted(BuildEvent var1) {
   }

   public void buildFinished(BuildEvent var1) {
   }

   public void targetStarted(BuildEvent var1) {
   }

   public void targetFinished(BuildEvent var1) {
   }

   public void taskStarted(BuildEvent var1) {
   }

   public void taskFinished(BuildEvent var1) {
   }

   public void messageLogged(BuildEvent var1) {
      this.logger.log(this.getEventLevel(var1.getPriority()), var1.getMessage());
   }

   private EventLevel getEventLevel(int var1) {
      EventLevel var2 = (EventLevel)PRIORITIES.get(var1);
      if (var2 == null) {
         var2 = EventLevel.VERBOSE;
      }

      return var2;
   }

   static {
      PRIORITIES.put(0, EventLevel.ERROR);
      PRIORITIES.put(1, EventLevel.WARNING);
      PRIORITIES.put(2, EventLevel.INFO);
      PRIORITIES.put(3, EventLevel.VERBOSE);
      PRIORITIES.put(4, EventLevel.DEBUG);
   }
}
