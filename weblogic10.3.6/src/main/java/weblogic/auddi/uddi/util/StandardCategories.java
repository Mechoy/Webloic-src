package weblogic.auddi.uddi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Logger;

public class StandardCategories extends StandardTModels {
   private static StandardCategories s_instance = null;

   private StandardCategories() throws UDDIException {
      String var1 = "listTypesTModel.xml";
      String var2 = "listRelationshipsTModel.xml";
      String var3 = "listUNSPSC31TModel.xml";
      String var4 = "listUNSPSC73TModel.xml";
      String var5 = "listNAICSTModel.xml";
      String var6 = "listISO3166TModel.xml";
      String var7 = "/weblogic/auddi/uddi/resources/";
      this.loadFile(var7 + var1);
      this.loadFile(var7 + var2);
      this.loadFile(var7 + var3);
      this.loadFile(var7 + var4);
      this.loadFile(var7 + var6);
      this.loadFile(var7 + var5);
      this.addUserCategories();
   }

   public static StandardCategories getInstance() throws UDDIException {
      if (s_instance == null) {
         Class var0 = StandardCategories.class;
         synchronized(StandardCategories.class) {
            if (s_instance == null) {
               s_instance = new StandardCategories();
            }
         }
      }

      return s_instance;
   }

   public boolean isFormatCorrect(String var1, String var2, String var3) {
      Logger.trace("+isFormatCorrect");
      var1 = var1.toLowerCase();
      if (!var1.equals("UUID:CD153257-086A-4237-B336-6BDCBDCC6634".toLowerCase()) && !var1.equals("UUID:DB77450D-9FA8-45D4-A7BC-04411D14E384".toLowerCase()) && !var1.equals("UUID:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2".toLowerCase()) || var2.length() >= 2 && var2.substring(0, 2).toLowerCase().equals(var3.substring(0, 2).toLowerCase())) {
         Logger.trace("true");
         Logger.trace("-isFormatCorrect");
         return true;
      } else {
         Logger.trace("false");
         Logger.trace("-isFormatCorrect");
         return false;
      }
   }

   public List getCheckedCategoryForTModel() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         XMLToTree var3 = (XMLToTree)var2.next();
         String var4 = var3.getKey();
         if (!var4.toLowerCase().equals("UUID:807A2C6A-EE22-470D-ADC7-E0424A337C03".toLowerCase()) && !var4.toLowerCase().equals("UUID:807A2C6A-EE22-470D-ADC7-E0424A337C03".toLowerCase())) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public List getCheckedCategoryForBusiness() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         XMLToTree var3 = (XMLToTree)var2.next();
         String var4 = var3.getKey();
         if (!var4.toLowerCase().equals("UUID:C1ACF26D-9672-4404-9D70-39B756E62AB4".toLowerCase()) && !var4.toLowerCase().equals("UUID:807A2C6A-EE22-470D-ADC7-E0424A337C03".toLowerCase())) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Hashtable getUncheckedCategoryAsKeyNamePair() throws UDDIException {
      Hashtable var1 = new Hashtable();
      String var2 = "UUID:A035A07C-F362-44DD-8F95-E2B134BF43B4";
      var1.put(var2, UDDICoreTModels.getInstance().getName(var2));
      return var1;
   }

   private void addUserCategories() throws UDDIException {
      HashMap var1 = UserCategories.getInstance().getCategorizationItems();
      if (var1.size() > 0) {
         this.m_items.putAll(var1);
      }

   }

   public static void main(String[] var0) throws Exception {
      ArrayList var1 = (ArrayList)getInstance().getCheckedCategoryForTModel();
      JFrame var2 = XMLToTree.getJTreeFrame((XMLToTree[])((XMLToTree[])var1.toArray(new XMLToTree[0])), "Standard Categories");
      var2.setVisible(true);
   }
}
