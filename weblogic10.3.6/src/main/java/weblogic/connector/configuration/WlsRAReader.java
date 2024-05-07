package weblogic.connector.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader;
import weblogic.application.descriptor.BasicMunger;
import weblogic.application.descriptor.ReaderEvent;
import weblogic.connector.common.Debug;
import weblogic.j2ee.descriptor.ConnectionDefinitionBean;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.OutboundResourceAdapterBean;
import weblogic.j2ee.descriptor.ResourceAdapterBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;

public class WlsRAReader extends BasicMunger {
   private boolean createExtensionBean = true;
   private static final Map wlraNameChanges = new HashMap();
   ConnectorBean connBean;
   ArrayList linkRefQueue = null;
   ArrayList proxyQueue = null;
   ArrayList loggingQueue = null;
   ArrayList nativeLibdirQueue = null;
   ArrayList descriptionQueue = null;
   ArrayList jndiNameQueue = null;
   ArrayList configPropQueue = null;
   ArrayList poolParamsQueue = null;
   ArrayList unknownQueue = null;
   ArrayList currentQueue = null;
   boolean inConfigProps = false;
   boolean ignore = false;
   char[] initial_capacity = null;
   char[] max_capacity = null;
   char[] capacity_increment = null;
   char[] shrinking_enabled = null;
   char[] shrink_frequency_seconds = null;
   char[] highest_num_waiters = null;
   char[] highest_num_unavailable = null;
   char[] connection_creation_retry_frequency_seconds = null;
   char[] connection_reserve_timeout_seconds = null;
   char[] test_frequency_seconds = null;
   char[] match_connections_supported = null;
   char[] inactive_connection_timeout_seconds = null;
   String lastLocalElement;
   boolean debug = false;

   public WlsRAReader(ConnectorBean var1, XMLStreamReader var2, AbstractDescriptorLoader var3, DeploymentPlanBean var4, String var5, String var6) {
      super(var2, var3, var4, var5, "rar", var6);
      this.connBean = var1;
      this.createExtensionBean = true;
   }

   public WlsRAReader(ConnectorBean var1, XMLStreamReader var2, AbstractDescriptorLoader var3, DeploymentPlanBean var4, String var5, String var6, boolean var7) {
      super(var2, var3, var4, var5, "rar", var6);
      this.connBean = var1;
      this.createExtensionBean = var7;
   }

   public String getDtdNamespaceURI() {
      return "http://xmlns.oracle.com/weblogic/weblogic-connector";
   }

   public Map getLocalNameMap() {
      return wlraNameChanges;
   }

   public int next() throws XMLStreamException {
      int var1 = super.next();
      if (!this.playback && this.usingDTD()) {
         switch (var1) {
            case 1:
               this.lastLocalElement = this.getLocalName();
               if (this.lastLocalElement.equals("native-libdir")) {
                  this.currentQueue = this.getNativeLibdirQueue();
                  this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
               } else if (!this.lastLocalElement.equals("use-connection-proxies") && !this.lastLocalElement.equals("connection-profiling-enabled")) {
                  if (!this.lastLocalElement.equals("connection-factory-name") && !this.lastLocalElement.equals("ra-link-ref")) {
                     if (this.lastLocalElement.equals("description")) {
                        this.currentQueue = this.getDescriptionQueue();
                        this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
                     } else if (this.lastLocalElement.equals("jndi-name")) {
                        this.currentQueue = this.getJNDINameQueue();
                        this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
                     } else if (!this.lastLocalElement.equals("logging-enabled") && !this.lastLocalElement.equals("log-filename")) {
                        if (this.lastLocalElement.equals("property")) {
                           this.inConfigProps = true;
                           this.currentQueue = this.getConfigPropQueue();
                           this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
                        } else if (!this.lastLocalElement.equals("name") && !this.lastLocalElement.equals("value")) {
                           if (this.lastLocalElement.equals("security-principal-map")) {
                              Debug.logSecurityPrincipalMapNotAllowed();
                              this.currentQueue = this.getUknownQueue();
                              this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
                           } else if (!this.lastLocalElement.equals("initial-capacity") && !this.lastLocalElement.equals("capacity-increment") && !this.lastLocalElement.equals("connection-cleanup-frequency") && !this.lastLocalElement.equals("connection-creation-retry-frequency-seconds") && !this.lastLocalElement.equals("connection-duration-time") && !this.lastLocalElement.equals("connection-maxidle-time") && !this.lastLocalElement.equals("connection-reserve-timeout-seconds") && !this.lastLocalElement.equals("highest-num-unavailable") && !this.lastLocalElement.equals("highest-num-waiters") && !this.lastLocalElement.equals("inactive-connection-timeout-seconds") && !this.lastLocalElement.equals("map-config-property") && !this.lastLocalElement.equals("match-connections-supported") && !this.lastLocalElement.equals("max-capacity") && !this.lastLocalElement.equals("pool-params") && !this.lastLocalElement.equals("shrink-frequency-seconds") && !this.lastLocalElement.equals("shrink-period-minutes") && !this.lastLocalElement.equals("shrinking-enabled") && !this.lastLocalElement.equals("test-frequency-seconds") && !this.lastLocalElement.equals("weblogic-connection-factory") && !this.ignore) {
                              this.currentQueue = this.getUknownQueue();
                              this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
                           }
                        } else if (this.currentQueue != null) {
                           this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
                        }
                     } else {
                        this.currentQueue = this.getLoggingQueue();
                        this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
                     }
                  } else {
                     this.currentQueue = this.getLinkRefQueue();
                     this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
                  }
               } else {
                  this.currentQueue = this.getProxyQueue();
                  this.currentQueue.add(this.getQueuedEvent(1, this.lastLocalElement));
               }

               return this.skip(var1);
            case 2:
               if (this.getLocalName().equals("weblogic-connection-factory")) {
                  if (this.queuedEvents != null && this.queuedEvents.size() == 0) {
                     return var1;
                  }

                  this.buildQueuedEvents();
                  if (this.debug) {
                     this.toXML();
                  }

                  this.setPlayback(true);
                  return this.next();
               }

               if (this.currentQueue != null) {
                  if (this.getLocalName().equals("property")) {
                     this.inConfigProps = false;
                  }

                  this.currentQueue.add(this.getQueuedEvent(2, this.getLocalName()));
                  if (!this.inConfigProps) {
                     this.currentQueue = null;
                  }
               }

               return this.skip(var1);
            case 3:
            default:
               return var1;
            case 4:
               if (!this.isWhiteSpace() && !this.ignore) {
                  if (this.currentQueue == null) {
                     if (this.lastLocalElement.equals("inactive-connection-timeout-seconds")) {
                        this.inactive_connection_timeout_seconds = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("connection-maxidle-time")) {
                        if (this.inactive_connection_timeout_seconds == null) {
                           this.inactive_connection_timeout_seconds = this.getTextCharacters();
                        }
                     } else if (this.lastLocalElement.equals("initial-capacity")) {
                        this.initial_capacity = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("max-capacity")) {
                        this.max_capacity = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("capacity-increment")) {
                        this.capacity_increment = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("shrinking-enabled")) {
                        this.shrinking_enabled = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("shrink-period-minutes")) {
                        if (this.shrink_frequency_seconds == null) {
                           try {
                              String var2 = new String(this.getTextCharacters());
                              if (var2 != null && var2.length() > 0) {
                                 int var3 = Integer.parseInt(var2);
                                 int var4 = var3 * 60;
                                 String var5 = Integer.toString(var4);
                                 this.shrink_frequency_seconds = var5.toCharArray();
                              }
                           } catch (Exception var6) {
                              this.shrink_frequency_seconds = this.getTextCharacters();
                           }
                        }
                     } else if (this.lastLocalElement.equals("shrink-frequency-seconds")) {
                        this.shrink_frequency_seconds = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("highest-num-waiters")) {
                        this.highest_num_waiters = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("highest-num-unavailable")) {
                        this.highest_num_unavailable = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("connection-creation-retry-frequency-seconds")) {
                        this.connection_creation_retry_frequency_seconds = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("connection-reserve-timeout-seconds")) {
                        this.connection_reserve_timeout_seconds = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("test-frequency-seconds")) {
                        this.test_frequency_seconds = this.getTextCharacters();
                     } else if (this.lastLocalElement.equals("match-connections-supported")) {
                        this.match_connections_supported = this.getTextCharacters();
                     }
                  } else {
                     this.currentQueue.add(this.getQueuedEvent(4, this.getTextCharacters()));
                  }
               }

               return this.skip(var1);
         }
      } else {
         return var1;
      }
   }

   private void buildQueuedEvents() {
      if (this.createExtensionBean) {
         this.getQueuedEvents().add(this.getQueuedEvent(1, "weblogic-connector-extension"));
      } else {
         this.getQueuedEvents().add(this.getQueuedEvent(1, "weblogic-connector"));
      }

      if (this.nativeLibdirQueue != null) {
         this.getQueuedEvents().addAll(this.getNativeLibdirQueue());
      }

      this.getQueuedEvents().add(this.getQueuedEvent(1, "enable-access-outside-app"));
      this.getQueuedEvents().add(this.getQueuedEvent(4, "true".toCharArray()));
      this.getQueuedEvents().add(this.getQueuedEvent(2, "enable-access-outside-app"));
      this.buildOutboundResourceAdapter();
      if (this.createExtensionBean && this.linkRefQueue != null) {
         this.getQueuedEvents().add(this.getQueuedEvent(1, "link-ref"));
         this.getQueuedEvents().addAll(this.getLinkRefQueue());
         this.getQueuedEvents().add(this.getQueuedEvent(2, "link-ref"));
      }

      if (this.createExtensionBean && (this.proxyQueue != null || this.inactive_connection_timeout_seconds != null)) {
         this.getQueuedEvents().add(this.getQueuedEvent(1, "proxy"));
         if (this.inactive_connection_timeout_seconds != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "inactive-connection-timeout-seconds"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.inactive_connection_timeout_seconds));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "inactive-connection-timeout-seconds"));
         }

         if (this.proxyQueue != null) {
            this.getQueuedEvents().addAll(this.getProxyQueue());
         }

         this.getQueuedEvents().add(this.getQueuedEvent(2, "proxy"));
      }

      if (this.unknownQueue != null) {
         this.getQueuedEvents().addAll(this.getUknownQueue());
      }

      if (this.createExtensionBean) {
         this.getQueuedEvents().add(this.getQueuedEvent(2, "weblogic-connector-extension"));
      } else {
         this.getQueuedEvents().add(this.getQueuedEvent(2, "weblogic-connector"));
      }

   }

   private void buildOutboundResourceAdapter() {
      ResourceAdapterBean var1 = null;
      OutboundResourceAdapterBean var2 = null;
      ConnectionDefinitionBean[] var3 = null;
      char[] var4 = null;
      String var5 = "";
      this.getQueuedEvents().add(this.getQueuedEvent(1, "outbound-resource-adapter"));
      this.getQueuedEvents().add(this.getQueuedEvent(1, "connection-definition-group"));
      var5 = var5 + "/connector";
      if (this.connBean != null) {
         var5 = var5 + "/resourceadapter";
         var1 = this.connBean.getResourceAdapter();
         if (var1 != null) {
            var5 = var5 + "/outbound-resourceadapter";
            var2 = var1.getOutboundResourceAdapter();
            if (var2 != null) {
               var5 = var5 + "/connection-definition";
               var3 = var2.getConnectionDefinitions();
               if (var3 != null && var3.length > 0) {
                  var5 = var5 + "/connectionfactory-interface";
                  String var6 = var3[0].getConnectionFactoryInterface();
                  if (var6 != null) {
                     var4 = var6.toCharArray();
                  }
               }
            }
         }

         if (var4 == null) {
            var4 = new char[]{' '};
            Debug.logBuildOutboundFailed(var5);
         }
      } else {
         var4 = "LinkRef".toCharArray();
      }

      this.getQueuedEvents().add(this.getQueuedEvent(1, "connection-factory-interface"));
      this.getQueuedEvents().add(this.getQueuedEvent(4, var4));
      this.getQueuedEvents().add(this.getQueuedEvent(2, "connection-factory-interface"));
      this.getQueuedEvents().add(this.getQueuedEvent(1, "connection-instance"));
      if (this.descriptionQueue != null) {
         this.getQueuedEvents().addAll(this.getDescriptionQueue());
      }

      if (this.jndiNameQueue != null) {
         this.getQueuedEvents().addAll(this.getJNDINameQueue());
      }

      this.getQueuedEvents().add(this.getQueuedEvent(1, "connection-properties"));
      this.buildPoolParams();
      if (this.loggingQueue != null) {
         this.getQueuedEvents().add(this.getQueuedEvent(1, "logging"));
         this.getQueuedEvents().addAll(this.getLoggingQueue());
         this.getQueuedEvents().add(this.getQueuedEvent(2, "logging"));
      }

      if (this.configPropQueue != null) {
         this.getQueuedEvents().add(this.getQueuedEvent(1, "properties"));
         this.getQueuedEvents().addAll(this.getConfigPropQueue());
         this.getQueuedEvents().add(this.getQueuedEvent(2, "properties"));
      }

      this.getQueuedEvents().add(this.getQueuedEvent(2, "connection-properties"));
      this.getQueuedEvents().add(this.getQueuedEvent(2, "connection-instance"));
      this.getQueuedEvents().add(this.getQueuedEvent(2, "connection-definition-group"));
      this.getQueuedEvents().add(this.getQueuedEvent(2, "outbound-resource-adapter"));
   }

   private void buildPoolParams() {
      if (this.initial_capacity != null || this.max_capacity != null || this.capacity_increment != null || this.shrinking_enabled != null || this.shrink_frequency_seconds != null || this.highest_num_waiters != null || this.highest_num_unavailable != null || this.connection_creation_retry_frequency_seconds != null || this.connection_reserve_timeout_seconds != null || this.test_frequency_seconds != null || this.match_connections_supported != null) {
         this.getQueuedEvents().add(this.getQueuedEvent(1, "pool-params"));
         if (this.initial_capacity != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "initial-capacity"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.initial_capacity));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "initial-capacity"));
         }

         if (this.max_capacity != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "max-capacity"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.max_capacity));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "max-capacity"));
         }

         if (this.capacity_increment != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "capacity-increment"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.capacity_increment));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "capacity-increment"));
         }

         if (this.shrinking_enabled != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "shrinking-enabled"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.shrinking_enabled));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "shrinking-enabled"));
         }

         if (this.shrink_frequency_seconds != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "shrink-frequency-seconds"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.shrink_frequency_seconds));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "shrink-frequency-seconds"));
         }

         if (this.highest_num_waiters != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "highest-num-waiters"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.highest_num_waiters));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "highest-num-waiters"));
         }

         if (this.highest_num_unavailable != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "highest-num-unavailable"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.highest_num_unavailable));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "highest-num-unavailable"));
         }

         if (this.connection_creation_retry_frequency_seconds != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "connection-creation-retry-frequency-seconds"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.connection_creation_retry_frequency_seconds));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "connection-creation-retry-frequency-seconds"));
         }

         if (this.connection_reserve_timeout_seconds != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "connection-reserve-timeout-seconds"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.connection_reserve_timeout_seconds));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "connection-reserve-timeout-seconds"));
         }

         if (this.test_frequency_seconds != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "test-frequency-seconds"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.test_frequency_seconds));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "test-frequency-seconds"));
         }

         if (this.match_connections_supported != null) {
            this.getQueuedEvents().add(this.getQueuedEvent(1, "match-connections-supported"));
            this.getQueuedEvents().add(this.getQueuedEvent(4, this.match_connections_supported));
            this.getQueuedEvents().add(this.getQueuedEvent(2, "match-connections-supported"));
         }

         this.getQueuedEvents().add(this.getQueuedEvent(2, "pool-params"));
      }

   }

   private void toXML() {
      int var1 = 0;
      ReaderEvent var2 = null;
      String var3 = "";

      for(int var4 = 0; var4 < this.getQueuedEvents().size(); ++var4) {
         var2 = (ReaderEvent)this.getQueuedEvents().get(var4);
         if (var2 != null) {
            switch (var2.getEventType()) {
               case 1:
                  ++var1;
                  var3 = this.indent(var1) + "<" + var2.getLocalName() + ">";
                  break;
               case 2:
                  var3 = this.indent(var1) + "</" + var2.getLocalName() + ">";
                  --var1;
               case 3:
               default:
                  break;
               case 4:
                  if (var2.getCharacters() != null) {
                     var3 = this.indent(var1) + new String((char[])var2.getCharacters());
                  } else {
                     var3 = "";
                  }
            }

            System.out.println(var3);
         }
      }

   }

   private String indent(int var1) {
      String var2 = "";
      String var3 = "  ";

      for(int var4 = 0; var4 < var1; ++var4) {
         var2 = var2 + var3;
      }

      return var2;
   }

   private ArrayList getQueuedEvents() {
      if (this.queuedEvents == null) {
         this.queuedEvents = new ArrayList();
      }

      return this.queuedEvents;
   }

   private ArrayList getUknownQueue() {
      if (this.unknownQueue == null) {
         this.unknownQueue = new ArrayList();
      }

      return this.unknownQueue;
   }

   private ArrayList getConfigPropQueue() {
      if (this.configPropQueue == null) {
         this.configPropQueue = new ArrayList();
      }

      return this.configPropQueue;
   }

   private ArrayList getDescriptionQueue() {
      if (this.descriptionQueue == null) {
         this.descriptionQueue = new ArrayList();
      }

      return this.descriptionQueue;
   }

   private ArrayList getJNDINameQueue() {
      if (this.jndiNameQueue == null) {
         this.jndiNameQueue = new ArrayList();
      }

      return this.jndiNameQueue;
   }

   private ArrayList getLinkRefQueue() {
      if (this.linkRefQueue == null) {
         this.linkRefQueue = new ArrayList();
      }

      return this.linkRefQueue;
   }

   private ArrayList getLoggingQueue() {
      if (this.loggingQueue == null) {
         this.loggingQueue = new ArrayList();
      }

      return this.loggingQueue;
   }

   private ArrayList getProxyQueue() {
      if (this.proxyQueue == null) {
         this.proxyQueue = new ArrayList();
      }

      return this.proxyQueue;
   }

   private ArrayList getNativeLibdirQueue() {
      if (this.nativeLibdirQueue == null) {
         this.nativeLibdirQueue = new ArrayList();
      }

      return this.nativeLibdirQueue;
   }

   public boolean supportsValidation() {
      return true;
   }

   static {
      wlraNameChanges.put("weblogic-connection-factory-dd", "weblogic-connection-factory");
      wlraNameChanges.put("map-config-property", "property");
      wlraNameChanges.put("map-config-property-name", "name");
      wlraNameChanges.put("map-config-property-value", "value");
   }
}
