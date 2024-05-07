package weblogic.diagnostics.harvester.internal;

import java.io.StringReader;
import weblogic.diagnostics.harvester.AttributeNameNormalizer;
import weblogic.diagnostics.i18n.DiagnosticsTextHarvesterTextFormatter;

public class HarvesterDefaultAttributeNormalizer implements AttributeNameNormalizer {
   private static final String INVALID_ATTRIBUTE_SPEC_TEXT = DiagnosticsTextHarvesterTextFormatter.getInstance().getInvalidAttributeSpecText();

   public String getNormalizedAttributeName(String var1) {
      HarvesterDefaultAttributeNormalizerLexer var2 = new HarvesterDefaultAttributeNormalizerLexer(new StringReader(var1));
      HarvesterDefaultAttributeNormalizerParser var3 = new HarvesterDefaultAttributeNormalizerParser(var2);

      try {
         String var4 = var3.normalizeAttributeSpec();
         return var4;
      } catch (Exception var6) {
         throw new IllegalArgumentException(INVALID_ATTRIBUTE_SPEC_TEXT + var6.getMessage(), var6);
      }
   }

   public String getAttributeName(String var1) {
      HarvesterDefaultAttributeNormalizerLexer var2 = new HarvesterDefaultAttributeNormalizerLexer(new StringReader(var1));
      HarvesterDefaultAttributeNormalizerParser var3 = new HarvesterDefaultAttributeNormalizerParser(var2);

      try {
         String var4 = var3.attributeNameSpec();
         return var4;
      } catch (Exception var6) {
         throw new IllegalArgumentException(INVALID_ATTRIBUTE_SPEC_TEXT + var6.getMessage(), var6);
      }
   }
}
