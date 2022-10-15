/*
 * Copyright 2022 Kat+ Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package plus.kat.chain;

import plus.kat.anno.NotNull;
import plus.kat.anno.Nullable;

import plus.kat.crash.*;
import plus.kat.kernel.*;
import plus.kat.stream.*;
import plus.kat.utils.*;

import java.io.IOException;
import java.io.InputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author kraity
 * @since 0.0.1
 */
public class Value extends Alpha {
    /**
     * Constructs a mutable value
     */
    public Value() {
        super();
    }

    /**
     * Constructs a mutable value
     *
     * @param size the initial capacity
     */
    public Value(
        int size
    ) {
        super(size);
    }

    /**
     * Constructs a mutable value
     *
     * @param data the initial byte array
     */
    public Value(
        @NotNull byte[] data
    ) {
        super(data);
    }

    /**
     * Constructs a mutable value
     *
     * @param data the specified chain to be used
     */
    public Value(
        @NotNull Chain data
    ) {
        super(data);
    }

    /**
     * Constructs a mutable value
     *
     * @param bucket the specified bucket to be used
     */
    public Value(
        @Nullable Bucket bucket
    ) {
        super(bucket);
    }

    /**
     * Constructs a mutable value
     *
     * @param chain  the specified chain to be used
     * @param bucket the specified bucket to be used
     */
    public Value(
        @NotNull Chain chain,
        @Nullable Bucket bucket
    ) {
        super(
            chain, bucket
        );
    }

    /**
     * Constructs a mutable value
     *
     * @param sequence the specified sequence to be used
     */
    public Value(
        @Nullable CharSequence sequence
    ) {
        super(sequence);
    }

    /**
     * Returns a {@link Value} that
     * is a subsequence of this {@link Value}
     *
     * @param start the start index, inclusive
     * @param end   the end index, exclusive
     */
    @Override
    public Value subSequence(
        int start, int end
    ) {
        return new Value(
            toBytes(start, end)
        );
    }

    /**
     * Concatenates the string representation
     * of the integer value to this {@link Value}
     *
     * <pre>{@code
     *   Value value = ...
     *   value.add(1024); // 1024
     * }</pre>
     *
     * @param num the specified int value
     * @since 0.0.5
     */
    public void emit(
        int num
    ) {
        concat(num);
    }

    /**
     * Concatenates the string representation
     * of the long value to this {@link Value}
     *
     * <pre>{@code
     *   Value value = ...
     *   value.add(1024L); // 1024
     * }</pre>
     *
     * @param num the specified long value
     * @since 0.0.5
     */
    public void emit(
        long num
    ) {
        concat(num);
    }

    /**
     * Concatenates the string representation
     * of the float value to this {@link Value}
     *
     * <pre>{@code
     *   Value value = ...
     *   value.add(10.24F); // 10.24
     * }</pre>
     *
     * @param num the specified float value
     * @since 0.0.5
     */
    public void emit(
        float num
    ) {
        concat(num);
    }

    /**
     * Concatenates the string representation
     * of the double value to this {@link Value}
     *
     * <pre>{@code
     *   Value value = ...
     *   value.add(10.24D); // 10.24
     * }</pre>
     *
     * @param num the specified double value
     * @since 0.0.5
     */
    public void emit(
        double num
    ) {
        concat(num);
    }

    /**
     * Concatenates the string representation
     * of the boolean value to this {@link Value}
     *
     * <pre>{@code
     *   Value value = ...
     *   value.add(true); // true
     * }</pre>
     *
     * @param bool the specified boolean value
     * @since 0.0.5
     */
    public void emit(
        boolean bool
    ) {
        concat(bool);
    }

    /**
     * Concatenates the char value to this {@link Value}
     *
     * <pre>{@code
     *   Value value = ...
     *   value.add('k');
     * }</pre>
     *
     * @param c the specified char value
     * @since 0.0.5
     */
    public void emit(
        char c
    ) {
        concat(c);
    }

    /**
     * Concatenates the byte array to this {@link Value}
     *
     * @param b the specified byte array
     * @since 0.0.5
     */
    public void emit(
        byte[] b
    ) {
        if (b != null) {
            concat(
                b, 0, b.length
            );
        }
    }

    /**
     * Concatenates the byte array to this {@link Value}
     *
     * @param b the specified byte array
     * @param i the specified index
     * @param l the specified length
     * @throws ArrayIndexOutOfBoundsException If the index or length out of range
     * @since 0.0.5
     */
    public void emit(
        byte[] b, int i, int l
    ) {
        if (b != null && l != 0) {
            if (i >= 0 && i + l <= b.length) {
                concat(
                    b, i, l
                );
            } else {
                throw new ArrayIndexOutOfBoundsException(
                    "Out of bounds, i:" + i + " l:" + l + " length:" + b.length
                );
            }
        }
    }

    /**
     * Concatenates the char array to this {@link Value}
     *
     * @param c the specified char array
     * @since 0.0.5
     */
    public void emit(
        char[] c
    ) {
        if (c != null) {
            int len = c.length;
            if (len != 0) {
                concat(
                    c, 0, len
                );
            }
        }
    }

    /**
     * Concatenates the char array to this {@link Value}
     *
     * @param c the specified byte array
     * @param i the specified index
     * @param l the specified length
     * @throws ArrayIndexOutOfBoundsException If the index or length out of range
     * @since 0.0.5
     */
    public void emit(
        char[] c, int i, int l
    ) {
        if (c != null && l != 0) {
            if (i >= 0 && i + l <= c.length) {
                concat(
                    c, i, l
                );
            } else {
                throw new ArrayIndexOutOfBoundsException(
                    "Out of bounds, i:" + i + " l:" + l + " length:" + c.length
                );
            }
        }
    }

    /**
     * Concatenates the {@link InputStream} to this {@link Value}
     *
     * <pre>{@code
     *  Value value = ...
     *  InputStream in = ...
     *  value.add(in); // auto close
     * }</pre>
     *
     * @param in the specified {@link InputStream} will be used and closed
     * @since 0.0.5
     */
    public void emit(
        InputStream in
    ) {
        if (in != null) {
            concat(in);
        }
    }

    /**
     * Concatenates the {@link InputStream} to this {@link Value}
     *
     * <pre>{@code
     *  Value value = ...
     *  InputStream in = ...
     *  value.add(in, 512);
     *  in.close(); // close it
     *
     *  // or
     *  try (InputStream in = ...) {
     *      value.add(in, 512);
     *  }
     * }</pre>
     *
     * @param range the specified range
     * @param in    the specified {@link InputStream} will be used but will not be closed
     * @throws IOException If an I/O error occurs
     * @since 0.0.5
     */
    public void emit(
        InputStream in, int range
    ) throws IOException {
        if (in != null) {
            if (range > 0) {
                concat(
                    in, range
                );
            } else {
                throw new UnexpectedCrash(
                    "Unexpectedly, the range is not a positive number"
                );
            }
        }
    }

    /**
     * Concatenates the {@link CharSequence} to this {@link Value}
     *
     * @param c the specified char array
     * @since 0.0.5
     */
    public void emit(
        CharSequence c
    ) {
        if (c != null) {
            int len = c.length();
            if (len != 0) {
                concat(
                    c, 0, len
                );
            }
        }
    }

    /**
     * Concatenates the {@link CharSequence} to this {@link Value}
     *
     * @param c the specified byte array
     * @param i the specified index
     * @param l the specified length
     * @throws ArrayIndexOutOfBoundsException If the index or length out of range
     * @since 0.0.5
     */
    public void emit(
        CharSequence c, int i, int l
    ) {
        if (c != null && l != 0) {
            if (i >= 0 && i + l <= c.length()) {
                concat(
                    c, i, l
                );
            } else {
                throw new ArrayIndexOutOfBoundsException(
                    "Out of bounds, i:" + i + " l:" + l + " length:" + c.length()
                );
            }
        }
    }

    /**
     * Concatenates the uppercase hexadecimal of the data to this {@link Value}
     *
     * <pre>{@code
     *   Value value = ...
     *   value.upper(new byte[]{1, 11, 111}); // 010B6F
     * }</pre>
     *
     * @param data the specified data to be encoded
     * @see Value#lower(byte[])
     * @since 0.0.4
     */
    public void upper(
        byte[] data
    ) {
        if (data != null) {
            int size = data.length;
            if (size != 0) {
                grow(count * size * 2);
                asset = 0;
                int i = 0;
                byte[] it = value;
                while (i < size) {
                    int o = data[i++] & 0xFF;
                    it[count++] = Binary.upper(o >> 4);
                    it[count++] = Binary.upper(o & 0xF);
                }
            }
        }
    }

    /**
     * Concatenates the lowercase hexadecimal of the data to this {@link Value}
     *
     * <pre>{@code
     *   Value value = ...
     *   value.lower(new byte[]{1, 11, 111}); // 010b6f
     * }</pre>
     *
     * @param data the specified data to be encoded
     * @see Value#upper(byte[])
     * @since 0.0.4
     */
    public void lower(
        byte[] data
    ) {
        if (data != null) {
            int size = data.length;
            if (size != 0) {
                grow(count * size * 2);
                asset = 0;
                int i = 0;
                byte[] it = value;
                while (i < size) {
                    int o = data[i++] & 0xFF;
                    it[count++] = Binary.lower(o >> 4);
                    it[count++] = Binary.lower(o & 0xF);
                }
            }
        }
    }

    /**
     * Returns this {@link Chain} as a {@link SecretKeySpec}
     *
     * @throws IllegalArgumentException If the algo is null
     * @since 0.0.5
     */
    @NotNull
    public SecretKeySpec asSecretKeySpec(
        @NotNull String algo
    ) {
        return new SecretKeySpec(
            value, 0, count, algo
        );
    }

    /**
     * Returns this {@link Chain} as a {@link SecretKeySpec}
     *
     * @throws IllegalArgumentException       If the algo is null or the offset out of range
     * @throws ArrayIndexOutOfBoundsException If the length is negative
     * @since 0.0.5
     */
    @NotNull
    public SecretKeySpec asSecretKeySpec(
        @NotNull String algo, int offset, int length
    ) {
        return new SecretKeySpec(
            value, offset, length, algo
        );
    }

    /**
     * Returns this {@link Chain} as a {@link IvParameterSpec}
     *
     * @since 0.0.5
     */
    @NotNull
    public IvParameterSpec asIvParameterSpec() {
        return new IvParameterSpec(
            value, 0, count
        );
    }

    /**
     * Returns this {@link Chain} as a {@link IvParameterSpec}
     *
     * @throws IllegalArgumentException       If the offset out of range
     * @throws ArrayIndexOutOfBoundsException If the length is negative
     * @since 0.0.5
     */
    @NotNull
    public IvParameterSpec asIvParameterSpec(
        int offset, int length
    ) {
        return new IvParameterSpec(
            value, offset, length
        );
    }

    /**
     * Returns a mutable value of clone this {@link Value}
     */
    @NotNull
    public Value copy() {
        return new Value(this);
    }

    /**
     * @see Value#Value(Bucket)
     */
    public static Value apply() {
        return new Value(
            Buffer.INS
        );
    }

    /**
     * @param b the {@code byte} to be compared
     */
    public static boolean esc(byte b) {
        switch (b) {
            case '^':
            case '(':
            case ')': {
                return true;
            }
        }
        return false;
    }

    /**
     * @author kraity
     * @since 0.0.4
     */
    public static class Buffer implements Bucket {

        private static final int SIZE, LIMIT, SCALE;

        static {
            SIZE = Config.get(
                "kat.value.size", 8
            );
            LIMIT = Config.get(
                "kat.value.limit", 16
            );

            if (LIMIT < SIZE) {
                throw new Error(
                    "Bucket's size(" + SIZE + ") cannot be greater than the limit(" + LIMIT + ")"
                );
            }

            SCALE = Config.get(
                "kat.value.scale", 1024
            );
        }

        public static final Buffer
            INS = new Buffer();

        private final byte[][]
            bucket = new byte[SIZE][];

        @Override
        public boolean share(
            @NotNull byte[] it
        ) {
            int i = it.length / SCALE;
            if (i < SIZE) {
                synchronized (this) {
                    bucket[i] = it;
                }
                return true;
            }
            return false;
        }

        @Override
        public byte[] swop(
            @NotNull byte[] it
        ) {
            int i = it.length / SCALE;
            if (i == 0) {
                return it;
            }

            byte[] data;
            synchronized (this) {
                if (i < SIZE) {
                    bucket[i] = it;
                }
                data = bucket[i];
                bucket[i] = null;
            }
            return data == null ? EMPTY_BYTES : data;
        }

        @Override
        public byte[] apply(
            @NotNull byte[] it, int len, int size
        ) {
            byte[] data;
            int i = size / SCALE;

            if (i < SIZE) {
                synchronized (this) {
                    data = bucket[i];
                    bucket[i] = null;
                }
                if (data == null ||
                    data.length < size) {
                    data = new byte[(i + 1) * SCALE - 1];
                }
            } else {
                if (i < LIMIT) {
                    data = new byte[(i + 1) * SCALE - 1];
                } else {
                    throw new FatalCrash(
                        "Unexpectedly, Exceeding range '" + LIMIT * SCALE + "' in value"
                    );
                }
            }

            if (it.length != 0) {
                System.arraycopy(
                    it, 0, data, 0, len
                );

                int k = it.length / SCALE;
                if (k < SIZE) {
                    synchronized (this) {
                        bucket[k] = it;
                    }
                }
            }

            return data;
        }
    }
}
