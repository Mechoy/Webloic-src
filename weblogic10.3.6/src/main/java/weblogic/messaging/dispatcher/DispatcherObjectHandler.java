package weblogic.messaging.dispatcher;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;
import weblogic.jms.common.JMSDebug;
import weblogic.messaging.ID;

public abstract class DispatcherObjectHandler {
   protected int MASK;

   protected DispatcherObjectHandler(int var1) {
      this.MASK = var1;
   }

   public void writeRequest(ObjectOutput var1, Request var2) throws IOException {
      var1.writeInt(var2.getMethodId());
      var2.writeShortened(var1);
   }

   public Request readRequest(int var1, ObjectInput var2, ID var3) throws ClassNotFoundException, IOException {
      Request var4 = this.instantiate(var1 & this.MASK);

      try {
         var4.readExternal(var2);
      } catch (IOException var6) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            debugWireOperation("RecvReq  ", (byte)15, var4, -1, var3, (Response)null, var6);
         }

         throw var6;
      } catch (ClassNotFoundException var7) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            debugWireOperation("RecvReq  ", (byte)15, var4, -1, var3, (Response)null, var7);
         }

         throw var7;
      }

      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         debugWireOperation("RecvReq  ", (byte)15, var4, -1, var3, (Response)null, (Throwable)null);
      }

      var4.setMethodId(var1);
      var4.setInvocableId(var3);
      return var4;
   }

   static void debugWireOperation(String var0, byte var1, Request var2, int var3, ID var4, Response var5, Throwable var6) {
      String var7;
      if (var3 != 0 && var3 != -1) {
         var7 = "workId " + Integer.toString(var3) + " ";
      } else {
         var7 = "";
      }

      String var8;
      if (15 == var1) {
         var8 = "";
      } else {
         var8 = ", requestType x" + Integer.toHexString(var1).toUpperCase();
      }

      String var9;
      if (var5 == null) {
         var9 = "";
      } else {
         var9 = ", Response " + var5.getClass().getName();
      }

      String var10 = var0 + var7 + var8 + ", ID " + var4 + ", " + var2.getClass().getName() + var9;
      if (var6 == null) {
         JMSDebug.JMSDispatcher.debug(var10);
      } else {
         JMSDebug.JMSDispatcher.debug(var10, var6);
      }

   }

   public void writeResponse(ObjectOutput var1, Request var2, Response var3) throws IOException {
      try {
         var1.writeInt(var2.getMethodId() & this.MASK);
         var3.writeExternal(var1);
      } catch (IOException var5) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            debugWireOperation("SendResp ", (byte)15, var2, -1, var2.getInvocableId(), var3, var5);
         }

         throw var5;
      }

      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         debugWireOperation("SendResp ", (byte)15, var2, -1, var2.getInvocableId(), var3, (Throwable)null);
      }

   }

   public Response readResponse(ObjectInput var1, Request var2) throws ClassNotFoundException, IOException {
      int var3 = var1.readInt();
      if (var3 != (var2.getMethodId() & this.MASK)) {
         StreamCorruptedException var8 = new StreamCorruptedException("Unexpected response for " + var2.getClass().getName() + " : " + var3);
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            debugWireOperation("RecvResp ", (byte)15, var2, -1, var2.getInvocableId(), (Response)null, var8);
         }

         throw var8;
      } else {
         Response var4 = var2.createResponse();

         try {
            var4.readExternal(var1);
         } catch (IOException var6) {
            if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
               debugWireOperation("RecvResp ", (byte)15, var2, -1, var2.getInvocableId(), var4, var6);
            }

            throw var6;
         } catch (ClassNotFoundException var7) {
            if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
               debugWireOperation("RecvResp ", (byte)15, var2, -1, var2.getInvocableId(), var4, var7);
            }

            throw var7;
         }

         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            debugWireOperation("RecvResp ", (byte)15, var2, -1, var2.getInvocableId(), var4, (Throwable)null);
         }

         return var4;
      }
   }

   protected Request instantiate(int var1) throws IOException {
      throw new StreamCorruptedException("Unknown typecode: " + var1);
   }

   static DispatcherObjectHandler load(String var0) {
      try {
         return (DispatcherObjectHandler)Class.forName(var0).newInstance();
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      } catch (InstantiationException var3) {
         throw new AssertionError(var3);
      } catch (IllegalAccessException var4) {
         throw new AssertionError(var4);
      }
   }
}
