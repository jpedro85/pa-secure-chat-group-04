package Utils.Concurrency;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class VarSyncTest {

    @Test
    @DisplayName("Test syncGet() and syncSet()")
    public void testSyncGetAndSyncSet() {
        VarSync<Integer> varSync = new VarSync<>(10,true);

        assertEquals(10, varSync.syncGet());

        varSync.syncSet(20);
        assertEquals(20, varSync.syncGet());
    }

    @Test
    @DisplayName("Test asyncGet() and asyncSet()")
    public void testAsyncGetAndAsyncSet() {
        VarSync<Integer> varSync = new VarSync<>(30);

        assertEquals(30, varSync.asyncGet());

        varSync.asyncSet(40);
        assertEquals(40, varSync.asyncGet());
    }

    @Test
    @DisplayName("Test locking behavior")
    public void testLocking() throws InterruptedException {
        VarSync<Integer> varSync = new VarSync<>(50);

        // Create a thread that tries to increment the value while the lock is held
        Thread thread = new Thread(() -> {
            varSync.lock();
            varSync.syncSet(varSync.syncGet() + 1);
            varSync.unlock();
        });

        varSync.lock();
        thread.start();
        // Sleep to give the thread a chance to acquire the lock
        Thread.sleep(100);
        assertEquals(50, varSync.syncGet()); // Value should remain unchanged
        varSync.unlock();

        // Wait for the thread to finish
        thread.join();
        assertEquals(51, varSync.syncGet()); // Value should be incremented by 1
    }
}
