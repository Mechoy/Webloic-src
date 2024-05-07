package weblogic.auddi.uddi.util;

import java.util.Hashtable;
import java.util.Iterator;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.TModelExt;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.TModels;
import weblogic.auddi.util.Logger;

public class UDDICoreTModels {
   public static final String UDDI_TYPES_TMODEL = "UUID:C1ACF26D-9672-4404-9D70-39B756E62AB4";
   public static final String UDDI_NAICS_1997_TMODEL = "UUID:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2";
   public static final String UDDI_UNSPSC_31_TMODEL = "UUID:DB77450D-9FA8-45D4-A7BC-04411D14E384";
   public static final String UDDI_UNSPSC_7_TMODEL = "UUID:CD153257-086A-4237-B336-6BDCBDCC6634";
   public static final String UDDI_ISO_3166_1999_TMODEL = "UUID:4E49A8D6-D5A2-4FC2-93A0-0411D8D19E88";
   public static final String UDDI_RELATIONSHIPS_TMODEL = "UUID:807A2C6A-EE22-470D-ADC7-E0424A337C03";
   public static final String UDDI_OPERATORS_TMODEL = "UUID:327A56F0-3299-4461-BC23-5CD513E95C55";
   public static final String UDDI_DUNS_TMODEL = "UUID:8609C81E-EE1F-4D5A-B202-3EB13AD01823";
   public static final String UDDI_THOMAS_REGISTRY_TMODEL = "UUID:B1B1BAF5-2329-43E6-AE13-BA8E97195039";
   public static final String UDDI_GENERAL_KEYWORDS_TMODEL = "UUID:A035A07C-F362-44DD-8F95-E2B134BF43B4";
   public static final String UDDI_OWNING_BUSINESS_TMODEL = "UUID:4064C064-6D14-4F35-8953-9652106476A9";
   public static final String UDDI_IS_REPLACED_BY_TMODEL = "UUID:E59AE320-77A5-11D5-B898-0004AC49CC1E";
   public static final String UDDI_SMTP_TMODEL = "UUID:93335D49-3EFB-48A0-ACEA-EA102B60DDC6";
   public static final String UDDI_FAX_TMODEL = "UUID:1A2B00BE-6E2C-42F5-875B-56F32686E0E7";
   public static final String UDDI_FTP_TMODEL = "UUID:5FCF5CD0-629A-4C50-8B16-F94E9CF2A674";
   public static final String UDDI_TELEPHONE_TMODEL = "UUID:38E12427-5536-4260-A6F9-B5B530E63A07";
   public static final String UDDI_HTTP_TMODEL = "UUID:68DE9E80-AD09-469D-8A37-088422BFBC36";
   public static final String UDDI_HOMEPAGE_TMODEL = "UUID:4CEC1CEF-1F68-4B23-8CB7-8BAA763AEB89";
   private static UDDICoreTModels s_instance = null;
   private Hashtable m_CoreTModels = null;
   private Hashtable m_OtherCoreTModels = null;
   private Hashtable m_businessIdentifiers = null;
   private Hashtable m_tModelIdentifiers = null;
   public static final String TYPE_CATEGORIZATION = "categorization";
   public static final String TYPE_IDENTIFIER = "identifier";
   public static final String TYPE_RELATIONSHIP = "relationship";
   public static final String TYPE_TRANSPORT = "transport";
   public static final String TYPE_PROTOCOL = "protocol";
   public static final String TYPE_SPECIFICATION = "specification";

   public static UDDICoreTModels getInstance() throws UDDIException {
      if (s_instance == null) {
         Class var0 = UDDICoreTModels.class;
         synchronized(UDDICoreTModels.class) {
            if (s_instance == null) {
               try {
                  s_instance = new UDDICoreTModels();
               } catch (Exception var3) {
                  throw new FatalErrorException("Problems during instantiation of UDDICoreTModels", var3);
               }
            }
         }
      }

      return s_instance;
   }

   private UDDICoreTModels() throws UDDIException {
      this.m_CoreTModels = new Hashtable();
      this.m_CoreTModels.put("UUID:C1ACF26D-9672-4404-9D70-39B756E62AB4", new String[]{"categorization", "checked", "uddi-org:types", "tModel"});
      this.m_CoreTModels.put("UUID:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2", new String[]{"categorization", "checked", "ntis-gov:naics:1997", "businessEntity", "businessService", "tModel"});
      this.m_CoreTModels.put("UUID:DB77450D-9FA8-45D4-A7BC-04411D14E384", new String[]{"categorization", "checked", "unspsc-org:unspsc:3.1", "businessEntity", "businessService", "tModel"});
      this.m_CoreTModels.put("UUID:CD153257-086A-4237-B336-6BDCBDCC6634", new String[]{"categorization", "checked", "unspsc-org:unspsc:7.3", "businessEntity", "businessService", "tModel"});
      this.m_CoreTModels.put("UUID:4E49A8D6-D5A2-4FC2-93A0-0411D8D19E88", new String[]{"categorization", "checked", "uddi-org:iso-ch:3166:1999", "businessEntity", "businessService", "tModel"});
      this.m_CoreTModels.put("UUID:A035A07C-F362-44DD-8F95-E2B134BF43B4", new String[]{"categorization", "unchecked", "uddi-org:general_keywords", "businessEntity", "businessService", "tModel"});
      this.m_CoreTModels.put("UUID:4064C064-6D14-4F35-8953-9652106476A9", new String[]{"identifier", "checked", "uddi-org:owningBusiness", "tModel"});
      this.m_CoreTModels.put("UUID:807A2C6A-EE22-470D-ADC7-E0424A337C03", new String[]{"relationship", "unchecked", "uddi-org:relationships", "publisherAssertion"});
      this.m_CoreTModels.put("UUID:327A56F0-3299-4461-BC23-5CD513E95C55", new String[]{"identifier", "unchecked", "uddi-org:operators"});
      this.m_CoreTModels.put("UUID:8609C81E-EE1F-4D5A-B202-3EB13AD01823", new String[]{"identifier", "unchecked", "dnb-com:D-U-N-S", "businessEntity", "tModel"});
      this.m_CoreTModels.put("UUID:E59AE320-77A5-11D5-B898-0004AC49CC1E", new String[]{"identifier", "checked", "uddi-org:isReplacedBy", "businessEntity", "tModel"});
      this.m_CoreTModels.put("UUID:B1B1BAF5-2329-43E6-AE13-BA8E97195039", new String[]{"identifier", "unchecked", "thomasregister-com:supplierID", "businessEntity", "tModel"});
      this.m_CoreTModels.put("UUID:93335D49-3EFB-48A0-ACEA-EA102B60DDC6", new String[]{"transport", "unchecked", "uddi-org:smtp", "bindingTemplate"});
      this.m_CoreTModels.put("UUID:1A2B00BE-6E2C-42F5-875B-56F32686E0E7", new String[]{"protocol", "unchecked", "uddi-org:fax", "bindingTemplate"});
      this.m_CoreTModels.put("UUID:5FCF5CD0-629A-4C50-8B16-F94E9CF2A674", new String[]{"transport", "unchecked", "uddi-org:ftp", "bindingTemplate"});
      this.m_CoreTModels.put("UUID:38E12427-5536-4260-A6F9-B5B530E63A07", new String[]{"specification", "unchecked", "uddi-org:telephone", "bindingTemplate"});
      this.m_CoreTModels.put("UUID:68DE9E80-AD09-469D-8A37-088422BFBC36", new String[]{"transport", "unchecked", "uddi-org:http", "bindingTemplate"});
      this.m_CoreTModels.put("UUID:4CEC1CEF-1F68-4B23-8CB7-8BAA763AEB89", new String[]{"specification", "unchecked", "uddi-org:homepage", "bindingTemplate"});
      this.initOtherCoreTModels();
      this.initIdentifiers();
   }

   private void initOtherCoreTModels() throws UDDIException {
      this.m_OtherCoreTModels = new Hashtable();
      Iterator var1 = this.m_CoreTModels.keySet().iterator();

      while(true) {
         String var2;
         String var3;
         do {
            if (!var1.hasNext()) {
               return;
            }

            var2 = (String)var1.next();
            var3 = this.getType(var2);
            Logger.Log(3, (String)(" tModelKey = " + var2 + "; \ntype: " + var3));
         } while(!var3.equals("transport") && !var3.equals("specification") && !var3.equals("protocol"));

         this.m_OtherCoreTModels.put(var2, this.m_CoreTModels.get(var2));
      }
   }

   private void initIdentifiers() throws UDDIException {
      this.m_businessIdentifiers = new Hashtable();
      this.m_tModelIdentifiers = new Hashtable();
      Iterator var1 = this.m_CoreTModels.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         String var3 = this.getType(var2);
         Logger.Log(3, (String)(" tModelKey = " + var2 + "; \ntype: " + var3));
         if (var3.equals("identifier") && !var2.equals("UUID:327A56F0-3299-4461-BC23-5CD513E95C55")) {
            if (!var2.equals("UUID:4064C064-6D14-4F35-8953-9652106476A9")) {
               this.m_businessIdentifiers.put(var2, this.getName(var2));
            }

            this.m_tModelIdentifiers.put(var2, this.getName(var2));
         }
      }

   }

   public Hashtable getKeyNamePairForOtherCoreTModel() throws UDDIException {
      Hashtable var1 = new Hashtable();
      Iterator var2 = this.m_OtherCoreTModels.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = this.getName(var3);
         var1.put(var3, var4);
      }

      return var1;
   }

   public Hashtable getKeyNamePairForTModelIdentifiers() throws UDDIException {
      this.m_tModelIdentifiers = new Hashtable();
      this.m_tModelIdentifiers.put("UUID:8609C81E-EE1F-4D5A-B202-3EB13AD01823", this.getName("UUID:8609C81E-EE1F-4D5A-B202-3EB13AD01823"));
      this.m_tModelIdentifiers.put("UUID:B1B1BAF5-2329-43E6-AE13-BA8E97195039", this.getName("UUID:B1B1BAF5-2329-43E6-AE13-BA8E97195039"));
      this.m_tModelIdentifiers.put("UUID:E59AE320-77A5-11D5-B898-0004AC49CC1E", this.getName("UUID:E59AE320-77A5-11D5-B898-0004AC49CC1E"));
      this.m_tModelIdentifiers.put("UUID:4064C064-6D14-4F35-8953-9652106476A9", this.getName("UUID:4064C064-6D14-4F35-8953-9652106476A9"));
      TModels var1 = UserCategories.getInstance().getPluggableTModels();
      if (var1 != null) {
         for(TModelExt var2 = (TModelExt)var1.getFirst(); var2 != null; var2 = (TModelExt)var1.getNext()) {
            if (var2.getType().equalsIgnoreCase("identifier") && var2.getApplicableScopes().size() != 0 && var2.getApplicableScopes().contains("tModel")) {
            }

            this.m_tModelIdentifiers.put(var2.getTModelKey().getKey(), var2.getName().getName());
         }
      }

      return this.m_tModelIdentifiers;
   }

   public Hashtable getKeyNamePairForBusinessIdentifiers() throws UDDIException {
      this.m_businessIdentifiers = new Hashtable();
      this.m_businessIdentifiers.put("UUID:8609C81E-EE1F-4D5A-B202-3EB13AD01823", this.getName("UUID:8609C81E-EE1F-4D5A-B202-3EB13AD01823"));
      this.m_businessIdentifiers.put("UUID:B1B1BAF5-2329-43E6-AE13-BA8E97195039", this.getName("UUID:B1B1BAF5-2329-43E6-AE13-BA8E97195039"));
      this.m_businessIdentifiers.put("UUID:E59AE320-77A5-11D5-B898-0004AC49CC1E", this.getName("UUID:E59AE320-77A5-11D5-B898-0004AC49CC1E"));
      TModels var1 = UserCategories.getInstance().getPluggableTModels();
      if (var1 != null) {
         for(TModelExt var2 = (TModelExt)var1.getFirst(); var2 != null; var2 = (TModelExt)var1.getNext()) {
            if (var2.getType().equalsIgnoreCase("identifier") && var2.getApplicableScopes().size() != 0 && var2.getApplicableScopes().contains("businessEntity")) {
            }

            this.m_tModelIdentifiers.put(var2.getTModelKey().getKey(), var2.getName().getName());
         }
      }

      return this.m_businessIdentifiers;
   }

   public TModelExt getTModelExt(String var1) throws UDDIException {
      if (this.isCoreTModel(var1)) {
         TModelExt var2 = new TModelExt();
         var2.setTModelKey(new TModelKey(var1));
         var2.setName(new Name(this.getName(var1)));
         var2.setChecked(this.isChecked(var1));
         setTModelType(var2, this.getType(var1));
         this.setApplicableScopes(var2);
         return var2;
      } else {
         return null;
      }
   }

   public static void setTModelType(TModelExt var0, String var1) throws UDDIException {
      var0.setType(var1);
   }

   private void setApplicableScopes(TModelExt var1) {
      String[] var2 = (String[])((String[])this.m_CoreTModels.get(var1.getTModelKey().getKey().toUpperCase()));
      if (var2 != null && var2.length > 3) {
         for(int var3 = 3; var3 < var2.length; ++var3) {
            var1.addApplicableScope(var2[var3]);
         }
      }

   }

   public String getName(String var1) throws UDDIException {
      String[] var2 = (String[])((String[])this.m_CoreTModels.get(var1.toUpperCase()));
      return var2 != null ? var2[2] : null;
   }

   public String getType(String var1) throws UDDIException {
      String[] var2 = (String[])((String[])this.m_CoreTModels.get(var1.trim().toUpperCase()));
      return var2 != null ? var2[0] : null;
   }

   public boolean isChecked(String var1) throws UDDIException {
      String[] var2 = (String[])((String[])this.m_CoreTModels.get(var1.toUpperCase()));
      return var2 != null && var2[1].equals("checked");
   }

   public boolean isCoreTModel(String var1) throws UDDIException {
      return this.m_CoreTModels.get(var1.toUpperCase()) != null;
   }

   public boolean isOtherCoreTModel(String var1) throws UDDIException {
      return this.m_OtherCoreTModels.get(var1.toUpperCase()) != null;
   }

   public boolean isGeneralKeywordsTModel(String var1) throws UDDIException {
      return var1.toUpperCase().equals("UUID:A035A07C-F362-44DD-8F95-E2B134BF43B4");
   }

   public boolean isOwningBusinessTModel(String var1) throws UDDIException {
      return var1.toUpperCase().equals("UUID:4064C064-6D14-4F35-8953-9652106476A9");
   }

   public boolean isReplacedByTModel(String var1) throws UDDIException {
      return var1.toUpperCase().equals("UUID:E59AE320-77A5-11D5-B898-0004AC49CC1E");
   }

   public static void main(String[] var0) throws Exception {
      UDDICoreTModels var1 = getInstance();
      Hashtable var2 = var1.m_CoreTModels;
      Hashtable var3 = var1.getKeyNamePairForOtherCoreTModel();
      Iterator var4 = var2.keySet().iterator();
      Iterator var5 = var3.keySet().iterator();
      System.out.println("------------ UDDI Core TModels ------------ ");

      String var6;
      String var7;
      while(var4.hasNext()) {
         var6 = (String)var4.next();
         var7 = var1.getType(var6);
         String var8 = "unchecked";
         if (var1.isChecked(var6)) {
            var8 = "checked";
         }

         String var9 = var1.getName(var6);
         System.out.println(var6 + "; " + var7 + "; " + var8 + "; " + var9);
      }

      System.out.println("------------ UDDI Other Core TModels ------------ ");

      while(var5.hasNext()) {
         var6 = (String)var5.next();
         var7 = (String)var3.get(var6);
         System.out.println(var6 + "; " + var7);
      }

   }
}
