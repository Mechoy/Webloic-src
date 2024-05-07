package weblogic.jms.dotnet.proxy.protocol;

import java.io.Externalizable;
import java.io.IOException;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import weblogic.jms.common.BufferDataInputStream;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.ObjectIOBypass;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class ProxyDestinationImpl implements MarshalReadable, MarshalWritable {
   private static final int EXTVERSION = 1;
   private static final int _SERIALIZED_DESTINATION = 1;
   private static final int _HAS_CREATE_DESTINATION_ARG = 2;
   private static final int _IS_DISTRIBUTED_DESTINATION = 3;
   public static final byte TYPE_QUEUE = 1;
   public static final byte TYPE_TOPIC = 2;
   public static final byte TYPE_TEMP_QUEUE = 3;
   public static final byte TYPE_TEMP_TOPIC = 4;
   public transient Destination destination;
   private String name;
   private int type;
   private String createDestinationArgument;
   private boolean marshaled;
   private byte[] marshaledDestination;
   private boolean isDD;

   public ProxyDestinationImpl(String var1, Destination var2) throws JMSException {
      this.setupDestination(var2);
   }

   public ProxyDestinationImpl(Destination var1) throws JMSException {
      this.setupDestination(var1);
   }

   private void setupDestination(Destination var1) throws JMSException {
      this.destination = var1;
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Destination = " + var1.getClass());
      }

      if (var1 instanceof Externalizable) {
         if (var1 instanceof DestinationImpl) {
            this.name = ((DestinationImpl)var1).getName();
            this.type = ((DestinationImpl)var1).getType();
            if (var1 instanceof DistributedDestinationImpl) {
               this.isDD = true;
               this.createDestinationArgument = ((DistributedDestinationImpl)var1).getCreateDestinationArgument();
            } else {
               this.createDestinationArgument = ((DestinationImpl)var1).getCreateDestinationArgument();
            }
         } else if (var1 instanceof Queue) {
            this.name = ((Queue)var1).getQueueName();
            if (var1 instanceof TemporaryQueue) {
               this.type = 4;
            } else {
               this.type = 1;
            }
         } else if (var1 instanceof Topic) {
            this.name = ((Topic)var1).getTopicName();
            if (var1 instanceof TemporaryTopic) {
               this.type = 8;
            } else {
               this.type = 2;
            }
         }

         this.marshaledDestination = ProxyUtil.marshalExternalizable((Externalizable)var1);
         this.marshaled = true;
      }

   }

   public String getName() {
      return this.name;
   }

   public Destination getJMSDestination() {
      return this.destination;
   }

   public byte[] getMarshaledDestination() {
      return this.marshaledDestination;
   }

   private void unmarshalDestination() {
      BufferDataInputStream var1 = new BufferDataInputStream((ObjectIOBypass)null, this.marshaledDestination);
      if (this.isDD) {
         this.destination = new DistributedDestinationImpl();
      } else {
         this.destination = new DestinationImpl();
      }

      try {
         if (this.isDD) {
            ((DistributedDestinationImpl)this.destination).readExternal(var1);
         } else {
            ((DestinationImpl)this.destination).readExternal(var1);
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      } catch (ClassNotFoundException var4) {
         var4.printStackTrace();
      }

   }

   public ProxyDestinationImpl() {
   }

   public int getMarshalTypeCode() {
      return 41;
   }

   public void marshal(MarshalWriter var1) {
      MarshalBitMask var2 = new MarshalBitMask(1);
      if (this.marshaled) {
         var2.setBit(1);
      }

      if (this.createDestinationArgument != null) {
         var2.setBit(2);
      }

      if (this.isDD) {
         var2.setBit(3);
      }

      var2.marshal(var1);
      var1.writeString(this.name);
      var1.writeByte((byte)this.type);
      if (this.marshaled) {
         var1.writeInt(this.marshaledDestination.length);
         var1.write(this.marshaledDestination, 0, this.marshaledDestination.length);
      }

      if (this.createDestinationArgument != null) {
         var1.writeString(this.createDestinationArgument);
      }

   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      this.marshaled = var2.isSet(1);
      this.name = var1.readString();
      this.type = var1.readByte();
      this.isDD = var2.isSet(3);
      if (this.marshaled) {
         int var3 = var1.readInt();
         this.marshaledDestination = new byte[var3];
         var1.read(this.marshaledDestination, 0, var3);
         this.unmarshalDestination();
      }

      if (var2.isSet(2)) {
         this.createDestinationArgument = var1.readString();
      }

   }

   public String toString() {
      return "ProxyDestination<+ " + this.name + ">";
   }
}
