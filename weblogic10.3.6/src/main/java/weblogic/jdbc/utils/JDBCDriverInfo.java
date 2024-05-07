package weblogic.jdbc.utils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.jdbc.common.internal.AddressList;

public class JDBCDriverInfo implements Serializable, Comparable {
   private static final long serialVersionUID = 6919106869180583924L;
   private static boolean debug = false;
   private boolean triedToLoadDriver = false;
   private boolean driverLoaded = false;
   private Exception loadDriverException;
   private MetaJDBCDriverInfo metaInfo = null;
   private Map myJDBCDriverAtributes = null;
   private Map unknownDriverAttributes = null;
   private AddressList hostPorts = new AddressList();
   public static final String DB_HOST = "DbmsHost";
   public static final String DB_PORT = "DbmsPort";
   public static final String DB_SERVERNAME = "DbmsName";
   public static final String DB_USER = "DbmsUsername";
   public static final String DB_PASS = "DbmsPassword";
   public static final String[] WELL_KNOWN_KEYS = new String[]{"DbmsHost", "DbmsPort", "DbmsName", "DbmsUsername", "DbmsPassword"};

   JDBCDriverInfo(MetaJDBCDriverInfo var1) {
      this.metaInfo = var1;
   }

   public String getDriverPK() {
      return this.metaInfo.toString();
   }

   public List getDbmsVersionList() {
      return this.metaInfo.getDbmsVersionList();
   }

   public String getDbmsVersion() {
      return this.metaInfo.getDbmsVersion();
   }

   public String getDbmsVendor() {
      return this.metaInfo.getDbmsVendor();
   }

   public String getDriverVendor() {
      return this.metaInfo.getDriverVendor();
   }

   public String getDriverClassName() {
      return this.metaInfo.getDriverClassName();
   }

   public String getURLHelperClassName() {
      return this.metaInfo.getURLHelperClassName();
   }

   public String getType() {
      return this.metaInfo.getType();
   }

   public String getTestSQL() {
      return this.metaInfo.getTestSQL();
   }

   public String getInstallURL() {
      return this.metaInfo.getInstallURL();
   }

   public String getDescription() {
      return this.metaInfo.getDescription();
   }

   public boolean isForXA() {
      return this.metaInfo.isForXA();
   }

   public boolean isCert() {
      return this.metaInfo.isCert();
   }

   public void setDbmsName(String var1) {
      this.setWellKnownAttribute("DbmsName", var1);
   }

   public String getDbmsName() {
      return this.getWellKnownAttribute("DbmsName");
   }

   public String getDbmsNameDefault() {
      return this.getDefaultFor("DbmsName");
   }

   public void setDbmsHost(String var1) {
      this.setWellKnownAttribute("DbmsHost", var1);
   }

   public String getDbmsHost() {
      return this.getWellKnownAttribute("DbmsHost");
   }

   public String getDbmsHostDefault() {
      return this.getDefaultFor("DbmsHost");
   }

   public void setDbmsPort(String var1) {
      this.setWellKnownAttribute("DbmsPort", var1);
   }

   public String getDbmsPort() {
      return this.getWellKnownAttribute("DbmsPort");
   }

   public String getDbmsPortDefault() {
      return this.getDefaultFor("DbmsPort");
   }

   public void setPassword(String var1) {
      this.setWellKnownAttribute("DbmsPassword", var1);
   }

   public String getPassword() {
      return this.getWellKnownAttribute("DbmsPassword");
   }

   public void setUserName(String var1) {
      this.setWellKnownAttribute("DbmsUsername", var1);
   }

   public String getUserName() {
      return this.getWellKnownAttribute("DbmsUsername");
   }

   public void setUknownAttribute(String var1, String var2) {
      this.setUnknownAttribute(var1, var2);
   }

   public boolean isServerNameRequired() {
      return this.isAttributeRequired("DbmsName");
   }

   public boolean isPortRequired() {
      return this.isAttributeRequired("DbmsPort");
   }

   public boolean isHostNameRequired() {
      return this.isAttributeRequired("DbmsHost");
   }

   public boolean isUserNameRequired() {
      return this.isAttributeRequired("DbmsUsername");
   }

   public boolean isPassWordRequired() {
      return this.isAttributeRequired("DbmsPassword");
   }

   public void addHostPort(String var1, int var2) {
      this.hostPorts.add(var1, var2);
   }

   public void setHostPortAddressList(AddressList var1) {
      this.hostPorts = var1;
   }

   public boolean removeHostPort(String var1, int var2) {
      return this.hostPorts.remove(var1, var2);
   }

   public AddressList getHostPorts() {
      return this.hostPorts;
   }

   public Map getUnknownDriverAttributes() {
      if (this.unknownDriverAttributes == null) {
         this.unknownDriverAttributes = new LinkedHashMap(this.getUnknownDriverAttributesKeys().size());
         Set var1 = this.getUnknownDriverAttributesKeys();
         Iterator var2 = var1.iterator();
         Map var3 = this.getDriverAttributes();

         while(var2.hasNext()) {
            Object var4 = var2.next();
            this.unknownDriverAttributes.put(var4, var3.get(var4));
         }
      }

      return this.unknownDriverAttributes;
   }

   public Map getDriverAttributes() {
      if (this.myJDBCDriverAtributes == null) {
         this.myJDBCDriverAtributes = this.metaInfo.getDriverAttributes();
      }

      return this.myJDBCDriverAtributes;
   }

   public Set getUnknownDriverAttributesKeys() {
      return this.metaInfo.getUnknownDriverAttributesKeys();
   }

   public boolean isDriverInClasspath() {
      if (!this.triedToLoadDriver) {
         try {
            Class.forName(this.getDriverClassName()).newInstance();
            this.driverLoaded = true;
         } catch (Exception var2) {
            this.loadDriverException = var2;
         }
      }

      return this.driverLoaded;
   }

   public Exception exceptionEncounteredLoadingDriver() {
      return this.loadDriverException;
   }

   public String displayString() {
      StringBuffer var1 = new StringBuffer();
      if (this.isCert()) {
         var1.append("*");
      }

      var1.append(this.getDriverVendor());
      var1.append("'s ");
      if (!this.getDriverVendor().equals(this.getDbmsVendor())) {
         var1.append(this.getDbmsVendor() + " ");
      }

      var1.append("Type ");
      var1.append(this.getType());
      var1.append(" Driver ");
      if (this.isForXA()) {
         var1.append("for Distributed Transactions (XA) ");
      }

      var1.append(" - Versions : " + this.getDbmsVersion());
      return var1.toString();
   }

   public String toString() {
      return this.metaInfo.toString();
   }

   public String toVerboseString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("DBMS Vendor      :  " + (this.getDbmsVendor() == null ? "null" : this.getDbmsVendor()) + "\n");
      var1.append("Driver Vendor    :  " + (this.getDriverVendor() == null ? "null" : this.getDriverVendor()) + "\n");
      var1.append("Driver Type      :  " + (this.getType() == null ? "null" : this.getType()) + "\n");
      var1.append("Driver Class     :  " + (this.getDriverClassName() == null ? "null" : this.getDriverClassName()) + "\n");
      var1.append("XA ?             :  " + (new Boolean(this.isForXA())).toString() + "\n");
      var1.append("URLHelperClass   :  " + (this.getURLHelperClassName() == null ? "null" : this.getURLHelperClassName()) + "\n");
      var1.append("DBMS Version     :  " + (this.getDbmsVersion() == null ? "null" : this.getDbmsVersion()) + "\n");
      var1.append("DBMS Name        :  " + (this.getDbmsName() == null ? "null" : this.getDbmsName()) + "\n");
      var1.append("DBMS Host        :  " + (this.getDbmsHost() == null ? "null" : this.getDbmsHost()) + "\n");
      var1.append("isHostRequired   :  " + (new Boolean(this.isHostNameRequired())).toString() + "\n");
      var1.append("DBMS Port        :  " + (this.getDbmsPort() == null ? "null" : this.getDbmsPort()) + "\n");
      var1.append("isPortRequired   :  " + (new Boolean(this.isPortRequired())).toString() + "\n");
      var1.append("DBMS password    :  " + (this.getPassword() == null ? "null" : this.getPassword()) + "\n");
      var1.append("DBMS user        :  " + (this.getUserName() == null ? "null" : this.getUserName()) + "\n");
      var1.append("DBMS test sql    :  " + (this.getTestSQL() == null ? "null" : this.getTestSQL()) + "\n");
      var1.append("Description      :  " + (this.getDescription() == null ? "null" : this.getDescription()) + "\n");
      return var1.toString();
   }

   private void setWellKnownAttribute(String var1, String var2) {
      JDBCDriverAttribute var3 = null;
      if (this.getDriverAttributes().containsKey(var1)) {
         var3 = (JDBCDriverAttribute)this.getDriverAttributes().get(var1);
      } else {
         var3 = new JDBCDriverAttribute(this.metaInfo);
         var3.setName(var2);
      }

      var3.setValue(var2);
      this.getDriverAttributes().put(var1, var3);
   }

   private void setUnknownAttribute(String var1, String var2) {
      if (!this.getDriverAttributes().containsKey(var1)) {
         throw new AssertionError("Trying to set a value '" + var2 + "' on an unknown attribute '" + var1 + "'");
      } else {
         JDBCDriverAttribute var3 = (JDBCDriverAttribute)this.getDriverAttributes().get(var1);
         var3.setValue(var2);
         this.getDriverAttributes().put(var1, var3);
      }
   }

   private String getWellKnownAttribute(String var1) {
      return this.getWellKnownAttribute(var1, false);
   }

   private String getWellKnownAttribute(String var1, boolean var2) {
      if (this.getDriverAttributes().containsKey(var1)) {
         JDBCDriverAttribute var3 = (JDBCDriverAttribute)this.getDriverAttributes().get(var1);
         if (var3.getValue() != null) {
            return var3.getValue();
         }

         if (var2 && var3.getDefaultValue() != null) {
            return var3.getDefaultValue();
         }
      }

      return null;
   }

   private String getDefaultFor(String var1) {
      return this.getAttribute(var1) != null ? this.getAttribute(var1).getDefaultValue() : null;
   }

   private JDBCDriverAttribute getAttribute(String var1) {
      return this.getDriverAttributes().containsKey(var1) ? (JDBCDriverAttribute)this.getDriverAttributes().get(var1) : null;
   }

   private boolean isAttributeRequired(String var1) {
      if (this.getDriverAttributes().containsKey(var1)) {
         JDBCDriverAttribute var2 = (JDBCDriverAttribute)this.getDriverAttributes().get(var1);
         return var2.isRequired();
      } else {
         return false;
      }
   }

   public int compareTo(Object var1) throws ClassCastException {
      JDBCDriverInfo var2 = (JDBCDriverInfo)var1;
      return this.getDriverPK().compareTo(var2.getDriverPK());
   }
}
