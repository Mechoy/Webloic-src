package weblogic.jms.backend;

import weblogic.application.ModuleException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSMessageCursorRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.messaging.kernel.runtime.MessageCursorRuntimeImpl;
import weblogic.messaging.runtime.CursorDelegate;

public class JMSMessageCursorRuntimeImpl extends MessageCursorRuntimeImpl implements JMSMessageCursorRuntimeMBean {
   public JMSMessageCursorRuntimeImpl(String var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public JMSMessageCursorRuntimeImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public JMSMessageCursorRuntimeImpl(String var1, boolean var2) throws ManagementException {
      super(var1, var2);
   }

   public Long sort(String var1, Long var2, String[] var3, Boolean[] var4) throws ManagementException {
      CursorDelegate var5 = this.getCursorDelegate(var1);

      try {
         return new Long(((JMSMessageCursorDelegate)var5).sort(var2, var3, var4));
      } catch (ModuleException var7) {
         throw new ManagementException("Error while sorting cursor " + var1, var7);
      }
   }
}
