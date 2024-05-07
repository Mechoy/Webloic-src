package weblogic.messaging.saf.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFErrorHandler;
import weblogic.messaging.saf.internal.SAFDiagnosticImageSource;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.messaging.saf.utils.Util;

public final class SAFConversationInfoImpl implements SAFConversationInfo {
   static final long serialVersionUID = -6685748132717633470L;
   private static final int EXTVERSION1 = 1;
   private static final int VERSION_MASK = 255;
   private static final int EXTVERSION = 1;
   private String conversationName;
   private int transportType;
   private int qos = 1;
   private long timestamp;
   private int destinationType;
   private String sourceURL;
   private String destinationURL;
   private long ttl = Long.MAX_VALUE;
   private long maximumIdleTime = Long.MAX_VALUE;
   private boolean inorder = true;
   private SAFRemoteContext remoteContext;
   private SAFErrorHandler errorHandler;
   private int conversationType;
   private boolean dynamic = true;
   private String dynamicConversationName;
   private Externalizable conversationContext;
   private SAFConversationInfo conversationOffer;
   private long conversationTimeout = Long.MAX_VALUE;
   private int timeoutPolicy;
   private String createConvMsgID;
   private boolean conversationAlreadyCreated = false;
   private static final int _HASREMOTECONTEXT = 512;
   private static final int _ISINORDER = 2048;
   private static final int _HASERRORHANDLER = 4096;
   private static final int _HASTIMESTAMP = 8192;
   private static final int _ISDYNAMIC = 32768;
   private static final int _ISWSRMSAFCONVERSATIONINFO = 65536;
   private static final int _HASDYNAMICCONVERSATIONNAME = 131072;
   private static final int _HASCONVERSATIONCONTEXT = 262144;
   private static final int _HASCONVTIMEOUT = 524288;
   private static final int _HASOFFER = 1048576;
   private static final int _HASCONVTIMEOUTPOLICY = 2097152;
   private static final int _HASCREATECONVMSGID = 4194304;
   private static final int _ISCONVERSATIONALREADYCREATED = 16777216;

   public SAFConversationInfoImpl(int var1) {
      this.conversationType = var1;
   }

   public synchronized SAFConversationInfo getConversationOffer() {
      return this.conversationOffer;
   }

   public synchronized void setConversationOffer(SAFConversationInfo var1) {
      this.conversationOffer = var1;
   }

   public int getTimeoutPolicy() {
      return this.timeoutPolicy;
   }

   public void setTimeoutPolicy(int var1) {
      this.timeoutPolicy = var1;
   }

   public void setCreateConversationMessageID(String var1) {
      this.createConvMsgID = var1;
   }

   public String getCreateConversationMessageID() {
      return this.createConvMsgID;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof SAFConversationInfoImpl)) {
         return false;
      } else {
         SAFConversationInfoImpl var2 = (SAFConversationInfoImpl)var1;
         if (this.conversationType == 2) {
            return this.conversationName.equals(var2.conversationName);
         } else if (this.destinationType != var2.destinationType) {
            return false;
         } else if (this.qos != var2.qos) {
            return false;
         } else if (this.transportType != var2.transportType) {
            return false;
         } else if (!this.conversationName.equals(var2.conversationName)) {
            return false;
         } else if (!this.destinationURL.equals(var2.destinationURL)) {
            return false;
         } else {
            return this.sourceURL.equals(var2.sourceURL);
         }
      }
   }

   public int hashCode() {
      int var1 = this.conversationName != null ? this.conversationName.hashCode() : 0;
      if (this.conversationType == 2) {
         return var1;
      } else {
         var1 = 29 * var1 + this.transportType;
         var1 = 29 * var1 + this.qos;
         var1 = 29 * var1 + this.destinationType;
         if (this.sourceURL != null) {
            var1 = 29 * var1 + this.sourceURL.hashCode();
         }

         if (this.destinationURL != null) {
            var1 = 29 * var1 + this.destinationURL.hashCode();
         }

         return var1;
      }
   }

   public String toString() {
      return "<ConversationInfo> = { ConversationName=" + this.conversationName + "," + " DynamicName=" + this.dynamicConversationName + "," + " SourceURL=" + this.sourceURL + "," + " DestinationURL=" + this.destinationURL + "," + " DestinationType=" + this.destinationType + "," + " QOS=" + this.qos + "," + " timestamp=" + this.timestamp + "," + " timeoutPolicy=" + this.timeoutPolicy + "," + " timeToLive=" + this.ttl + "," + " conversationTimeout=" + this.conversationTimeout + "," + " maximumIdleTime=" + this.maximumIdleTime + "," + " createConversationMessageID=" + this.createConvMsgID + "," + " offeredConversationName=" + (this.getConversationOffer() == null ? null : this.getConversationOffer().getConversationName()) + "}";
   }

   public int getQOS() {
      return this.qos;
   }

   public void setQOS(int var1) {
      this.qos = var1;
   }

   public String getSourceURL() {
      return this.sourceURL;
   }

   public void setSourceURL(String var1) {
      this.sourceURL = var1;
   }

   public int getTransportType() {
      return this.transportType;
   }

   public void setTransportType(int var1) {
      this.transportType = var1;
   }

   public int getDestinationType() {
      return this.destinationType;
   }

   public void setDestinationType(int var1) {
      this.destinationType = var1;
   }

   public String getDestinationURL() {
      return this.destinationURL;
   }

   public void setDestinationURL(String var1) {
      this.destinationURL = var1;
   }

   public long getTimeToLive() {
      return this.ttl;
   }

   public void setTimeToLive(long var1) {
      this.ttl = var1;
   }

   public long getMaximumIdleTime() {
      return this.maximumIdleTime;
   }

   public void setMaximumIdleTime(long var1) {
      this.maximumIdleTime = var1;
   }

   public long getConversationTimeout() {
      return this.conversationTimeout;
   }

   public void setConversationTimeout(long var1) {
      this.conversationTimeout = var1;
   }

   public String getConversationName() {
      return this.conversationName;
   }

   public void setConversationName(String var1) {
      this.conversationName = var1;
   }

   public boolean isInorder() {
      return this.inorder;
   }

   public void setInorder(boolean var1) {
      this.inorder = var1;
   }

   public SAFRemoteContext getRemoteContext() {
      return this.remoteContext;
   }

   public void setRemoteContext(SAFRemoteContext var1) {
      this.remoteContext = var1;
   }

   public SAFErrorHandler getErrorHandler() {
      return this.errorHandler;
   }

   public void setErrorHandler(SAFErrorHandler var1) {
      this.errorHandler = var1;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(long var1) {
      this.timestamp = var1;
   }

   public boolean isDynamic() {
      return this.dynamic;
   }

   public void setDynamic(boolean var1) {
      this.dynamic = var1;
   }

   public synchronized String getDynamicConversationName() {
      return this.dynamicConversationName;
   }

   public synchronized void setDynamicConversationName(String var1) {
      this.dynamicConversationName = var1;
   }

   public SAFConversationInfoImpl() {
   }

   public synchronized void setContext(Externalizable var1) {
      this.conversationContext = var1;
   }

   public synchronized Externalizable getContext() {
      return this.conversationContext;
   }

   public boolean isConversationAlreadyCreated() {
      return this.conversationAlreadyCreated;
   }

   public void setConversationAlreadyCreated(boolean var1) {
      this.conversationAlreadyCreated = var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      int var3 = 0;
      var3 |= var2;
      if (this.timestamp != 0L) {
         var3 |= 8192;
      }

      if (this.remoteContext != null) {
         var3 |= 512;
      }

      if (this.inorder) {
         var3 |= 2048;
      }

      if (this.dynamic) {
         var3 |= 32768;
      }

      if (this.errorHandler != null) {
         var3 |= 4096;
      }

      if (this.conversationType == 2) {
         var3 |= 65536;
      }

      if (this.dynamicConversationName != null) {
         var3 |= 131072;
      }

      if (this.conversationContext != null) {
         var3 |= 262144;
      }

      if (this.conversationTimeout != Long.MAX_VALUE) {
         var3 |= 524288;
      }

      if (this.conversationOffer != null) {
         var3 |= 1048576;
      }

      if (this.timeoutPolicy != 0) {
         var3 |= 2097152;
      }

      if (this.createConvMsgID != null) {
         var3 |= 4194304;
      }

      if (this.conversationAlreadyCreated) {
         var3 |= 16777216;
      }

      var1.writeInt(var3);
      var1.writeInt(this.qos);
      var1.writeInt(this.destinationType);
      var1.writeInt(this.transportType);
      var1.writeUTF(this.conversationName);
      if (this.dynamicConversationName != null) {
         var1.writeUTF(this.dynamicConversationName);
      }

      var1.writeObject(this.sourceURL);
      var1.writeUTF(this.destinationURL);
      var1.writeLong(this.ttl);
      var1.writeLong(this.maximumIdleTime);
      if (this.conversationTimeout != Long.MAX_VALUE) {
         var1.writeLong(this.conversationTimeout);
      }

      if (this.remoteContext != null) {
         this.remoteContext.writeExternal(var1);
      }

      if (this.conversationContext != null) {
         var1.writeObject(this.conversationContext);
      }

      if (this.errorHandler != null) {
         this.errorHandler.writeExternal(var1);
      }

      if (this.timestamp != 0L) {
         var1.writeLong(this.timestamp);
      }

      var1.writeInt(0);
      if (this.conversationOffer != null) {
         this.conversationOffer.writeExternal(var1);
      }

      if (this.timeoutPolicy != 0) {
         var1.writeInt(this.timeoutPolicy);
      }

      if (this.createConvMsgID != null) {
         var1.writeUTF(this.createConvMsgID);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw Util.versionIOException(var3, 1, 1);
      } else {
         this.conversationType = (var2 & 65536) != 0 ? 2 : 1;
         this.qos = var1.readInt();
         this.destinationType = var1.readInt();
         this.transportType = var1.readInt();
         this.conversationName = var1.readUTF();
         if ((var2 & 131072) != 0) {
            this.dynamicConversationName = var1.readUTF();
         }

         this.sourceURL = (String)var1.readObject();
         this.destinationURL = var1.readUTF();
         this.ttl = var1.readLong();
         this.maximumIdleTime = var1.readLong();
         if ((var2 & 524288) != 0) {
            this.conversationTimeout = var1.readLong();
         }

         if ((var2 & 512) != 0) {
            this.remoteContext = new SAFRemoteContext();
            this.remoteContext.readExternal(var1);
         }

         if ((var2 & 262144) != 0) {
            this.conversationContext = (Externalizable)var1.readObject();
         }

         this.inorder = (var2 & 2048) != 0;
         this.dynamic = (var2 & '耀') != 0;
         if ((var2 & 4096) != 0) {
            this.errorHandler = SAFManagerImpl.getManager().getEndpointManager(this.destinationType).createErrorHandlerInstance();
            if (this.errorHandler != null) {
               this.errorHandler.readExternal(var1);
            }
         }

         if ((var2 & 8192) != 0) {
            this.timestamp = var1.readLong();
         }

         var1.readInt();
         if ((var2 & 1048576) != 0) {
            this.conversationOffer = new SAFConversationInfoImpl();
            this.conversationOffer.readExternal(var1);
         }

         if ((var2 & 2097152) != 0) {
            this.timeoutPolicy = var1.readInt();
         }

         if ((var2 & 4194304) != 0) {
            this.createConvMsgID = var1.readUTF();
         }

         this.conversationAlreadyCreated = (var2 & 16777216) != 0;
      }
   }

   public void dump(SAFDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("ConversationInfo");
      var2.writeAttribute("qos", String.valueOf(this.qos));
      var2.writeAttribute("sourceURL", this.sourceURL);
      var2.writeAttribute("destinationType", String.valueOf(this.destinationType));
      var2.writeAttribute("destinationURL", this.destinationURL);
      var2.writeAttribute("name", this.conversationName);
      var2.writeAttribute("transportType", String.valueOf(this.transportType));
      var2.writeAttribute("isInorder", String.valueOf(this.inorder));
      var2.writeAttribute("isDynamic", String.valueOf(this.dynamic));
      var2.writeAttribute("ttl", String.valueOf(this.ttl));
      var2.writeAttribute("maximumIdleTime", String.valueOf(this.maximumIdleTime));
      var2.writeAttribute("timeout", String.valueOf(this.conversationTimeout));
      var2.writeAttribute("timeoutPolicy", String.valueOf(this.timeoutPolicy));
      var2.writeEndElement();
   }
}
