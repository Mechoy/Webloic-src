package weblogic.socket;

final class PollError {
   int badFdCount = 0;
   int[] fds;
   int[] revents;

   PollError(int[] var1) {
      this.fds = var1;
      this.revents = (int[])((int[])var1.clone());
   }

   void clear() {
      this.badFdCount = 0;
   }
}
