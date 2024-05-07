package weblogic.jms.dotnet.proxy.protocol;

import java.util.Enumeration;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyMapMessageImpl extends ProxyMessageImpl {
   private static final int EXTVERSION = 1;
   private static final int _HAS_DATA = 1;
   private PrimitiveMap map;
   private transient String contentStr;

   public ProxyMapMessageImpl() {
   }

   public ProxyMapMessageImpl(PrimitiveMap var1) {
      this.map = var1;
   }

   public ProxyMapMessageImpl(MapMessage var1) throws JMSException {
      super(var1);
      this.copyMap(var1);
   }

   private void copyMap(MapMessage var1) throws JMSException {
      Enumeration var2 = var1.getMapNames();
      if (var2.hasMoreElements()) {
         this.map = new PrimitiveMap();

         do {
            String var3 = (String)var2.nextElement();
            this.map.put(var3, var1.getObject(var3));
         } while(var2.hasMoreElements());
      }

   }

   public byte getType() {
      return 3;
   }

   public boolean itemExists(String var1) throws JMSException {
      return this.map.containsKey(var1);
   }

   public String toString() {
      if (this.contentStr == null) {
         this.contentStr = "";

         String var2;
         Object var3;
         for(Iterator var1 = this.map.keySet().iterator(); var1.hasNext(); this.contentStr = this.contentStr + "(" + var2 + "," + var3 + ")") {
            var2 = (String)var1.next();
            var3 = this.map.get(var2);
         }
      }

      return "MapMessage[" + this.getMessageID() + this.contentStr + "]";
   }

   public void populateJMSMessage(MapMessage var1) throws JMSException {
      super.populateJMSMessage(var1);
      if (this.map != null) {
         Iterator var2 = this.map.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Object var4 = this.map.get(var3);
            var1.setObject(var3, var4);
         }
      }

   }

   public int getMarshalTypeCode() {
      return 37;
   }

   public void marshal(MarshalWriter var1) {
      super.marshal(var1);
      MarshalBitMask var2 = new MarshalBitMask(1);
      if (this.map != null) {
         var2.setBit(1);
      }

      var2.marshal(var1);
      if (this.map != null) {
         this.map.marshal(var1);
      }

   }

   public void unmarshal(MarshalReader var1) {
      super.unmarshal(var1);
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         this.map = new PrimitiveMap();
         this.map.unmarshal(var1);
      }

   }
}
