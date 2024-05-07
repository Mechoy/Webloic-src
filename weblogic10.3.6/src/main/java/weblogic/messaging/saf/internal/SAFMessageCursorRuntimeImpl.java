package weblogic.messaging.saf.internal;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.SAFMessageCursorRuntimeMBean;
import weblogic.messaging.kernel.runtime.MessageCursorRuntimeImpl;
import weblogic.messaging.runtime.CursorDelegate;

public class SAFMessageCursorRuntimeImpl extends MessageCursorRuntimeImpl implements SAFMessageCursorRuntimeMBean {
   static final long serialVersionUID = 7182873948110830340L;

   public SAFMessageCursorRuntimeImpl(String var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public Long sort(String var1, Long var2, String[] var3, Boolean[] var4) throws ManagementException {
      CursorDelegate var5 = this.getCursorDelegate(var1);
      return new Long(((SAFMessageCursorDelegate)var5).sort(var2, var3, var4));
   }
}
