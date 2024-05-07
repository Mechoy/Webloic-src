package weblogic.diagnostics.snmp.mib;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class WLSMibTableColumnsMetadata implements Serializable {
   private static final long serialVersionUID = 5241613599471877544L;
   private Map columnAttributeMap = new HashMap();
   private Map attributeColumnMap = new HashMap();

   public WLSMibTableColumnsMetadata(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         String[] var3 = (String[])((String[])var1.get(var2));
         String var4 = var3[0];
         String var5 = var3[1];
         this.columnAttributeMap.put(var4, var5);
         this.attributeColumnMap.put(var5, var4);
      }

   }

   public String getColumnName(String var1) {
      return (String)this.attributeColumnMap.get(var1);
   }

   public String getAttributeName(String var1) {
      return (String)this.columnAttributeMap.get(var1);
   }

   public Iterator getSnmpTableColumnNames() {
      return this.columnAttributeMap.keySet().iterator();
   }

   public Iterator getAttributeNames() {
      return this.attributeColumnMap.keySet().iterator();
   }

   public Map getColumnAttributeMap() {
      return this.columnAttributeMap;
   }

   public Map getAttributeColumnMap() {
      return this.attributeColumnMap;
   }
}
