package weblogic.xml.security.utils;

import java.util.NoSuchElementException;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class XMLEventBuffer {
   private static final int INITIAL_BUFFER_SIZE = 256;
   private int initialSize = 256;
   private int bufferSize;
   private int nextWrite;
   private int nextRead;
   private int bufferCount;
   private XMLEvent[] buffer;

   public XMLEventBuffer() {
      this.initialSize = 256;
   }

   public XMLEventBuffer(int var1) {
      this.initialSize = var1;
   }

   public void add(XMLEvent var1) {
      if (this.buffer == null) {
         this.init(this.initialSize);
      }

      if (this.bufferCount == this.bufferSize) {
         this.expand();
      }

      this.buffer[this.nextWrite] = var1;
      this.eventWritten();
   }

   private void init(int var1) {
      this.bufferSize = var1;
      this.buffer = new XMLEvent[this.bufferSize];
      this.nextWrite = this.nextRead = this.bufferCount = 0;
   }

   public XMLEvent peek() {
      if (this.hasNext()) {
         return this.buffer[this.nextRead];
      } else {
         throw new NoSuchElementException("buffer is empty");
      }
   }

   public boolean hasNext() {
      return this.bufferCount > 0;
   }

   public XMLEvent next() {
      if (this.hasNext()) {
         XMLEvent var1 = this.buffer[this.nextRead];
         this.buffer[this.nextRead] = null;
         this.eventRead();
         return var1;
      } else {
         throw new NoSuchElementException("buffer is empty");
      }
   }

   public void process(XMLOutputStream var1) throws XMLStreamException {
      while(this.hasNext()) {
         var1.add(this.next());
      }

   }

   private void eventWritten() {
      ++this.bufferCount;
      ++this.nextWrite;
      if (this.nextWrite == this.bufferSize) {
         this.nextWrite = 0;
      }

   }

   private void eventRead() {
      --this.bufferCount;
      ++this.nextRead;
      if (this.nextRead == this.bufferSize) {
         this.nextRead = 0;
      }

      if (this.bufferCount < 1) {
         this.collapse();
      }

   }

   private void collapse() {
      this.buffer = null;
      this.bufferCount = 0;
      this.bufferSize = 0;
      this.nextRead = 0;
      this.nextWrite = 0;
   }

   private void expand() {
      XMLEvent[] var1 = new XMLEvent[this.bufferSize * 2];
      int var2 = this.nextRead;

      for(int var3 = 0; var3 < this.bufferSize; ++var3) {
         if (var2 == this.bufferSize) {
            var2 = 0;
         }

         var1[var3] = this.buffer[var2];
         ++var2;
      }

      this.nextRead = 0;
      this.nextWrite = this.bufferSize;
      this.bufferSize = 2 * this.bufferSize;
      this.buffer = var1;
   }
}
