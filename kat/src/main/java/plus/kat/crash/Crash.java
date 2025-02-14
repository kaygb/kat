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
package plus.kat.crash;

import plus.kat.anno.NotNull;
import plus.kat.anno.Nullable;

import plus.kat.*;

/**
 * @author kraity
 * @since 0.0.1
 */
public class Crash extends RuntimeException {
    /**
     * code
     */
    private int code;

    /**
     * @param m the detail message
     */
    public Crash(
        @Nullable String m
    ) {
        super(m);
    }

    /**
     * @param e the specified cause to saved
     */
    public Crash(
        @NotNull Crash e
    ) {
        super(e.getMessage(), e, false, false);
        this.code = e.getCode();
    }

    /**
     * @param e the specified cause to saved
     */
    public Crash(
        @NotNull Throwable e
    ) {
        super(e.getMessage(), e, false, false);
    }

    /**
     * @param m the detail message
     * @param c the crash code
     */
    public Crash(String m, int c) {
        super(m);
        this.code = c;
    }

    /**
     * @param m the detail message
     * @param t enable suppression and writing stack trace
     */
    public Crash(String m, boolean t) {
        super(m, null, t, t);
    }

    /**
     * @param m the detail message
     * @param e the specified cause to saved
     */
    public Crash(String m, Throwable e) {
        super(m, e, false, false);
    }

    /**
     * Returns the code of this
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns a serialized string of {@code Kat}
     */
    @Override
    public String toString() {
        return Kat.encode(this);
    }
}
