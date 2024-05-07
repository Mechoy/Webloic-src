package weblogic.wsee.buffer2.spi;

import weblogic.wsee.buffer2.api.common.BufferingManager;
import weblogic.wsee.buffer2.exception.BufferingException;

public abstract class BufferingProvider {
   private static volatile BufferingManager _theBufferingManager;

   public final BufferingManager getBufferingManager() throws BufferingException {
      if (_theBufferingManager == null) {
         Class var1 = BufferingProvider.class;
         synchronized(BufferingProvider.class) {
            if (_theBufferingManager == null) {
               _theBufferingManager = this.getBufferingManagerInternal();
            }
         }
      }

      return _theBufferingManager;
   }

   protected abstract <T extends BufferingManager> T getBufferingManagerInternal() throws BufferingException;
}
