package weblogic.wsee.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.monitoring.HandlerStats;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public final class HandlerListImpl implements HandlerList {
   private List handlers = new ArrayList();
   private List handlerInfos = new ArrayList();
   private List handlerStats = new ArrayList();
   private boolean initialized = false;
   private static final boolean verbose = Verbose.isVerbose(HandlerListImpl.class);

   public HandlerListImpl() {
      this.initialized = true;
   }

   public HandlerListImpl(HandlerListImpl var1) {
      Iterator var2 = var1.handlerInfos.iterator();

      while(var2.hasNext()) {
         NamedHandlerInfo var3 = (NamedHandlerInfo)var2.next();
         this.handlerInfos.add(new NamedHandlerInfo(var3));
      }

   }

   public int size() {
      return this.handlers.size();
   }

   public Handler get(int var1) {
      return (Handler)this.handlers.get(var1);
   }

   public HandlerInfo getInfo(int var1) {
      return ((NamedHandlerInfo)this.handlerInfos.get(var1)).info;
   }

   public HandlerStats getStats(int var1) {
      return var1 >= 0 && var1 < this.handlerStats.size() ? (HandlerStats)this.handlerStats.get(var1) : null;
   }

   public String getName(int var1) {
      return ((NamedHandlerInfo)this.handlerInfos.get(var1)).name;
   }

   public void add(String var1, HandlerInfo var2) throws HandlerException {
      this.add(new NamedHandlerInfo(var1, var2));
   }

   private void add(NamedHandlerInfo var1) throws HandlerException {
      if (verbose) {
         Verbose.log((Object)("In HandlerList " + super.toString() + " adding handler: " + var1.name + " class=" + var1.info.getHandlerClass() + " at pos " + this.handlers.size() + " of " + this.handlers.size()));
      }

      Handler var2 = this.newHandler(var1.info);
      this.handlers.add(var2);
      this.handlerInfos.add(var1);
      if (verbose) {
         this.dumpHandlers();
      }

   }

   public void insert(String var1, int var2, HandlerInfo var3) throws HandlerException {
      if (verbose) {
         Verbose.log((Object)("In HandlerList " + super.toString() + " inserting handler: " + var1 + " class=" + var3.getHandlerClass() + " at pos " + var2 + " of " + this.handlers.size()));
      }

      Handler var4 = this.newHandler(var3);
      this.handlers.add(var2, var4);
      this.handlerInfos.add(var2, new NamedHandlerInfo(var1, var3));
      if (verbose) {
         this.dumpHandlers();
      }

   }

   public void insert(int var1, HandlerStats var2) {
      this.handlerStats.add(var1, var2);
   }

   public int lenientInsert(String var1, HandlerInfo var2, List<String> var3, List<String> var4) throws HandlerException {
      return this.internalInsert(var1, var2, var3, var4, true);
   }

   public int insert(String var1, HandlerInfo var2, List<String> var3, List<String> var4) throws HandlerException {
      return this.internalInsert(var1, var2, var3, var4, false);
   }

   private int internalInsert(String var1, HandlerInfo var2, List<String> var3, List<String> var4, boolean var5) throws HandlerException {
      int var6 = -1;
      if (verbose) {
         String var7 = this.dumpList(var4);
         String var8 = this.dumpList(var3);
         Verbose.log((Object)("In HandlerList " + super.toString() + " adding handler: " + var1 + " class=" + var2.getHandlerClass() + " before (" + var7 + ") and after (" + var8 + ") of " + this.handlers.size() + " total handlers"));
      }

      for(int var9 = 0; var9 < this.handlerInfos.size(); ++var9) {
         if (this.checkAfterConstraint(var9, var3, var5) && this.checkBeforeConstraint(var9, var4, var5)) {
            var6 = var9;
            break;
         }
      }

      if (var6 != -1) {
         this.insert(var1, var6, var2);
         return var6;
      } else {
         throw new HandlerException(this.getMessage(var3, var4));
      }
   }

   private String dumpList(List<String> var1) {
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         var2.append((String)var3.next());
         if (var3.hasNext()) {
            var2.append(",");
         }
      }

      return var2.toString();
   }

   private void dumpHandlers() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.handlerInfos.iterator();

      while(var2.hasNext()) {
         NamedHandlerInfo var3 = (NamedHandlerInfo)var2.next();
         var1.append(var3.name);
         if (var2.hasNext()) {
            var1.append(",");
         }
      }

      Verbose.log((Object)("In HandlerList " + super.toString() + " current is (" + var1.toString() + ")"));
   }

   private String getMessage(List<String> var1, List<String> var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append("Failed to insert handler after [");
      var3.append(var1);
      var3.append("] and before [");
      var3.append(var2);
      var3.append("] into handler chain [");
      Iterator var4 = this.handlerInfos.iterator();

      while(var4.hasNext()) {
         NamedHandlerInfo var5 = (NamedHandlerInfo)var4.next();
         var3.append(var5.name);
         var3.append(",");
      }

      return var3.toString();
   }

   private boolean checkBeforeConstraint(int var1, List<String> var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.addAll(var2);

      for(int var5 = var1; var5 < this.handlerInfos.size(); ++var5) {
         NamedHandlerInfo var6 = (NamedHandlerInfo)this.handlerInfos.get(var5);
         var4.remove(var6.name);
      }

      if (var4.isEmpty()) {
         return true;
      } else {
         return var3 && this.handlerNotPresent(var4);
      }
   }

   private boolean handlerNotPresent(ArrayList<String> var1) {
      for(int var2 = 0; var2 < this.handlerInfos.size(); ++var2) {
         NamedHandlerInfo var3 = (NamedHandlerInfo)this.handlerInfos.get(var2);
         if (var1.contains(var3.name)) {
            return false;
         }
      }

      return true;
   }

   private boolean checkAfterConstraint(int var1, List<String> var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.addAll(var2);

      for(int var5 = 0; var5 < var1; ++var5) {
         NamedHandlerInfo var6 = (NamedHandlerInfo)this.handlerInfos.get(var5);
         var4.remove(var6.name);
      }

      if (var4.isEmpty()) {
         return true;
      } else {
         return var3 && this.handlerNotPresent(var4);
      }
   }

   public void remove(int var1) {
      Handler var2 = this.get(var1);
      var2.destroy();
      this.handlers.remove(var1);
      this.handlerInfos.remove(var1);
      if (verbose) {
         this.dumpHandlers();
      }

   }

   public boolean remove(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("name == null");
      } else {
         if (verbose) {
            Verbose.log((Object)("In HandlerList " + super.toString() + " removing handler: " + var1));
         }

         int var2 = 0;

         for(Iterator var3 = this.handlerInfos.iterator(); var3.hasNext(); ++var2) {
            NamedHandlerInfo var4 = (NamedHandlerInfo)var3.next();
            if (var1.equals(var4.name)) {
               this.remove(var2);
               return true;
            }
         }

         return false;
      }
   }

   public boolean contains(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("name == null");
      } else {
         Iterator var2 = this.handlerInfos.iterator();

         NamedHandlerInfo var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (NamedHandlerInfo)var2.next();
         } while(!var1.equals(var3.name));

         return true;
      }
   }

   public String[] getHandlerNames() {
      String[] var1 = new String[this.handlerInfos.size()];
      int var2 = 0;

      for(Iterator var3 = this.handlerInfos.iterator(); var3.hasNext(); ++var2) {
         NamedHandlerInfo var4 = (NamedHandlerInfo)var3.next();
         var1[var2] = var4.name;
      }

      return var1;
   }

   public void destroy() {
      for(int var1 = this.size() - 1; var1 > -1; --var1) {
         Handler var2 = this.get(var1);

         try {
            var2.destroy();
         } catch (Throwable var4) {
            assert false : "Failed to destroy handler:" + var4;

            if (verbose) {
               Verbose.log("Failed to destroy handler", var4);
            }
         }
      }

      this.handlerInfos.clear();
      this.handlers.clear();
   }

   public void init() throws HandlerException {
      assert this.handlerInfos != null;

      assert this.handlerInfos.size() > 0;

      if (!this.initialized) {
         Iterator var1 = this.handlerInfos.iterator();

         while(var1.hasNext()) {
            NamedHandlerInfo var2 = (NamedHandlerInfo)var1.next();

            try {
               Handler var3 = this.newHandler(var2.info);
               this.handlers.add(var3);
            } catch (Throwable var4) {
               this.destroy();
               throw new HandlerException("Failed to init handlers", var4);
            }
         }

         this.initialized = true;
      }
   }

   private Handler newHandler(HandlerInfo var1) throws HandlerException {
      Class var2 = var1.getHandlerClass();

      try {
         Handler var3 = (Handler)var2.newInstance();
         var3.init(var1);
         return var3;
      } catch (InstantiationException var4) {
         throw new HandlerException("Exception in handler:" + var2.getName(), var4);
      } catch (IllegalAccessException var5) {
         throw new AssertionError(var5);
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeArray("handlers", this.handlers.iterator());
      var1.writeArray("handlerInfos", this.handlerInfos.iterator());
      var1.end();
   }

   private static class NamedHandlerInfo {
      public HandlerInfo info;
      public String name;

      public NamedHandlerInfo(String var1, HandlerInfo var2) {
         this.name = var1;
         this.info = var2;
      }

      public NamedHandlerInfo(NamedHandlerInfo var1) {
         this.name = var1.name;
         this.info = new HandlerInfo();
         this.info.setHandlerClass(var1.info.getHandlerClass());
         this.info.setHeaders(var1.info.getHeaders());
         Map var2 = var1.info.getHandlerConfig();
         HashMap var3 = null;
         if (var2 != null) {
            var3 = new HashMap();
            Iterator var4 = var2.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               var3.put(var5.getKey(), var5.getValue());
            }
         }

         this.info.setHandlerConfig(var3);
      }

      public String toString() {
         return this.name + " = " + this.info;
      }
   }
}
