package weblogic.messaging.runtime;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;

public class ArrayCursorDelegate extends CursorDelegate {
   protected transient Object[] entryArray;

   public ArrayCursorDelegate(CursorRuntimeImpl var1, OpenDataConverter var2, int var3) {
      super(var1, var2, var3);
      this.startPosition = this.endPosition = -1L;
   }

   public CompositeData[] getNext(int var1) throws OpenDataException {
      int var2 = var1;
      if ((long)((int)this.getSize() - 1) - this.endPosition < (long)var1) {
         var2 = (int)this.getSize() - 1 - (int)this.endPosition;
      }

      return this.returnItems(this.endPosition + 1L, var2);
   }

   public CompositeData[] getPrevious(int var1) throws OpenDataException {
      int var2 = var1;
      if (this.startPosition < (long)var1) {
         var2 = (int)this.startPosition;
      }

      return this.returnItems(this.startPosition - (long)var2, var2);
   }

   public CompositeData[] getItems(long var1, int var3) throws OpenDataException {
      this.updateAccessTime();
      if (var1 >= 0L && var1 < (long)this.entryArray.length) {
         this.startPosition = this.endPosition = var1 - 1L;
         return this.getNextItems(var3);
      } else {
         throw new IndexOutOfBoundsException("Value of start argument (" + var1 + ") is invalid.  Start index must be between 0 and " + this.entryArray.length + " - 1.");
      }
   }

   protected CompositeData[] getPreviousItems(int var1) throws OpenDataException {
      int var2 = var1;
      if (this.startPosition < (long)var1) {
         var2 = (int)this.startPosition;
      }

      return this.returnItems(this.startPosition - (long)var2, var2);
   }

   protected CompositeData[] getNextItems(int var1) throws OpenDataException {
      int var2 = var1;
      if ((long)((int)this.getSize() - 1) - this.endPosition < (long)var1) {
         var2 = (int)this.getSize() - 1 - (int)this.endPosition;
      }

      return this.returnItems(this.endPosition + 1L, var2);
   }

   public Long getCursorSize() {
      this.updateAccessTime();
      return new Long(this.getSize());
   }

   private long getSize() {
      return (long)this.entryArray.length;
   }

   private CompositeData[] returnItems(long var1, int var3) throws OpenDataException {
      this.updateAccessTime();
      if (var3 == 0) {
         return null;
      } else {
         CompositeData[] var4 = new CompositeData[var3];

         for(int var5 = 0; var5 < var3; ++var5) {
            var4[var5] = this.openDataConverter.createCompositeData(this.entryArray[var5]);
         }

         this.startPosition = var1;
         this.endPosition = this.startPosition + (long)var3 - 1L;
         return var4;
      }
   }
}
