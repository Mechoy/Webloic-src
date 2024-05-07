package weblogic.messaging.runtime;

import java.util.HashMap;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.CursorRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public abstract class CursorRuntimeImpl extends RuntimeMBeanDelegate implements CursorRuntimeMBean {
   private transient long counter;
   private transient HashMap cursors = new HashMap();

   public CursorRuntimeImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public CursorRuntimeImpl(String var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public CursorRuntimeImpl(String var1, boolean var2) throws ManagementException {
      super(var1, var2);
   }

   public Long getCursorStartPosition(String var1) throws ManagementException {
      CursorDelegate var2 = this.getCursorDelegate(var1);
      return var2.getCursorStartPosition();
   }

   public Long getCursorEndPosition(String var1) throws ManagementException {
      CursorDelegate var2 = this.getCursorDelegate(var1);
      return var2.getCursorEndPosition();
   }

   public CompositeData[] getItems(String var1, Long var2, Integer var3) throws ManagementException {
      CursorDelegate var4 = this.getCursorDelegate(var1);

      try {
         return var4.getItems(var2, var3);
      } catch (OpenDataException var6) {
         throw new ManagementException("Failed to get " + var3 + " items at " + var2 + " on cursor " + var1 + ": " + var6.toString(), var6);
      }
   }

   public CompositeData[] getNext(String var1, Integer var2) throws ManagementException {
      CursorDelegate var3 = this.getCursorDelegate(var1);

      try {
         return var3.getNext(var2);
      } catch (OpenDataException var5) {
         throw new ManagementException("Failed to get next " + var2 + " items " + " on cursor " + var1, var5);
      }
   }

   public CompositeData[] getPrevious(String var1, Integer var2) throws ManagementException {
      CursorDelegate var3 = this.getCursorDelegate(var1);

      try {
         return var3.getPrevious(var2);
      } catch (OpenDataException var5) {
         throw new ManagementException("Failed to get previous " + var2 + " items " + " on cursor " + var1, var5);
      }
   }

   public Long getCursorSize(String var1) throws ManagementException {
      CursorDelegate var2 = this.getCursorDelegate(var1);
      return var2.getCursorSize();
   }

   public Void closeCursor(String var1) throws ManagementException {
      CursorDelegate var2 = this.getCursorDelegate(var1);
      var2.close();
      this.removeCursorDelegate(var1);
      return null;
   }

   public synchronized String addCursorDelegate(CursorDelegate var1) {
      String var2 = this.name + ":" + this.counter++;
      var1.setHandle(var2);
      this.cursors.put(var2, var1);
      return var2;
   }

   protected synchronized CursorDelegate getCursorDelegate(String var1) throws ManagementException {
      CursorDelegate var2 = (CursorDelegate)this.cursors.get(var1);
      if (var2 == null) {
         throw new ManagementException("no cursor matching handle " + var1);
      } else {
         return var2;
      }
   }

   public synchronized CursorDelegate removeCursorDelegate(String var1) {
      return (CursorDelegate)this.cursors.remove(var1);
   }
}
