package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyConsumerCreateRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private static final int _HAS_CLIENTID = 1;
   private static final int _HAS_NAME = 2;
   private static final int _HAS_SELECTOR = 3;
   private static final int _IS_NOLOCAL = 4;
   private static final int _HAS_MESSAGES_MAXIMUM = 5;
   private static final int _HAS_REDELIVERY_DELAY = 6;
   private String clientId;
   private String name;
   private ProxyDestinationImpl destination;
   private String selector;
   private boolean noLocal;
   private int messagesMaximum;
   private long redeliveryDelay;

   public ProxyConsumerCreateRequest(String var1, String var2, ProxyDestinationImpl var3, String var4, boolean var5, int var6, long var7) {
      this.clientId = var1;
      this.name = var2;
      this.destination = var3;
      this.selector = var4;
      this.noLocal = var5;
      this.messagesMaximum = var6;
      this.redeliveryDelay = var7;
   }

   public String getClientId() {
      return this.clientId;
   }

   public String getName() {
      return this.name;
   }

   public ProxyDestinationImpl getDestination() {
      return this.destination;
   }

   public String getSelector() {
      return this.selector;
   }

   public boolean getNoLocal() {
      return this.noLocal;
   }

   public int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   public long getRedeliveryDelay() {
      return this.redeliveryDelay;
   }

   public ProxyConsumerCreateRequest() {
   }

   public int getMarshalTypeCode() {
      return 13;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.clientId != null) {
         this.versionFlags.setBit(1);
      }

      if (this.name != null) {
         this.versionFlags.setBit(2);
      }

      if (this.selector != null) {
         this.versionFlags.setBit(3);
      }

      if (this.noLocal) {
         this.versionFlags.setBit(4);
      }

      if (this.messagesMaximum != 1) {
         this.versionFlags.setBit(5);
      }

      if (this.redeliveryDelay != -1L) {
         this.versionFlags.setBit(6);
      }

      this.versionFlags.marshal(var1);
      this.destination.marshal(var1);
      if (this.versionFlags.isSet(1)) {
         var1.writeString(this.clientId);
      }

      if (this.versionFlags.isSet(2)) {
         var1.writeString(this.name);
      }

      if (this.versionFlags.isSet(3)) {
         var1.writeString(this.selector);
      }

      if (this.versionFlags.isSet(5)) {
         var1.writeInt(this.messagesMaximum);
      }

      if (this.versionFlags.isSet(6)) {
         var1.writeLong(this.redeliveryDelay);
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.destination = new ProxyDestinationImpl();
      this.destination.unmarshal(var1);
      if (this.versionFlags.isSet(1)) {
         this.clientId = var1.readString();
      }

      if (this.versionFlags.isSet(2)) {
         this.name = var1.readString();
      }

      if (this.versionFlags.isSet(3)) {
         this.selector = var1.readString();
      }

      if (this.versionFlags.isSet(4)) {
         this.noLocal = true;
      }

      if (this.versionFlags.isSet(5)) {
         this.messagesMaximum = var1.readInt();
      }

      if (this.versionFlags.isSet(6)) {
         this.redeliveryDelay = var1.readLong();
      }

   }
}
