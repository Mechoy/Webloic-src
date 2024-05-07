package weblogic.jms.extensions;

import java.util.HashMap;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

public class ConsumerInfo {
   private static final String OPEN_TYPE_NAME = "ConsumerInfo";
   private static final String OPEN_DESCRIPTION = "This object represents a JMS Consumer.";
   private static final String ITEM_VERSION_NUMBER = "VersionNumber";
   private static final String ITEM_NAME = "Name";
   private static final String ITEM_DURABLE = "Durable";
   private static final String ITEM_SELECTOR = "Selector";
   private static final String ITEM_CLIENT_ID = "ClientID";
   private static final String ITEM_NO_LOCAL = "NoLocal";
   private static final String ITEM_CONNECTION_ADDRESS = "ConnectionAddress";
   private static String[] itemNames = new String[]{"VersionNumber", "Name", "Durable", "Selector", "ClientID", "NoLocal", "ConnectionAddress"};
   private static String[] itemDescriptions = new String[]{"The version number.", "The name of the consumer/subscription.", "Indicates whether this consumer is a durable subscriber.", "The JMS message selector associated with this consumer.", "The clientID of this consumer's connection.", "The NoLocal attribute of this subscriber.", "Addressing information about the consumer's connection that consists of the client's host address"};
   private static OpenType[] itemTypes;
   private static final int VERSION = 1;
   private String name;
   private boolean durable;
   private String selector;
   private String clientID;
   private boolean noLocal;
   private String connectionAddress;

   public ConsumerInfo(CompositeData var1) {
      this.readCompositeData(var1);
   }

   public ConsumerInfo(String var1, boolean var2, String var3, String var4, boolean var5, String var6) {
      this.name = var1;
      this.durable = var2;
      this.selector = var3;
      this.clientID = var4;
      this.noLocal = var5;
      this.connectionAddress = var6;
   }

   public CompositeData toCompositeData() throws OpenDataException {
      CompositeDataSupport var1 = new CompositeDataSupport(this.getCompositeType(), this.getCompositeDataMap());
      return var1;
   }

   public String getClientID() {
      return this.clientID;
   }

   public void setClientID(String var1) {
      this.clientID = var1;
   }

   public String getConnectionAddress() {
      return this.connectionAddress;
   }

   public void setConnectionAddress(String var1) {
      this.connectionAddress = var1;
   }

   public boolean isDurable() {
      return this.durable;
   }

   public void setDurable(boolean var1) {
      this.durable = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public boolean isNoLocal() {
      return this.noLocal;
   }

   public void setNoLocal(boolean var1) {
      this.noLocal = var1;
   }

   public String getSelector() {
      return this.selector;
   }

   public void setSelector(String var1) {
      this.selector = var1;
   }

   protected void readCompositeData(CompositeData var1) {
      String var2 = (String)var1.get("Name");
      if (var2 != null) {
         this.setName(var2);
      }

      Boolean var3 = (Boolean)var1.get("Durable");
      if (var3 != null) {
         this.setDurable(var3);
      }

      String var4 = (String)var1.get("Selector");
      if (var4 != null) {
         this.setSelector(var4);
      }

      String var5 = (String)var1.get("ClientID");
      if (var5 != null) {
         this.setClientID(var5);
      }

      Boolean var6 = (Boolean)var1.get("NoLocal");
      if (var6 != null) {
         this.setNoLocal(var6);
      }

      String var7 = (String)var1.get("ConnectionAddress");
      if (var7 != null) {
         this.setConnectionAddress(var7);
      }

   }

   protected Map getCompositeDataMap() {
      HashMap var1 = new HashMap();
      var1.put("VersionNumber", new Integer(1));
      var1.put("Name", this.name);
      var1.put("Durable", new Boolean(this.durable));
      var1.put("Selector", this.selector);
      var1.put("ClientID", this.clientID);
      var1.put("NoLocal", new Boolean(this.noLocal));
      var1.put("ConnectionAddress", this.connectionAddress);
      return var1;
   }

   protected CompositeType getCompositeType() throws OpenDataException {
      CompositeType var1 = new CompositeType("ConsumerInfo", "This object represents a JMS Consumer.", itemNames, itemDescriptions, itemTypes);
      return var1;
   }

   static {
      itemTypes = new OpenType[]{SimpleType.INTEGER, SimpleType.STRING, SimpleType.BOOLEAN, SimpleType.STRING, SimpleType.STRING, SimpleType.BOOLEAN, SimpleType.STRING};
   }
}
