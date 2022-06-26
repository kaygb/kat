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

/**
 * @author kraity
 * @since 0.0.1
 */
public interface Flag {
    /**
     * Internal Flags
     */
    long PRETTY = 0x1;
    long UNICODE = 0x2;
    long ENUM_ORDINAL = 0x4;
    long FLOAT_BITMAP = 0x8;

    /**
     * Check if this {@link Object} use the {@code flag}
     *
     * @param flag the specified {@code flag}
     */
    boolean isFlag(
        long flag
    );
}
