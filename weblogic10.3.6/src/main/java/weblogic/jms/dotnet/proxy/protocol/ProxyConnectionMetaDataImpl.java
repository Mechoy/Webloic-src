package weblogic.jms.dotnet.proxy.protocol;

import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class ProxyConnectionMetaDataImpl implements MarshalReadable, MarshalWritable, ConnectionMetaData {
   private static final int EXTVERSION = 1;
   private static final int _HAS_PROVIDER_NAME = 1;
   private static final int _HAS_JMSSPEC_VERSION = 2;
   private MarshalBitMask versionFlags;
   private int majorVersion;
   private int minorVersion;
   private String providerName;
   private String version;
   private String propertyNames;
   private int providerMajorVersion;
   private int providerMinorVersion;
   private String providerVersion;

   public ProxyConnectionMetaDataImpl() {
   }

   public ProxyConnectionMetaDataImpl(ConnectionMetaData var1) throws JMSException {
      this.versionFlags = new MarshalBitMask(1);
      this.majorVersion = var1.getJMSMajorVersion();
      this.minorVersion = var1.getJMSMinorVersion();
      this.providerName = var1.getJMSProviderName();
      if (this.providerName != null) {
         this.versionFlags.setBit(1);
      }

      this.version = var1.getJMSVersion();
      if (this.version != null) {
         this.versionFlags.setBit(2);
      }

      this.propertyNames = "";
      Enumeration var2 = var1.getJMSXPropertyNames();

      for(int var3 = 0; var2.hasMoreElements(); this.propertyNames = this.propertyNames + (String)var2.nextElement()) {
         if (var3++ > 0) {
            this.propertyNames = this.propertyNames + " ";
         }
      }

      this.providerMajorVersion = var1.getProviderMajorVersion();
      this.providerMinorVersion = var1.getProviderMinorVersion();
      this.providerVersion = var1.getProviderVersion();
   }

   public int getMarshalTypeCode() {
      return 50;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags.marshal(var1);
      var1.writeInt(this.majorVersion);
      var1.writeInt(this.minorVersion);
      if (this.providerName != null) {
         var1.writeString(this.providerName);
      }

      if (this.version != null) {
         var1.writeString(this.version);
      }

      var1.writeString(this.propertyNames);
      var1.writeInt(this.providerMajorVersion);
      var1.writeInt(this.providerMinorVersion);
      var1.writeString(this.providerVersion);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.majorVersion = var1.readInt();
      this.minorVersion = var1.readInt();
      if (this.versionFlags.isSet(1)) {
         this.providerName = var1.readString();
      }

      if (this.versionFlags.isSet(2)) {
         this.version = var1.readString();
      }

      this.propertyNames = var1.readString();
      this.providerMajorVersion = var1.readInt();
      this.providerMinorVersion = var1.readInt();
      this.providerVersion = var1.readString();
   }

   public String toString() {
      return "ProxyConnectionMetaData<JMS Spec. version=" + this.version + ", JMS spec. major version=" + this.majorVersion + ", JMS spec. minor ver.=" + this.minorVersion + " Vendor=" + this.providerName + " Version= " + this.providerVersion + ", Provider major version=" + this.providerMajorVersion + ", Provider minor version=" + this.providerMinorVersion + " PropertyNames=" + this.propertyNames + " >";
   }

   public int getJMSMajorVersion() throws JMSException {
      return this.majorVersion;
   }

   public int getJMSMinorVersion() throws JMSException {
      return this.minorVersion;
   }

   public String getJMSProviderName() throws JMSException {
      return this.providerName;
   }

   public String getJMSVersion() throws JMSException {
      return this.version;
   }

   public Enumeration getJMSXPropertyNames() throws JMSException {
      StringTokenizer var1 = new StringTokenizer(this.propertyNames);
      return var1;
   }

   public int getProviderMajorVersion() throws JMSException {
      return this.providerMajorVersion;
   }

   public int getProviderMinorVersion() throws JMSException {
      return this.providerMinorVersion;
   }

   public String getProviderVersion() throws JMSException {
      return this.providerVersion;
   }
}
