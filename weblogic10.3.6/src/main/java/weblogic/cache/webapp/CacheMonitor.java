package weblogic.cache.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CacheMonitor implements CacheListener, ServletContextListener {
   public static final String ATTRIBUTE = "weblogic.cache.CacheMonitor";
   private Node root = new Node();
   private static final int UPDATE = 1;
   private static final int ACCESS = 2;
   private static final int FLUSH = 3;

   public void contextInitialized(ServletContextEvent var1) {
      ServletContext var2 = var1.getServletContext();
      Object var3 = (List)var2.getAttribute("weblogic.cache.CacheListener");
      if (var3 == null) {
         var3 = new ArrayList();
         var2.setAttribute("weblogic.cache.CacheListener", var3);
      }

      ((List)var3).add(this);
      var2.setAttribute("weblogic.cache.CacheMonitor", this);
   }

   public void contextDestroyed(ServletContextEvent var1) {
   }

   public void cacheUpdateOccurred(CacheListener.CacheEvent var1) {
      this.update(1, var1);
   }

   public void cacheAccessOccurred(CacheListener.CacheEvent var1) {
      this.update(2, var1);
   }

   public void cacheFlushOccurred(CacheListener.CacheEvent var1) {
      this.update(3, var1);
   }

   private void update(int var1, CacheListener.CacheEvent var2) {
      this.update(this.root, var1, var2);
      String var3 = var2.getScopeType() + " " + var2.getScope();
      Node var4 = this.getSubNode(this.root, var3);
      this.update(var4, var1, var2);
      String var5 = var2.getName();
      if (var5 != null) {
         Node var6 = this.getSubNode(var4, var5);
         this.update(var6, var1, var2);
         KeySet var7 = var2.getKeySet();
         if (var7 != null) {
            Node var8 = this.getSubNode(var6, var7);
            this.update(var8, var1, var2);
         }
      }
   }

   private void update(Node var1, int var2, CacheListener.CacheEvent var3) {
      synchronized(var1) {
         switch (var2) {
            case 1:
               var1.updates++;
               var1.updateTime = var3.getTime();
               break;
            case 2:
               var1.accesses++;
               var1.accessTime = var3.getTime();
               break;
            case 3:
               var1.flushes++;
         }

      }
   }

   private Node getSubNode(Node var1, Object var2) {
      synchronized(var1) {
         Node var3 = var1.getNode(var2);
         if (var3 == null) {
            var3 = new Node();
            var1.subMap.put(var2, var3);
         }

         return var3;
      }
   }

   public Node getRoot() {
      return this.root;
   }

   public static class Node {
      private int updates;
      private int accesses;
      private int flushes;
      private int updateTime;
      private int accessTime;
      private Map subMap = new HashMap();

      public int getUpdates() {
         return this.updates;
      }

      public int getAccesses() {
         return this.accesses;
      }

      public int getFlushes() {
         return this.flushes;
      }

      public int getUpdateTime() {
         return this.updateTime;
      }

      public int getAccessTime() {
         return this.accessTime;
      }

      public synchronized Object[] getKeys() {
         return this.subMap.keySet().toArray();
      }

      public synchronized Node getNode(Object var1) {
         return (Node)this.subMap.get(var1);
      }
   }
}
