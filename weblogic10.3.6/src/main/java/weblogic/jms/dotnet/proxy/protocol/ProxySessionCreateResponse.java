package weblogic.jms.dotnet.proxy.protocol;

import javax.jms.JMSException;
import javax.jms.Session;
import weblogic.jms.client.WLSessionImpl;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.extensions.WLSession;

public class ProxySessionCreateResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private static final int _HAS_EXTENSIONS = 1;
   private long sessionId;
   private int messagesMaximum;
   private long redeliveryDelay;
   private int pipelineGeneration;
   private long sequenceNumber;
   private boolean hasExtensions;

   public ProxySessionCreateResponse(long var1, Session var3) {
      this.sessionId = var1;
      if (var3 instanceof WLSession) {
         try {
            this.hasExtensions = true;
            this.messagesMaximum = ((WLSession)var3).getMessagesMaximum();
            this.redeliveryDelay = ((WLSession)var3).getRedeliveryDelay();
            this.pipelineGeneration = ((WLSessionImpl)var3).getPipelineGenerationFromProxy();
            this.sequenceNumber = ((WLSessionImpl)var3).getLastSequenceNumber();
         } catch (JMSException var5) {
            throw new RuntimeException(var5);
         }
      }

   }

   public long getSessionId() {
      return this.sessionId;
   }

   public int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   public long getRedeliveryDelay() {
      return this.redeliveryDelay;
   }

   public ProxySessionCreateResponse() {
   }

   public int getMarshalTypeCode() {
      return 31;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.hasExtensions) {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
      var1.writeLong(this.sessionId);
      if (this.hasExtensions) {
         var1.writeInt(this.messagesMaximum);
         var1.writeLong(this.redeliveryDelay);
         var1.writeInt(this.pipelineGeneration);
         var1.writeLong(this.sequenceNumber);
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.sessionId = var1.readLong();
      if (this.versionFlags.isSet(1)) {
         this.messagesMaximum = var1.readInt();
         this.redeliveryDelay = var1.readLong();
         this.pipelineGeneration = var1.readInt();
         this.sequenceNumber = var1.readLong();
      }

   }
}
