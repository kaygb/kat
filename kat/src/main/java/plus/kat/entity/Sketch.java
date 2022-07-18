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

import plus.kat.*;
import plus.kat.chain.*;
import plus.kat.crash.*;
import plus.kat.reflex.*;

import java.lang.reflect.Type;

/**
 * @author kraity
 * @since 0.0.2
 */
public interface Sketch<K> extends Spare<K>, Maker<K> {
    /**
     * @param alias the alias of entity
     * @throws Crash If a failure occurs
     */
    @Nullable
    K apply(
        @NotNull Alias alias
    ) throws Crash;

    /**
     * @param alias the alias of param
     */
    @Nullable
    Param param(
        @NotNull int index,
        @NotNull Alias alias
    );

    /**
     * @param alias the alias of setter
     */
    @Nullable
    Setter<K, ?> setter(
        @NotNull int index,
        @NotNull Alias alias
    );

    /**
     * Returns a {@link Builder} of {@link K}
     */
    @Nullable
    @Override
    default Builder<K> getBuilder(
        @Nullable Type type
    ) {
        return new Builder0<>(this);
    }

    /**
     * @author kraity
     * @since 0.0.2
     */
    class Builder0<K> extends Builder<K> {

        protected K entity;
        protected int index = -1;

        protected Sketch<K> sketch;
        protected Setter<K, ?> setter;

        /**
         * default
         */
        public Builder0(
            @NotNull Sketch<K> sketch
        ) {
            this.sketch = sketch;
        }

        @Override
        public void onCreate(
            @NotNull Alias alias
        ) throws Crash, IOCrash {
            // get an instance
            entity = sketch.apply(alias);

            // check this instance
            if (entity == null) {
                throw new Crash(
                    "Entity created through Sketch is null", false
                );
            }
        }

        public void onAccept(
            @Nullable Object value
        ) throws IOCrash {
            setter.onAccept(
                entity, value
            );
        }

        @Override
        public void onAccept(
            @NotNull Alias alias,
            @NotNull Builder<?> child
        ) throws IOCrash {
            setter.onAccept(
                entity, child.getResult()
            );
        }

        @Override
        public void onAccept(
            @NotNull Space space,
            @NotNull Alias alias,
            @NotNull Value value
        ) throws IOCrash {
            setter = sketch.setter(
                ++index, alias
            );

            if (setter == null) {
                return;
            }

            // specified coder
            Coder<?> coder = setter.getCoder();

            if (coder != null) {
                onAccept(
                    coder.read(
                        flag, value
                    )
                );
                return;
            }

            Spare<?> spare;
            Class<?> klass = setter.getKlass();

            // lookup
            if (klass == null) {
                // specified spare
                spare = supplier.lookup(space);

                if (spare != null) {
                    onAccept(
                        spare.read(
                            flag, value
                        )
                    );
                }
            } else {
                // specified spare
                spare = supplier.lookup(klass);

                // skip if null
                if (spare != null) {
                    onAccept(
                        spare.read(
                            flag, value
                        )
                    );
                    return;
                }

                // specified spare
                spare = supplier.lookup(space);

                // skip if null
                if (spare != null &&
                    spare.accept(klass)) {
                    onAccept(
                        spare.read(
                            flag, value
                        )
                    );
                }
            }
        }

        @Nullable
        public Builder<?> getBuilder(
            @NotNull Space space,
            @NotNull Alias alias
        ) throws IOCrash {
            setter = sketch.setter(
                ++index, alias
            );

            if (setter == null) {
                return null;
            }

            // specified coder
            Coder<?> coder = setter.getCoder();

            if (coder != null) {
                return coder.getBuilder(
                    setter.getType()
                );
            }

            Spare<?> spare;
            Class<?> klass = setter.getKlass();

            // lookup
            if (klass == null) {
                // specified spare
                spare = supplier.lookup(space);

                // skip if null
                if (spare != null) {
                    return spare.getBuilder(
                        setter.getType()
                    );
                }
            } else {
                // specified spare
                spare = supplier.lookup(klass);

                // skip if null
                if (spare != null) {
                    return spare.getBuilder(
                        setter.getType()
                    );
                }

                // specified spare
                spare = supplier.lookup(space);

                // skip if null
                if (spare != null &&
                    spare.accept(klass)) {
                    return spare.getBuilder(
                        setter.getType()
                    );
                }
            }

            return null;
        }

        @Nullable
        @Override
        public K getResult() {
            return entity;
        }

        @Override
        public void onDestroy() {
            entity = null;
            index = -1;
            setter = null;
            sketch = null;
        }
    }

    /**
     * @author kraity
     * @since 0.0.2
     */
    class Builder1<K> extends Builder0<K> {

        protected int count;
        protected Cache<K> cache;

        protected Param param;
        protected Object[] params;

        public Builder1(
            @NotNull Sketch<K> sketch,
            @NotNull Object[] params
        ) {
            super(sketch);
            this.params = params;
        }

        @Override
        public void onCreate(
            @NotNull Alias alias
        ) {
            // Nothing
        }

        @Override
        public void onAccept(
            @Nullable Object value
        ) throws IOCrash {
            if (entity != null) {
                setter.onAccept(
                    entity, value
                );
            } else {
                Cache<K> c = new Cache<>();
                c.value = value;
                c.setter = setter;

                if (cache == null) {
                    cache = c;
                } else {
                    cache.next = c;
                }
            }
        }

        @Override
        public void onAccept(
            @NotNull Space space,
            @NotNull Alias alias,
            @NotNull Value value
        ) throws IOCrash {
            if (entity == null) {
                Param param = sketch.param(
                    index, alias
                );
                if (param != null) {
                    ++index; // increment

                    // specified coder
                    Coder<?> coder = param.getCoder();

                    if (coder != null) {
                        params[param.getIndex()] =
                            coder.read(
                                flag, value
                            );
                    } else {
                        // specified spare
                        coder = supplier.lookup(
                            param.getKlass()
                        );

                        // skip if null
                        if (coder != null) {
                            params[param.getIndex()] =
                                coder.read(
                                    flag, value
                                );
                        }
                    }

                    embark();
                    return;
                }
            }

            super.onAccept(
                space, alias, value
            );
        }

        @Override
        public void onAccept(
            @NotNull Alias alias,
            @NotNull Builder<?> child
        ) throws IOCrash {
            if (entity != null) {
                setter.onAccept(
                    entity, child.getResult()
                );
            } else if (param == null) {
                onAccept(
                    child.getResult()
                );
            } else {
                params[param.getIndex()] = child.getResult();
                param = null;
                embark();
            }
        }

        @Override
        public Builder<?> getBuilder(
            @NotNull Space space,
            @NotNull Alias alias
        ) throws IOCrash {
            if (entity == null) {
                param = sketch.param(
                    index, alias
                );
                if (param != null) {
                    ++index; // increment

                    // specified coder
                    Coder<?> coder = param.getCoder();

                    if (coder == null) {
                        // specified spare
                        coder = supplier.lookup(
                            param.getKlass()
                        );

                        if (coder == null) {
                            return null;
                        }
                    }

                    return coder.getBuilder(
                        param.getType()
                    );
                }
            }

            return super.getBuilder(
                space, alias
            );
        }

        static class Cache<K> {
            Object value;
            Cache<K> next;
            Setter<K, ?> setter;
        }

        private void embark()
            throws IOCrash {
            if (++count == params.length) {
                try {
                    entity = sketch.apply(
                        getAlias(), params
                    );
                    while (cache != null) {
                        cache.setter.onAccept(
                            entity, cache.value
                        );
                        cache = cache.next;
                    }
                } catch (Exception e) {
                    throw new IOCrash(e);
                }
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            param = null;
            cache = null;
            params = null;
        }
    }
}
