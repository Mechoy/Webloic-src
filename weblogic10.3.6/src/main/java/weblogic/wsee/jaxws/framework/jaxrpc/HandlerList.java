package weblogic.wsee.jaxws.framework.jaxrpc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.jaxws.tubeline.Ordering;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;

public class HandlerList implements weblogic.wsee.handler.HandlerList {
   private List<NamedHandlerInfo> internal = new ArrayList();
   protected Set<TubelineAssemblerItem> items;

   public HandlerList(Set<TubelineAssemblerItem> var1) {
      this.items = var1;
      this.initialize();
   }

   protected void initialize() {
      Ordering var1 = new Ordering(this.items);
      int var2 = var1.size();
      TubelineAssemblerItem[] var3 = new TubelineAssemblerItem[var2];

      TubelineAssemblerItem var5;
      for(Iterator var4 = var1.iterator(); var4.hasNext(); var3[var2] = var5) {
         var5 = (TubelineAssemblerItem)var4.next();
         --var2;
      }

      TubelineAssemblerItem[] var8 = var3;
      int var9 = var3.length;

      for(int var6 = 0; var6 < var9; ++var6) {
         TubelineAssemblerItem var7 = var8[var6];
         this.add(var7.getName(), var7);
      }

   }

   protected void add(String var1, TubelineAssemblerItem var2) {
      Iterator var3 = this.internal.iterator();

      NamedHandlerInfo var4;
      do {
         if (!var3.hasNext()) {
            this.internal.add(new NamedHandlerInfo(var1, var2));
            return;
         }

         var4 = (NamedHandlerInfo)var3.next();
      } while(!var4.getName().equals(var1));

      var4.item = var2;
   }

   public void add(String var1, HandlerInfo var2) throws HandlerException {
      this.insert(var1, this.size(), var2);
   }

   public boolean contains(String var1) {
      Iterator var2 = this.internal.iterator();

      NamedHandlerInfo var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (NamedHandlerInfo)var2.next();
      } while(!var3.getName().equals(var1));

      return true;
   }

   public String[] getHandlerNames() {
      String[] var1 = new String[this.internal.size()];

      for(int var2 = 0; var2 < this.internal.size(); ++var2) {
         var1[var2] = ((NamedHandlerInfo)this.internal.get(var2)).getName();
      }

      return var1;
   }

   public HandlerInfo getInfo(int var1) {
      return ((NamedHandlerInfo)this.internal.get(var1)).getInfo();
   }

   public String getName(int var1) {
      return ((NamedHandlerInfo)this.internal.get(var1)).getName();
   }

   public void insert(String var1, int var2, HandlerInfo var3) throws HandlerException {
      TubelineAssemblerItem var4 = new TubelineAssemblerItem(var1, new TubeFactory(var3));
      this.internal.add(var2, new NamedHandlerInfo(var1, var4));
      this.items.clear();
      Iterator var5 = this.internal.iterator();

      while(var5.hasNext()) {
         NamedHandlerInfo var6 = (NamedHandlerInfo)var5.next();
         if (var6.getItem() != null) {
            this.items.add(var6.getItem());
         }
      }

   }

   public int insert(String var1, HandlerInfo var2, List<String> var3, List<String> var4) throws HandlerException {
      return this.orderedInsert(var1, var2, var3, var4, false);
   }

   public int lenientInsert(String var1, HandlerInfo var2, List<String> var3, List<String> var4) throws HandlerException {
      return this.orderedInsert(var1, var2, var3, var4, true);
   }

   private int orderedInsert(String var1, HandlerInfo var2, List<String> var3, List<String> var4, boolean var5) throws HandlerException {
      HashSet var6 = var3 != null ? new HashSet(var3) : null;
      HashSet var7 = var4 != null ? new HashSet(var4) : null;
      int var8 = -1;

      for(int var9 = 0; var9 < this.internal.size(); ++var9) {
         if (this.checkAfterConstraint(var9, var6, var5) && this.checkBeforeConstraint(var9, var7, var5)) {
            var8 = var9;
            break;
         }
      }

      if (var8 != -1) {
         this.insert(var1, var8, var2);
         return var8;
      } else {
         throw new HandlerException(this.getMessage(var6, var7));
      }
   }

   private String getMessage(Set<String> var1, Set<String> var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append("Failed to insert handler after [");
      var3.append(var1);
      var3.append("] and before [");
      var3.append(var2);
      var3.append("] into handler chain [");
      Iterator var4 = this.internal.iterator();

      while(var4.hasNext()) {
         NamedHandlerInfo var5 = (NamedHandlerInfo)var4.next();
         var3.append(var5.name);
         var3.append(",");
      }

      return var3.toString();
   }

   private boolean checkBeforeConstraint(int var1, Set<String> var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.addAll(var2);

      for(int var5 = var1; var5 < this.internal.size(); ++var5) {
         NamedHandlerInfo var6 = (NamedHandlerInfo)this.internal.get(var5);
         var4.remove(var6.name);
      }

      if (var4.isEmpty()) {
         return true;
      } else {
         return var3 && this.handlerNotPresent(var4);
      }
   }

   private boolean handlerNotPresent(ArrayList<String> var1) {
      for(int var2 = 0; var2 < this.internal.size(); ++var2) {
         NamedHandlerInfo var3 = (NamedHandlerInfo)this.internal.get(var2);
         if (var1.contains(var3.name)) {
            return false;
         }
      }

      return true;
   }

   private boolean checkAfterConstraint(int var1, Set<String> var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.addAll(var2);

      for(int var5 = 0; var5 < var1; ++var5) {
         NamedHandlerInfo var6 = (NamedHandlerInfo)this.internal.get(var5);
         var4.remove(var6.name);
      }

      if (var4.isEmpty()) {
         return true;
      } else {
         return var3 && this.handlerNotPresent(var4);
      }
   }

   public void remove(int var1) {
      NamedHandlerInfo var2 = (NamedHandlerInfo)this.internal.remove(var1);
   }

   public boolean remove(String var1) {
      Iterator var2 = this.internal.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!((NamedHandlerInfo)var2.next()).getName().equals(var1));

      var2.remove();
      return true;
   }

   public int size() {
      return this.internal.size();
   }

   protected HandlerInfo getHandlerInfo(TubelineAssemblerItem var1) {
      return var1 != null && var1.getFactory() instanceof TubeFactory ? ((TubeFactory)var1.getFactory()).getHandlerInfo() : null;
   }

   private class NamedHandlerInfo {
      private String name;
      private TubelineAssemblerItem item;

      public NamedHandlerInfo(String var2, TubelineAssemblerItem var3) {
         this.name = var2;
         this.item = var3;
      }

      public TubelineAssemblerItem getItem() {
         return this.item;
      }

      public HandlerInfo getInfo() {
         return HandlerList.this.getHandlerInfo(this.item);
      }

      public String getName() {
         return this.name;
      }
   }
}
