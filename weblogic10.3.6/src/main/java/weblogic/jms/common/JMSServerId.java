package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import weblogic.messaging.dispatcher.DispatcherId;

public final class JMSServerId implements Externalizable, Comparable {
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   static final long serialVersionUID = 7779784416562889644L;
   private JMSID id;
   private DispatcherId dispatcherId = null;

   public JMSServerId(JMSID var1, DispatcherId var2) {
      this.id = var1;
      this.dispatcherId = var2;
   }

   public String toString() {
      return this.id.toString() + ":" + this.dispatcherId.toString();
   }

   public JMSServerId(JMSServerId var1) {
      this.id = var1.getId();
      this.dispatcherId = var1.getDispatcherId();
   }

   public JMSID getId() {
      return this.id;
   }

   public DispatcherId getDispatcherId() {
      return this.dispatcherId;
   }

   public long getTimestamp() {
      return this.id.getTimestamp();
   }

   public int getSeed() {
      return this.id.getSeed();
   }

   public JMSServerId() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(2);
      this.id.writeExternal(var1);
      this.dispatcherId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1 && var2 != 2) {
         throw JMSUtilities.versionIOException(var2, 1, 2);
      } else {
         this.id = new JMSID();
         this.id.readExternal(var1);
         if (var2 == 1) {
            this.consumeJVMID(var1);
         } else {
            this.dispatcherId = new DispatcherId();
            this.dispatcherId.readExternal(var1);
         }

      }
   }

   private void consumeJVMID(ObjectInput var1) throws ClassNotFoundException, IOException {
      try {
         Class var2 = Class.forName("weblogic.rjvm.JVMID");
         Object var8 = var2.newInstance();
         Method var4 = var2.getMethod("readExternal", ObjectInput.class);
         var4.invoke(var8, var1);
      } catch (InvocationTargetException var5) {
         Throwable var3 = var5.getTargetException();
         if (var3 instanceof IOException) {
            throw (IOException)var3;
         } else {
            throw new AssertionError(var5);
         }
      } catch (ClassNotFoundException var6) {
         throw var6;
      } catch (Exception var7) {
         throw new AssertionError(var7);
      }
   }

   public boolean equals(Object var1) {
      return !this.id.equals(((JMSServerId)var1).getId()) ? false : this.dispatcherId.equals(((JMSServerId)var1).getDispatcherId());
   }

   public int compareTo(Object var1) {
      JMSServerId var2 = (JMSServerId)var1;
      int var3 = this.id.compareTo(var2.id);
      if (var3 < 0) {
         return -1;
      } else {
         return var3 > 0 ? 1 : this.dispatcherId.compareTo(var2.dispatcherId);
      }
   }

   public int hashCode() {
      return this.id.hashCode() ^ this.dispatcherId.hashCode();
   }
}
