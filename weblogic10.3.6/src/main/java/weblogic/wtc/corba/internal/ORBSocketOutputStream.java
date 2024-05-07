package weblogic.wtc.corba.internal;

import com.bea.core.jatmi.common.ntrace;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.wtc.gwt.MethodParameters;
import weblogic.wtc.gwt.TuxedoCorbaConnection;
import weblogic.wtc.jatmi.BEAObjectKey;
import weblogic.wtc.jatmi.BindInfo;
import weblogic.wtc.jatmi.CallDescriptor;
import weblogic.wtc.jatmi.ClientInfo;
import weblogic.wtc.jatmi.ObjinfoImpl;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TypedTGIOP;

public final class ORBSocketOutputStream extends OutputStream {
   ORBSocket orbSocket;
   TuxedoCorbaConnection tuxConnection;
   boolean closed;
   byte[] hdrBuf;
   int hdrOffset;
   TypedTGIOP tuxTGIOPBuf;
   int bufOffset;
   static final int GIOPBigEndian = 0;

   public ORBSocketOutputStream(ORBSocket var1, TuxedoCorbaConnection var2) throws IOException {
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORBSocketOutputStream");
      }

      this.orbSocket = var1;
      this.tuxConnection = var2;
      this.closed = false;
      this.hdrBuf = new byte[12];
      this.hdrOffset = 0;
      this.tuxTGIOPBuf = null;
      this.bufOffset = 0;
      if (var3) {
         ntrace.doTrace("]/ORBSocketOutputStream");
      }

   }

   public synchronized void write(int var1) throws IOException {
      boolean var2 = ntrace.isTraceEnabled(8);
      if (var2) {
         ntrace.doTrace("[/ORBSocketOutputStream/write/" + var1);
      }

      if (this.closed) {
         if (var2) {
            ntrace.doTrace("*]/ORBSocketOutputStream/write/10");
         }

         throw new IOException("Output Stream is closed");
      } else {
         if (this.hdrOffset == 12) {
            this.tuxTGIOPBuf.tgiop[this.bufOffset++] = (byte)var1;
            if (this.bufOffset == this.tuxTGIOPBuf.send_size) {
               this.handleCompleteMsg(this.tuxTGIOPBuf);
            }
         } else {
            this.hdrBuf[this.hdrOffset++] = (byte)var1;
            if (this.hdrOffset == 12) {
               this.handleCompleteHdr(this.hdrBuf);
            }
         }

         if (var2) {
            ntrace.doTrace("]/ORBSocketOutputStream/write/50");
         }

      }
   }

   public synchronized void write(byte[] var1, int var2, int var3) throws IOException {
      boolean var4 = ntrace.isTraceEnabled(8);
      if (var4) {
         ntrace.doTrace("[/ORBSocketOutputStream/write/" + var1 + "/" + var2 + "/" + var3);
      }

      if (this.closed) {
         if (var4) {
            ntrace.doTrace("*]/ORBSocketOutputStream/write/10");
         }

         throw new IOException("Output Stream is closed");
      } else {
         if (this.hdrOffset == 12) {
            this.writeMsgBody(var1, var2, var3);
         } else {
            int var5 = 12 - this.hdrOffset;
            int var6 = var5 < var3 ? var5 : var3;
            System.arraycopy(var1, var2, this.hdrBuf, this.hdrOffset, var6);
            this.hdrOffset += var6;
            if (this.hdrOffset == 12) {
               this.handleCompleteHdr(this.hdrBuf);
               if (var6 < var3) {
                  this.writeMsgBody(var1, var2 + var6, var3 - var6);
               }
            }
         }

         if (var4) {
            ntrace.doTrace("]/ORBSocketOutputStream/write/50");
         }

      }
   }

   public void flush() throws IOException {
      if (this.closed) {
         boolean var1 = ntrace.isTraceEnabled(8);
         if (var1) {
            ntrace.doTrace("[/ORBSocketOutputStream/flush");
            ntrace.doTrace("*]/ORBSocketOutputStream/flush/10");
         }

         throw new IOException("Output Stream is closed");
      }
   }

   public void close() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocketOutputStream/close");
      }

      this.closed = true;
      if (var1) {
         ntrace.doTrace("]/ORBSocketOutputStream/close/30");
      }

   }

   private void writeMsgBody(byte[] var1, int var2, int var3) throws IOException {
      boolean var4 = ntrace.isTraceEnabled(8);
      if (var4) {
         ntrace.doTrace("[/ORBSocketOutputStream/writeMsgBody/" + var1 + "/" + var2 + "/" + var3);
      }

      int var5 = this.tuxTGIOPBuf.send_size - this.bufOffset;
      int var6 = var5 < var3 ? var5 : var3;
      System.arraycopy(var1, var2, this.tuxTGIOPBuf.tgiop, this.bufOffset, var6);
      this.bufOffset += var6;
      if (this.bufOffset == this.tuxTGIOPBuf.send_size) {
         this.handleCompleteMsg(this.tuxTGIOPBuf);
         if (var6 < var3) {
            this.write(var1, var2 + var6, var3 - var6);
         }
      }

      if (var4) {
         ntrace.doTrace("]/ORBSocketOutputStream/writeMsgBody/50");
      }

   }

   private void handleCompleteHdr(byte[] var1) throws IOException {
      boolean var2 = ntrace.isTraceEnabled(8);
      if (var2) {
         ntrace.doTrace("[/ORBSocketOutputStream/handleCompleteHdr/" + var1);
      }

      if (var1[0] == 71 && var1[1] == 73 && var1[2] == 79 && var1[3] == 80) {
         int var3;
         int var4;
         int var5;
         int var6;
         if (var1[6] == 0) {
            var3 = var1[8] << 24 & -16777216;
            var4 = var1[9] << 16 & 16711680;
            var5 = var1[10] << 8 & '\uff00';
            var6 = var1[11] << 0 & 255;
         } else {
            var3 = var1[11] << 24 & -16777216;
            var4 = var1[10] << 16 & 16711680;
            var5 = var1[9] << 8 & '\uff00';
            var6 = var1[8] << 0 & 255;
         }

         int var7 = (var3 | var4 | var5 | var6) + 12;
         this.tuxTGIOPBuf = new TypedTGIOP(var7);
         System.arraycopy(var1, 0, this.tuxTGIOPBuf.tgiop, 0, 12);
         this.bufOffset = 12;
         if (var2) {
            ntrace.doTrace("]/ORBSocketOutputStream/handleCompleteHdr/50");
         }

      } else {
         if (var2) {
            ntrace.doTrace("*]/ORBSocketOutputStream/handleCompleteHdr/10");
         }

         throw new IOException("Invalid GIOP message");
      }
   }

   private void handleCompleteMsg(TypedTGIOP var1) throws IOException {
      boolean var2 = ntrace.isTraceEnabled(8);
      if (var2) {
         ntrace.doTrace("[/ORBSocketOutputStream/handleCompleteMsg/" + var1);
      }

      try {
         this.hdrOffset = 0;
         BEAObjectKey var3 = new BEAObjectKey(var1);
         ObjinfoImpl var4 = new ObjinfoImpl(var3, (ClientInfo)null, (BindInfo)null, 0);
         int var5 = 0;
         int var6 = var3.getMsgType();
         if (var6 == 2 || var6 == 5 || var6 == 6 || var6 == 0 && var3.getResponseExpected() == 0) {
            var5 |= 4;
         }

         CallDescriptor var7 = this.tuxConnection.tpMethodReq(var1, var4, (MethodParameters)null, var5);
         if ((var5 & 4) == 0) {
            this.orbSocket.addOutstandingRequest(var7, var3);
         }
      } catch (TPException var8) {
         if (var2) {
            ntrace.doTrace("*]/ORBSocketOutputStream/handleCompleteMsg/20/" + var8);
         }

         throw new IOException("Could not send message via WTC : " + var8);
      } catch (Exception var9) {
         if (var2) {
            ntrace.doTrace("*]/ORBSocketOutputStream/handleCompleteMsg/30/" + var9);
            var9.printStackTrace();
         }

         throw new IOException("Could not send message via WTC : " + var9);
      }

      if (var2) {
         ntrace.doTrace("]/ORBSocketOutputStream/handleCompleteMsg/50");
      }

   }
}
