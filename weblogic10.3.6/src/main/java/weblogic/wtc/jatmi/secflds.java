package weblogic.wtc.jatmi;

import java.util.Hashtable;

public final class secflds implements FldTbl {
   Hashtable nametofieldHashTable;
   Hashtable fieldtonameHashTable;
   public static final int AAA_MAJ_VERSION = 33554532;
   public static final int AAA_MIN_VERSION = 33554533;
   public static final int AAA_ATZ_TOKEN = 201326694;
   public static final int AAA_AUD_TOKEN = 201326695;

   public String Fldid_to_name(int var1) {
      if (this.fieldtonameHashTable == null) {
         this.fieldtonameHashTable = new Hashtable();
         this.fieldtonameHashTable.put(new Integer(33554532), "AAA_MAJ_VERSION");
         this.fieldtonameHashTable.put(new Integer(33554533), "AAA_MIN_VERSION");
         this.fieldtonameHashTable.put(new Integer(201326694), "AAA_ATZ_TOKEN");
         this.fieldtonameHashTable.put(new Integer(201326695), "AAA_AUD_TOKEN");
      }

      return (String)this.fieldtonameHashTable.get(new Integer(var1));
   }

   public int name_to_Fldid(String var1) {
      if (this.nametofieldHashTable == null) {
         this.nametofieldHashTable = new Hashtable();
         this.nametofieldHashTable.put("AAA_MAJ_VERSION", new Integer(33554532));
         this.nametofieldHashTable.put("AAA_MIN_VERSION", new Integer(33554533));
         this.nametofieldHashTable.put("AAA_ATZ_TOKEN", new Integer(201326694));
         this.nametofieldHashTable.put("AAA_AUD_TOKEN", new Integer(201326695));
      }

      Integer var2 = (Integer)this.nametofieldHashTable.get(var1);
      return var2 == null ? -1 : var2;
   }

   public String[] getFldNames() {
      String[] var1 = new String[]{new String("AAA_MAJ_VERSION"), new String("AAA_MIN_VERSION"), new String("AAA_ATZ_TOKEN"), new String("AAA_AUD_TOKEN")};
      return var1;
   }
}
