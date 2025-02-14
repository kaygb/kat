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
package plus.kat.spare;

import plus.kat.anno.NotNull;
import plus.kat.anno.Nullable;

import plus.kat.*;
import plus.kat.chain.*;
import plus.kat.stream.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.BitSet;

/**
 * @author kraity
 * @since 0.0.2
 */
public class BitSetSpare extends Property<BitSet> {

    public static final BitSetSpare
        INSTANCE = new BitSetSpare();

    public BitSetSpare() {
        super(BitSet.class);
    }

    @Override
    public String getSpace() {
        return "BitSet";
    }

    @Override
    public boolean accept(
        @NotNull Class<?> clazz
    ) {
        return clazz == BitSet.class
            || clazz == Object.class;
    }

    @Override
    public Boolean getFlag() {
        return Boolean.FALSE;
    }

    @Override
    public BitSet read(
        @NotNull Flag flag,
        @NotNull Value value
    ) {
        if (flag.isFlag(Flag.STRING_AS_OBJECT)) {
            return Convert.toObject(
                this, flag, value
            );
        }
        return null;
    }

    @Override
    public void write(
        @NotNull Chan chan,
        @NotNull Object value
    ) throws IOException {
        BitSet set = (BitSet) value;
        int len = set.length();
        for (int i = 0; i < len; i++) {
            chan.set(
                null, set.get(i) ? 1 : 0
            );
        }
    }

    @Override
    public BitSet cast(
        @Nullable Object data,
        @NotNull Supplier supplier
    ) {
        if (data != null) {
            if (data instanceof BitSet) {
                return (BitSet) data;
            }

            if (data instanceof CharSequence) {
                return Convert.toObject(
                    this, (CharSequence) data, null, supplier
                );
            }
        }
        return null;
    }

    @Override
    public Builder<BitSet> getBuilder(
        @Nullable Type type
    ) {
        return new Builder0();
    }

    public static class Builder0 extends Builder<BitSet> {

        protected int index;
        protected BitSet bundle;

        @Override
        public void onCreate() {
            bundle = new BitSet();
        }

        @Override
        public void onAttain(
            @NotNull Space space,
            @NotNull Alias alias,
            @NotNull Value value
        ) {
            int i = index++;
            if (value.toBoolean()) {
                bundle.set(i);
            }
        }

        @Override
        public BitSet onPacket() {
            return bundle;
        }

        @Override
        public void onDestroy() {
            bundle = null;
        }
    }
}
