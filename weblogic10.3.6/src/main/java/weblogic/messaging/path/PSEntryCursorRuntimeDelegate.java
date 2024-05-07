package weblogic.messaging.path;

import javax.management.openmbean.CompositeData;
import weblogic.management.ManagementException;
import weblogic.management.runtime.PSEntryCursorRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.messaging.runtime.ArrayCursorRuntimeDelegate;
import weblogic.messaging.runtime.CursorDelegate;

public class PSEntryCursorRuntimeDelegate extends ArrayCursorRuntimeDelegate implements PSEntryCursorRuntimeMBean {
   public PSEntryCursorRuntimeDelegate(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public Void remove(String var1, Integer var2) throws ManagementException {
      CursorDelegate var3 = this.getCursorDelegate(var1);
      ((PSEntryCursorDelegate)var3).remove(var2);
      return null;
   }

   public Void update(String var1, Integer var2, CompositeData var3) throws ManagementException {
      CursorDelegate var4 = this.getCursorDelegate(var1);
      ((PSEntryCursorDelegate)var4).update(var2, var3);
      return null;
   }

   public CompositeData getMember(String var1, Integer var2) throws ManagementException {
      CursorDelegate var3 = this.getCursorDelegate(var1);
      return ((PSEntryCursorDelegate)var3).getMember(var2);
   }
}
