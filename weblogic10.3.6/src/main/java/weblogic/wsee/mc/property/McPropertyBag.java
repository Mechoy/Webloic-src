package weblogic.wsee.mc.property;

import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.PropertySet.Property;
import com.sun.xml.ws.api.message.Packet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.wsee.jaxws.framework.PropertySetUtil;
import weblogic.wsee.jaxws.persistence.PacketPersistencePropertyBag;

public class McPropertyBag extends PropertySet {
   public static final String POLL_ID = "weblogic.wsee.mc.PollId";
   public static final String IS_POLL = "weblogic.wsee.mc.IsPoll";
   public static final String MSG_ID = "weblogic.wsee.mc.msgId";
   public static final String RQST_ACTION = "weblogic.wsee.mc.RqstAction";
   public static PropertySetUtil.PropertySetRetriever<McPropertyBag> propertySetRetriever = PropertySetUtil.getRetriever(McPropertyBag.class);
   private static final PropertySet.PropertyMap model = parse(McPropertyBag.class);
   public static final Set<String> PERSISTENT_PROP_NAMES = new HashSet();
   private Packet _packet;
   private String _pollId;
   private boolean _isPoll;
   private String _msgId;
   private String _rqstAction;

   protected PropertySet.PropertyMap getPropertyMap() {
      return model;
   }

   public McPropertyBag(Packet var1) {
      this._packet = var1;
      this._isPoll = false;
   }

   public static void flagPersistentPropsOnPacket(Packet var0) {
      propertySetRetriever.getFromPacket(var0);
      PacketPersistencePropertyBag var1 = (PacketPersistencePropertyBag)PacketPersistencePropertyBag.propertySetRetriever.getFromPacket(var0);
      var1.getPersistablePropertyBagClassNames().add(McPropertyBag.class.getName());
      Set var2 = var1.getPersistablePropertyNames();
      Iterator var3 = PERSISTENT_PROP_NAMES.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(var4);
      }

   }

   @Property({"weblogic.wsee.mc.PollId"})
   public String getPollId() {
      return this._pollId;
   }

   public void setPollId(String var1) {
      this._pollId = var1;
   }

   @Property({"weblogic.wsee.mc.IsPoll"})
   public boolean getIsPoll() {
      return this._isPoll;
   }

   public void setIsPoll(boolean var1) {
      this._isPoll = var1;
   }

   @Property({"weblogic.wsee.mc.msgId"})
   public String getMsgId() {
      return this._msgId;
   }

   public void setMsgId(String var1) {
      this._msgId = var1;
   }

   @Property({"weblogic.wsee.mc.RqstAction"})
   public String getRqstAction() {
      return this._rqstAction;
   }

   public void setRqstAction(String var1) {
      this._rqstAction = var1;
   }

   static {
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.mc.PollId");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.mc.IsPoll");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.mc.msgId");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.mc.RqstAction");
   }
}
