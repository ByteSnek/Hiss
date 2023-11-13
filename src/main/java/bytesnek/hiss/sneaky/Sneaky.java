package bytesnek.hiss.sneaky;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import bytesnek.hiss.math.Maths;
import bytesnek.hiss.system.OS;
import bytesnek.hiss.thread.UncaughtExceptionThread;
import sun.misc.Unsafe;

/**
 * Created by SnakerBone on 3/07/2023
 **/
public class Sneaky
{
    private static final SNKR SNKR = new SNKR();
    private static Unsafe theUnsafe;

    private static final ByteOrder BYTE_ORDER = ByteOrder.nativeOrder();
    private static final ByteShift SHIFT;

    static {
        if (BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
            SHIFT = new ByteShift()
            {
                @Override
                public long left(long value, int bytes)
                {
                    return value << (bytes << 3);
                }

                @Override
                public long right(long value, int bytes)
                {
                    return value >>> (bytes << 3);
                }
            };
        } else {
            SHIFT = new ByteShift()
            {
                @Override
                public long left(long value, int bytes)
                {
                    return value >>> (bytes << 3);
                }

                @Override
                public long right(long value, int bytes)
                {
                    return value << (bytes << 3);
                }
            };
        }

        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            theUnsafe = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            UncaughtExceptionThread.createAndRun("Failed to get theUnsafe", e);
        }
    }

    /**
     * Returns an object that can be assigned to anything that is not a primitive type
     *
     * @return An empty versatile object
     **/
    public static <V> V versatileObject()
    {
        return cast(new Object[0]);
    }

    /**
     * Attempts to cast anything to anything
     *
     * @param object The object to cast
     * @return The result of the cast
     * @throws ClassCastException if the value could not be cast
     **/
    @SuppressWarnings("unchecked")
    public static <V> V cast(Object object)
    {
        V value;

        try {
            value = (V) object;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }

        return value;
    }

    /**
     * Attempts to create a new instance of any object
     *
     * @param clazz The class to instantiate
     * @return The class instance
     * @throws RuntimeException     if the class could not be instantiated
     * @throws NullPointerException if the class is null
     **/
    @NotNull
    public static <T> T instantiate(Class<T> clazz)
    {
        try {
            return cast(theUnsafe.allocateInstance(clazz));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempts to create a new instance of any object
     *
     * @param obj The object to get
     * @return The object if it isn't null or a versatile object
     **/
    public static <V> V getOrUnsafeAssign(V obj)
    {
        return Objects.requireNonNullElse(obj, versatileObject());
    }

    /**
     * Force crashes the JVM by starving its memory
     **/
    public static void forceCrashJVM()
    {
        setMemory(0, 0, 1);
    }

    /**
     * Says goodbye to the current context
     **/
    public static void goodbyeWorld()
    {
        OS os = OS.identify();

        if (os != OS.WINDOWS) {
            throw new IllegalStateException("Current OS is not windows");
        }

        SNKR.goodbyeWorld();
    }

    /**
     * Sets the memory of the JVM
     *
     * @param pointer The pointer
     * @param value   The value
     * @param bytes   The bytes
     **/
    public static void setMemory(long pointer, int value, long bytes)
    {
        if (bytes < 256) {
            int ptr = (int) pointer;

            if (is64bit()) {
                int arch = ptr & 7;

                if (arch == 0) {
                    setMemory64(pointer, value, (int) bytes & 0xFF);
                }
            } else {
                int arch = ptr & 3;

                if (arch == 0) {
                    setMemory32(ptr, value, (int) bytes & 0xFF);
                }
            }
        }
    }

    /**
     * Gets uninitialized memory that is present on the current stack. Potentially can be unsafe due to UB and unwanted code execution before security checks are done. Can be useful as Java does not permit reading uninitialized memory addresses directly
     *
     * @param alloc The amount of memory to allocate to the uninitialized array (clamped between 0 and 1024)
     * @return The memory address as an array
     **/
    public static long[] uMemoryArray(int alloc)
    {
        int adjAlloc = Maths.clamp(alloc, 0, 1024);
        long[] array = new long[adjAlloc];

        for (int i = 0; i < adjAlloc; i++) {
            array[i] = uMemory(adjAlloc, i);
        }

        return array;
    }

    /**
     * Gets uninitialized memory that is present on the current stack with the current index. Potentially can be unsafe due to UB and unwanted code execution before security checks are done. Can be useful as Java does not permit reading unitialized memory addresses directly
     *
     * @param alloc The amount of memory to allocate to the uninitialized array (clamped between 0 and 1024)
     * @param i     The index of the memory array
     * @return The memory address at the specified index
     * @see Sneaky#uMemoryArray(int)
     **/
    public static long uMemory(int alloc, int i)
    {
        return SNKR.uMemory(alloc, i);
    }

    public static Unsafe getTheUnsafe()
    {
        return theUnsafe;
    }

    /**
     * Sets the 64bit memory of the JVM
     *
     * @param pointer The pointer
     * @param value   The value
     * @param bytes   The bytes
     **/
    static void setMemory64(long pointer, int value, int bytes)
    {
        long ptrn = Long.divideUnsigned(-1, 255);
        long rdx = value & 0XFF;
        long fill = rdx * ptrn;

        int byteCount = bytes;

        while (8 <= byteCount) {
            theUnsafe.putLong(null, pointer, fill);
            byteCount -= 8;
            pointer += 8;
        }

        if (byteCount != 0) {
            long mask = SHIFT.right(-1L, byteCount);
            long bytePointer = fill ^ theUnsafe.getLong(null, pointer);
            long offset = fill ^ bytePointer & mask;
            theUnsafe.putLong(null, pointer, offset);
        }
    }

    /**
     * Sets the 32bit memory of the JVM
     *
     * @param pointer The pointer
     * @param value   The value
     * @param bytes   The bytes
     **/
    static void setMemory32(int pointer, int value, int bytes)
    {
        int ptrn = Integer.divideUnsigned(-1, 255);
        int rdx = value & 0xFF;
        int fill = rdx * ptrn;

        int i = 0;

        for (; i <= bytes - 4; i += 4) {
            int offset = pointer + i;
            theUnsafe.putInt(null, offset, fill);
        }

        for (; i < bytes; i++) {
            int offset = pointer + i;
            theUnsafe.putByte(null, offset, (byte) fill);
        }
    }

    /**
     * Checks if the current system is 64bit
     *
     * @return True if the current system is 64bit
     **/
    static boolean is64bit()
    {
        return System.getProperty("sun.arch.data.model").equals("64");
    }

    /**
     * A byte shift interface for shifting memory addresses
     **/
    interface ByteShift
    {
        long left(long value, int bytes);

        long right(long value, int bytes);
    }
}