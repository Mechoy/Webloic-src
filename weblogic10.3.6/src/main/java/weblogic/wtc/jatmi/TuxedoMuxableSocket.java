package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import weblogic.socket.MuxableSocket;
import weblogic.socket.NIOConnection;
import weblogic.socket.SocketInfo;
import weblogic.socket.SocketMuxer;
import weblogic.wtc.WTCLogger;

public final class TuxedoMuxableSocket implements MuxableSocket {
   private rdsession myTuxReadSession;
   private Socket in;
   private SocketMuxer myMuxer;
   private InputStream in_stream;
   private int in_timeout;
   private boolean opened = false;
   private MuxableSocket filter = null;
   private int elevel = 0;
   private tplle lle = null;
   private int myProtocol = 10;
   private static final int INITIAL_SIZE = 1000;
   private byte[] myTuxBuf;
   private int myOffset = 0;
   private int myDecryptOffset = 0;
   private ByteArrayInputStream myInputStream;
   private DataInputStream myDataStream;
   private SocketInfo sockInfo;
   private boolean canDispatch;

   public TuxedoMuxableSocket() {
      this.myTuxBuf = new byte[1000];
      this.myInputStream = new ByteArrayInputStream(this.myTuxBuf);
      this.myInputStream.mark(0);
      this.myDataStream = new DataInputStream(this.myInputStream);
      this.myMuxer = SocketMuxer.getMuxer();

      try {
         this.myMuxer.register(this);
      } catch (IOException var2) {
      }

      this.setSocketFilter(this);
   }

   public DataInputStream getInputStream() {
      return this.myDataStream;
   }

   public TuxedoMuxableSocket(Socket var1, boolean var2) throws IOException, SocketException {
      this.in = var1;
      this.in_stream = this.in.getInputStream();
      this.in_timeout = this.in.getSoTimeout();
      this.opened = true;
      this.canDispatch = false;
      this.myTuxBuf = new byte[1000];
      this.myInputStream = new ByteArrayInputStream(this.myTuxBuf);
      this.myInputStream.mark(0);
      this.myDataStream = new DataInputStream(this.myInputStream);
      this.myMuxer = SocketMuxer.getMuxer();
      if (!var2) {
         this.canDispatch = true;
         this.myMuxer.register(this);
      }

      this.setSocketFilter(this);
   }

   public void setRecvSession(rdsession var1) {
      this.myTuxReadSession = var1;
      synchronized(this) {
         this.canDispatch = true;
      }

      this.myMuxer.read(this.getSocketFilter());
   }

   public byte[] getBuffer() {
      if (this.myOffset >= this.myTuxBuf.length) {
         byte[] var1 = new byte[this.myTuxBuf.length + 1000];
         System.arraycopy(this.myTuxBuf, 0, var1, 0, this.myOffset);
         this.myTuxBuf = var1;
         this.myInputStream = new ByteArrayInputStream(this.myTuxBuf);
         this.myInputStream.mark(0);
         this.myDataStream = new DataInputStream(this.myInputStream);
      }

      return this.myTuxBuf;
   }

   public int getBufferOffset() {
      return this.myOffset;
   }

   public void setBufferOffset(int var1) {
      this.myOffset = var1;
   }

   public void incrementBufferOffset(int var1) {
      this.myOffset += var1;
   }

   public boolean isMessageComplete() {
      return true;
   }

   public void dispatch() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/TuxedoMuxableSocket/dispatch/");
      }

      int var2 = 0;
      if (var1) {
         ntrace.doTrace("/TuxedoMuxableSocket/dispatch/myOffset=" + this.myOffset + "/myDecryptOffset=" + this.myDecryptOffset + "/myProtocol=" + this.myProtocol);
      }

      synchronized(this) {
         if (!this.canDispatch) {
            if (var1) {
               ntrace.doTrace("/TuxedoMuxableSocket/dispatch/no dispatch yet");
            }

            this.myInputStream.reset();
            this.myMuxer.read(this.getSocketFilter());
            this.notify();
            return;
         }
      }

      if (this.myOffset < 32) {
         this.myMuxer.read(this.getSocketFilter());
         if (var1) {
            ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/10");
         }

      } else {
         if (this.lle != null && this.elevel != 0 && this.myDecryptOffset < this.myOffset) {
            if (this.lle.crypGetRBuf(this.myTuxBuf, this.myDecryptOffset, this.myOffset - this.myDecryptOffset) != 0) {
               this.myMuxer.closeSocket(this.getSocketFilter());
               this.opened = false;
               if (this.myTuxReadSession != null) {
                  this.myTuxReadSession.connectionHasTerminated();
               }

               if (var1) {
                  ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/20");
               }

               return;
            }

            this.myDecryptOffset = this.myOffset;
         }

         this.myInputStream.reset();

         while(var2 <= this.myOffset) {
            int var3 = this.myOffset - var2;
            int var5;
            if (var3 < 32) {
               for(var5 = var2; var5 < this.myOffset; ++var5) {
                  this.myTuxBuf[var5 - var2] = this.myTuxBuf[var5];
               }

               this.myOffset = var3;
               this.myDecryptOffset = var3;
               this.myMuxer.read(this.getSocketFilter());
               if (var1) {
                  ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/30/");
               }

               return;
            }

            int var4;
            if (this.myProtocol <= 13) {
               if (com.bea.core.jatmi.common.Utilities.baReadInt(this.myTuxBuf, 0) != 1938831426) {
                  this.myMuxer.closeSocket(this.getSocketFilter());
                  this.opened = false;
                  if (this.myTuxReadSession != null) {
                     this.myTuxReadSession.connectionHasTerminated();
                  }

                  if (var1) {
                     ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/35/Invalid MagicNumber");
                  }

                  WTCLogger.logErrorInvalidMagicNumber();
                  return;
               }

               var4 = com.bea.core.jatmi.common.Utilities.baReadInt(this.myTuxBuf, var2 + 28);
            } else {
               var4 = com.bea.core.jatmi.common.Utilities.baReadInt(this.myTuxBuf, var2 + 16);
            }

            if (var4 < 32) {
               this.myMuxer.closeSocket(this.getSocketFilter());
               this.opened = false;
               if (this.myTuxReadSession != null) {
                  this.myTuxReadSession.connectionHasTerminated();
               }

               if (var1) {
                  ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/40/invalid size=" + var4);
               }

               return;
            }

            if (var1) {
               ntrace.doTrace("/TuxedoMuxableSocket/dispatch/size=" + var4);
            }

            if (var4 > this.myTuxBuf.length) {
               byte[] var16 = new byte[var4];
               System.arraycopy(this.myTuxBuf, var2, var16, 0, var3);
               this.myTuxBuf = var16;
               this.myInputStream = new ByteArrayInputStream(this.myTuxBuf);
               this.myInputStream.mark(0);
               this.myDataStream = new DataInputStream(this.myInputStream);
               this.myOffset = var3;
               this.myDecryptOffset = var3;
               this.myMuxer.read(this.getSocketFilter());
               if (var1) {
                  ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/50");
               }

               return;
            }

            if (var4 > var3) {
               if (var2 != 0) {
                  for(var5 = var2; var5 < this.myOffset; ++var5) {
                     this.myTuxBuf[var5 - var2] = this.myTuxBuf[var5];
                  }

                  this.myOffset = var3;
                  this.myDecryptOffset = var3;
               }

               this.myMuxer.read(this.getSocketFilter());
               if (var1) {
                  ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/60");
               }

               return;
            }

            tfmh var6 = this.myTuxReadSession.allocTfmh();
            if (ntrace.isTraceEnabled(64)) {
               var6.dumpUData(true);
            }

            int var10;
            try {
               if (this.myProtocol <= 13) {
                  var10 = var6.read_dom_65_tfmh(this.myDataStream, this.myProtocol);
               } else {
                  var10 = var6.read_tfmh(this.myDataStream);
               }
            } catch (IOException var14) {
               this.myMuxer.closeSocket(this.getSocketFilter());
               this.opened = false;
               if (this.myTuxReadSession != null) {
                  this.myTuxReadSession.connectionHasTerminated();
               }

               if (var1) {
                  ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/70/" + var14);
               }

               return;
            }

            switch (var10) {
               case -2:
                  WTCLogger.logDroppedMessage();
                  if (var1) {
                     ntrace.doTrace("/TuxedoMuxableSocket/dispatch/dropped message");
                  }

                  TdomTcb var7;
                  int var11;
                  if (var6.tdom != null && (var7 = (TdomTcb)var6.tdom.body) != null) {
                     var11 = var7.get_reqid();
                  } else {
                     var11 = 0;
                  }

                  TdomTcb var12 = new TdomTcb(3, var11, 0, (String)null);
                  var12.set_diagnostic(12);
                  tfmh var13 = new tfmh(1);
                  var13.tdom = new tcm((short)7, var12);
                  var6 = var13;
               case 0:
                  this.myTuxReadSession.dispatch(var6);
                  var2 += var4;
                  break;
               case -1:
               default:
                  this.myMuxer.closeSocket(this.getSocketFilter());
                  this.opened = false;
                  if (this.myTuxReadSession != null) {
                     this.myTuxReadSession.connectionHasTerminated();
                  }

                  if (var1) {
                     ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/90/failure=" + var10);
                  }

                  return;
            }
         }

         this.myMuxer.closeSocket(this.getSocketFilter());
         this.opened = false;
         if (this.myTuxReadSession != null) {
            this.myTuxReadSession.connectionHasTerminated();
         }

         if (var1) {
            ntrace.doTrace("]/TuxedoMuxableSocket/dispatch/100/metahdrOffset=" + var2 + "/myOffset=" + this.myOffset);
         }

      }
   }

   public Socket getSocket() {
      return this.in;
   }

   public boolean closeSocketOnError() {
      return true;
   }

   public InputStream getSocketInputStream() {
      return this.in_stream;
   }

   public void setSoTimeout(int var1) throws SocketException {
      if (var1 != this.in_timeout) {
         this.in.setSoTimeout(var1);
      }
   }

   public boolean isClosed() {
      return !this.opened;
   }

   public void hasException(Throwable var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/TuxedoMuxableSocket/hasException/" + var1);
      }

      this.opened = false;
      if (this.myTuxReadSession != null) {
         this.myTuxReadSession.connectionHasTerminated();
      }

      if (var2) {
         ntrace.doTrace("]/TuxedoMuxableSocket/hasException/10");
      }

   }

   public void endOfStream() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/TuxedoMuxableSocket/endOfStream/");
      }

      this.opened = false;
      if (this.myTuxReadSession != null) {
         this.myTuxReadSession.connectionHasTerminated();
      }

      if (var1) {
         ntrace.doTrace("]/TuxedoMuxableSocket/endOfStream/10");
      }

   }

   public boolean timeout() {
      this.endOfStream();
      return true;
   }

   public final boolean requestTimeout() {
      return true;
   }

   public int getIdleTimeoutMillis() {
      return 0;
   }

   public int getCompleteMessageTimeoutMillis() {
      return 0;
   }

   public int setElevel(int var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/TuxedoMuxableSocket/setELevel(" + var1 + ")/");
      }

      if (var1 != 1 && var1 != 2 && var1 != 32 && var1 != 4) {
         if (var2) {
            ntrace.doTrace("]/TuxedoMuxableSocket/setElevel/20/1");
         }

         return 1;
      } else {
         this.elevel = var1;
         if (var2) {
            ntrace.doTrace("]/TuxedoMuxableSocket/setElevel/10/0");
         }

         return 0;
      }
   }

   public void setLLE(tplle var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/TuxedoMuxableSocket/setLLE/");
      }

      this.lle = var1;
      if (var2) {
         ntrace.doTrace("]/TuxedoMuxableSocket/setLLE/10/");
      }

   }

   public void setProtocol(int var1) {
      this.myProtocol = var1;
   }

   public void close() {
      if (this.sockInfo != null) {
         this.myMuxer.closeSocket(this.getSocketFilter());
      }

      this.opened = false;
      if (this.myTuxReadSession != null) {
         this.myTuxReadSession.connectionHasTerminated();
      }

   }

   public void setSocketFilter(MuxableSocket var1) {
      this.filter = var1;
   }

   public MuxableSocket getSocketFilter() {
      return this.filter;
   }

   public SocketInfo getSocketInfo() {
      return this.sockInfo;
   }

   public void setSocketInfo(SocketInfo var1) {
      this.sockInfo = var1;
   }

   public boolean supportsScatteredRead() {
      return false;
   }

   public long read(NIOConnection var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   public ByteBuffer[] getAvailableBufferofSize(int var1) {
      throw new UnsupportedOperationException();
   }
}
