package weblogic.jms.extensions;

import javax.jms.Destination;

public class DestinationDetailImpl implements DestinationDetail {
   private String ddConfigName;
   private String ddJndiName;
   private int destinationType;
   private String memberConfigName;
   private String memberJndiName;
   private String memberLocalJndiName;
   private String createDestinationIdentifier;
   private String jmsServerName;
   private String persistentStoreName;
   private String serverName;
   private String migratableTargetName;
   private boolean isLocalWLSServer;
   private boolean isLocalCluster;
   private boolean isAdvancedTopicSupported;
   private Destination destination;

   public DestinationDetailImpl(String var1, String var2, int var3, String var4, String var5, String var6, String var7, String var8, String var9, Destination var10, String var11, String var12, boolean var13, boolean var14, boolean var15) {
      this.ddConfigName = var1;
      this.ddJndiName = var2;
      this.destinationType = var3;
      this.destination = var10;
      this.memberConfigName = var4;
      this.memberJndiName = var5;
      this.memberLocalJndiName = var6;
      this.createDestinationIdentifier = var7;
      this.jmsServerName = var8;
      this.persistentStoreName = var9;
      this.serverName = var11;
      this.migratableTargetName = var12;
      this.isLocalWLSServer = var13;
      this.isLocalCluster = var14;
      this.isAdvancedTopicSupported = var15;
   }

   public String getJNDIName() {
      return this.memberJndiName;
   }

   public Destination getDestination() {
      return this.destination;
   }

   public int getType() {
      return this.destinationType;
   }

   public String getWLSServerName() {
      return this.serverName;
   }

   public String getJMSServerName() {
      return this.jmsServerName;
   }

   public String getStoreName() {
      return this.persistentStoreName;
   }

   public String getMigratableTargetName() {
      return this.migratableTargetName;
   }

   public String getCreateDestinationArgument() {
      return this.createDestinationIdentifier;
   }

   public boolean isAdvancedTopicSupported() {
      return this.isAdvancedTopicSupported;
   }

   public boolean isLocalWLSServer() {
      return this.isLocalWLSServer;
   }

   public boolean isLocalCluster() {
      return this.isLocalCluster;
   }

   public String toString() {
      return this.memberJndiName;
   }

   private String getDDConfigName() {
      return this.ddConfigName;
   }

   private String getDdJndiName() {
      return this.ddJndiName;
   }

   public String getMemberConfigName() {
      return this.memberConfigName;
   }

   public String getMemberLocalJNDIName() {
      return this.memberLocalJndiName;
   }
}
