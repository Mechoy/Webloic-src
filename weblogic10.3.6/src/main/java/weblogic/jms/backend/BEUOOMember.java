package weblogic.jms.backend;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.CompletionRequest;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.path.Key;
import weblogic.messaging.path.LegalMember;
import weblogic.messaging.path.UpdatableMember;
import weblogic.messaging.path.helper.KeyString;
import weblogic.messaging.path.helper.MemberString;
import weblogic.messaging.saf.utils.SAFClientUtil;
import weblogic.store.PersistentStoreTransaction;

public class BEUOOMember extends MemberString implements LegalMember, UpdatableMember {
   static final long serialVersionUID = -1978058611468713508L;
   private static int EXTVERSION = 1;
   private static int VERSION_MASK = 4095;
   private static int FLAG_DYNAMIC = 4096;
   protected boolean dynamic;

   public BEUOOMember(String var1, String var2, boolean var3) {
      super(var1, var2);
      this.dynamic = var3;
   }

   public BEUOOMember() {
   }

   public boolean getDynamic() {
      return this.dynamic;
   }

   public void setTimestamp(long var1) {
      this.timestamp = var1;
   }

   public void setGeneration(int var1) {
      this.generation = var1;
   }

   public void update(Key var1, UpdatableMember var2, CompletionRequest var3) {
      BEOrderUpdateParentRequest var6;
      DistributedDestinationImpl var7;
      BEOrderUpdateRequest var8;
      try {
         KeyString var4 = (KeyString)var1;
         BEUOOMember var5 = (BEUOOMember)var2;
         if (!DDManager.isMember(var1.getAssemblyId(), this.getStringId())) {
            var3.setResult(new Exception("member " + this.getMemberId() + " unavailable within " + var4.getAssemblyId()));
            return;
         }

         var7 = DDManager.findDDImplByMemberName(this.getStringId());
         var8 = new BEOrderUpdateRequest(var7.getId(), var4, var5, this);
         var6 = new BEOrderUpdateParentRequest(var7.getId(), var8, var3);
      } catch (Error var20) {
         var3.setResult(var20);
         throw var20;
      } catch (RuntimeException var21) {
         var3.setResult(var21);
         throw var21;
      }

      try {
         var6.dispatchAsync(JMSDispatcherManager.dispatcherFindOrCreate(var7.getDispatcherId()), var8);
      } catch (Error var17) {
         synchronized(var6) {
            if (var6.getState() == Integer.MAX_VALUE) {
               return;
            }

            var6.setState(Integer.MAX_VALUE);
         }

         var3.setResult(var17);
         throw var17;
      } catch (DispatcherException var18) {
         synchronized(var6) {
            if (var6.getState() == Integer.MAX_VALUE) {
               return;
            }

            var6.setState(Integer.MAX_VALUE);
         }

         var3.setResult(var18);
      } catch (RuntimeException var19) {
         synchronized(var6) {
            if (var6.getState() == Integer.MAX_VALUE) {
               return;
            }

            var6.setState(Integer.MAX_VALUE);
         }

         var3.setResult(var19);
         throw var19;
      }

   }

   public void isLegal(Key var1, LegalMember var2, CompletionRequest var3) {
      Object var4 = null;
      RuntimeException var5 = null;
      Error var6 = null;

      try {
         var3.runListenersInSetResult(true);
         if (this.dynamic) {
            if (DDManager.isMember(var1.getAssemblyId(), this.getStringId())) {
               var4 = Boolean.TRUE;
            } else {
               var4 = Boolean.FALSE;
            }

            return;
         }

         var4 = Boolean.TRUE;
      } catch (RuntimeException var13) {
         var5 = var13;
         var4 = var13;
         return;
      } catch (Error var14) {
         var6 = var14;
         var4 = var14;
         return;
      } finally {
         var3.setResult(var4);
         if (var5 != null) {
            throw var5;
         }

         if (var6 != null) {
            throw var6;
         }

      }

   }

   public boolean updateException(Throwable var1, Key var2, UpdatableMember var3, PersistentStoreTransaction var4, CompletionRequest var5) {
      var5.setResult(Boolean.FALSE);
      return false;
   }

   public boolean equals(Object var1) {
      return this == var1 || var1 instanceof BEUOOMember && this.dynamic == ((BEUOOMember)var1).dynamic && super.equals(var1);
   }

   public int compareTo(Object var1) {
      int var2 = super.compareTo(var1);
      if (var2 != 0) {
         return var2;
      } else if (this.dynamic == ((BEUOOMember)var1).dynamic) {
         return 0;
      } else {
         return this.dynamic ? 1 : -1;
      }
   }

   public String toString() {
      return this.dynamic ? super.toString() + "^dynamic" : super.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = this.dynamic ? FLAG_DYNAMIC | EXTVERSION : EXTVERSION;
      var1.writeInt(var2);
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & VERSION_MASK;
      if (var3 != EXTVERSION) {
         throw SAFClientUtil.versionIOException(var3, EXTVERSION, EXTVERSION);
      } else {
         this.dynamic = (var2 & FLAG_DYNAMIC) != 0;
         super.readExternal(var1);
      }
   }
}
