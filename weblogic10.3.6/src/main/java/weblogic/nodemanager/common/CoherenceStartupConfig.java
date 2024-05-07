package weblogic.nodemanager.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import weblogic.nodemanager.NodeManagerTextTextFormatter;

public class CoherenceStartupConfig extends StartupConfig {
   public static String UNICAST_PORT_PROP = "UnicastListenPort";
   public static String UNICAST_ADDR_PROP = "UnicastListenAddress";
   public static String UNICAST_PORT_AUTO_ADJUST_PROP = "UnicastPortAutoAdjust";
   public static String MULTICAST_PORT_PROP = "MulticastListenPort";
   public static String MULTICAST_ADDR_PROP = "MulticastListenAddress";
   public static String TTL_PROP = "TTL";
   public static String WKA_PROP = "WellKnownAddresses";
   public static String CLUSTER_CONFIG_FILE_PROP = "ClusterConfigurationFileName";
   public static String CLUSTER_NAME = "ClusterName";
   public static final int DEFAULT_TTL = 4;
   public static final boolean DEFAULT_UNICAST_PORT_AUTO_ADJUST = true;
   private String unicastListenAddress;
   private int unicastListenPort;
   private boolean unicastPortAutoAdjust = true;
   private String multicastListenAddress;
   private int multicastListenPort;
   private int timeToLive = 4;
   private final ArrayList<WellKnownAddress> wellKnownAddresses = new ArrayList();
   private String customClusterConfigurationFileName;
   private String clusterName;

   public CoherenceStartupConfig(Properties var1) throws ConfigException {
      super(var1);
      this.loadProperties();
   }

   public CoherenceStartupConfig() {
   }

   private void loadProperties() throws ConfigException {
      this.unicastListenAddress = this.getProperty(UNICAST_ADDR_PROP);
      this.unicastListenPort = this.getIntProperty(UNICAST_PORT_PROP, 0);
      this.unicastPortAutoAdjust = this.getBooleanProperty(UNICAST_PORT_AUTO_ADJUST_PROP, true);
      this.multicastListenAddress = this.getProperty(MULTICAST_ADDR_PROP);
      this.multicastListenPort = this.getIntProperty(MULTICAST_PORT_PROP, 0);
      this.timeToLive = this.getIntProperty(TTL_PROP, 4);
      this.customClusterConfigurationFileName = this.getProperty(CLUSTER_CONFIG_FILE_PROP);
      this.clusterName = this.getProperty(CLUSTER_NAME);
      this.loadWka(this.props);
   }

   public Properties getStartupProperties() {
      Properties var1 = super.getStartupProperties();
      if (this.unicastListenAddress != null) {
         var1.setProperty(UNICAST_ADDR_PROP, this.unicastListenAddress);
      }

      if (this.unicastListenPort != 0) {
         var1.setProperty(UNICAST_PORT_PROP, "" + this.unicastListenPort);
      }

      if (!this.unicastPortAutoAdjust) {
         var1.setProperty(UNICAST_PORT_AUTO_ADJUST_PROP, Boolean.toString(this.unicastPortAutoAdjust));
      }

      if (this.multicastListenAddress != null) {
         var1.setProperty(MULTICAST_ADDR_PROP, this.multicastListenAddress);
      }

      if (this.multicastListenPort != 0) {
         var1.setProperty(MULTICAST_PORT_PROP, "" + this.multicastListenPort);
      }

      if (this.timeToLive != 4) {
         var1.setProperty(TTL_PROP, "" + this.timeToLive);
      }

      if (this.customClusterConfigurationFileName != null) {
         var1.setProperty(CLUSTER_CONFIG_FILE_PROP, this.customClusterConfigurationFileName);
      }

      if (this.clusterName != null) {
         var1.setProperty(CLUSTER_NAME, this.clusterName);
      }

      this.getStartupWka(var1);
      return var1;
   }

   public String getUnicastListenAddress() {
      return this.unicastListenAddress;
   }

   public void setUnicastListenAddress(String var1) {
      this.unicastListenAddress = var1;
   }

   public int getUnicastListenPort() {
      return this.unicastListenPort;
   }

   public void setUnicastListenPort(int var1) {
      this.unicastListenPort = var1;
   }

   public boolean isUnicastPortAutoAdjust() {
      return this.unicastPortAutoAdjust;
   }

   public void setUnicastPortAutoAdjust(boolean var1) {
      this.unicastPortAutoAdjust = var1;
   }

   public String getMulticastListenAddress() {
      return this.multicastListenAddress;
   }

   public void setMulticastListenAddress(String var1) {
      this.multicastListenAddress = var1;
   }

   public int getMulticastListenPort() {
      return this.multicastListenPort;
   }

   public void setMulticastListenPort(int var1) {
      this.multicastListenPort = var1;
   }

   public int getTimeToLive() {
      return this.timeToLive;
   }

   public void setTimeToLive(int var1) {
      this.timeToLive = var1;
   }

   public void addWellKnownAddress(String var1, String var2, int var3) {
      this.wellKnownAddresses.add(new WellKnownAddress(var1, var2, var3));
   }

   public WellKnownAddress[] getWellKnownAddresses() {
      return (WellKnownAddress[])this.wellKnownAddresses.toArray(new WellKnownAddress[this.wellKnownAddresses.size()]);
   }

   public String getCustomClusterConfigurationFileName() {
      return this.customClusterConfigurationFileName;
   }

   public void setCustomClusterConfigurationFileName(String var1) {
      this.customClusterConfigurationFileName = var1;
   }

   public String getClusterName() {
      return this.clusterName;
   }

   public void setClusterName(String var1) {
      this.clusterName = var1;
   }

   private void getStartupWka(Properties var1) {
      if (this.wellKnownAddresses.size() != 0) {
         StringBuilder var2 = new StringBuilder();
         Iterator var3 = this.wellKnownAddresses.iterator();

         while(var3.hasNext()) {
            WellKnownAddress var4 = (WellKnownAddress)var3.next();
            if (var2.length() != 0) {
               var2.append(";");
            }

            var2.append(var4.getName()).append(",");
            var2.append(var4.getListenAddress()).append(",");
            var2.append(var4.getListenPort());
         }

         var1.setProperty(WKA_PROP, var2.toString());
      }
   }

   private void loadWka(Properties var1) throws ConfigException {
      String var2 = this.trim(var1.getProperty(WKA_PROP));
      if (var2 != null) {
         String[] var3 = var2.split(";");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            String[] var7 = var6.split(",");
            if (var7.length != 3) {
               throw new ConfigException(NodeManagerTextTextFormatter.getInstance().getInvalidPropValue(WKA_PROP, var2));
            }

            try {
               this.addWellKnownAddress(this.trim(var7[0]), this.trim(var7[1]), Integer.parseInt(var7[2]));
            } catch (NumberFormatException var9) {
               throw new ConfigException(NodeManagerTextTextFormatter.getInstance().getInvalidPropValue(WKA_PROP, var2));
            }
         }

      }
   }

   private String trim(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.length() == 0) {
            var1 = null;
         }
      }

      return var1;
   }

   public static class WellKnownAddress {
      private final String name;
      private final String address;
      private final int port;

      private WellKnownAddress(String var1, String var2, int var3) {
         this.name = var1;
         this.address = var2;
         this.port = var3;
      }

      public String getName() {
         return this.name;
      }

      public String getListenAddress() {
         return this.address;
      }

      public int getListenPort() {
         return this.port;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.getName()).append(",");
         var1.append(this.getListenAddress()).append(",");
         var1.append(this.getListenPort());
         return var1.toString();
      }

      // $FF: synthetic method
      WellKnownAddress(String var1, String var2, int var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
