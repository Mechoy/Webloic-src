package weblogic.management.descriptors.application.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EjbMBeanImpl extends XMLElementMBeanDelegate implements EjbMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_startMdbsWithApplication = false;
   private boolean startMdbsWithApplication = false;
   private boolean isSet_entityCaches = false;
   private List entityCaches;

   public boolean getStartMdbsWithApplication() {
      return this.startMdbsWithApplication;
   }

   public void setStartMdbsWithApplication(boolean var1) {
      boolean var2 = this.startMdbsWithApplication;
      this.startMdbsWithApplication = var1;
      this.isSet_startMdbsWithApplication = true;
      this.checkChange("startMdbsWithApplication", var2, this.startMdbsWithApplication);
   }

   public EntityCacheMBean[] getEntityCaches() {
      if (this.entityCaches == null) {
         return new EntityCacheMBean[0];
      } else {
         EntityCacheMBean[] var1 = new EntityCacheMBean[this.entityCaches.size()];
         var1 = (EntityCacheMBean[])((EntityCacheMBean[])this.entityCaches.toArray(var1));
         return var1;
      }
   }

   public void setEntityCaches(EntityCacheMBean[] var1) {
      EntityCacheMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEntityCaches();
      }

      this.isSet_entityCaches = true;
      if (this.entityCaches == null) {
         this.entityCaches = Collections.synchronizedList(new ArrayList());
      } else {
         this.entityCaches.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.entityCaches.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EntityCaches", var2, this.getEntityCaches());
      }

   }

   public void addEntityCache(EntityCacheMBean var1) {
      this.isSet_entityCaches = true;
      if (this.entityCaches == null) {
         this.entityCaches = Collections.synchronizedList(new ArrayList());
      }

      this.entityCaches.add(var1);
   }

   public void removeEntityCache(EntityCacheMBean var1) {
      if (this.entityCaches != null) {
         this.entityCaches.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<ejb");
      var2.append(">\n");
      if (null != this.getEntityCaches()) {
         for(int var3 = 0; var3 < this.getEntityCaches().length; ++var3) {
            var2.append(this.getEntityCaches()[var3].toXML(var1 + 2));
         }
      }

      if (this.isSet_startMdbsWithApplication || this.getStartMdbsWithApplication()) {
         var2.append(ToXML.indent(var1 + 2)).append("<start-mdbs-with-application>").append(ToXML.capitalize(Boolean.valueOf(this.getStartMdbsWithApplication()).toString())).append("</start-mdbs-with-application>\n");
      }

      var2.append(ToXML.indent(var1)).append("</ejb>\n");
      return var2.toString();
   }
}
