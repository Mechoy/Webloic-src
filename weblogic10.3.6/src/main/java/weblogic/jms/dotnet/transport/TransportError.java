package weblogic.jms.dotnet.transport;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.jms.JMSException;

public final class TransportError implements MarshalWritable, MarshalReadable {
   public static final int TYPE_CODE = 20001;
   private Throwable throwable;
   private boolean isRemoteException;
   private ArrayList exceptionArray;

   public TransportError() {
   }

   public TransportError(Throwable var1) {
      this.throwable = var1;
      this.isRemoteException = false;
   }

   public TransportError(Throwable var1, boolean var2) {
      this.throwable = var1;
      this.isRemoteException = false;
   }

   public TransportError(String var1, boolean var2) {
      this.throwable = new RuntimeException(var1);
      this.isRemoteException = false;
   }

   public boolean isPeerGone() {
      throw new AssertionError("not implemented");
   }

   public int getMarshalTypeCode() {
      return 20001;
   }

   public ArrayList getExceptionNames() {
      return this.exceptionArray;
   }

   public boolean IsRemoteException() {
      return this.isRemoteException;
   }

   public void marshal(MarshalWriter var1) {
      var1.writeUnsignedByte(1);
      var1.writeUnsignedByte(0);
      if (this.throwable != null) {
         ArrayList var2 = new ArrayList(5);
         var2.add(this.throwable.getClass().getName());
         if (this.throwable instanceof JMSException) {
            for(Object var3 = ((JMSException)this.throwable).getLinkedException(); var3 != null; var3 = ((Throwable)var3).getCause()) {
               var2.add(var3.getClass().getName());
            }
         }

         if (var2.size() == 1) {
            for(Throwable var4 = this.throwable.getCause(); var4 != null; var4 = var4.getCause()) {
               var2.add(var4.getClass().getName());
            }
         }

         var1.writeInt(var2.size() + 1);

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            var1.writeString((String)((String)var2.get(var5)));
         }

         ByteArrayOutputStream var6 = new ByteArrayOutputStream();
         this.throwable.printStackTrace(new PrintStream(var6));
         var1.writeString(var6.toString());
      } else {
         var1.writeString("null");
      }

   }

   public void unmarshal(MarshalReader var1) {
      int var2 = var1.read();

      while((var1.readByte() & 1) != 0) {
      }

      this.isRemoteException = true;
      int var3 = var1.readInt();

      for(int var4 = 0; var4 < var3 - 1; ++var4) {
         this.exceptionArray.add(var1.readString());
      }

      this.throwable = new Throwable(var1.readString());
   }

   public void printStackTrace() {
      if (this.throwable != null) {
         this.throwable.printStackTrace();
      }

   }

   public String toString() {
      return "TransportError:<" + this.throwable + ">";
   }
}
