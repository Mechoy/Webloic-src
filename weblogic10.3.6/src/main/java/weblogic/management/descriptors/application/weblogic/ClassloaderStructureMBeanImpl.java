package weblogic.management.descriptors.application.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ClassloaderStructureMBeanImpl extends XMLElementMBeanDelegate implements ClassloaderStructureMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_classloaderStructures = false;
   private List classloaderStructures;
   private boolean isSet_moduleRefs = false;
   private List moduleRefs;

   public ClassloaderStructureMBean[] getClassloaderStructures() {
      if (this.classloaderStructures == null) {
         return new ClassloaderStructureMBean[0];
      } else {
         ClassloaderStructureMBean[] var1 = new ClassloaderStructureMBean[this.classloaderStructures.size()];
         var1 = (ClassloaderStructureMBean[])((ClassloaderStructureMBean[])this.classloaderStructures.toArray(var1));
         return var1;
      }
   }

   public void setClassloaderStructures(ClassloaderStructureMBean[] var1) {
      ClassloaderStructureMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getClassloaderStructures();
      }

      this.isSet_classloaderStructures = true;
      if (this.classloaderStructures == null) {
         this.classloaderStructures = Collections.synchronizedList(new ArrayList());
      } else {
         this.classloaderStructures.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.classloaderStructures.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ClassloaderStructures", var2, this.getClassloaderStructures());
      }

   }

   public void addClassloaderStructure(ClassloaderStructureMBean var1) {
      this.isSet_classloaderStructures = true;
      if (this.classloaderStructures == null) {
         this.classloaderStructures = Collections.synchronizedList(new ArrayList());
      }

      this.classloaderStructures.add(var1);
   }

   public void removeClassloaderStructure(ClassloaderStructureMBean var1) {
      if (this.classloaderStructures != null) {
         this.classloaderStructures.remove(var1);
      }
   }

   public ModuleRefMBean[] getModuleRefs() {
      if (this.moduleRefs == null) {
         return new ModuleRefMBean[0];
      } else {
         ModuleRefMBean[] var1 = new ModuleRefMBean[this.moduleRefs.size()];
         var1 = (ModuleRefMBean[])((ModuleRefMBean[])this.moduleRefs.toArray(var1));
         return var1;
      }
   }

   public void setModuleRefs(ModuleRefMBean[] var1) {
      ModuleRefMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getModuleRefs();
      }

      this.isSet_moduleRefs = true;
      if (this.moduleRefs == null) {
         this.moduleRefs = Collections.synchronizedList(new ArrayList());
      } else {
         this.moduleRefs.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.moduleRefs.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ModuleRefs", var2, this.getModuleRefs());
      }

   }

   public void addModuleRef(ModuleRefMBean var1) {
      this.isSet_moduleRefs = true;
      if (this.moduleRefs == null) {
         this.moduleRefs = Collections.synchronizedList(new ArrayList());
      }

      this.moduleRefs.add(var1);
   }

   public void removeModuleRef(ModuleRefMBean var1) {
      if (this.moduleRefs != null) {
         this.moduleRefs.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<classloader-structure");
      var2.append(">\n");
      int var3;
      if (null != this.getModuleRefs()) {
         for(var3 = 0; var3 < this.getModuleRefs().length; ++var3) {
            var2.append(this.getModuleRefs()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getClassloaderStructures()) {
         for(var3 = 0; var3 < this.getClassloaderStructures().length; ++var3) {
            var2.append(this.getClassloaderStructures()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</classloader-structure>\n");
      return var2.toString();
   }
}
