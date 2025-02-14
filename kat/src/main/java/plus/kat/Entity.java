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

import java.io.IOException;

/**
 * @author kraity
 * @since 0.0.5
 */
@FunctionalInterface
public interface Entity {
    /**
     * Serializes this at the current hierarchy
     *
     * @throws IOException If an I/O error occurs
     * @see Chan#set(String, Entity)
     * @see Chan#set(String, String, Entity)
     */
    void serial(
        @NotNull Chan chan
    ) throws IOException;
}
