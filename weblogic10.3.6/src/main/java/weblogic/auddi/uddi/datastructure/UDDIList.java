package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.response.BusinessInfo;
import weblogic.auddi.uddi.response.BusinessInfos;
import weblogic.auddi.uddi.response.ServiceInfo;
import weblogic.auddi.uddi.response.ServiceInfos;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.Util;

public abstract class UDDIList implements Serializable {
   private ArrayList m_list;
   private int m_index = -1;

   public List toList() {
      return this.m_list;
   }

   public UDDIList() {
      this.m_list = new ArrayList();
   }

   public UDDIList(UDDIList var1) {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null") + " in class: " + this.getClass().getName());
      } else {
         this.m_list = new ArrayList();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            this.m_list.add(var1.get(var2));
         }

      }
   }

   public boolean contains(Object var1) {
      return var1 != null ? this.m_list.contains(var1) : false;
   }

   public void add(UDDIListObject var1) {
      this.m_list.add(var1);
   }

   public void remove(UDDIListObject var1) throws UDDIException {
      this.m_list.remove(var1);
   }

   public int size() {
      return this.m_list.size();
   }

   public UDDIListObject getVFirst() {
      if (this.m_list.size() == 0) {
         return null;
      } else {
         this.m_index = 0;
         return (UDDIListObject)this.m_list.get(this.m_index++);
      }
   }

   public UDDIListObject getVNext() {
      return this.m_index > -1 && this.m_index < this.m_list.size() ? (UDDIListObject)this.m_list.get(this.m_index++) : null;
   }

   public int getCount() {
      return this.m_list.size();
   }

   public UDDIListObject get(int var1) {
      return (UDDIListObject)this.m_list.get(var1);
   }

   public void sortByName() throws UDDIException {
      if (this instanceof BusinessInfos || this instanceof ServiceInfos) {
         this.sortByName(0, this.m_list.size() - 1);
      }

   }

   private void sortByName(int var1, int var2) {
      int var3 = var1;
      int var4 = var2;
      if (var2 > var1) {
         String var5 = this.getName((var1 + var2) / 2);

         while(var3 <= var4) {
            while(var3 < var2 && this.getName(var3).compareToIgnoreCase(var5) < 0) {
               ++var3;
            }

            while(var4 > var1 && this.getName(var4).compareToIgnoreCase(var5) > 0) {
               --var4;
            }

            if (var3 <= var4) {
               this.swap(var3, var4);
               ++var3;
               --var4;
            }
         }

         if (var1 < var4) {
            this.sortByName(var1, var4);
         }

         if (var3 < var2) {
            this.sortByName(var3, var2);
         }
      }

   }

   private String getName(int var1) {
      if (var1 >= 0 && var1 < this.m_list.size()) {
         Object var2 = this.m_list.get(var1);
         if (var2 instanceof ServiceInfo) {
            return ((ServiceInfo)var2).getNames().getFirst().getName();
         } else {
            return var2 instanceof BusinessInfo ? ((BusinessInfo)var2).getUniqueNames().getFirst().getName() : null;
         }
      } else {
         return null;
      }
   }

   private void swap(int var1, int var2) {
      if (var1 != var2 && var1 < this.m_list.size() && var2 < this.m_list.size()) {
         int var3;
         int var4;
         if (var1 < var2) {
            var3 = var1;
            var4 = var2;
         } else {
            var3 = var2;
            var4 = var1;
         }

         Object var5 = this.m_list.get(var4);
         this.m_list.remove(var4);
         this.m_list.add(var4, this.m_list.get(var3));
         this.m_list.remove(var3);
         this.m_list.add(var3, var5);
      }

   }

   public boolean hasEqualContent(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof UDDIList)) {
         Logger.debug("UDDIList equals(): object recieved isn't a UDDIList:::" + var1.getClass().getName() + ":::");
         return false;
      } else {
         UDDIList var2 = (UDDIList)var1;
         if (this.size() != var2.size()) {
            Logger.debug("UDDIList equals(): Size Mismatch :::" + this.size() + "!=" + var2.size());
            return false;
         } else {
            UDDIListObject var3;
            UDDIListObject var4;
            label57:
            for(var3 = this.getVFirst(); var3 != null; var3 = this.getVNext()) {
               for(var4 = var2.getVFirst(); var4 != null; var4 = var2.getVNext()) {
                  if (var3.equals(var4)) {
                     continue label57;
                  }
               }

               Logger.debug("First uddi list entry not found in second one:::" + var3.toString());
               return false;
            }

            label46:
            for(var3 = var2.getVFirst(); var3 != null; var3 = var2.getVNext()) {
               for(var4 = this.getVFirst(); var4 != null; var4 = this.getVNext()) {
                  if (var4.equals(var3)) {
                     continue label46;
                  }
               }

               Logger.debug("Second uddi list entry not found in first one:::" + var3.toString());
               return false;
            }

            return true;
         }
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof UDDIList)) {
         return false;
      } else {
         boolean var2 = true;
         UDDIList var3 = (UDDIList)var1;
         if (this.size() != var3.size()) {
            Logger.debug("UDDIList equals(): Size Mismatch :::" + this.size() + "!=" + var3.size());
            return false;
         } else {
            var2 &= Util.isEqual((Object)this.m_list, (Object)var3.m_list);
            return var2;
         }
      }
   }

   public String toXML(String var1) {
      StringBuffer var2 = new StringBuffer();
      if (var1 != null && !var1.equals("")) {
         var2.append("<" + var1 + ">");
      }

      for(UDDIListObject var3 = this.getVFirst(); var3 != null; var3 = this.getVNext()) {
         var2.append(var3.toXML());
      }

      if (var1 != null && !var1.equals("")) {
         var2.append("</" + var1 + ">");
      }

      return var2.toString();
   }
}
