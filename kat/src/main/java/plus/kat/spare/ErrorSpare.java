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
import plus.kat.crash.*;
import plus.kat.stream.*;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author kraity
 * @since 0.0.4
 */
@SuppressWarnings("unchecked")
public class ErrorSpare extends Property<Exception> {

    public static final ErrorSpare
        INSTANCE = new ErrorSpare(Crash.class);

    public ErrorSpare(
        @NotNull Class<?> klass
    ) {
        super((Class<Exception>) klass);
    }

    @Override
    public String getSpace() {
        return "E";
    }

    @Override
    public Boolean getFlag() {
        return Boolean.TRUE;
    }

    @Override
    public void write(
        @NotNull Chan chan,
        @NotNull Object value
    ) throws IOException {
        if (value instanceof Crash) {
            Crash e = (Crash) value;
            chan.set("c", e.getCode());
            chan.set("m", e.getMessage());
        } else {
            Exception e = (Exception) value;
            chan.set("message", e.getMessage());
        }
    }

    @Override
    public Exception cast(
        @Nullable Object data,
        @NotNull Supplier supplier
    ) {
        if (data != null) {
            if (klass.isInstance(data)) {
                return (Exception) data;
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
    public Builder<Crash> getBuilder(
        @Nullable Type type
    ) {
        return new Builder0();
    }

    public static class Builder0 extends Builder<Crash> {

        private Crash crash;
        private int code;
        private String message;

        @Override
        public void onCreate() {
            // Nothing
        }

        @Override
        public void onAttain(
            @NotNull Space space,
            @NotNull Alias alias,
            @NotNull Value value
        ) {
            switch (alias.get(0)) {
                case 'c': {
                    code = value.toInt();
                    break;
                }
                case 'm': {
                    message = value.toString();
                }
            }
        }

        @Nullable
        @Override
        public Crash onPacket() {
            if (crash != null) {
                return crash;
            }
            return crash = new Crash(
                message, code
            );
        }

        @Override
        public void onDestroy() {
            crash = null;
        }
    }
}
