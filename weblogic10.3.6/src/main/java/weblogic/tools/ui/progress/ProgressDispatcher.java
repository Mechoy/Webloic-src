package weblogic.tools.ui.progress;

import java.util.Arrays;
import java.util.Vector;
import weblogic.utils.Debug;

public class ProgressDispatcher implements ProgressListener {
   protected ProgressListener[] listeners;
   protected ProgressEvent event = new ProgressEvent();
   private ProgressProducer progressProducer;

   public void updateProgress(ProgressEvent var1) {
      this.update(var1);
   }

   public void update(String var1) {
      this.update(var1, 1);
   }

   public void update(String var1, int var2) {
      this.event.setEventInfo(var1, var2);
      this.update(this.event);
   }

   public void addProgressListener(ProgressListener var1) {
      Debug.assertion(var1 != null);
      Vector var2 = new Vector();
      if (this.listeners != null) {
         var2.addAll(Arrays.asList((Object[])this.listeners));
      }

      var2.add(var1);
      this.listeners = new ProgressListener[var2.size()];
      var2.copyInto(this.listeners);
   }

   private void update(ProgressEvent var1) {
      if (null != this.listeners) {
         for(int var2 = 0; var2 < this.listeners.length; ++var2) {
            this.listeners[var2].updateProgress(var1);
         }
      }

   }

   public void setProgressProducer(ProgressProducer var1) {
      this.progressProducer = var1;
   }
}
