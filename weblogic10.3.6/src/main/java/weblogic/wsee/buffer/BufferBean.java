package weblogic.wsee.buffer;

import javax.jms.Message;

public class BufferBean extends BaseDispatchMDB {
   protected void onMessage(String var1, Message var2) throws Exception {
      BufferManager.instance().dispatchBufferedMessage(var1, var2);
   }
}
