package weblogic.connector.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader;
import weblogic.application.descriptor.BasicMunger;

public class RAReader extends BasicMunger {
   private static final Map raNameChanges = new HashMap(1);
   private ArrayList resAdapterQueue;
   private ArrayList outResAdapterQueue;
   private ArrayList connDefQueue;
   private ArrayList configPropertyQueue;
   private boolean consume = false;
   private boolean inSecPerm = false;
   private boolean inOutResAdapter = false;
   private boolean inConnDef = false;
   private boolean inConfigProperty = false;
   private boolean inManagedConnectionFactoryClass = false;
   private char[] managedConnectionFactoryClass = null;
   private boolean inDisplayName = false;
   private StringBuffer displayName = null;

   public RAReader(XMLStreamReader var1, AbstractDescriptorLoader var2) {
      super(var1, var2);
   }

   public String getDtdNamespaceURI() {
      return "http://java.sun.com/xml/ns/j2ee";
   }

   public Map getLocalNameMap() {
      return raNameChanges;
   }

   public int next() throws XMLStreamException {
      int var1 = super.next();
      if (!this.playback && this.usingDTD()) {
         switch (var1) {
            case 1:
               if (this.getLocalName().equals("display-name")) {
                  this.inDisplayName = true;
                  this.displayName = new StringBuffer();
                  return this.skip(var1);
               }

               if ((this.getLocalName().equals("icon") || this.getLocalName().equals("vendor-name")) && this.displayName != null && this.displayName.length() > 0) {
                  this.getQueuedEvents().add(this.getQueuedEvent(1, "display-name"));
                  this.getQueuedEvents().add(this.getQueuedEvent(4, this.displayName.toString().toCharArray()));
                  this.getQueuedEvents().add(this.getQueuedEvent(2, "display-name"));
                  this.getQueuedEvents().add(this.getQueuedEvent(1, this.getLocalName()));
                  this.displayName = null;
                  this.setPlayback(true);
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("spec-version")) {
                  this.consume = true;
                  return this.skip(var1);
               }

               if (this.inConnDef) {
                  this.getConnDefQueue().add(this.getQueuedEvent(1, this.getLocalName()));
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("managedconnectionfactory-class")) {
                  this.inManagedConnectionFactoryClass = true;
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("config-property")) {
                  this.inConfigProperty = true;
                  this.getConfigPropertyQueue().add(this.getQueuedEvent(1, "config-property"));
                  return this.skip(var1);
               }

               if (this.inConfigProperty) {
                  this.getConfigPropertyQueue().add(this.getQueuedEvent(1, this.getLocalName()));
                  return this.skip(var1);
               }

               if (!this.getLocalName().equals("connectionfactory-interface") && !this.getLocalName().equals("connectionfactory-impl-class") && !this.getLocalName().equals("connection-interface") && !this.getLocalName().equals("connection-impl-class")) {
                  if (this.inOutResAdapter) {
                     this.getOutResAdapterQueue().add(this.getQueuedEvent(1, this.getLocalName()));
                     return this.skip(var1);
                  }

                  if (this.getLocalName().equals("transaction-support") || this.getLocalName().equals("authentication-mechanism") || this.getLocalName().equals("reauthentication-support")) {
                     this.inOutResAdapter = true;
                     this.getOutResAdapterQueue().add(this.getQueuedEvent(1, this.getLocalName()));
                     return this.skip(var1);
                  }

                  if (this.getLocalName().equals("resourceadapter")) {
                     this.getResAdapterQueue().add(this.getQueuedEvent(1, this.getLocalName()));
                     return this.skip(var1);
                  }

                  if (this.inSecPerm) {
                     this.getResAdapterQueue().add(this.getQueuedEvent(1, this.getLocalName()));
                     return this.skip(var1);
                  }

                  if (this.getLocalName().equals("security-permission")) {
                     this.inSecPerm = true;
                     this.getResAdapterQueue().add(this.getQueuedEvent(1, this.getLocalName()));
                     return this.skip(var1);
                  }
                  break;
               }

               this.inConnDef = true;
               this.getConnDefQueue().add(this.getQueuedEvent(1, this.getLocalName()));
               return this.skip(var1);
            case 2:
               if (this.inDisplayName && this.getLocalName().equals("display-name")) {
                  this.inDisplayName = false;
                  return this.skip(var1);
               }

               if (this.consume && this.getLocalName().equals("spec-version")) {
                  this.consume = false;
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("managedconnectionfactory-class")) {
                  this.inManagedConnectionFactoryClass = false;
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("config-property")) {
                  this.inConfigProperty = false;
                  this.getConfigPropertyQueue().add(this.getQueuedEvent(2, "config-property"));
                  return this.skip(var1);
               }

               if (this.inConfigProperty) {
                  this.getConfigPropertyQueue().add(this.getQueuedEvent(2, this.getLocalName()));
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("connectionfactory-interface") || this.getLocalName().equals("connectionfactory-impl-class") || this.getLocalName().equals("connection-interface") || this.getLocalName().equals("connection-impl-class")) {
                  this.inConnDef = false;
                  this.getConnDefQueue().add(this.getQueuedEvent(2, this.getLocalName()));
                  return this.skip(var1);
               }

               if (this.inConnDef) {
                  this.getConnDefQueue().add(this.getQueuedEvent(2, this.getLocalName()));
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("transaction-support") || this.getLocalName().equals("authentication-mechanism") || this.getLocalName().equals("reauthentication-support")) {
                  this.inOutResAdapter = false;
                  this.getOutResAdapterQueue().add(this.getQueuedEvent(2, this.getLocalName()));
                  return this.skip(var1);
               }

               if (this.inOutResAdapter) {
                  this.getOutResAdapterQueue().add(this.getQueuedEvent(2, this.getLocalName()));
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("security-permission")) {
                  this.inSecPerm = false;
                  this.getResAdapterQueue().add(this.getQueuedEvent(2, this.getLocalName()));
                  return this.skip(var1);
               }

               if (this.inSecPerm) {
                  this.getResAdapterQueue().add(this.getQueuedEvent(2, this.getLocalName()));
                  return this.skip(var1);
               }

               if (this.getLocalName().equals("connector")) {
                  if (this.configPropertyQueue != null) {
                     this.getConnDefQueue().addAll(1, this.getConfigPropertyQueue());
                  }

                  if (this.managedConnectionFactoryClass != null) {
                     this.getConnDefQueue().add(1, this.getQueuedEvent(2, "managedconnectionfactory-class"));
                     this.getConnDefQueue().add(1, this.getQueuedEvent(4, this.managedConnectionFactoryClass));
                     this.getConnDefQueue().add(1, this.getQueuedEvent(1, "managedconnectionfactory-class"));
                  }

                  if (this.connDefQueue != null) {
                     this.getConnDefQueue().add(this.getQueuedEvent(2, "connection-definition"));
                     this.getOutResAdapterQueue().addAll(1, this.getConnDefQueue());
                  }

                  if (this.outResAdapterQueue != null) {
                     this.getOutResAdapterQueue().add(this.getQueuedEvent(2, "outbound-resourceadapter"));
                     this.getResAdapterQueue().addAll(1, this.getOutResAdapterQueue());
                  }

                  if (this.resAdapterQueue != null) {
                     this.getQueuedEvents().addAll(this.getResAdapterQueue());
                     this.getQueuedEvents().add(this.getQueuedEvent(2, "resourceadapter"));
                  }

                  this.getQueuedEvents().add(this.getQueuedEvent(2, "connector"));
                  this.playback = true;
                  return this.next();
               }

               if (this.getLocalName().equals("resourceadapter")) {
                  return this.skip(var1);
               }
            case 3:
            default:
               break;
            case 4:
               char[] var2 = this.getTextCharacters();
               String var3 = new String(var2);
               String var4;
               if (var3.equals("javax.resource.security.PasswordCredential")) {
                  var4 = "javax.resource.spi.security.PasswordCredential";
                  var2 = var4.toCharArray();
               } else if (var3.equals("javax.resource.security.GenericCredential")) {
                  var4 = "javax.resource.spi.security.GenericCredential";
                  var2 = var4.toCharArray();
               }

               if (this.consume) {
                  return this.skip(var1);
               }

               if (this.inManagedConnectionFactoryClass) {
                  this.managedConnectionFactoryClass = var2;
                  return this.skip(var1);
               }

               if (this.inConfigProperty) {
                  this.getConfigPropertyQueue().add(this.getQueuedEvent(4, var2));
                  return this.skip(var1);
               }

               if (this.inConnDef) {
                  this.getConnDefQueue().add(this.getQueuedEvent(4, var2));
                  return this.skip(var1);
               }

               if (this.inOutResAdapter) {
                  this.getOutResAdapterQueue().add(this.getQueuedEvent(4, var2));
                  return this.skip(var1);
               }

               if (this.inSecPerm) {
                  this.getResAdapterQueue().add(this.getQueuedEvent(4, var2));
                  return this.skip(var1);
               }

               if (this.inDisplayName && var3 != null) {
                  this.displayName.append(var3.trim());
                  return this.skip(var1);
               }
         }

         return var1;
      } else {
         return var1;
      }
   }

   private ArrayList getQueuedEvents() {
      if (this.queuedEvents == null) {
         this.queuedEvents = new ArrayList();
      }

      return this.queuedEvents;
   }

   private ArrayList getResAdapterQueue() {
      if (this.resAdapterQueue == null) {
         this.resAdapterQueue = new ArrayList();
      }

      return this.resAdapterQueue;
   }

   private ArrayList getOutResAdapterQueue() {
      if (this.outResAdapterQueue == null) {
         this.outResAdapterQueue = new ArrayList();
         this.outResAdapterQueue.add(this.getQueuedEvent(1, "outbound-resourceadapter"));
      }

      return this.outResAdapterQueue;
   }

   private ArrayList getConnDefQueue() {
      if (this.connDefQueue == null) {
         this.connDefQueue = new ArrayList();
         this.getConnDefQueue().add(0, this.getQueuedEvent(1, "connection-definition"));
      }

      return this.connDefQueue;
   }

   private ArrayList getConfigPropertyQueue() {
      if (this.configPropertyQueue == null) {
         this.configPropertyQueue = new ArrayList();
      }

      return this.configPropertyQueue;
   }

   public boolean supportsValidation() {
      return true;
   }

   static {
      raNameChanges.put("version", "resourceadapter-version");
   }
}
