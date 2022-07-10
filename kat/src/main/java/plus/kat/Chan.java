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
package plus.kat;

import plus.kat.anno.NotNull;
import plus.kat.anno.Nullable;

import plus.kat.spare.*;
import plus.kat.chain.*;
import plus.kat.crash.*;
import plus.kat.entity.*;
import plus.kat.stream.*;

import java.util.*;

import static plus.kat.chain.Space.*;
import static plus.kat.Supplier.Impl.INS;

/**
 * @author kraity
 * @since 0.0.1
 */
public class Chan implements Flag {

    protected Flow flow;
    protected Supplier supplier;

    /**
     * default
     */
    public Chan() {
        this(new Flow(), (Supplier) null);
    }

    /**
     * @param flags the specified {@code flags}
     */
    public Chan(
        long flags
    ) {
        this(flags, null);
    }

    /**
     * @param flow the specified {@code page}
     */
    public Chan(
        @NotNull Flow flow
    ) {
        this(flow, (Supplier) null);
    }

    /**
     * @param supplier the specified {@code supplier}
     */
    public Chan(
        @Nullable Supplier supplier
    ) {
        this(new Flow(), supplier);
    }

    /**
     * @param flags    the specified {@code flags}
     * @param supplier the specified {@code supplier}
     */
    public Chan(
        long flags,
        @Nullable Supplier supplier
    ) {
        this(new Flow(flags), supplier);
    }

    /**
     * @param flow     the specified {@code flow}
     * @param supplier the specified {@code supplier}
     */
    public Chan(
        @NotNull Flow flow,
        @Nullable Supplier supplier
    ) {
        this.flow = flow;
        this.supplier = supplier == null ? INS : supplier;
    }

    /**
     * @param value the specified {@code value}
     */
    public Chan(
        @Nullable Object value
    ) {
        this();
        try {
            set(null, value);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param alias the alias
     * @param value the specified {@code value}
     */
    public Chan(
        @Nullable CharSequence alias,
        @Nullable Object value
    ) {
        this();
        try {
            set(alias, value);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param flags the flags
     * @param value the specified {@code value}
     */
    public Chan(
        @Nullable Object value, long flags
    ) {
        this(flags);
        try {
            set(null, value);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param flags the flags
     * @param alias the alias
     * @param value the specified {@code value}
     */
    public Chan(
        @Nullable CharSequence alias,
        @Nullable Object value, long flags
    ) {
        this(flags);
        try {
            set(alias, value);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param entry the specified {@code entry}
     */
    public Chan(
        @Nullable Entry entry
    ) {
        this();
        try {
            set(null, entry);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param space the space
     * @param entry the specified {@code entry}
     */
    public Chan(
        @Nullable CharSequence space,
        @Nullable Entry entry
    ) {
        this();
        try {
            set(null, space, entry);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param alias the alias
     * @param space the space
     * @param entry the specified {@code entry}
     */
    public Chan(
        @Nullable CharSequence space,
        @Nullable CharSequence alias,
        @Nullable Entry entry
    ) {
        this();
        try {
            set(alias, space, entry);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param coder the specified {@code coder}
     * @param value the specified {@code value}
     */
    public Chan(
        @Nullable Coder<?> coder,
        @Nullable Object value
    ) {
        this();
        try {
            set(null, coder, value);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param flags the flags
     * @param coder the specified {@code coder}
     * @param value the specified {@code value}
     */
    public Chan(
        @Nullable Coder<?> coder,
        @Nullable Object value, long flags
    ) {
        this(flags);
        try {
            set(null, coder, value);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param value    the specified {@code value}
     * @param supplier the specified {@code supplier}
     */
    public Chan(
        @Nullable Supplier supplier,
        @Nullable Object value
    ) {
        this(supplier);
        try {
            set(null, value);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @param flags    the flags
     * @param value    the specified {@code value}
     * @param supplier the specified {@code supplier}
     */
    public Chan(
        @Nullable Supplier supplier,
        @Nullable Object value, long flags
    ) {
        this(flags, supplier);
        try {
            set(null, value);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @author kraity
     * @since 0.0.1
     */
    @FunctionalInterface
    public interface Entry {
        /**
         * @throws IOCrash If an I/O error occurs
         */
        void accept(
            @NotNull Chan chan
        ) throws IOCrash;
    }

    /**
     * Serializes the specified {@code alias} and {@code value} at the current hierarchy
     *
     * @return {@code true} if successful
     * @throws IOCrash If an I/O error occurs
     */
    public boolean set(
        @Nullable CharSequence alias,
        @Nullable Object value
    ) throws IOCrash {
        if (value == null) {
            return coding(alias);
        }

        // get class specified
        Class<?> klass = value.getClass();

        // get spare specified
        Spare<?> spare = supplier.lookup(klass);

        if (spare != null) {
            return coding(
                alias, spare, value
            );
        }

        if (value instanceof Kat) {
            return coding(
                alias, (Kat) value
            );
        }

        if (klass.isEnum()) {
            return coding(
                alias, (Enum<?>) value
            );
        }

        if (!klass.isArray()) {
            return coding(
                alias, value
            );
        }

        // get spare specified
        spare = supplier.lookup(
            Object[].class
        );

        if (spare == null) {
            return false;
        }

        return coding(
            alias, spare, value
        );
    }

    /**
     * Serializes the specified {@code alias} and {@code entry} at the current hierarchy
     *
     * @return {@code true} if successful
     * @throws IOCrash If an I/O error occurs
     */
    public boolean set(
        @Nullable CharSequence alias,
        @Nullable Entry entry
    ) throws IOCrash {
        if (entry != null) {
            flow.addSpace($M);
            flow.addAlias(alias);
            flow.leftBrace();
            entry.accept(this);
            flow.rightBrace();
            return true;
        }
        return coding(alias);
    }

    /**
     * Serializes the specified {@code alias}, {@code space} and {@code entry} at the current hierarchy
     *
     * @return {@code true} if successful
     * @throws IOCrash If an I/O error occurs
     */
    public boolean set(
        @Nullable CharSequence alias,
        @Nullable CharSequence space,
        @Nullable Entry entry
    ) throws IOCrash {
        if (entry != null) {
            flow.addSpace(
                space == null ? $M : space
            );
            flow.addAlias(alias);
            flow.leftBrace();
            entry.accept(this);
            flow.rightBrace();
            return true;
        }
        return coding(alias);
    }

    /**
     * Serializes the specified {@code alias}, {@code coder} and {@code value} at the current hierarchy
     *
     * @return {@code true} if successful
     * @throws IOCrash If an I/O error occurs
     */
    public boolean set(
        @Nullable CharSequence alias,
        @Nullable Coder<?> coder,
        @Nullable Object value
    ) throws IOCrash {
        if (coder == null) {
            return set(
                alias, value
            );
        }

        if (value == null) {
            return coding(alias);
        }

        flow.addSpace(
            coder.getSpace()
        );
        flow.addAlias(alias);
        if (coder.getFlag() != null) {
            flow.leftBrace();
            coder.write(this, value);
            flow.rightBrace();
        } else {
            flow.leftParen();
            coder.write(flow, value);
            flow.rightParen();
        }
        return true;
    }

    /**
     * Writes the specified {@code alias}
     *
     * @return {@code true} if successful
     */
    protected boolean coding(
        @Nullable CharSequence alias
    ) {
        flow.addSpace($);
        flow.addAlias(alias);
        flow.leftParen();
        flow.rightParen();
        return true;
    }

    /**
     * Writes the specified {@code alias} and {@code value}
     *
     * @return {@code true} if successful
     * @throws IOCrash If an I/O error occurs
     */
    protected boolean coding(
        @Nullable CharSequence alias,
        @NotNull Object value
    ) throws IOCrash {
        if (value instanceof Map) {
            return coding(
                alias, MapSpare.INSTANCE, value
            );
        }

        if (value instanceof List) {
            return coding(
                alias, ListSpare.INSTANCE, value
            );
        }

        if (value instanceof Set) {
            return coding(
                alias, SetSpare.INSTANCE, value
            );
        }

        return coding(alias);
    }

    /**
     * Writes the specified {@code alias} and {@code value}
     *
     * @return {@code true} if successful
     * @throws IOCrash If an I/O error occurs
     */
    protected boolean coding(
        @Nullable CharSequence alias,
        @NotNull Kat value
    ) throws IOCrash {
        CharSequence space =
            value.getSpace();
        if (space == null) {
            return coding(alias);
        }

        flow.addSpace(space);
        flow.addAlias(alias);
        if (value.getFlag() != null) {
            flow.leftBrace();
            value.onCoding(this);
            flow.rightBrace();
        } else {
            flow.leftParen();
            value.onCoding(flow);
            flow.rightParen();
        }
        return true;
    }

    /**
     * Writes the specified {@code alias} and {@code value}
     *
     * @return {@code true} if successful
     */
    protected boolean coding(
        @Nullable CharSequence alias,
        @NotNull Enum<?> value
    ) {
        flow.addSpace(
            value.getClass().getSimpleName()
        );
        flow.addAlias(alias);
        flow.leftParen();
        if (flow.isFlag(Flag.ENUM_AS_INDEX)) {
            flow.addInt(
                value.ordinal()
            );
        } else {
            flow.addText(
                value.name()
            );
        }
        flow.rightParen();
        return true;
    }

    /**
     * Writes the specified {@code alias} and {@code value} by specified {@link Spare}
     *
     * @return {@code true} if successful
     * @throws IOCrash If an I/O error occurs
     */
    protected boolean coding(
        @Nullable CharSequence alias,
        @NotNull Spare<?> spare,
        @NotNull Object value
    ) throws IOCrash {
        flow.addSpace(
            spare.getSpace()
        );
        flow.addAlias(alias);
        if (spare.getFlag() != null) {
            flow.leftBrace();
            spare.write(this, value);
            flow.rightBrace();
        } else {
            flow.leftParen();
            spare.write(flow, value);
            flow.rightParen();
        }
        return true;
    }

    /**
     * Check if this {@link Flow} use the {@code flag}
     *
     * @param flag the specified {@code flag}
     */
    @Override
    public boolean isFlag(
        long flag
    ) {
        return flow.isFlag(flag);
    }

    /**
     * Returns the job of {@link Chan}
     */
    @Nullable
    public Job getJob() {
        return Job.KAT;
    }

    /**
     * Returns the internal {@link Paper}
     */
    @Nullable
    public Paper getFlow() {
        return flow;
    }

    /**
     * Returns a serialized string of {@link Flow}
     */
    @Override
    public String toString() {
        return flow.toString();
    }

    /**
     * @author kraity
     * @since 0.0.1
     */
    public static class Flow extends Paper {

        protected int depth;

        /**
         * default
         */
        public Flow() {
            super();
        }

        /**
         * @param size the initial capacity
         */
        public Flow(
            int size
        ) {
            super(size);
        }

        /**
         * @param flags the specified {@code flags}
         */
        public Flow(
            long flags
        ) {
            super(flags);
            if (isFlag(Flag.PRETTY)) ++depth;
        }

        /**
         * @param data the initial byte array
         */
        public Flow(
            @NotNull byte[] data
        ) {
            super(data);
        }

        /**
         * @param bucket the specified {@link Bucket} to be used
         */
        public Flow(
            @Nullable Bucket bucket
        ) {
            super(bucket);
        }

        /**
         * Returns a {@link Flow} of this {@link Flow}
         *
         * @param start the start index, inclusive
         * @param end   the end index, exclusive
         */
        @NotNull
        @Override
        public Flow subSequence(
            int start, int end
        ) {
            return new Flow(
                copyBytes(start, end)
            );
        }

        /**
         * Writes left paren
         */
        public void leftParen() {
            grow(count + 1);
            value[count++] = '(';
        }

        /**
         * Writes right paren
         */
        public void rightParen() {
            grow(count + 1);
            value[count++] = ')';
        }

        /**
         * Writes left Brace
         */
        public void leftBrace() {
            grow(count + 1);
            value[count++] = '{';
            if (depth != 0) ++depth;
        }

        /**
         * Writes right Brace
         */
        public void rightBrace() {
            if (depth == 0) {
                grow(count + 1);
            } else {
                int range = --depth;
                if (range != 1) {
                    grow(count + range * 2);
                    value[count++] = '\n';
                    for (int i = 1; i < range; i++) {
                        value[count++] = ' ';
                        value[count++] = ' ';
                    }
                } else {
                    grow(count + 2);
                    value[count++] = '\n';
                }
            }
            value[count++] = '}';
        }

        @Override
        public void addData(byte b) {
            switch (b) {
                case '^':
                case '(':
                case ')': {
                    grow(count + 2);
                    hash = 0;
                    value[count++] = '^';
                    value[count++] = b;
                    break;
                }
                default: {
                    grow(count + 1);
                    hash = 0;
                    value[count++] = b;
                }
            }
        }

        @Override
        public void addData(char c) {
            switch (c) {
                case '^':
                case '(':
                case ')': {
                    grow(count + 2);
                    hash = 0;
                    value[count++] = '^';
                    value[count++] = (byte) c;
                    break;
                }
                default: {
                    addChar(c);
                }
            }
        }

        /**
         * Writes the specified space
         */
        public void addSpace(
            @NotNull CharSequence c
        ) {
            int range = depth;
            if (range > 1) {
                grow(count + range * 2);
                value[count++] = '\n';
                for (int i = 1; i < range; i++) {
                    value[count++] = ' ';
                    value[count++] = ' ';
                }
            }

            if (c instanceof Space) {
                chain(
                    (Space) c
                );
            } else {
                int i = 0,
                    l = c.length();
                grow(count + l);

                while (i < l) {
                    char d = c.charAt(i++);
                    if (d >= 0x80) {
                        continue;
                    }

                    byte b = (byte) d;
                    if (b <= 0x20) {
                        addSpecial(b);
                    } else {
                        if (Space.esc(b)) {
                            grow(count + 2);
                            value[count++] = '^';
                        } else {
                            grow(count + 1);
                        }
                        hash = 0;
                        value[count++] = b;
                    }
                }
            }
        }

        /**
         * Writes the specified alias
         */
        public void addAlias(
            @Nullable CharSequence c
        ) {
            if (c == null) return;

            int i = 0,
                l = c.length();
            grow(count + l + 1);
            hash = 0;
            value[count++] = ':';

            while (i < l) {
                char d = c.charAt(i++);
                if (d >= 0x80) {
                    continue;
                }

                byte b = (byte) d;
                if (b <= 0x20) {
                    addSpecial(b);
                } else {
                    if (Alias.esc(b)) {
                        grow(count + 2);
                        value[count++] = '^';
                    } else {
                        grow(count + 1);
                    }
                    value[count++] = b;
                }
            }
        }

        /**
         * Writes special byte value
         */
        public void addSpecial(byte b) {
            switch (b) {
                case ' ': {
                    b = 's';
                    break;
                }
                case '\r': {
                    b = 'r';
                    break;
                }
                case '\n': {
                    b = 'n';
                    break;
                }
                case '\t': {
                    b = 't';
                    break;
                }
                default: {
                    return;
                }
            }

            grow(count + 2);
            hash = 0;
            value[count++] = '^';
            value[count++] = b;
        }
    }
}
