package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.LocalizedString;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class InternationalStringImpl extends BaseInfoModelObject implements InternationalString {
   private static final long serialVersionUID = -1L;
   static final String DEFAULT_CHARSET = "UTF-8";
   private Map m_strings;

   public InternationalStringImpl(RegistryServiceImpl var1) {
      super(var1);
      this.m_strings = new HashMap();
   }

   public InternationalStringImpl(InternationalString var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      if (var1 != null) {
         Collection var3 = var1.getLocalizedStrings();
         if (var3 != null) {
            Iterator var4 = var3.iterator();
            this.m_strings = new HashMap();

            while(var4.hasNext()) {
               LocalizedString var5 = (LocalizedString)var4.next();
               LocalizedStringImpl var6 = new LocalizedStringImpl(var5, var2);
               String var7 = this.makeKey(var5.getCharsetName(), var5.getLocale());
               this.m_strings.put(var7, var6);
            }
         }
      }

   }

   public InternationalStringImpl(LocalizedString var1, RegistryServiceImpl var2) throws JAXRException {
      this(var2);
      String var3 = this.makeKey(var1.getCharsetName(), var1.getLocale());
      this.m_strings.put(var3, var1);
   }

   public InternationalStringImpl(Locale var1, String var2, RegistryServiceImpl var3) throws JAXRException {
      this(var3);
      String var4 = this.makeKey("UTF-8", var1);
      this.m_strings.put(var4, new LocalizedStringImpl(var1, var2, var3));
   }

   public InternationalStringImpl(String var1, RegistryServiceImpl var2) throws JAXRException {
      this(Locale.getDefault(), var1, var2);
   }

   public String getValue() throws JAXRException {
      return this.getValue(Locale.getDefault());
   }

   public String getValue(Locale var1) throws JAXRException {
      if (var1 == null) {
         return null;
      } else {
         String var2 = this.makeKey("UTF-8", var1);
         LocalizedString var3 = (LocalizedString)this.m_strings.get(var2);
         return var3 != null ? var3.getValue() : null;
      }
   }

   public void setValue(String var1) throws JAXRException {
      this.setValue(Locale.getDefault(), var1);
   }

   public void setValue(Locale var1, String var2) throws JAXRException {
      String var3 = this.makeKey("UTF-8", var1);
      this.m_strings.put(var3, new LocalizedStringImpl(var1, var2, this.getRegistryService()));
   }

   public void addLocalizedString(LocalizedString var1) throws JAXRException {
      if (var1 == null) {
         throw new JAXRException();
      } else {
         String var2 = this.makeKey(var1.getCharsetName(), var1.getLocale());
         this.m_strings.put(var2, var1);
      }
   }

   public void addLocalizedStrings(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, LocalizedString.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         LocalizedString var3 = (LocalizedString)var2.next();
         this.addLocalizedString(var3);
      }

   }

   public void removeLocalizedString(LocalizedString var1) throws JAXRException {
      if (var1 != null) {
         String var2 = this.makeKey(var1.getCharsetName(), var1.getLocale());
         this.m_strings.remove(var2);
      }
   }

   public void removeLocalizedStrings(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, LocalizedString.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         LocalizedString var3 = (LocalizedString)var2.next();
         this.removeLocalizedString(var3);
      }

   }

   public LocalizedString getLocalizedString(Locale var1, String var2) throws JAXRException {
      String var3 = this.makeKey(var2, var1);
      return (LocalizedString)this.m_strings.get(var3);
   }

   public Collection getLocalizedStrings() throws JAXRException {
      return new ArrayList(this.m_strings.values());
   }

   private String makeKey(String var1, Locale var2) {
      return new String(var1 + var2.toString());
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_strings};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_strings"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
