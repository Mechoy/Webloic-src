package weblogic.diagnostics.snmp.agent.monfox;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import monfox.toolkit.snmp.agent.ext.table.SnmpMibTableAdaptor;
import monfox.toolkit.snmp.metadata.builder.MibApi;
import weblogic.diagnostics.snmp.agent.MBeanServerSubAgentX;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.diagnostics.snmp.agent.SNMPConstants;
import weblogic.diagnostics.snmp.agent.SNMPUtil;

public class MBeanServerSubAgentXImpl extends SNMPSubAgentXImpl implements MBeanServerSubAgentX, SNMPConstants {
   private Map snmpColumns = new HashMap();

   public MBeanServerSubAgentXImpl(String var1, int var2, String var3, String var4) throws SNMPAgentToolkitException {
      super(var1, var2, var3, var4);
   }

   public void addSNMPTableRowForMBeanInstance(MBeanServerConnection var1, String var2, ObjectName var3) throws SNMPAgentToolkitException {
      SnmpMibTableAdaptor var4 = null;

      try {
         if (!this.snmpTables.containsKey(var2)) {
            var4 = this.createSNMPTable(var1, var2, var3);
            this.snmpTables.put(var2, var4);
         } else {
            var4 = (SnmpMibTableAdaptor)this.snmpTables.get(var2);
         }

         Map var5 = (Map)this.snmpColumns.get(var2);
         if (var5 != null) {
            MBeanInstanceTableRow var6 = new MBeanInstanceTableRow(var1, var3, var5);
            var4.addRow(var6);
         }

      } catch (Exception var7) {
         throw new SNMPAgentToolkitException(var7);
      }
   }

   public void deleteSNMPTableRowForMBeanInstance(String var1, ObjectName var2) throws SNMPAgentToolkitException {
      SnmpMibTableAdaptor var3 = (SnmpMibTableAdaptor)this.snmpTables.get(var1);
      if (var3 != null) {
         String var4 = MBeanInstanceTableRow.computeIndex(var2.toString());
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Deleting custom mbean row for index " + var4);
         }

         try {
            var3.removeRow(new String[]{var4});
         } catch (Exception var6) {
            throw new SNMPAgentToolkitException(var6);
         }
      }

   }

   private SnmpMibTableAdaptor createSNMPTable(MBeanServerConnection var1, String var2, ObjectName var3) throws Exception {
      String var4 = SNMPUtil.convertTypeNameToSNMPTableName(var2);
      String var5 = var4;
      MibApi.Table var6 = this.mibApi.createTable(this.moduleName, var4, "Dynamically created table for type " + var2);
      MBeanAttributeInfo[] var7 = var1.getMBeanInfo(var3).getAttributes();
      Comparator var8 = new Comparator() {
         public int compare(Object var1, Object var2) {
            MBeanAttributeInfo var3 = (MBeanAttributeInfo)var1;
            MBeanAttributeInfo var4 = (MBeanAttributeInfo)var2;
            return var3.getName().compareTo(var4.getName());
         }
      };
      Arrays.sort(var7, var8);
      String var9 = var4 + "Index";
      var6.addColumn(var9, "SNMPv2-TC.DisplayString", "na", "Index column", true);
      String var10 = var4 + "ObjectName";
      var6.addColumn(var10, "SNMPv2-TC.DisplayString", "ro", "ObjectName column", false);
      HashMap var11 = new HashMap();
      var11.put(var9, "Index");
      var11.put(var10, "ObjectName");
      if (var7 != null) {
         for(int var12 = 0; var12 < var7.length; ++var12) {
            MBeanAttributeInfo var13 = var7[var12];
            String var14 = var13.getName();
            String var15 = var13.getType();
            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("Discovered attribute " + var14 + " of type " + var15);
            }

            String var16 = var5 + var14;
            String var17 = "SNMPv2-TC.DisplayString";
            var6.addColumn(var16, var17, "ro", var13.getDescription(), false);
            var11.put(var16, var14);
         }
      }

      var6.exportToMetadata();
      this.snmpColumns.put(var2, var11);
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Adding table name " + var4 + " for type " + var2);
      }

      SnmpMibTableAdaptor var18 = new SnmpMibTableAdaptor(this.snmpMetadata, var4);
      var18.isLazyLoadingEnabled(true);
      this.snmpMib.add(var18);
      return var18;
   }
}
