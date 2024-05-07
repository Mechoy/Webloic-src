package weblogic.jms.multicast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.jms.client.ConsumerInternal;
import weblogic.jms.client.JMSSession;
import weblogic.jms.common.BufferDataInputStream;
import weblogic.jms.common.BufferDataOutputStream;
import weblogic.jms.common.BufferInputStreamChunked;
import weblogic.jms.common.BufferOutputStream;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.JMSPushRequest;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.ObjectIOBypass;
import weblogic.jms.extensions.SequenceGapException;
import weblogic.utils.expressions.ExpressionEvaluationException;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedDataInputStream;

public class JMSTMSocket implements Runnable {
   private static final int MAX_FRAGMENT_SIZE = 10240;
   private static final int PAYLOAD_FUDGE_FACTOR = 232;
   private static final int MESSAGE_VERSION = 1;
   private static final int FRAGMENT_VERSION = 1;
   private static final int VERSION_MASK = 4095;
   private static final int FRAGMENT_MAGIC = 199886103;
   protected static final int INITIAL_SEQNO = 0;
   private JMSTDMSocket sock;
   private final Object wantLock = new Object();
   private int wantLockCount = 0;
   private final JMSSession session;
   protected boolean closed;
   private final JMSTMObjectIOBypassImpl objectIOBypassImpl = new JMSTMObjectIOBypassImpl();
   private final byte[] bdosMsgBuffer = new byte[10240];
   private final byte[] bdosFragBuffer = new byte[10240];
   private final byte[] bdisFragBuffer = new byte[10240];
   private final BufferOutputStream bdosMsg;
   private final BufferOutputStream bdosFrag;
   private final BufferDataInputStream bdisFrag;
   private HashMap stashes;
   private final int receivePort;
   private final HashMap groups;
   private final HashMap destinations;
   private final HashMap dests;
   private long fragmentDelay;
   private long lastDelay;
   private long lastSendTime;
   private static final String PROTOCOL = "WeblogicMulticast";

   public JMSTMSocket(JMSSession var1, JMSTDMSocket var2, int var3, int var4) throws IOException {
      this.bdosMsg = new BufferDataOutputStream(this.objectIOBypassImpl, this.bdosMsgBuffer);
      this.bdosFrag = new BufferDataOutputStream((ObjectIOBypass)null, this.bdosFragBuffer);
      this.bdisFrag = new BufferDataInputStream((ObjectIOBypass)null, this.bdisFragBuffer);
      this.session = var1;
      this.sock = var2;
      this.receivePort = var4;
      this.fragmentDelay = (long)var3;
      this.lastDelay = 0L;
      this.lastSendTime = 0L;
      this.closed = false;
      this.groups = new HashMap();
      this.destinations = new HashMap();
      this.dests = new HashMap();
      this.stashes = new HashMap();
      this.bdosMsg.setIsJMSMulticastOutputStream();
      this.bdosFrag.setIsJMSMulticastOutputStream();
   }

   public final void setFragmentDelay(long var1) {
      this.fragmentDelay = var1;
   }

   public final boolean isClosed() {
      return this.closed;
   }

   public final void close() {
      this.incWantLockCount();
      synchronized(this) {
         if (!this.closed) {
            this.closed = true;
            this.sock = null;
            this.stashes = null;
         }
      }

      this.decWantLockCount();
   }

   public final void send(MessageImpl var1, DestinationImpl var2, JMSID var3, InetAddress var4, int var5, byte var6, long var7) throws IOException {
      String var11 = var2.getServerName() + "/" + var2.getName();
      if (this.closed) {
         throw new IOException("Attempt to send message on multicast socket that is closed");
      } else {
         this.bdosMsg.reset();
         this.bdosMsg.writeShort(1);
         this.bdosMsg.writeByte(var1.getType());
         var1.writeExternal(this.bdosMsg.getObjectOutput());
         var3.writeExternal(this.bdosMsg.getObjectOutput());
         this.bdosMsg.flush();
         int var9 = this.bdosMsg.size();
         int var10 = 0;

         for(int var12 = 0; var10 < var9; ++var12) {
            this.bdosFrag.reset();
            this.bdosFrag.writeInt(199886103);
            this.bdosFrag.writeShort(1);
            this.bdosFrag.writeUTF(var11);
            this.bdosFrag.writeLong(var7);
            this.bdosFrag.writeInt(var9);
            this.bdosFrag.writeInt(var12);
            this.bdosFrag.writeInt(var10);
            this.bdosFrag.flush();
            int var13 = Math.min(10008 - this.bdosFrag.size(), var9 - var10);
            this.bdosFrag.writeInt(var13);
            if (var13 > 0) {
               this.bdosFrag.write(this.bdosMsgBuffer, var10, var13);
            }

            this.bdosFrag.flush();
            this.sendThrottled(this.bdosFragBuffer, this.bdosFrag.size(), var4, var5, var6);
            var10 += var13;
         }

      }
   }

   private void sendThrottled(byte[] var1, int var2, InetAddress var3, int var4, byte var5) throws IOException {
      long var6 = System.currentTimeMillis();
      this.lastDelay = Math.max(this.lastDelay - Math.max(var6 - this.lastSendTime, 0L) + this.fragmentDelay, 0L);
      this.lastSendTime = var6;
      if (this.lastDelay > 0L) {
         try {
            Thread.sleep(this.lastDelay);
         } catch (InterruptedException var9) {
         }
      }

      this.sock.send(var1, var2, var3, var4, var5);
   }

   public final String getProtocol() {
      return "WeblogicMulticast";
   }

   public void run() {
      while(true) {
         if (this.getWantLockCount() != 0) {
            Thread.yield();
         }

         MessageImpl var2;
         synchronized(this) {
            if (this.sock == null) {
               return;
            }

            try {
               var2 = this.receive();
            } catch (Exception var15) {
               throw new RuntimeException(var15);
            }

            if (var2 == null) {
               continue;
            }
         }

         synchronized(this.session) {
            synchronized(this) {
               JMSID var5 = var2.getConnectionId();
               var2.setConnectionId((JMSID)null);
               DestinationImpl var6 = (DestinationImpl)var2.getJMSDestination();
               String var7 = var6.getServerName() + "/" + var6.getName();
               JMSPushRequest var8 = new JMSPushRequest(0, (JMSID)null, var2);
               ArrayList var1;
               if ((var1 = (ArrayList)this.destinations.get(var7)) != null) {
                  for(int var9 = 0; var9 < var1.size(); ++var9) {
                     ConsumerInternal var10 = (ConsumerInternal)var1.get(var9);
                     if (!var10.privateGetNoLocal() || !this.session.getConnection().getJMSID().equals(var5)) {
                        try {
                           if (var10.getExpressionEvaluator() != null && !var10.getExpressionEvaluator().evaluate(var2)) {
                              continue;
                           }
                        } catch (ExpressionEvaluationException var16) {
                           continue;
                        } catch (ClassCastException var17) {
                           continue;
                        }

                        JMSID var11 = var10.getJMSID();
                        if (var11 != null) {
                           JMSPushEntry var12 = new JMSPushEntry((JMSID)null, var11, Long.MAX_VALUE, Long.MAX_VALUE, 1, 0);
                           var8.addPushEntry(var12);
                        }
                     }
                  }

                  this.session.pushMessage(var8, false);
               }
            }
         }
      }
   }

   private MessageImpl receive() throws Exception {
      try {
         if (this.closed) {
            return null;
         }

         if (this.sock.receive(this.bdisFragBuffer) == 0) {
            return null;
         }

         this.bdisFrag.reset();
         if (this.bdisFrag.readInt() != 199886103) {
            return null;
         }

         if ((this.bdisFrag.readShort() & 4095) != 1) {
            return null;
         }

         String var1 = this.bdisFrag.readUTF();
         DestinationImpl var2 = (DestinationImpl)this.dests.get(var1);
         if (var2 == null) {
            return null;
         }

         long var3 = this.bdisFrag.readLong();
         int var5 = this.bdisFrag.readInt();
         int var6 = this.bdisFrag.readInt();
         int var7 = this.bdisFrag.readInt();
         int var8 = this.bdisFrag.readInt();
         JMSFragmentStash var9 = (JMSFragmentStash)this.stashes.get(var1);
         if (var9 == null) {
            var9 = new JMSFragmentStash(this.session, var3, var2);
            this.stashes.put(var1, var9);
         }

         Chunk var10 = var9.processFragment(var3, var5, var6, var7, this.bdisFrag, var8);
         if (var10 != null) {
            BufferInputStreamChunked var11 = new BufferInputStreamChunked(this.objectIOBypassImpl, new ChunkedDataInputStream(var10, 0));
            if ((var11.readShort() & 4095) != 1) {
               return null;
            }

            MessageImpl var12 = MessageImpl.createMessageImpl(var11.readByte());
            var12.readExternal(var11);
            JMSID var13 = new JMSID();
            var13.readExternal(var11);
            var12.setJMSDestinationImpl(var2);
            var12.setConnectionId(var13);
            return var12;
         }
      } catch (IOException var14) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("IOException", var14);
         }
      } catch (ClassNotFoundException var15) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("ClassNotFoundException", var15);
         }
      } catch (SequenceGapException var16) {
         this.session.onException(var16);
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("SequenceGapException", var16);
         }
      } catch (Throwable var17) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("Throwable", var17);
         }
      }

      return null;
   }

   public final void joinGroup(DestinationImpl var1, ConsumerInternal var2) throws IOException {
      this.incWantLockCount();
      synchronized(this) {
         try {
            if (this.sock == null) {
               throw new IOException("socket closed");
            }

            if (var1.getPort() != this.receivePort) {
               throw new IOException("Wrong port");
            }

            String var7 = var1.getServerName() + "/" + var1.getName();

            InetAddress var3;
            try {
               var3 = InetAddress.getByName(var1.getMulticastAddress());
            } catch (UnknownHostException var10) {
               throw new IOException("Cannot parse multicast address " + var1.getMulticastAddress());
            }

            ArrayList var4;
            ArrayList var5;
            if ((var4 = (ArrayList)this.groups.get(var3)) != null) {
               if (var4.indexOf(var2) < 0) {
                  var4.add(var2);
                  var5 = (ArrayList)this.destinations.get(var7);
                  if (var5 == null) {
                     var5 = new ArrayList();
                     this.destinations.put(var7, var5);
                     this.dests.put(var7, var1);
                  }

                  var5.add(var2);
               }
            } else {
               this.sock.joinGroup(var3);
               var4 = new ArrayList();
               var4.add(var2);
               this.groups.put(var3, var4);
               var5 = new ArrayList();
               this.destinations.put(var7, var5);
               this.dests.put(var7, var1);
               var5.add(var2);
            }
         } catch (Throwable var11) {
            this.decWantLockCount();
            throw new IOException(var11.toString());
         }
      }

      this.decWantLockCount();
   }

   public final void leaveGroup(DestinationImpl var1, ConsumerInternal var2) throws IOException {
      this.incWantLockCount();
      synchronized(this) {
         try {
            label58: {
               if (this.sock == null) {
                  throw new IOException("socket closed");
               }

               InetAddress var3;
               try {
                  var3 = InetAddress.getByName(var1.getMulticastAddress());
               } catch (UnknownHostException var10) {
                  throw new IOException("Cannot parse multicast address " + var1.getMulticastAddress());
               }

               ArrayList var5 = (ArrayList)this.groups.get(var3);
               int var4;
               if (var5 != null && (var4 = var5.indexOf(var2)) >= 0) {
                  var5.remove(var4);
                  String var8 = var1.getServerName() + "/" + var1.getName();
                  ArrayList var6 = (ArrayList)this.destinations.get(var8);
                  if (var6 != null && (var4 = var6.indexOf(var2)) >= 0) {
                     var6.remove(var4);
                     if (var6.size() == 0) {
                        this.destinations.remove(var8);
                        this.dests.remove(var8);
                        this.stashes.remove(var8);
                     }

                     if (var5.size() == 0) {
                        this.groups.remove(var3);
                        this.sock.leaveGroup(var3);
                     }
                     break label58;
                  }

                  throw new IOException("can not find destination info about consumer");
               }

               throw new IOException("Cannot find group info about consumer");
            }
         } catch (Throwable var11) {
            this.decWantLockCount();
            throw new IOException(var11.toString());
         }
      }

      this.decWantLockCount();
   }

   public final void start() throws IOException {
      this.incWantLockCount();
      synchronized(this) {
         try {
            if (this.sock == null) {
               throw new IOException("socket is closed");
            }

            Iterator var2 = this.groups.keySet().iterator();

            while(var2.hasNext()) {
               InetAddress var3 = (InetAddress)var2.next();
               this.sock.joinGroup(var3);
            }

            this.stashes = new HashMap();
         } catch (Throwable var5) {
            this.decWantLockCount();
            throw new IOException(var5.toString());
         }
      }

      this.decWantLockCount();
   }

   public final void stop() throws IOException {
      this.incWantLockCount();
      synchronized(this) {
         try {
            if (this.sock == null) {
               throw new IOException("socket is closed");
            }

            Iterator var2 = this.groups.keySet().iterator();

            while(var2.hasNext()) {
               InetAddress var3 = (InetAddress)var2.next();
               this.sock.leaveGroup(var3);
            }

            this.stashes = null;
         } catch (Throwable var5) {
            this.decWantLockCount();
            throw new IOException(var5.toString());
         }
      }

      this.decWantLockCount();
   }

   private void incWantLockCount() {
      synchronized(this.wantLock) {
         ++this.wantLockCount;
      }
   }

   private void decWantLockCount() {
      synchronized(this.wantLock) {
         --this.wantLockCount;
      }
   }

   private int getWantLockCount() {
      synchronized(this.wantLock) {
         return this.wantLockCount;
      }
   }
}
