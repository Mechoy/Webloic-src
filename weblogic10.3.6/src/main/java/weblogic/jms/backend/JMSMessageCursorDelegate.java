package weblogic.jms.backend;

import java.util.ArrayList;
import javax.jms.JMSException;
import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.messaging.Message;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.runtime.MessageCursorDelegate;
import weblogic.messaging.runtime.CursorRuntimeImpl;
import weblogic.messaging.runtime.OpenDataConverter;

public class JMSMessageCursorDelegate extends MessageCursorDelegate {
   private BEDestinationImpl destination;

   public JMSMessageCursorDelegate(CursorRuntimeImpl var1, OpenDataConverter var2, Cursor var3, OpenDataConverter var4, BEDestinationImpl var5, int var6) {
      super(var1, var2, var3, var4, var6);
      this.destination = var5;
   }

   public JMSMessageCursorDelegate(CursorRuntimeImpl var1, OpenDataConverter var2, Cursor var3, OpenDataConverter var4, int var5) {
      super(var1, var2, var3, var4, var5);
   }

   public long sort(long var1, String[] var3, Boolean[] var4) throws ModuleException {
      this.updateAccessTime();
      Object var5 = null;
      if (var1 != -1L) {
         this.cursorIterator.seek(var1);
      }

      ArrayList var6 = new ArrayList();
      if (var3 != null) {
         for(int var7 = 0; var7 < var3.length; ++var7) {
            boolean var8 = true;
            if (var4 != null && var7 < var4.length && !var4[var7]) {
               var8 = false;
            }

            var6.add(new BECursorDestinationKey(this.destination, new CursorDestinationKeyBean(var3[var7], var8)));
         }
      }

      this.cursorIterator.setComparator(new JMSMessageCursorComparator(var6));
      this.cursorIterator.rewind();
      if (var5 != null) {
         this.cursorIterator.seek((MessageElement)var5);
         return this.cursorIterator.getPosition();
      } else {
         return 0L;
      }
   }

   protected boolean compareMessageID(Message var1, String var2) {
      if (var1 != null && var1 instanceof javax.jms.Message) {
         javax.jms.Message var3 = (javax.jms.Message)var1;

         try {
            return var3.getJMSMessageID().equals(var2);
         } catch (JMSException var5) {
            return false;
         }
      } else {
         return false;
      }
   }

   class CursorDestinationKeyBean implements DestinationKeyBean {
      String name;
      String property;
      String order = "Ascending";

      CursorDestinationKeyBean(String var2, boolean var3) {
         this.property = this.name = var2;
         if (!var3) {
            this.order = "Descending";
         }

      }

      public String getKeyType() {
         return "String";
      }

      public void setKeyType(String var1) {
      }

      public boolean isSetType() {
         return false;
      }

      public void unsetType() {
      }

      public String getProperty() {
         return this.property;
      }

      public void setProperty(String var1) {
         this.property = var1;
      }

      public boolean isSetProperty() {
         return this.property != null;
      }

      public void unsetProperty() {
         this.property = null;
      }

      public String getSortOrder() {
         return this.order;
      }

      public void setSortOrder(String var1) {
         this.order = var1;
      }

      public boolean isSetSortOrder() {
         return this.order != null;
      }

      public void unsetSortOrder() {
         this.order = null;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public boolean isSetName() {
         return this.name != null;
      }

      public void unsetName() {
         this.name = null;
      }

      public String getNotes() {
         return null;
      }

      public void setNotes(String var1) {
      }

      public boolean isSet(String var1) {
         return true;
      }

      public void unSet(String var1) {
      }
   }
}
