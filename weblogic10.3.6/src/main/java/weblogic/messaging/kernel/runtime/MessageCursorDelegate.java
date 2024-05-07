package weblogic.messaging.kernel.runtime;

import java.util.ArrayList;
import java.util.Comparator;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.messaging.Message;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.runtime.CursorDelegate;
import weblogic.messaging.runtime.CursorRuntimeImpl;
import weblogic.messaging.runtime.OpenDataConverter;

public class MessageCursorDelegate extends CursorDelegate {
   protected OpenDataConverter messageOpenDataConverter;
   protected transient CursorIterator cursorIterator;
   protected transient ArrayList lastPageList;

   public MessageCursorDelegate(CursorRuntimeImpl var1, OpenDataConverter var2, Cursor var3, OpenDataConverter var4, int var5) {
      super(var1, var2, var5);
      this.messageOpenDataConverter = var4;
      this.cursorIterator = new CursorIterator(var3);
      this.startPosition = this.endPosition = 0L;
   }

   public CompositeData[] getNext(int var1) throws OpenDataException {
      this.updateAccessTime();
      if (this.endPosition >= this.cursorIterator.size()) {
         return null;
      } else {
         int var2 = 0;
         this.lastPageList = new ArrayList();
         this.startPosition = this.endPosition;
         this.cursorIterator.seek(this.startPosition);

         do {
            this.lastPageList.add(this.cursorIterator.currentElement());
            ++var2;
         } while(this.cursorIterator.next() != null && var2 < var1);

         this.endPosition = this.startPosition + (long)var2;
         return this.getCompositeDataArray(this.lastPageList);
      }
   }

   public CompositeData[] getPrevious(int var1) throws OpenDataException {
      this.updateAccessTime();
      if (this.startPosition <= 0L) {
         this.startPosition = 0L;
         return null;
      } else {
         int var2 = 1;
         this.lastPageList = new ArrayList();
         this.endPosition = this.startPosition;
         this.cursorIterator.seek(this.startPosition - 1L);
         this.lastPageList.add(0, this.cursorIterator.currentElement());

         while(this.cursorIterator.previous() != null && var2 < var1) {
            this.lastPageList.add(0, this.cursorIterator.currentElement());
            ++var2;
         }

         this.startPosition = this.endPosition - (long)var2;
         return this.getCompositeDataArray(this.lastPageList);
      }
   }

   public CompositeData[] getItems(long var1, int var3) throws OpenDataException {
      this.updateAccessTime();
      if (this.cursorIterator.size() == 0L) {
         return null;
      } else if (var1 >= 0L && var1 < this.cursorIterator.size()) {
         this.cursorIterator.seek(var1);
         this.startPosition = this.endPosition = this.cursorIterator.getPosition();
         return this.getNext(var3);
      } else {
         throw new IndexOutOfBoundsException("Value of start argument (" + var1 + ") is invalid.  Start index must be between 0 and " + this.cursorIterator.size() + " - 1.");
      }
   }

   public Long getCursorSize() {
      this.updateAccessTime();
      return new Long(this.cursorIterator.size());
   }

   public void close() {
      super.close();
      this.cursorIterator.close();
   }

   public CompositeData getMessage(String var1) throws OpenDataException {
      this.updateAccessTime();
      MessageElement var2 = this.findMessage(new MessageIDMessageKey(var1));
      return var2 == null ? null : this.messageOpenDataConverter.createCompositeData(var2);
   }

   public CompositeData getMessage(long var1) throws OpenDataException {
      this.updateAccessTime();
      MessageElement var3 = this.findMessage(new HandleMessageKey(var1));
      return var3 == null ? null : this.messageOpenDataConverter.createCompositeData(var3);
   }

   protected CompositeData[] getCompositeDataArray(ArrayList var1) throws OpenDataException {
      CompositeData[] var2 = new CompositeData[var1.size()];

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2[var3] = this.openDataConverter.createCompositeData((MessageElement)var1.get(var3));
      }

      return var2;
   }

   protected boolean compareMessageID(Message var1, String var2) {
      return var1 != null && var1.getMessageID().toString().equals(var2);
   }

   protected MessageElement findMessage(MessageKey var1) {
      if (this.lastPageList != null) {
         for(int var2 = 0; var2 < this.lastPageList.size(); ++var2) {
            MessageElement var3 = (MessageElement)this.lastPageList.get(var2);
            if (var1.matches(var3)) {
               return var3;
            }
         }
      }

      this.cursorIterator.rewind();

      while(this.cursorIterator.hasNext()) {
         MessageElement var4 = this.cursorIterator.currentElement();
         if (var1.matches(var4)) {
            return var4;
         }

         this.cursorIterator.next();
      }

      return null;
   }

   public class CursorIterator {
      private Cursor cursor;
      private long currentPosition;
      private MessageElement currentElement;

      public CursorIterator(Cursor var2) {
         this.cursor = var2;
         this.rewind();
      }

      public void close() {
         this.cursor.close();
      }

      public boolean hasPrevious() {
         return this.currentPosition != 0L;
      }

      public boolean hasNext() {
         if (this.size() == 0L) {
            return false;
         } else {
            return this.currentPosition != this.size() - 1L;
         }
      }

      public MessageElement previous() {
         if (this.currentPosition == 0L) {
            return null;
         } else {
            try {
               MessageElement var1 = this.cursor.previous();
               if (var1 != null && var1.getMessage().equals(this.currentElement.getMessage())) {
                  var1 = this.cursor.previous();
               }

               if (var1 != null) {
                  --this.currentPosition;
               }

               this.currentElement = var1;
            } catch (KernelException var2) {
               this.currentElement = null;
            } catch (Exception var3) {
               this.currentElement = null;
            }

            return this.currentElement;
         }
      }

      public MessageElement next() {
         if (this.currentPosition == (long)(this.cursor.size() - 1)) {
            return null;
         } else {
            try {
               MessageElement var1 = this.cursor.next();
               if (var1 != null && var1.getMessage().equals(this.currentElement.getMessage())) {
                  var1 = this.cursor.next();
               }

               if (var1 != null) {
                  ++this.currentPosition;
               }

               this.currentElement = var1;
            } catch (KernelException var2) {
               this.currentElement = null;
            } catch (Exception var3) {
               this.currentElement = null;
            }

            return this.currentElement;
         }
      }

      public long getPosition() {
         return this.currentPosition;
      }

      public MessageElement currentElement() {
         return this.currentElement;
      }

      public MessageElement seek(long var1) {
         if (var1 < this.size() && var1 >= 0L) {
            while(this.getPosition() < var1) {
               this.next();
            }

            while(this.getPosition() > var1) {
               this.previous();
            }

            return this.currentElement;
         } else {
            return null;
         }
      }

      public MessageElement seek(MessageElement var1) {
         this.rewind();

         while(this.currentElement() != var1) {
            if (this.next() == null) {
               return null;
            }
         }

         return var1;
      }

      public MessageElement rewind() {
         while(true) {
            try {
               if (this.cursor.previous() != null) {
                  continue;
               }

               this.currentElement = this.cursor.next();
            } catch (KernelException var2) {
            }

            this.currentPosition = 0L;
            return this.currentElement;
         }
      }

      public long size() {
         return (long)this.cursor.size();
      }

      public void setComparator(Comparator var1) {
         this.cursor.setElementComparator(var1);
      }
   }

   class MessageIDMessageKey implements MessageKey {
      String messageID;

      MessageIDMessageKey(String var2) {
         this.messageID = var2;
      }

      public boolean matches(MessageElement var1) {
         Message var2 = var1.getMessage();
         return MessageCursorDelegate.this.compareMessageID(var2, this.messageID);
      }
   }

   class HandleMessageKey implements MessageKey {
      long handle;

      HandleMessageKey(long var2) {
         this.handle = var2;
      }

      public boolean matches(MessageElement var1) {
         return var1 != null && var1.getInternalSequenceNumber() == this.handle;
      }
   }

   interface MessageKey {
      boolean matches(MessageElement var1);
   }
}
