package weblogic.messaging.path;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.naming.NamingException;
import weblogic.common.CompletionRequest;
import weblogic.jms.backend.BEUOOMember;
import weblogic.jms.frontend.FEProducer;
import weblogic.management.ManagementException;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.messaging.runtime.ArrayCursorDelegate;
import weblogic.messaging.runtime.CursorRuntimeImpl;
import weblogic.messaging.runtime.OpenDataConverter;

public class PSEntryCursorDelegate extends ArrayCursorDelegate {
   private PSEntryInfo[] PSEntryArray;
   private PathServiceMap pathService;

   public PSEntryCursorDelegate(CursorRuntimeImpl var1, OpenDataConverter var2, int var3, Key[] var4, PathServiceMap var5) {
      super(var1, var2, var3);
      this.pathService = var5;
      this.entryArray = this.PSEntryArray = new PSEntryInfo[var4.length];

      for(int var6 = 0; var6 < var4.length; ++var6) {
         this.PSEntryArray[var6] = new PSEntryInfo(var4[var6], var6);
      }

   }

   private String jndiName() {
      return this.pathService.getJndiName();
   }

   public void remove(int var1) throws ManagementException {
      this.updateAccessTime();

      try {
         PathHelper.manager().cachedRemove(this.jndiName(), this.PSEntryArray[var1].getKey(), (Member)null, 32776);
      } catch (NamingException var3) {
         throw new ManagementException("remove operation failed on item handle " + var1, var3);
      } catch (PathHelper.PathServiceException var4) {
         throw new ManagementException("remove operation failed on item handle " + var1, var4);
      }
   }

   public void update(int var1, CompositeData var2) throws ManagementException {
      this.updateAccessTime();

      Member var3;
      try {
         MemberInfo var4 = new MemberInfo(var2);
         var3 = var4.getMember();
      } catch (OpenDataException var10) {
         throw new ManagementException("update operation failed on item handle " + var1, var10);
      }

      CompletionRequest var11 = new CompletionRequest();

      try {
         PathHelper.manager().update(this.jndiName(), this.PSEntryArray[var1].getKey(), var3, var11);
      } catch (NamingException var9) {
         throw new ManagementException("update operation failed on item handle " + var1, var9);
      }

      try {
         var11.getResult();
      } catch (RuntimeException var6) {
         throw var6;
      } catch (Error var7) {
         throw var7;
      } catch (Throwable var8) {
         throw new ManagementException("update operation failed on item handle " + var1, var8);
      }
   }

   public CompositeData getMember(int var1) throws ManagementException {
      this.updateAccessTime();
      CompletionRequest var2 = new CompletionRequest();

      try {
         PathHelper.manager().cachedGet(this.jndiName(), this.PSEntryArray[var1].getKey(), 33280, var2);
      } catch (NamingException var8) {
         throw new ManagementException("remove operation failed on item handle " + var1, var8);
      }

      Object var3;
      try {
         var3 = (Member)var2.getResult();
      } catch (Throwable var7) {
         throw new ManagementException("getMember operation failed on item handle " + var1, var7);
      }

      if (var3 instanceof FEProducer.ExtendedBEUOOMember) {
         FEProducer.ExtendedBEUOOMember var4 = (FEProducer.ExtendedBEUOOMember)var3;
         BEUOOMember var5 = new BEUOOMember(var4.getStringId(), ((Member)var3).getWLServerName(), var4.getDynamic());
         var5.setTimestamp(var4.getTimeStamp());
         var5.setGeneration(var4.getGeneration());
         var3 = var5;
      }

      MemberInfo var9 = new MemberInfo((Member)var3);

      try {
         return var9.toCompositeData();
      } catch (OpenDataException var6) {
         throw new ManagementException("getMember operation failed on item handle " + var1, var6);
      }
   }
}
