package weblogic.jms.extensions;

import java.io.IOException;
import java.util.Map;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.StreamMessage;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.SimpleType;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.XMLHelper;
import weblogic.messaging.Message;
import weblogic.messaging.runtime.MessageInfo;

public class JMSMessageInfo extends MessageInfo {
   private static final String OPEN_TYPE_NAME = "JMSMessageInfo";
   private static final String OPEN_DESCRIPTION = "This object represents information about a JMS message.  In addition to the message itself, meta-data is included that describes the context of the message at the time the management operation was invoked.";
   private static final String ITEM_VERSION_NUMBER = "VersionNumber";
   private static final String ITEM_MESSAGE_XMLTEXT = "MessageXMLText";
   private static final String ITEM_MESSAGE_SIZE = "MessageSize";
   private static final String ITEM_DESTINATION_NAME = "DestinationName";
   private static final String ITEM_BODY_INCLUDED = "BodyIncluded";
   private static final int VERSION = 1;
   private WLMessage message;
   private long messageSize;
   private String destinationName;
   private boolean bodyIncluded;

   public JMSMessageInfo(CompositeData var1) throws OpenDataException {
      super(var1);
   }

   public JMSMessageInfo(long var1, int var3, String var4, long var5, String var7, WLMessage var8, String var9, boolean var10) {
      super(var1, var3, var4, var5, var7);
      this.message = var8;
      if (var8 != null) {
         this.messageSize = ((Message)var8).size();
      }

      this.destinationName = var9;
      this.bodyIncluded = var10;
   }

   public JMSMessageInfo(WLMessage var1) {
      this.message = var1;
      this.bodyIncluded = true;
   }

   protected void initOpenInfo() {
      super.initOpenInfo();
      this.openItemNames.add("VersionNumber");
      this.openItemNames.add("MessageXMLText");
      this.openItemNames.add("MessageSize");
      this.openItemNames.add("DestinationName");
      this.openItemNames.add("BodyIncluded");
      this.openItemDescriptions.add("The JMS version number.");
      this.openItemDescriptions.add("The message in XML String representation.  Note that the message body may be ommitted if the IncludeBody attribute is false.");
      this.openItemDescriptions.add("The size of the message in bytes.");
      this.openItemDescriptions.add("The destination name on which the message is pending.");
      this.openItemDescriptions.add("A boolean that indicates whether the JMS message item includes the body.");
      this.openItemTypes.add(SimpleType.INTEGER);
      this.openItemTypes.add(SimpleType.STRING);
      this.openItemTypes.add(SimpleType.LONG);
      this.openItemTypes.add(SimpleType.STRING);
      this.openItemTypes.add(SimpleType.BOOLEAN);
   }

   public WLMessage getMessage() {
      return this.message;
   }

   public void setMessage(WLMessage var1) {
      this.message = var1;
   }

   public long getMessageSize() {
      return this.messageSize;
   }

   public void setMessageSize(long var1) {
      this.messageSize = var1;
   }

   public String getDestinationName() {
      return this.destinationName;
   }

   public void setDestinationName(String var1) {
      this.destinationName = var1;
   }

   public boolean isBodyIncluded() {
      return this.bodyIncluded;
   }

   public void setBodyIncluded(boolean var1) {
      this.bodyIncluded = var1;
   }

   protected void readCompositeData(CompositeData var1) throws OpenDataException {
      super.readCompositeData(var1);
      String var2 = (String)var1.get("MessageXMLText");
      if (var2 != null) {
         WLMessage var3;
         try {
            var3 = XMLHelper.createMessage(var2);
            ((MessageImpl)var3).setPropertiesWritable(false);
            ((MessageImpl)var3).setBodyWritable(false);
            ((MessageImpl)var3).includeJMSXDeliveryCount(true);
            if (var3 instanceof BytesMessage) {
               ((BytesMessage)var3).reset();
            }

            if (var3 instanceof StreamMessage) {
               ((StreamMessage)var3).reset();
            }
         } catch (JMSException var6) {
            throw new OpenDataException(var6.toString());
         } catch (IOException var7) {
            throw new OpenDataException(var7.toString());
         } catch (ClassNotFoundException var8) {
            throw new OpenDataException(var8.toString());
         }

         this.setMessage(var3);
      }

      Long var9 = (Long)var1.get("MessageSize");
      if (var9 != null) {
         this.setMessageSize(var9);
      }

      String var4 = (String)var1.get("DestinationName");
      if (var4 != null) {
         this.setDestinationName(var4);
      }

      Boolean var5 = (Boolean)var1.get("BodyIncluded");
      if (var5 != null) {
         this.setBodyIncluded(var5);
      }

   }

   protected Map getCompositeDataMap() throws OpenDataException {
      Map var1 = super.getCompositeDataMap();
      var1.put("VersionNumber", new Integer(1));
      if (this.message != null) {
         try {
            var1.put("MessageXMLText", XMLHelper.getXMLText(this.message, this.bodyIncluded));
            var1.put("MessageSize", new Long(((Message)this.message).size()));
         } catch (JMSException var3) {
            throw new OpenDataException(var3.toString());
         }
      } else {
         var1.put("MessageXMLText", (Object)null);
         var1.put("MessageSize", new Long(0L));
      }

      var1.put("DestinationName", this.destinationName);
      var1.put("BodyIncluded", new Boolean(this.bodyIncluded));
      return var1;
   }
}
