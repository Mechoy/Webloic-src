package weblogic.j2ee.customizers;

import java.io.IOException;
import java.util.StringTokenizer;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.AnnotationInstanceBean;
import weblogic.j2ee.descriptor.wl.ArrayMemberBean;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.encoders.BASE64Encoder;

public class ArrayMemberBeanCustomizer implements weblogic.j2ee.descriptor.wl.customizers.ArrayMemberBeanCustomizer {
   private ArrayMemberBean _customized;
   private AnnotationInstanceBean _belongingAnnotation;
   private String _shortDescription;
   private static final BASE64Encoder ENCODER = new BASE64Encoder();
   private static final BASE64Decoder DECODER = new BASE64Decoder();
   private static final String DELIM = "@@";

   public ArrayMemberBeanCustomizer(ArrayMemberBean var1) {
      this._customized = var1;
   }

   public void _postCreate() {
      if (this._customized instanceof DescriptorBean) {
         DescriptorBean var1 = ((DescriptorBean)this._customized).getParentBean();
         if (var1 instanceof AnnotationInstanceBean) {
            this._belongingAnnotation = (AnnotationInstanceBean)var1;
         }
      }

   }

   public String[] getOverrideValues() {
      String[] var1 = null;
      if (this._customized.getRequiresEncryption()) {
         String var2 = this._customized.getSecuredOverrideValue();
         return this.stringToArray(var2);
      } else {
         var1 = this._customized.getCleartextOverrideValues();
         return var1;
      }
   }

   public void setOverrideValues(String[] var1) {
      if (var1 != null) {
         if (this._customized.getRequiresEncryption()) {
            this._customized.setSecuredOverrideValue(this.arrayToString(var1));
         } else {
            this._customized.setCleartextOverrideValues(var1);
         }

      }
   }

   private String arrayToString(String[] var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         if (var4 != null) {
            var2.append(ENCODER.encodeBuffer(var4.getBytes()));
         }

         if (var3 != var1.length - 1) {
            var2.append("@@");
         }
      }

      return var2.toString();
   }

   private String[] stringToArray(String var1) {
      if (var1 != null && var1.length() != 0) {
         StringTokenizer var2 = new StringTokenizer(var1, "@@", false);
         String[] var3 = new String[var2.countTokens()];

         String var5;
         for(int var4 = 0; var2.hasMoreTokens(); var3[var4++] = var5) {
            var5 = null;

            try {
               byte[] var6 = DECODER.decodeBuffer(var2.nextToken());
               var5 = new String(var6);
            } catch (IOException var7) {
               var7.printStackTrace();
            }
         }

         return var3;
      } else {
         return new String[0];
      }
   }

   public String getShortDescription() {
      if (this._shortDescription == null && this._belongingAnnotation != null) {
         String var1 = this._belongingAnnotation.getAnnotationClassName();
         String var2 = var1 + "." + this._customized.getMemberName();
         this._shortDescription = AnnotationLocalizer.getShortDescription(var1, var2);
      }

      return this._shortDescription;
   }
}
