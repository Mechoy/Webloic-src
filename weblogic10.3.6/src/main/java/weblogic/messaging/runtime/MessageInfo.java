package weblogic.messaging.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

public abstract class MessageInfo {
   public static final int STATE_VISIBLE = 1;
   public static final int STATE_ORDERED = 16;
   public static final int STATE_DELAYED = 32;
   public static final int STATE_RECEIVE = 4;
   public static final int STATE_SEND = 2;
   public static final int STATE_TRANSACTION = 8;
   public static final int STATE_PAUSED = 256;
   public static final int STATE_REDELIVERY_COUNT_EXCEEDED = 128;
   public static final int STATE_EXPIRED = 64;
   private static final String OPEN_TYPE_NAME = "MessageInfo";
   private static final String OPEN_DESCRIPTION = "This object represents message meta-data that describes the context of the message at the time the management operation was performed.";
   protected ArrayList openItemNames = new ArrayList();
   protected ArrayList openItemDescriptions = new ArrayList();
   protected ArrayList openItemTypes = new ArrayList();
   protected static final String ITEM_HANDLE = "Handle";
   protected static final String ITEM_STATE = "State";
   protected static final String ITEM_XID_STRING = "XidString";
   protected static final String ITEM_SEQUENCE_NUMBER = "SequenceNumber";
   protected static final String ITEM_CONSUMER_ID = "ConsumerID";
   protected Long handle;
   protected int state;
   protected String stateString;
   protected String xidString;
   protected long sequenceNumber;
   protected String consumerID;

   protected MessageInfo() {
      this.initOpenInfo();
   }

   public MessageInfo(CompositeData var1) throws OpenDataException {
      this.readCompositeData(var1);
      this.initOpenInfo();
   }

   public MessageInfo(long var1, int var3, String var4, long var5, String var7) {
      this.handle = new Long(var1);
      this.state = var3;
      this.stateString = getStateString(var3);
      this.xidString = var4;
      this.sequenceNumber = var5;
      this.consumerID = var7;
      this.initOpenInfo();
   }

   public CompositeData toCompositeData() throws OpenDataException {
      CompositeDataSupport var1 = new CompositeDataSupport(this.getCompositeType(), this.getCompositeDataMap());
      return var1;
   }

   public Long getHandle() {
      return this.handle;
   }

   public void setHandle(Long var1) {
      this.handle = var1;
   }

   public int getState() {
      return this.state;
   }

   public void setState(int var1) {
      this.state = var1;
      this.stateString = getStateString(var1);
   }

   public String getStateString() {
      return this.stateString;
   }

   public String getXidString() {
      return this.xidString;
   }

   public void setXidString(String var1) {
      this.xidString = var1;
   }

   public long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public void setSequenceNumber(long var1) {
      this.sequenceNumber = var1;
   }

   public String getConsumerID() {
      return this.consumerID;
   }

   public void setConsumerID(String var1) {
      this.consumerID = var1;
   }

   public static String getStateString(int var0) {
      StringBuffer var1 = new StringBuffer();
      if (var0 == 1) {
         var1.append("visible");
         return var1.toString();
      } else {
         if ((var0 & 2) != 0) {
            var1.append("send ");
         }

         if ((var0 & 4) != 0) {
            var1.append("receive ");
         }

         if ((var0 & 8) != 0) {
            var1.append("transaction ");
         }

         if ((var0 & 16) != 0) {
            var1.append("ordered ");
         }

         if ((var0 & 32) != 0) {
            var1.append("delayed ");
         }

         if ((var0 & 64) != 0) {
            var1.append("expired ");
         }

         if ((var0 & 128) != 0) {
            var1.append("redelivery-count-exceeded ");
         }

         if ((var0 & 256) != 0) {
            var1.append("paused ");
         }

         if ((var0 & 512) != 0) {
            var1.append("sequenced ");
         }

         if ((var0 & 1024) != 0) {
            var1.append("unit-of-work-component ");
         }

         return var1.toString().trim();
      }
   }

   protected void initOpenInfo() {
      this.openItemNames.add("Handle");
      this.openItemNames.add("State");
      this.openItemNames.add("XidString");
      this.openItemNames.add("SequenceNumber");
      this.openItemNames.add("ConsumerID");
      this.openItemDescriptions.add("A handle that identifies this object in the cursor.");
      this.openItemDescriptions.add("The state of the message at the time of the management operation invocation.");
      this.openItemDescriptions.add("The Xid of the transaction for which this message is pending.");
      this.openItemDescriptions.add("The sequence number of the message that indicates its position in the FIFO ordering of the destination.");
      this.openItemDescriptions.add("Information that identifies the consumer of the message");
      this.openItemTypes.add(SimpleType.LONG);
      this.openItemTypes.add(SimpleType.INTEGER);
      this.openItemTypes.add(SimpleType.STRING);
      this.openItemTypes.add(SimpleType.LONG);
      this.openItemTypes.add(SimpleType.STRING);
   }

   protected void readCompositeData(CompositeData var1) throws OpenDataException {
      Long var2 = (Long)var1.get("Handle");
      if (var2 != null) {
         this.setHandle(var2);
      }

      Integer var3 = (Integer)var1.get("State");
      if (var3 != null) {
         this.setState(var3);
      }

      String var4 = (String)var1.get("XidString");
      if (var4 != null) {
         this.setXidString(var4);
      }

      Long var5 = (Long)var1.get("SequenceNumber");
      if (var5 != null) {
         this.setSequenceNumber(var5);
      }

      String var6 = (String)var1.get("ConsumerID");
      if (var6 != null) {
         this.setConsumerID(var6);
      }

   }

   protected Map getCompositeDataMap() throws OpenDataException {
      HashMap var1 = new HashMap();
      var1.put("Handle", this.handle);
      var1.put("State", new Integer(this.state));
      var1.put("XidString", this.xidString);
      var1.put("SequenceNumber", new Long(this.sequenceNumber));
      var1.put("ConsumerID", this.consumerID);
      return var1;
   }

   protected CompositeType getCompositeType() throws OpenDataException {
      CompositeType var1 = new CompositeType("MessageInfo", "This object represents message meta-data that describes the context of the message at the time the management operation was performed.", (String[])((String[])this.openItemNames.toArray(new String[this.openItemNames.size()])), (String[])((String[])this.openItemDescriptions.toArray(new String[this.openItemDescriptions.size()])), (OpenType[])((OpenType[])this.openItemTypes.toArray(new OpenType[this.openItemTypes.size()])));
      return var1;
   }
}
