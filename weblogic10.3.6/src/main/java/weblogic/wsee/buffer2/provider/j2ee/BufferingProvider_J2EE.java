package weblogic.wsee.buffer2.provider.j2ee;

import weblogic.wsee.buffer2.api.common.BufferingManager;
import weblogic.wsee.buffer2.api.j2ee.BufferingManager_J2EE;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.buffer2.spi.BufferingProvider;

public class BufferingProvider_J2EE extends BufferingProvider {
   protected final BufferingManager getBufferingManagerInternal() throws BufferingException {
      return new BufferingManager_J2EE();
   }
}
