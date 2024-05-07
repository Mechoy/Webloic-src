package weblogic.xml.jaxr.registry.infomodel;

import java.util.Locale;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.LocalizedString;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class LocalizedStringImpl extends BaseInfoModelObject implements LocalizedString {
   private static final long serialVersionUID = -1L;
   private String m_charsetName;
   private Locale m_locale;
   private String m_value;

   public LocalizedStringImpl(RegistryServiceImpl var1) {
      super(var1);
      this.m_charsetName = "UTF-8";
      this.m_locale = Locale.getDefault();
   }

   public LocalizedStringImpl(LocalizedString var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      if (var1 != null) {
         this.m_charsetName = var1.getCharsetName();
         this.m_locale = var1.getLocale();
         this.m_value = var1.getValue();
      }

   }

   public LocalizedStringImpl(Locale var1, String var2, String var3, RegistryServiceImpl var4) {
      super(var4);
      this.m_charsetName = var3;
      this.m_locale = var1;
      this.m_value = var2;
   }

   public LocalizedStringImpl(Locale var1, String var2, RegistryServiceImpl var3) {
      super(var3);
      this.m_charsetName = "UTF-8";
      this.m_locale = var1;
      this.m_value = var2;
   }

   public String getCharsetName() throws JAXRException {
      return this.m_charsetName;
   }

   public Locale getLocale() throws JAXRException {
      return this.m_locale == null ? Locale.getDefault() : this.m_locale;
   }

   public String getValue() throws JAXRException {
      return this.m_value;
   }

   public void setCharsetName(String var1) throws JAXRException {
      this.m_charsetName = var1;
   }

   public void setLocale(Locale var1) throws JAXRException {
      this.m_locale = var1;
   }

   public void setValue(String var1) throws JAXRException {
      this.m_value = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_charsetName, this.m_locale, this.m_value};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_charsetName", "m_locale", "m_value"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
