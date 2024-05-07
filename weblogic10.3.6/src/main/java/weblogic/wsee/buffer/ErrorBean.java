package weblogic.wsee.buffer;

import javax.jms.Message;

public class ErrorBean extends BaseDispatchMDB {
   protected void onMessage(String var1, Message var2) throws Exception {
      BufferManager.instance().dispatchErrorMessage(var1, var2);
   }
}
