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

import java.io.IOException;
import java.util.Currency;

import static java.util.Currency.getInstance;

/**
 * @author kraity
 * @since 0.0.2
 */
public class CurrencySpare extends Property<Currency> {

    public static final CurrencySpare
        INSTANCE = new CurrencySpare();

    public CurrencySpare() {
        super(Currency.class);
    }

    @Override
    public String getSpace() {
        return "Currency";
    }

    @Override
    public boolean accept(
        @NotNull Class<?> clazz
    ) {
        return clazz == Currency.class
            || clazz == Object.class;
    }

    @Override
    public Currency read(
        @NotNull Flag flag,
        @NotNull Alias alias
    ) {
        if (alias.length() != 3) {
            return null;
        }

        return getInstance(
            alias.toString()
        );
    }

    @Override
    public Currency read(
        @NotNull Flag flag,
        @NotNull Value value
    ) {
        if (value.length() != 3) {
            return null;
        }

        return getInstance(
            value.toString()
        );
    }

    @Override
    public void write(
        @NotNull Flow flow,
        @NotNull Object value
    ) throws IOException {
        flow.emit(
            value.toString()
        );
    }

    @Override
    public Currency cast(
        @Nullable Object data,
        @NotNull Supplier supplier
    ) {
        if (data != null) {
            if (data instanceof Currency) {
                return (Currency) data;
            }

            if (data instanceof CharSequence) {
                CharSequence d = (CharSequence) data;
                if (d.length() != 3) {
                    return null;
                }
                try {
                    return getInstance(
                        data.toString()
                    );
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
}
