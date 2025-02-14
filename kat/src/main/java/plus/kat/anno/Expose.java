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
package plus.kat.anno;

import plus.kat.Flag;
import plus.kat.spare.Coder;

import java.lang.annotation.*;

/**
 * @author kraity
 * @since 0.0.1
 */
@Documented
@Target({
    ElementType.TYPE,
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Expose {
    /**
     * Returns the alias of this
     *
     * @since 0.0.1
     */
    String[] value() default {};

    /**
     * Returns the index of this
     *
     * @since 0.0.1
     */
    int index() default -1;

    /**
     * Returns the flags of this
     *
     * @see Flag
     * @since 0.0.4
     */
    int require() default 0;

    /**
     * Returns the specified {@link Coder} or the pointing {@link Class}
     *
     * @since 0.0.1
     */
    Class<?> with() default Coder.class;
}
