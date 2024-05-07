package weblogic.wtc.corba.internal;

import com.bea.core.jatmi.common.Utilities;
import com.bea.core.jatmi.common.ntrace;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import weblogic.wtc.gwt.TuxedoCorbaConnection;
import weblogic.wtc.jatmi.BEAObjectKey;
import weblogic.wtc.jatmi.CallDescriptor;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.ReqOid;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPReplyException;
import weblogic.wtc.jatmi.TypedBuffer;
import weblogic.wtc.jatmi.TypedTGIOP;

public final class ORBSocketInputStream extends InputStream {
   ORBSocket orbSocket;
   TuxedoCorbaConnection tuxConnection;
   boolean closed;
   int bufLen;
   int bufOffset;
   byte[] tuxBuf;
   Reply tuxReply;
   TypedTGIOP tuxReplyBuf;

   public ORBSocketInputStream(ORBSocket var1, TuxedoCorbaConnection var2) throws IOException {
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORBSocketInputStream");
      }

      this.orbSocket = var1;
      this.tuxConnection = var2;
      this.closed = false;
      this.bufOffset = 0;
      this.bufLen = 0;
      this.tuxBuf = null;
      this.tuxReply = null;
      this.tuxReplyBuf = null;
      if (var3) {
         ntrace.doTrace("]/ORBSocketInputStream");
      }

   }

   public synchronized int read() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocketInputStream/read");
      }

      if (this.closed) {
         if (var1) {
            ntrace.doTrace("*]/ORBSocketInputStream/read/10");
         }

         throw new IOException("Input stream is closed");
      } else {
         if (this.bufLen == 0) {
            this.getNextReply();
         }

         if (this.bufLen == 0) {
            if (var1) {
               ntrace.doTrace("*]/ORBSocketInputStream/read/20");
            }

            throw new IOException("No reply found");
         } else {
            byte var2 = this.tuxBuf[this.bufOffset++];
            if (this.bufOffset == this.bufLen) {
               this.bufOffset = 0;
               this.bufLen = 0;
               this.tuxBuf = null;
               this.tuxReply = null;
               this.tuxReplyBuf = null;
            }

            if (var1) {
               ntrace.doTrace("]/ORBSocketInputStream/read/30" + var2);
            }

            return var2;
         }
      }
   }

   public synchronized int read(byte[] var1, int var2, int var3) throws IOException {
      boolean var4 = ntrace.isTraceEnabled(8);
      if (var4) {
         ntrace.doTrace("[/ORBSocketInputStream/read/" + var1 + "/" + var2 + "/" + var3);
      }

      if (this.closed) {
         if (var4) {
            ntrace.doTrace("*]/ORBSocketInputStream/read/10");
         }

         throw new IOException("Input Stream is closed");
      } else if (var3 == 0) {
         return 0;
      } else {
         if (this.bufLen == 0) {
            this.getNextReply();
         }

         if (this.bufLen == 0) {
            if (var4) {
               ntrace.doTrace("*]/ORBSocketInputStream/read/20");
            }

            throw new IOException("No reply found");
         } else {
            int var5 = this.bufLen - this.bufOffset;
            int var6 = var5 < var3 ? var5 : var3;
            System.arraycopy(this.tuxBuf, this.bufOffset, var1, var2, var6);
            this.bufOffset += var6;
            if (this.bufOffset == this.bufLen) {
               this.bufOffset = 0;
               this.bufLen = 0;
               this.tuxBuf = null;
               this.tuxReply = null;
               this.tuxReplyBuf = null;
            }

            if (var4) {
               ntrace.doTrace("]/ORBSocketInputStream/read/30/" + var6);
            }

            return var6;
         }
      }
   }

   public int available() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocketInputStream/available");
      }

      if (this.closed) {
         if (var1) {
            ntrace.doTrace("*]/ORBSocketInputStream/available/10");
         }

         throw new IOException("Input Stream is closed");
      } else {
         if (var1) {
            ntrace.doTrace("]/ORBSocketInputStream/available/30/" + (this.bufLen - this.bufOffset));
         }

         return this.bufLen - this.bufOffset;
      }
   }

   public void close() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocketInputStream/close");
      }

      this.closed = true;
      if (var1) {
         ntrace.doTrace("]/ORBSocketInputStream/close/30");
      }

   }

   private void getNextReply() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocketInputStream/getNextReply");
      }

      short var2 = 128;

      try {
         this.tuxReply = this.tuxConnection.tpgetrply((CallDescriptor)null, var2);
         CallDescriptor var3 = this.tuxReply.getCallDescriptor();
         BEAObjectKey var4 = this.orbSocket.removeOutstandingRequest(var3);
         TypedBuffer var5 = this.tuxReply.getReplyBuffer();
         if (var5.getHintIndex() != 10 && var1) {
            ntrace.doTrace("[/ORBSocketInputStream/getNextReply/10/" + var5.getHintIndex() + "/" + var3 + "/" + var4);
         }

         this.tuxReplyBuf = (TypedTGIOP)var5;
         this.tuxBuf = this.tuxReplyBuf.tgiop;
         this.bufLen = this.tuxReplyBuf.send_size;
         this.bufOffset = 0;
      } catch (TPReplyException var6) {
         if (var1) {
            ntrace.doTrace("*]/ORBSocketInputStream/getNextReply/30/" + var6);
         }

         this.mapTPExceptionToCorbaException("Could not get reply via WTC : " + var6, var6.gettperrno(), var6.getExceptionReply() != null ? var6.getExceptionReply().getCallDescriptor() : null);
      } catch (TPException var7) {
         if (var1) {
            ntrace.doTrace("*]/ORBSocketInputStream/getNextReply/40/" + var7);
         }

         this.mapTPExceptionToCorbaException("Could not get reply via WTC : " + var7, var7.gettperrno(), var7.getReplyRtn() != null ? var7.getReplyRtn().getCallDescriptor() : null);
      }

      if (var1) {
         ntrace.doTrace("]/ORBSocketInputStream/getNextReply/60");
      }

   }

   private void mapTPExceptionToCorbaException(String var1, int var2, CallDescriptor var3) throws IOException {
      boolean var4 = ntrace.isTraceEnabled(8);
      if (var4) {
         ntrace.doTrace("[/ORBSocketInputStream/mapTPExceptionToCorbaException/" + var1 + "/" + var2 + "/" + var3);
      }

      boolean var5 = false;

      try {
         if (((ReqOid)var3).getXID() != null) {
            var5 = true;
         } else {
            CallDescriptor var6 = this.orbSocket.getOriginalCallDescriptor(var3);
            if (var6 != null && ((ReqOid)var6).getXID() != null) {
               var5 = true;
            }
         }
      } catch (Exception var11) {
      }

      String var13;
      try {
         switch (var2) {
            case 1:
               var13 = "IDL:omg.org/CORBA/TRANSACTION_ROLLEDBACK:1.0";
               break;
            case 2:
            case 4:
            case 7:
            case 9:
            case 11:
            case 12:
            case 16:
            case 19:
            case 20:
            case 21:
            case 22:
            case 24:
            case 25:
            default:
               var13 = "IDL:omg.org/CORBA/INTERNAL:1.0";
               break;
            case 3:
               var13 = "IDL:omg.org/CORBA/TRANSIENT:1.0";
               break;
            case 5:
               var13 = "IDL:omg.org/CORBA/NO_RESOURCE:1.0";
               break;
            case 6:
               var13 = "IDL:omg.org/CORBA/NO_IMPLEMENT:1.0";
               break;
            case 8:
               var13 = "IDL:omg.org/CORBA/NO_PERMISSION:1.0";
               break;
            case 10:
               if (var5) {
                  var13 = "IDL:omg.org/CORBA/TRANSACTION_ROLLEDBACK:1.0";
               } else {
                  var13 = "IDL:omg.org/CORBA/OBJ_ADAPTER:1.0";
               }
               break;
            case 13:
               if (var5) {
                  var13 = "IDL:omg.org/CORBA/TRANSACTION_ROLLEDBACK:1.0";
               } else {
                  var13 = "IDL:omg.org/CORBA/NO_RESPONSE:1.0";
               }
               break;
            case 14:
               var13 = "IDL:omg.org/CORBA/INVALIDTRANSACTION:1.0";
               break;
            case 15:
               var13 = "IDL:omg.org/CORBA/TRANSIENT:1.0";
               break;
            case 17:
            case 18:
               var13 = "IDL:omg.org/CORBA/MARSHAL:1.0";
               break;
            case 23:
               var13 = "IDL:omg.org/CORBA/BAD_INV_ORDER:1.0";
         }

         ByteArrayOutputStream var7 = new ByteArrayOutputStream(100);
         DataOutputStream var8 = new DataOutputStream(var7);
         BEAObjectKey var9 = this.orbSocket.removeOutstandingRequest(var3);
         var8.write(71);
         var8.write(73);
         var8.write(79);
         var8.write(80);
         if (var9 != null) {
            var8.write(var9.getMajorVersion());
            var8.write(var9.getMinorVersion());
         } else {
            var8.write(1);
            var8.write(0);
         }

         var8.write(0);
         if (var9 != null && var9.getMsgType() == 0) {
            var8.write(1);
            var8.writeInt(0);
            var8.writeInt(0);
            var8.writeInt(var9.getRequestId());
            var8.writeInt(2);
            this.writeString(var8, var13);
            var8.writeInt(0);
            var8.writeInt(2);
         } else if (var9 != null && var9.getMsgType() == 3) {
            var8.write(4);
            var8.writeInt(0);
            var8.writeInt(var9.getRequestId());
            if (var9.getMajorVersion() == 1 && var9.getMinorVersion() <= 1) {
               var8.writeInt(0);
            } else {
               var8.writeInt(4);
               this.writeString(var8, var13);
               var8.writeInt(0);
               var8.writeInt(2);
            }
         } else {
            var8.write(6);
            var8.writeInt(0);
         }

         this.tuxBuf = var7.toByteArray();
         this.bufLen = var7.size();
         this.bufOffset = 0;
         int var10 = this.bufLen - 12;
         this.tuxBuf[8] = (byte)(var10 >>> 24 & 255);
         this.tuxBuf[9] = (byte)(var10 >>> 16 & 255);
         this.tuxBuf[10] = (byte)(var10 >>> 8 & 255);
         this.tuxBuf[11] = (byte)(var10 >>> 0 & 255);
      } catch (Exception var12) {
         if (var4) {
            ntrace.doTrace("*]/ORBSocketInputStream/mapTPExceptionToCorbaException/50/" + var12);
         }

         throw new IOException("Error creating CORBA System exception: " + var12);
      }

      if (var4) {
         ntrace.doTrace("]/ORBSocketInputStream/mapTPExceptionToCorbaException/60/" + var13 + "/" + this.bufLen + "/" + this.tuxBuf);
      }

   }

   private void writeString(DataOutputStream var1, String var2) throws IOException {
      byte[] var3 = Utilities.getEncBytes(var2);
      int var4 = var3.length + 1;
      var1.writeInt(var4);
      var1.write(var3);
      var1.write(0);
      int var5 = var4 % 4;
      if (var5 > 0) {
         for(int var6 = 0; var6 < 4 - var5; ++var6) {
            var1.write(0);
         }
      }

   }
}
