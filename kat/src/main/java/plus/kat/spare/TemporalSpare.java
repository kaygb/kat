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

import plus.kat.anno.Format;
import plus.kat.anno.NotNull;
import plus.kat.anno.Nullable;

import plus.kat.*;
import plus.kat.chain.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.format.DateTimeFormatter.*;

/**
 * @author kraity
 * @since 0.0.2
 */
public abstract class TemporalSpare<K extends TemporalAccessor> extends Property<K> {

    public static final Map<String, DateTimeFormatter>
        CACHE = new ConcurrentHashMap<>();

    static {
        CACHE.put(
            "yyyy-MM-dd", ISO_LOCAL_DATE
        );
    }

    protected final DateTimeFormatter formatter;

    protected TemporalSpare(
        @NotNull Class<K> klass,
        @NotNull DateTimeFormatter formatter
    ) {
        super(klass);
        this.formatter = formatter;
    }

    protected TemporalSpare(
        @NotNull Class<K> klass,
        @NotNull Format format
    ) {
        this(klass, format.value(), format.zone(), format.lang());
    }

    protected TemporalSpare(
        @NotNull Class<K> klass,
        @NotNull String pattern,
        @NotNull String zone,
        @NotNull String language
    ) {
        super(klass);
        DateTimeFormatter fmt =
            CACHE.get(pattern);

        if (fmt == null) {
            CACHE.put(
                pattern, fmt = DateTimeFormatter.ofPattern(pattern)
            );
        }

        if (!language.isEmpty()) {
            fmt = fmt.withLocale(
                LocaleSpare.lookup(language)
            );
        }

        if (zone.isEmpty()) {
            formatter = fmt;
        } else {
            formatter = fmt.withZone(
                ZoneId.of(zone)
            );
        }
    }

    @Override
    public K read(
        @NotNull Flag flag,
        @NotNull Value value
    ) throws IOException {
        if (value.isEmpty()) {
            return null;
        }
        return cast(
            value.toString()
        );
    }

    @Override
    public void write(
        @NotNull Flow flow,
        @NotNull Object value
    ) throws IOException {
        StringBuilder builder
            = new StringBuilder(32);
        formatter.formatTo(
            (TemporalAccessor) value, builder
        );
        flow.emit(builder);
    }

    @Nullable
    public K cast(
        @NotNull String value
    ) throws IOException {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public K cast(
        @Nullable Object data,
        @NotNull Supplier supplier
    ) {
        if (data != null) {
            if (klass.isInstance(data)) {
                return (K) data;
            }

            if (data instanceof CharSequence) {
                String d = data.toString();
                if (d.isEmpty()) {
                    return null;
                }
                try {
                    return cast(d);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public Builder<K> getBuilder(
        @Nullable Type type
    ) {
        return null;
    }
}
