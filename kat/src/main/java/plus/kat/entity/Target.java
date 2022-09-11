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
package plus.kat.entity;

import plus.kat.anno.NotNull;
import plus.kat.anno.Nullable;

import plus.kat.spare.*;

import java.lang.reflect.Type;
import java.lang.annotation.Annotation;

/**
 * @author kraity
 * @since 0.0.2
 */
@FunctionalInterface
public interface Target {
    /**
     * Returns the {@link Class} of {@link Target}
     */
    @NotNull
    Class<?> getType();

    /**
     * Returns the {@link Type} of {@link Target}
     */
    @NotNull
    default Type getRawType() {
        return getType();
    }

    /**
     * Returns the index of {@link Target}
     */
    default int getIndex() {
        return 0;
    }

    /**
     * Returns the {@link Coder} of {@link Target}
     */
    @Nullable
    default Coder<?> getCoder() {
        return null;
    }

    /**
     * Returns this element's annotation for the specified type
     * if such an annotation is {@code present}, else {@code null}
     *
     * @param annotationClass the specified annotation type
     * @since 0.0.4
     */
    @Nullable
    default <A extends Annotation> A getAnnotation(
        @NotNull Class<A> annotationClass
    ) {
        return null;
    }
}
