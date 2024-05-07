package weblogic.xml.util.cache.entitycache;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

class Persister extends WorkAdapter {
   static Persister persisterInstance = null;
   Vector persistQueue = new Vector();

   static Persister get() {
      Class var0 = Persister.class;
      synchronized(Persister.class) {
         if (persisterInstance == null) {
            persisterInstance = new Persister();
         }
      }

      return persisterInstance;
   }

   public void run() {
      synchronized(this) {
         try {
            Enumeration var2 = this.persistQueue.elements();

            while(true) {
               CacheEntry var3;
               do {
                  if (!var2.hasMoreElements()) {
                     return;
                  }

                  var3 = (CacheEntry)var2.nextElement();
               } while(!var3.isPersistent());

               synchronized(var3.cache) {
                  try {
                     var3.saveEntry();
                  } catch (CX.EntryTooLargeDisk var8) {
                     var3.cache.notifyListener(new Event.EntryDiskRejectionEvent(var3.cache, var3));
                     var3.makeTransient(true);
                  } catch (Exception var9) {
                     var3.makeTransient(true);
                     continue;
                  }

                  var3.cache.notifyListener(new Event.EntryPersistEvent(var3.cache, var3));
               }

               this.persistQueue.removeAllElements();
            }
         } catch (Exception var11) {
            Tools.px(var11);
         }
      }
   }

   void add(CacheEntry var1) {
      synchronized(this) {
         if (var1.isPersistent()) {
            this.persistQueue.addElement(var1);
            WorkManagerFactory.getInstance().getSystem().schedule(this);
         }
      }
   }
}
