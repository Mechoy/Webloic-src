package weblogic.wsee.reliability2.property;

import com.sun.istack.Nullable;
import com.sun.xml.ws.api.message.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.wsee.jaxws.framework.InvocationPropertySet;
import weblogic.wsee.jaxws.persistence.PacketPersistencePropertyBag;
import weblogic.wsee.jaxws.spi.ClientInstance;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.tube.WsrmClientRuntime;

public class WsrmInvocationPropertyBag extends InvocationPropertySet implements Serializable {
   private static final long serialVersionUID = 1L;
   public static final String key = WsrmInvocationPropertyBag.class.getName();
   public static final String RM_VERSION = "weblogic.wsee.wsrm.RMVersion";
   public static final String FORCE_WSRM_1_0_CLIENT = "weblogic.wsee.reliability.forceWSRM10Client";
   public static final String SEQUENCE_ID = "weblogic.wsee.reliability2.SequenceID";
   public static final String CLIENT_RUNTIME = "weblogic.wsee.reliability2.WsrmClientRuntime";
   public static final String MOST_RECENT_MSG_NUM = "weblogic.wsee.reliability2.MostRecentMsgNum";
   public static final String FINAL_MSG_FLAG = "weblogic.wsee.reliability2.FinalMessageFlag";
   public static final String LOGICAL_STORE_NAME = "weblogic.wsee.reliability2.LogicalStoreName";
   public static final Set<String> PERSISTENT_PROP_NAMES = new HashSet();

   public static WsrmInvocationPropertyBag getFromPacket(Packet var0) {
      return getFromMap(var0.invocationProperties);
   }

   public static WsrmInvocationPropertyBag getFromMap(Map<String, Object> var0) {
      WsrmInvocationPropertyBag var1 = (WsrmInvocationPropertyBag)var0.get(key);
      if (var1 == null) {
         var1 = new WsrmInvocationPropertyBag(var0);
         var0.put(key, var1);
      }

      return var1;
   }

   public WsrmInvocationPropertyBag() {
      super(new Packet());
      this.initProps();
   }

   public WsrmInvocationPropertyBag(Packet var1) {
      super(var1);
      internalFlagPersistentPropsOnPacket(var1);
      this.initProps();
   }

   public static void flagPersistentPropsOnPacket(Packet var0) {
      if (!var0.invocationProperties.containsKey(key)) {
         var0.invocationProperties.put(key, new WsrmInvocationPropertyBag(var0));
      }

      internalFlagPersistentPropsOnPacket(var0);
   }

   private static void internalFlagPersistentPropsOnPacket(Packet var0) {
      PacketPersistencePropertyBag var1 = (PacketPersistencePropertyBag)PacketPersistencePropertyBag.propertySetRetriever.getFromPacket(var0);
      Set var2 = var1.getPersistableInvocationPropertyNames();
      var2.add(key);
   }

   public WsrmInvocationPropertyBag(Map<String, Object> var1) {
      this._invocationProps = var1;
      this.initProps();
   }

   private void initProps() {
      WsrmClientRuntime var1 = new WsrmClientRuntime(this.getClientInstanceRef());
      var1.internalSetSequenceId(this.getSequenceId());
      this.setWsrmClientRuntime(var1);
   }

   public void accept(WsrmInvocationPropertyBag var1) {
      this._invocationProps.putAll(var1._invocationProps);
   }

   public void clear() {
      this._invocationProps.clear();
      this.initProps();
   }

   public boolean getForceWsrm10Client() {
      Boolean var1 = (Boolean)this._invocationProps.get("weblogic.wsee.reliability.forceWSRM10Client");
      if (var1 == null) {
         var1 = Boolean.FALSE;
      }

      return var1;
   }

   public void setForceWsrm10Client(boolean var1) {
      this._invocationProps.put("weblogic.wsee.reliability.forceWSRM10Client", var1);
   }

   public WsrmConstants.RMVersion getWsrmVersion() {
      WsrmConstants.RMVersion var1 = (WsrmConstants.RMVersion)this._invocationProps.get("weblogic.wsee.wsrm.RMVersion");
      if (var1 == null) {
         var1 = WsrmConstants.RMVersion.latest();
      }

      return var1;
   }

   public void setLogicalStoreName(String var1) {
      this._invocationProps.put("weblogic.wsee.reliability2.LogicalStoreName", var1);
   }

   public void setWsrmVersion(WsrmConstants.RMVersion var1) {
      this._invocationProps.put("weblogic.wsee.wsrm.RMVersion", var1);
   }

   public String getSequenceId() {
      return (String)this._invocationProps.get("weblogic.wsee.reliability2.SequenceID");
   }

   public void setSequenceId(String var1) {
      this.internalSetSequenceId(var1);
      WsrmClientRuntime var2 = this.getWsrmClientRuntime();
      if (var2 != null) {
         var2.internalSetSequenceId(var1);
      }

   }

   public void internalSetSequenceId(String var1) {
      this._invocationProps.put("weblogic.wsee.reliability2.SequenceID", var1);
   }

   @Nullable
   public ClientInstanceIdentity getClientInstanceId() {
      WeakReference var1 = this.getClientInstanceRef();
      return var1 != null ? ((ClientInstance)var1.get()).getId() : null;
   }

   @Nullable
   private WeakReference<ClientInstance> getClientInstanceRef() {
      return (WeakReference)this._invocationProps.get("weblogic.wsee.jaxws.spi.ClientInstanceWeakRef");
   }

   private WsrmClientRuntime getWsrmClientRuntime() {
      return (WsrmClientRuntime)this._invocationProps.get("weblogic.wsee.reliability2.WsrmClientRuntime");
   }

   public void setWsrmClientRuntime(WsrmClientRuntime var1) {
      this._invocationProps.put("weblogic.wsee.reliability2.WsrmClientRuntime", var1);
   }

   public long getMostRecentMsgNum() {
      Long var1 = (Long)this._invocationProps.get("weblogic.wsee.reliability2.MostRecentMsgNum");
      if (var1 == null) {
         var1 = Long.MIN_VALUE;
      }

      return var1;
   }

   public void setMostRecentMsgNum(long var1) {
      this._invocationProps.put("weblogic.wsee.reliability2.MostRecentMsgNum", var1);
   }

   public boolean getFinalMsgFlag() {
      Boolean var1 = (Boolean)this._invocationProps.get("weblogic.wsee.reliability2.FinalMessageFlag");
      if (var1 == null) {
         var1 = Boolean.FALSE;
      }

      return var1;
   }

   public void setFinalMsgFlag(boolean var1) {
      this._invocationProps.put("weblogic.wsee.reliability2.FinalMessageFlag", var1);
   }

   public boolean containsProp(String var1) {
      return this._invocationProps.containsKey(var1);
   }

   public Object getProp(String var1) {
      return this._invocationProps.get(var1);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      HashMap var2 = new HashMap();
      Iterator var3 = PERSISTENT_PROP_NAMES.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Serializable var5 = (Serializable)this.getProp(var4);
         var2.put(var4, var5);
      }

      var1.writeObject(var2);
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.readObject();
      this._invocationProps = new HashMap();
      Map var2 = (Map)var1.readObject();
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this._invocationProps.put(var4, var2.get(var4));
      }

      var1.defaultReadObject();
      this.initProps();
   }

   static {
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.wsrm.RMVersion");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability.forceWSRM10Client");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.SequenceID");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.MostRecentMsgNum");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.FinalMessageFlag");
   }
}
