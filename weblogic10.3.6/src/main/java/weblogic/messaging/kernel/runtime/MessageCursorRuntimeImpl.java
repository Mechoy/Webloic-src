package weblogic.messaging.kernel.runtime;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.MessageCursorRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.messaging.runtime.CursorDelegate;
import weblogic.messaging.runtime.CursorRuntimeImpl;

public class MessageCursorRuntimeImpl extends CursorRuntimeImpl implements MessageCursorRuntimeMBean {
   public MessageCursorRuntimeImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public MessageCursorRuntimeImpl(String var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public MessageCursorRuntimeImpl(String var1, boolean var2) throws ManagementException {
      super(var1, var2);
   }

   public CompositeData getMessage(String var1, String var2) throws ManagementException {
      CursorDelegate var3 = this.getCursorDelegate(var1);

      try {
         return ((MessageCursorDelegate)var3).getMessage(var2);
      } catch (OpenDataException var5) {
         throw new ManagementException("Failed to get message for message ID " + var2 + " on cursor " + var1, var5);
      }
   }

   public CompositeData getMessage(String var1, Long var2) throws ManagementException {
      CursorDelegate var3 = this.getCursorDelegate(var1);

      try {
         return ((MessageCursorDelegate)var3).getMessage(var2);
      } catch (OpenDataException var5) {
         throw new ManagementException("Failed to get message for message handle " + var2 + " on cursor " + var1, var5);
      }
   }
}
