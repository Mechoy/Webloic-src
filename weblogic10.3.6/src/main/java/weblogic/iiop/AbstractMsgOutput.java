package weblogic.iiop;

import java.io.IOException;
import org.omg.CORBA.SystemException;
import weblogic.rmi.spi.MsgOutput;

public abstract class AbstractMsgOutput implements MsgOutput {
   protected static final boolean DEBUG = false;
   final IIOPOutputStream delegate;

   public AbstractMsgOutput(IIOPOutputStream var1) {
      this.delegate = var1;
   }

   public final void write(int var1) throws IOException {
      this.delegate.write(var1);
   }

   public final void write(byte[] var1) throws IOException {
      try {
         this.delegate.write(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void write(byte[] var1, int var2, int var3) throws IOException {
      try {
         this.delegate.write(var1, var2, var3);
      } catch (SystemException var5) {
         throw Utils.mapSystemException(var5);
      }
   }

   public final void writeBoolean(boolean var1) throws IOException {
      try {
         this.delegate.writeBoolean(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void writeInt(int var1) throws IOException {
      try {
         this.delegate.writeInt(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void writeShort(int var1) throws IOException {
      try {
         this.delegate.writeShort((short)var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void writeLong(long var1) throws IOException {
      try {
         this.delegate.writeLong(var1);
      } catch (SystemException var4) {
         throw Utils.mapSystemException(var4);
      }
   }

   public final void writeDouble(double var1) throws IOException {
      try {
         this.delegate.writeDouble(var1);
      } catch (SystemException var4) {
         throw Utils.mapSystemException(var4);
      }
   }

   public final void writeFloat(float var1) throws IOException {
      try {
         this.delegate.writeFloat(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void writeByte(int var1) throws IOException {
      try {
         this.delegate.writeByte((byte)var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void close() throws IOException {
      try {
         this.delegate.close();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public void flush() throws IOException {
      this.delegate.flush();
   }

   abstract AbstractMsgInput createMsgInput(IIOPInputStream var1);

   protected final void p(String var1) {
      System.out.println("<AbstractMsgOutput>: " + var1);
   }

   static {
      IIOPService.load();
   }
}
