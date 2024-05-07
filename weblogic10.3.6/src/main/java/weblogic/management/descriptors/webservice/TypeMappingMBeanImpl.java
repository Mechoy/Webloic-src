package weblogic.management.descriptors.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class TypeMappingMBeanImpl extends XMLElementMBeanDelegate implements TypeMappingMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_typeMappingEntries = false;
   private List typeMappingEntries;

   public TypeMappingEntryMBean[] getTypeMappingEntries() {
      if (this.typeMappingEntries == null) {
         return new TypeMappingEntryMBean[0];
      } else {
         TypeMappingEntryMBean[] var1 = new TypeMappingEntryMBean[this.typeMappingEntries.size()];
         var1 = (TypeMappingEntryMBean[])((TypeMappingEntryMBean[])this.typeMappingEntries.toArray(var1));
         return var1;
      }
   }

   public void setTypeMappingEntries(TypeMappingEntryMBean[] var1) {
      TypeMappingEntryMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getTypeMappingEntries();
      }

      this.isSet_typeMappingEntries = true;
      if (this.typeMappingEntries == null) {
         this.typeMappingEntries = Collections.synchronizedList(new ArrayList());
      } else {
         this.typeMappingEntries.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.typeMappingEntries.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("TypeMappingEntries", var2, this.getTypeMappingEntries());
      }

   }

   public void addTypeMappingEntry(TypeMappingEntryMBean var1) {
      this.isSet_typeMappingEntries = true;
      if (this.typeMappingEntries == null) {
         this.typeMappingEntries = Collections.synchronizedList(new ArrayList());
      }

      this.typeMappingEntries.add(var1);
   }

   public void removeTypeMappingEntry(TypeMappingEntryMBean var1) {
      if (this.typeMappingEntries != null) {
         this.typeMappingEntries.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<type-mapping");
      var2.append(">\n");
      if (null != this.getTypeMappingEntries()) {
         for(int var3 = 0; var3 < this.getTypeMappingEntries().length; ++var3) {
            var2.append(this.getTypeMappingEntries()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</type-mapping>\n");
      return var2.toString();
   }
}
