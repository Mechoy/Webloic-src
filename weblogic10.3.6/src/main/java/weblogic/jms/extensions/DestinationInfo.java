package weblogic.jms.extensions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import weblogic.jms.common.DestinationImpl;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.encoders.BASE64Encoder;

public class DestinationInfo {
   private static final String OPEN_TYPE_NAME = "DestinationInfo";
   private static final String OPEN_DESCRIPTION = "This object represents a JMS Destination.";
   private static final String ITEM_VERSION_NUMBER = "VersionNumber";
   private static final String ITEM_NAME = "Name";
   private static final String ITEM_SERVER_NAME = "ServerName";
   private static final String ITEM_APPLICATION_NAME = "ApplicationName";
   private static final String ITEM_MODULE_NAME = "ModuleName";
   private static final String ITEM_TOPIC = "Topic";
   private static final String ITEM_QUEUE = "Queue";
   private static final String ITEM_SERIALIZED_DESTINATION = "SerializedDestination";
   private static String[] itemNames = new String[]{"VersionNumber", "Name", "ServerName", "ApplicationName", "ModuleName", "Topic", "Queue", "SerializedDestination"};
   private static String[] itemDescriptions = new String[]{"The version number.", "The name of the destination.", "The name of the JMS server hosting the destination.", "The name of the application that the destination is associated with.", "The name of the module that the destination is associated with.", "Indicates whether the destination is a topic.", "Indicates whether the destination is a queue.", "The serialized Destination instance."};
   private static OpenType[] itemTypes;
   private static final int VERSION = 1;
   private String name;
   private String serverName;
   private String applicationName;
   private String moduleName;
   private boolean topic;
   private boolean queue;
   private WLDestination destination;

   public DestinationInfo(CompositeData var1) throws OpenDataException {
      this.readCompositeData(var1);
   }

   public DestinationInfo(WLDestination var1) {
      this.destination = var1;
      DestinationImpl var2 = (DestinationImpl)var1;
      this.name = var2.getName();
      this.serverName = var2.getServerName();
      this.applicationName = var2.getApplicationName();
      this.moduleName = var2.getModuleName();
      this.topic = var2.isTopic();
      this.queue = var2.isQueue();
   }

   public CompositeData toCompositeData() throws OpenDataException {
      CompositeDataSupport var1 = new CompositeDataSupport(this.getCompositeType(), this.getCompositeDataMap());
      return var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public void setApplicationName(String var1) {
      this.applicationName = var1;
   }

   public WLDestination getDestination() {
      return this.destination;
   }

   public void setDestination(WLDestination var1) {
      this.destination = var1;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public void setModuleName(String var1) {
      this.moduleName = var1;
   }

   public boolean isQueue() {
      return this.queue;
   }

   public void setQueue(boolean var1) {
      this.queue = var1;
   }

   public String getServerName() {
      return this.serverName;
   }

   public void setServerName(String var1) {
      this.serverName = var1;
   }

   public boolean isTopic() {
      return this.topic;
   }

   public void setTopic(boolean var1) {
      this.topic = var1;
   }

   protected void readCompositeData(CompositeData var1) throws OpenDataException {
      String var2 = (String)var1.get("Name");
      if (var2 != null) {
         this.setName(var2);
      }

      String var3 = (String)var1.get("ServerName");
      if (var3 != null) {
         this.setServerName(var3);
      }

      String var4 = (String)var1.get("ApplicationName");
      if (var4 != null) {
         this.setApplicationName(var4);
      }

      String var5 = (String)var1.get("ModuleName");
      if (var5 != null) {
         this.setModuleName(var5);
      }

      Boolean var6 = (Boolean)var1.get("Queue");
      if (var6 != null) {
         this.setQueue(var6);
      }

      Boolean var7 = (Boolean)var1.get("Topic");
      if (var7 != null) {
         this.setTopic(var7);
      }

      String var8 = (String)var1.get("SerializedDestination");
      if (var8 != null) {
         OpenDataException var10;
         try {
            BASE64Decoder var9 = new BASE64Decoder();
            byte[] var15 = var9.decodeBuffer(var8);
            ByteArrayInputStream var11 = new ByteArrayInputStream(var15);
            ObjectInputStream var12 = new ObjectInputStream(var11);
            this.setDestination((WLDestination)var12.readObject());
         } catch (IOException var13) {
            var10 = new OpenDataException("Unable to deserialize destination.");
            var10.initCause(var13);
            throw var10;
         } catch (ClassNotFoundException var14) {
            var10 = new OpenDataException("Unable to deserialize destination.");
            var10.initCause(var14);
            throw var10;
         }
      }

   }

   protected Map getCompositeDataMap() throws OpenDataException {
      HashMap var1 = new HashMap();
      var1.put("VersionNumber", new Integer(1));
      var1.put("Name", this.name);
      var1.put("ServerName", this.serverName);
      var1.put("ApplicationName", this.applicationName);
      var1.put("ModuleName", this.moduleName);
      var1.put("Topic", new Boolean(this.topic));
      var1.put("Queue", new Boolean(this.queue));

      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ObjectOutputStream var7 = new ObjectOutputStream(var2);
         var7.writeObject(this.destination);
         BASE64Encoder var4 = new BASE64Encoder();
         String var5 = var4.encodeBuffer(var2.toByteArray());
         var1.put("SerializedDestination", var5);
         return var1;
      } catch (IOException var6) {
         OpenDataException var3 = new OpenDataException("Unable to serialize destination.");
         var3.initCause(var6);
         throw var3;
      }
   }

   protected CompositeType getCompositeType() throws OpenDataException {
      CompositeType var1 = new CompositeType("DestinationInfo", "This object represents a JMS Destination.", itemNames, itemDescriptions, itemTypes);
      return var1;
   }

   static {
      itemTypes = new OpenType[]{SimpleType.INTEGER, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.BOOLEAN, SimpleType.BOOLEAN, SimpleType.STRING};
   }
}
