package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.CharsetMappingMBean;
import weblogic.management.descriptors.webappext.CharsetParamsMBean;
import weblogic.management.descriptors.webappext.InputCharsetDescriptorMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class CharsetParams extends BaseServletDescriptor implements CharsetParamsMBean {
   private static final long serialVersionUID = 1946096778263982155L;
   private static final String INPUT_CHARSET = "input-charset";
   private static final String CHARSET_MAPPING = "charset-mapping";
   private CharsetMappingMBean[] charsetMappings;
   private InputCharsetDescriptorMBean[] inputCharsets;

   public CharsetParams() {
   }

   public CharsetParams(Element var1) throws DOMProcessingException {
      List var2 = DOMUtils.getOptionalElementsByTagName(var1, "input-charset");
      if (var2 != null) {
         ArrayList var3 = new ArrayList();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.add(new InputCharsetDescriptor((Element)var4.next()));
         }

         this.inputCharsets = (InputCharsetDescriptorMBean[])((InputCharsetDescriptorMBean[])var3.toArray(new InputCharsetDescriptorMBean[0]));
      }

      List var6 = DOMUtils.getOptionalElementsByTagName(var1, "charset-mapping");
      if (var6 != null) {
         ArrayList var7 = new ArrayList();
         Iterator var5 = var6.iterator();

         while(var5.hasNext()) {
            var7.add(new CharsetMapping((Element)var5.next()));
         }

         this.charsetMappings = (CharsetMappingMBean[])((CharsetMappingMBean[])var7.toArray(new CharsetMappingMBean[0]));
      }

   }

   public InputCharsetDescriptorMBean[] getInputCharsets() {
      return this.inputCharsets;
   }

   public void setInputCharsets(InputCharsetDescriptorMBean[] var1) {
      InputCharsetDescriptorMBean[] var2 = this.inputCharsets;
      this.inputCharsets = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("inputCharsets", var2, var1);
      }

   }

   public void addInputCharset(InputCharsetDescriptorMBean var1) {
      InputCharsetDescriptorMBean[] var2 = this.getInputCharsets();
      if (var2 == null) {
         InputCharsetDescriptor[] var4 = new InputCharsetDescriptor[]{var1};
         this.setInputCharsets(var4);
      } else {
         InputCharsetDescriptorMBean[] var3 = new InputCharsetDescriptorMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setInputCharsets(var3);
      }
   }

   public void removeInputCharset(InputCharsetDescriptorMBean var1) {
      InputCharsetDescriptorMBean[] var2 = this.getInputCharsets();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            InputCharsetDescriptorMBean[] var5 = new InputCharsetDescriptorMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setInputCharsets(var5);
         }

      }
   }

   public CharsetMappingMBean[] getCharsetMappings() {
      if (this.charsetMappings == null) {
         this.charsetMappings = new CharsetMappingMBean[0];
      }

      return this.charsetMappings;
   }

   public void setCharsetMappings(CharsetMappingMBean[] var1) {
      CharsetMappingMBean[] var2 = this.charsetMappings;
      this.charsetMappings = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("charsetMappings", var2, var1);
      }

   }

   public void addCharsetMapping(CharsetMappingMBean var1) {
      CharsetMappingMBean[] var2 = this.getCharsetMappings();
      if (var2 == null) {
         var2 = new CharsetMappingMBean[]{var1};
         this.setCharsetMappings(var2);
      } else {
         CharsetMappingMBean[] var3 = new CharsetMappingMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setCharsetMappings(var3);
      }
   }

   public void removeCharsetMapping(CharsetMappingMBean var1) {
      CharsetMappingMBean[] var2 = this.getCharsetMappings();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            CharsetMappingMBean[] var5 = new CharsetMappingMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setCharsetMappings(var5);
         }

      }
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      boolean var1 = true;
      int var2;
      if (this.inputCharsets != null) {
         for(var2 = 0; var2 < this.inputCharsets.length; ++var2) {
            var1 &= this.check(this.inputCharsets[var2]);
         }
      }

      if (this.charsetMappings != null) {
         for(var2 = 0; var2 < this.charsetMappings.length; ++var2) {
            var1 &= this.check(this.charsetMappings[var2]);
         }
      }

      if (!var1) {
         String[] var3 = this.getDescriptorErrors();
         throw new DescriptorValidationException(this.arrayToString(var3));
      }
   }

   public String toXML(int var1) {
      String var2 = "";
      boolean var3 = false;
      int var4;
      if (this.inputCharsets != null && this.inputCharsets.length > 0) {
         for(var4 = 0; var4 < this.inputCharsets.length; ++var4) {
            var2 = var2 + this.inputCharsets[var4].toXML(var1 + 2);
         }

         var3 = true;
      }

      if (this.charsetMappings != null && this.charsetMappings.length > 0) {
         for(var4 = 0; var4 < this.charsetMappings.length; ++var4) {
            var2 = var2 + this.charsetMappings[var4].toXML(var1 + 2);
         }

         var3 = true;
      }

      return var3 ? this.indentStr(var1) + "<charset-params>\n" + var2 + this.indentStr(var1) + "</charset-params>\n" : "";
   }
}
