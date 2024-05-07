package weblogic.logging;

import com.bea.logging.LogBufferHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import weblogic.management.configuration.LogMBean;

public final class LogBufferHandlerInitializer {
   public static void initServerLogBufferHandler(LogMBean var0) {
      int var1 = var0.getMemoryBufferSize();
      LogBufferHandler.getInstance().setBufferSizeLimit(var1);
      var0.addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1) {
            if (var1.getPropertyName().equals("MemoryBufferSize") && var1.getNewValue() instanceof Number) {
               int var2 = ((Number)var1.getNewValue()).intValue();
               LogBufferHandler.getInstance().setBufferSizeLimit(var2);
            }

         }
      });
   }
}
