package weblogic.upgrade.jms;

import javax.jms.JMSException;

public interface StoreReader {
   Record recover() throws JMSException;

   void close();

   void reOpen() throws JMSException;

   boolean requiresUpgrade();

   boolean alreadyUpgraded();

   public static final class Record {
      private long handle;
      private int state;
      private Object object;

      Record(long var1, int var3, Object var4) {
         this.handle = var1;
         this.state = var3;
         this.object = var4;
      }

      public long getHandle() {
         return this.handle;
      }

      public int getState() {
         return this.state;
      }

      public Object getObject() {
         return this.object;
      }
   }
}
