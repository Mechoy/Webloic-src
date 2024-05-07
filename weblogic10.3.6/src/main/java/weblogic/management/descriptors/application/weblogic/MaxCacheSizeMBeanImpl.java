package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class MaxCacheSizeMBeanImpl extends XMLElementMBeanDelegate implements MaxCacheSizeMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_bytes = false;
   private int bytes = -1;
   private boolean isSet_megabytes = false;
   private int megabytes = -1;

   public int getBytes() {
      return this.bytes;
   }

   public void setBytes(int var1) {
      int var2 = this.bytes;
      this.bytes = var1;
      this.isSet_bytes = var1 != -1;
      this.checkChange("bytes", var2, this.bytes);
   }

   public int getMegabytes() {
      return this.megabytes;
   }

   public void setMegabytes(int var1) {
      int var2 = this.megabytes;
      this.megabytes = var1;
      this.isSet_megabytes = var1 != -1;
      this.checkChange("megabytes", var2, this.megabytes);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<max-cache-size");
      var2.append(">\n");
      if (!this.isSet_megabytes && -1 == this.getMegabytes()) {
         if (this.isSet_bytes || -1 != this.getBytes()) {
            var2.append(ToXML.indent(var1 + 2)).append("<bytes>").append(this.getBytes()).append("</bytes>\n");
         }
      } else {
         var2.append(ToXML.indent(var1 + 2)).append("<megabytes>").append(this.getMegabytes()).append("</megabytes>\n");
      }

      var2.append(ToXML.indent(var1)).append("</max-cache-size>\n");
      return var2.toString();
   }
}
