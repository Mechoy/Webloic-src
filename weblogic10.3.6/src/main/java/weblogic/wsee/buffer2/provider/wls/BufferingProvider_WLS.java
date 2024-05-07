package weblogic.wsee.buffer2.provider.wls;

import weblogic.wsee.buffer2.api.common.BufferingManager;
import weblogic.wsee.buffer2.api.wls.BufferingManager_WLS;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.buffer2.spi.BufferingProvider;

public class BufferingProvider_WLS extends BufferingProvider {
   protected final <T extends BufferingManager> T getBufferingManagerInternal() throws BufferingException {
      return new BufferingManager_WLS();
   }
}
