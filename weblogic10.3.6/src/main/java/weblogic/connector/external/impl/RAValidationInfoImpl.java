package weblogic.connector.external.impl;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import weblogic.connector.external.PropSetterTable;
import weblogic.connector.external.RAValidationInfo;
import weblogic.j2ee.descriptor.ConfigPropertyBean;

public class RAValidationInfoImpl implements RAValidationInfo {
   boolean isInbound = false;
   boolean hasRAbean = false;
   boolean hasRAxml = false;
   boolean isLinkRef = false;
   String linkRef = null;
   boolean isCompliant = true;
   PropSetterTable raPropSetterTable = new PropSetterTable();
   Hashtable adminPropSetterTables = new Hashtable();
   Hashtable connectionFactoryPropSetterTables = new Hashtable();
   String moduleName = "";

   public boolean isCompliant() {
      return this.isCompliant;
   }

   public boolean isInbound() {
      return this.isInbound;
   }

   public boolean hasRAbean() {
      return this.hasRAbean;
   }

   public boolean hasRAxml() {
      return this.hasRAxml;
   }

   public boolean isLinkRef() {
      return this.isLinkRef;
   }

   public String getLinkRef() {
      return this.linkRef;
   }

   public PropSetterTable getRAPropSetterTable() {
      return this.raPropSetterTable;
   }

   public PropSetterTable getAdminPropSetterTable(String var1) {
      PropSetterTable var2 = (PropSetterTable)this.adminPropSetterTables.get(var1);
      if (var2 == null) {
         var2 = new PropSetterTable();
         this.adminPropSetterTables.put(var1, var2);
      }

      return var2;
   }

   public Collection getAllAdminPropSetters() {
      return this.adminPropSetterTables.values();
   }

   public Collection getAllConnectionFactoryPropSetters() {
      return this.connectionFactoryPropSetterTables.values();
   }

   public PropSetterTable getConnectionFactoryPropSetterTable(String var1) {
      PropSetterTable var2 = (PropSetterTable)this.connectionFactoryPropSetterTables.get(var1);
      if (var2 == null) {
         var2 = new PropSetterTable();
         this.connectionFactoryPropSetterTables.put(var1, var2);
      }

      return var2;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public ConfigPropertyBean getProperty(String var1, Collection var2) {
      Iterator var3 = var2.iterator();

      ConfigPropertyBean var4;
      for(var4 = null; var3.hasNext() && var4 == null; var4 = ((PropSetterTable)((PropSetterTable)var3.next())).getRAProperty(var1)) {
      }

      return var4;
   }
}
