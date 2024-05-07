package weblogic.jms.backend;

import javax.management.openmbean.CompositeData;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSMessageManagementRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;

public abstract class BEMessageManagementRuntimeDelegate extends JMSMessageCursorRuntimeImpl implements JMSMessageManagementRuntimeMBean {
   private BEMessageManagementImpl delegate;

   public BEMessageManagementRuntimeDelegate(String var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public BEMessageManagementRuntimeDelegate(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public BEMessageManagementRuntimeDelegate(String var1, RuntimeMBean var2, boolean var3, BEQueueImpl var4) throws ManagementException {
      this(var1, var2, var3);
      this.setMessageManagementDelegate(new BEMessageManagementImpl(var1, var4.getKernelQueue(), var4, this));
   }

   protected void setMessageManagementDelegate(BEMessageManagementImpl var1) {
      this.delegate = var1;
   }

   public Long getMessagesMovedCurrentCount() {
      return this.delegate == null ? new Long(0L) : this.delegate.getMessagesMovedCurrentCount();
   }

   public Long getMessagesDeletedCurrentCount() {
      return this.delegate == null ? new Long(0L) : this.delegate.getMessagesDeletedCurrentCount();
   }

   public CompositeData getMessage(String var1) throws ManagementException {
      if (this.delegate == null) {
         throw new UnsupportedOperationException("getMessage(String) not valid for " + this.getClass());
      } else {
         return this.delegate.getMessage(var1);
      }
   }

   public Integer moveMessages(String var1, CompositeData var2) throws ManagementException {
      if (this.delegate == null) {
         throw new UnsupportedOperationException("moveMessages(String, CompositeData) not valid for " + this.getClass());
      } else {
         return this.delegate.moveMessages(var1, var2);
      }
   }

   public Integer moveMessages(String var1, CompositeData var2, Integer var3) throws ManagementException {
      if (this.delegate == null) {
         throw new UnsupportedOperationException("moveMessages(String, CompositeData) not valid for " + this.getClass());
      } else {
         return this.delegate.moveMessages(var1, var2, var3);
      }
   }

   public Integer deleteMessages(String var1) throws ManagementException {
      if (this.delegate == null) {
         throw new UnsupportedOperationException("deleteMessages(String) not valid for " + this.getClass());
      } else {
         return this.delegate.deleteMessages(var1);
      }
   }

   public Void importMessages(CompositeData[] var1, Boolean var2) throws ManagementException {
      if (this.delegate == null) {
         throw new UnsupportedOperationException("importMessages(CompositeData[], Boolean) not valid for " + this.getClass());
      } else {
         return this.delegate.importMessages(var1, var2);
      }
   }

   public String getMessages(String var1, Integer var2) throws ManagementException {
      if (this.delegate == null) {
         throw new UnsupportedOperationException("getMessages(String, Integer) not valid for " + this.getClass());
      } else {
         return this.delegate.getMessages(var1, var2);
      }
   }

   public String getMessages(String var1, Integer var2, Integer var3) throws ManagementException {
      if (this.delegate == null) {
         throw new UnsupportedOperationException("getMessages(String, Integer, Integer) not valid for " + this.getClass());
      } else {
         return this.delegate.getMessages(var1, var2, var3);
      }
   }
}
