package weblogic.diagnostics.snmp.mib;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import weblogic.utils.PlatformConstants;

public class WLSMibMetadata implements Serializable, MibConstants {
   private static final long serialVersionUID = -2396017281810551450L;
   Map snmpTableNameToWLSTypeName = new HashMap();
   Map wlsTypeNameToSNMPTableName = new HashMap();
   Map snmpTableNameToColumns = new HashMap();
   Map wlsTypeNameToColumns = new HashMap();

   public static WLSMibMetadata loadResource() throws WLSMibMetadataException {
      InputStream var0 = WLSMibMetadata.class.getResourceAsStream("WLSMibMetadata.dat");

      try {
         ObjectInputStream var1 = new ObjectInputStream(var0);
         Object var2 = var1.readObject();
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Loaded " + var2);
         }

         return (WLSMibMetadata)var2;
      } catch (Exception var3) {
         throw new WLSMibMetadataException(var3);
      }
   }

   public String getSNMPTableName(String var1) {
      return (String)this.wlsTypeNameToSNMPTableName.get(var1);
   }

   public String getWLSTypeName(String var1) {
      return (String)this.snmpTableNameToWLSTypeName.get(var1);
   }

   public WLSMibTableColumnsMetadata getColumnsMetadataForSNMPTable(String var1) {
      return (WLSMibTableColumnsMetadata)this.snmpTableNameToColumns.get(var1);
   }

   public WLSMibTableColumnsMetadata getColumnsMetadataForWLSType(String var1) {
      return (WLSMibTableColumnsMetadata)this.wlsTypeNameToColumns.get(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("snmpTableNameToWLSTypeName = " + this.snmpTableNameToWLSTypeName);
      var1.append(PlatformConstants.EOL);
      var1.append("wlsTypeNameToSNMPTableName = " + this.wlsTypeNameToSNMPTableName);
      var1.append(PlatformConstants.EOL);
      var1.append("snmpTableNameToColumns = " + this.snmpTableNameToColumns);
      var1.append(PlatformConstants.EOL);
      var1.append("wlsTypeNameToColumns = " + this.wlsTypeNameToColumns);
      var1.append(PlatformConstants.EOL);
      return var1.toString();
   }

   public static void main(String[] var0) throws WLSMibMetadataException {
      WLSMibMetadata var1 = loadResource();
      System.out.println("Loaded MIB metadata ...");
      System.out.println(var1);
   }
}
