package Utils.Concurrency;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Generic Class that merges a var and a lock. Util when we need to sync a single variable.
 * Useful when synchronization of a single variable is required.
 * @param <T> Type of the variable.
 */
public class VarSync<T>{

    private T value;
    private final ReentrantLock lock;

    /**
     * Creates an instance of VarSync
     *
     * @param value variable to sync
     * @param fair if the lock is fair, that means the lock respect the order of lock calls.
     */
    public VarSync(T value, boolean fair ){
        this.value = value;
        this.lock = new ReentrantLock(fair);
    }

    /**
     * Creates an instance of VarSync with the default value of fair as true
     *
     * @param value value to sync
     */
    public VarSync(T value){
        this.value = value;
        this.lock = new ReentrantLock(true);
    }

    /**
     * Acquires the lock.
     */
    public void lock(){
        this.lock.lock();
    }

    /**
     * Releases the lock.
     */
    public void unlock(){
        this.lock.unlock();
    }

    /**
     * Retrieves the current value of the variable after acquiring the lock.
     *
     * @return The current value of the stored variable.
     */
    public T syncGet(){
        T var;
        this.lock.lock();
        var = this.value;
        this.lock.unlock();
        return var;
    }

    /**
     * Sets the value of the variable to the specified newValue after acquiring the lock.
     *
     * @param newValue The value to replace the current value.
     */
    public void syncSet(T newValue){
        this.lock.lock();
        this.value = newValue;
        this.lock.unlock();
    }

    /**
     * Retrieves the current value of the variable without acquiring the lock.
     * Useful in a multi-statement context where <b>already acquired the lock</b>.
     *
     * @return The current value of the stored variable.
     */
    public T asyncGet(){
        return this.value;
    }

    /**
     * Sets the value of the variable to the specified newValue without acquiring the lock.
     * Useful in a multi-statement context where we <b>already acquired the lock</b>.
     *
     * @param newValue The value to replace the current value.
     */
    public void asyncSet( T newValue){
        this.value = newValue;
    }

    /**
     * Retrieves the ReentrantLock used for synchronization.
     *
     * @return The ReentrantLock object associated with this VarSync instance.
     */
    public ReentrantLock getLock(){
        return this.lock;
    }

}
