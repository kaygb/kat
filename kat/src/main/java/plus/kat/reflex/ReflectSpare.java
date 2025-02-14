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
package plus.kat.reflex;

import plus.kat.anno.NotNull;
import plus.kat.anno.Nullable;

import java.lang.annotation.Annotation;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import plus.kat.*;
import plus.kat.anno.*;
import plus.kat.crash.*;
import plus.kat.spare.*;
import plus.kat.utils.*;

/**
 * @author kraity
 * @since 0.0.2
 */
@SuppressWarnings("unchecked")
public class ReflectSpare<T> extends AbstractSpare<T> {

    private MethodHandle handle;
    private Constructor<T> builder;

    private Class<?> cxt;
    private Class<?>[] args;

    private boolean marker;
    private boolean variable;

    public ReflectSpare(
        @Nullable Embed embed,
        @NotNull Class<T> klass,
        @NotNull Supplier supplier
    ) {
        super(embed, klass, supplier);
        Class<?> clazz = klass;
        onConstructors(
            clazz.getDeclaredConstructors()
        );
        int grade = 0;
        do {
            onFields(
                grade, clazz.getDeclaredFields()
            );
            onMethods(
                grade++, clazz.getDeclaredMethods()
            );
        } while (
            (clazz = clazz.getSuperclass()) != Object.class
        );
    }

    @NotNull
    public T apply() {
        MethodHandle m = handle;
        if (m == null) {
            throw new Collapse(
                "Not supported"
            );
        }
        try {
            return (T) m.invoke();
        } catch (Throwable e) {
            throw new Collapse(
                "Failed to create", e
            );
        }
    }

    @NotNull
    public T apply(
        @NotNull Object[] data
    ) {
        Constructor<T> b = builder;
        if (b == null) {
            throw new Collapse(
                "Not supported"
            );
        }

        int i = 0, flag = 0;
        Class<?>[] as = args;

        int count = as.length;
        if (marker) count -= 2;

        for (; i < count; i++) {
            if (data[i] == null) {
                flag |= (1 << i);
                Class<?> c = as[i];
                if (c.isPrimitive()) {
                    data[i] = Find.value(c);
                }
            }
        }

        if (marker) {
            data[i] = flag;
        }

        try {
            return b.newInstance(data);
        } catch (Throwable e) {
            throw new Collapse(
                "Failed to create", e
            );
        }
    }

    @Override
    public T apply(
        @NotNull Spoiler spoiler,
        @NotNull Supplier supplier
    ) throws Collapse {
        try {
            Class<?>[] as = args;
            if (as == null) {
                T bean = apply();
                update(
                    bean, spoiler, supplier
                );
                return bean;
            }

            if (cxt == null) {
                Object[] group = new Object[as.length];
                update(
                    group, spoiler, supplier
                );
                return apply(group);
            }
        } catch (Collapse e) {
            throw e;
        } catch (Throwable e) {
            throw new Collapse(
                "Error creating " + getType(), e
            );
        }

        throw new Collapse(
            "Not currently supported"
        );
    }

    @Override
    public T apply(
        @NotNull Supplier supplier,
        @NotNull ResultSet resultSet
    ) throws SQLException {
        try {
            Class<?>[] as = args;
            if (as == null) {
                T bean = apply();
                update(
                    bean, supplier, resultSet
                );
                return bean;
            }

            if (cxt == null) {
                Object[] group = new Object[as.length];
                update(
                    group, supplier, resultSet
                );
                return apply(group);
            }
        } catch (SQLException e) {
            throw e;
        } catch (Throwable e) {
            throw new SQLCrash(
                "Error creating " + getType(), e
            );
        }

        throw new SQLCrash(
            "Not currently supported"
        );
    }

    @Override
    public Builder<T> getBuilder(
        @Nullable Type type
    ) {
        Class<?>[] as = args;
        if (as == null) {
            return new Builder0<>(this);
        }

        Class<?> cls = cxt;
        int size = as.length;

        if (cls == null && !variable) {
            return new Builder1<>(
                new Object[size], this
            );
        }

        return new Builder2<>(
            cls, new Object[size], this
        );
    }

    protected void onFields(
        @NotNull int grade,
        @NotNull Field[] fields
    ) {
        boolean sealed = (flags & Flag.Sealed) != 0;
        boolean nimble = (flags & Flag.Nimble) != 0;

        for (Field field : fields)
            try {
                // its modifier
                int mod = field.getModifiers();

                // filter invalid
                if ((mod & Modifier.STATIC) != 0) {
                    continue;
                }

                Accessor<T> member;
                Expose expose = field
                    .getAnnotation(
                        Expose.class
                    );

                if (expose == null) {
                    // check flag
                    if (sealed) {
                        continue;
                    }

                    // filter invalid
                    if ((mod & Modifier.PUBLIC) == 0 ||
                        (mod & Modifier.TRANSIENT) != 0) {
                        continue;
                    }
                }

                String name;
                String[] keys;

                if (expose == null) {
                    name = field.getName();
                    if (set(name) == null) {
                        variable = true;
                        member = new Accessor<>(
                            null, field, this
                        );
                        setProperty(
                            grade, name, member
                        );
                    }
                    continue;
                }

                int flag = expose.require();
                boolean solveOnly = (flag & Flag.Internal) != 0;
                boolean serialOnly = (flag & Flag.Readonly) != 0;
                if (solveOnly && serialOnly) {
                    continue;
                }

                keys = expose.value();
                if (keys.length != 0) {
                    name = keys[0];
                } else {
                    name = field.getName();
                }

                if (serialOnly) {
                    if (get(name) != null) {
                        continue;
                    }

                    member = new Accessor<>(
                        expose, field, null, this
                    );

                    if (keys.length == 0) {
                        setWriter(
                            grade, name, member
                        );
                    } else {
                        for (String alias : keys) {
                            if (!alias.isEmpty()) {
                                setWriter(
                                    grade, alias, member
                                );
                            }
                        }
                    }
                } else {
                    if (set(name) != null) {
                        continue;
                    }

                    variable = true;
                    member = new Accessor<>(
                        expose, field, solveOnly, this
                    );

                    if (keys.length == 0) {
                        if (solveOnly) {
                            setReader(
                                name, member
                            );
                        } else {
                            setProperty(
                                grade, name, member
                            );
                        }
                    } else {
                        for (String alias : keys) {
                            if (!alias.isEmpty()) {
                                if (solveOnly) {
                                    setReader(
                                        alias, member
                                    );
                                } else {
                                    setProperty(
                                        grade, alias, member
                                    );
                                }
                            }
                        }
                    }

                    if (nimble) {
                        if (member.index >= 0) {
                            setReader(
                                member.index, member
                            );
                        }
                    }
                }
            } catch (Exception e) {
                // Nothing
            }
    }

    protected void onMethods(
        @NotNull int grade,
        @NotNull Method[] methods
    ) {
        boolean sealed = (flags & Flag.Sealed) != 0;
        boolean nimble = (flags & Flag.Nimble) != 0;

        for (Method method : methods)
            try {
                int count = method.
                    getParameterCount();
                if (count > 1) {
                    continue;
                }

                // its modifier
                int mod = method.getModifiers();

                // filter invalid
                if ((mod & Modifier.STATIC) != 0 ||
                    (mod & Modifier.ABSTRACT) != 0) {
                    continue;
                }

                Accessor<T> member;
                Expose expose = method
                    .getAnnotation(
                        Expose.class
                    );

                if (expose == null) {
                    // check flag
                    if (sealed) {
                        continue;
                    }

                    // filter invalid
                    if ((mod & Modifier.PUBLIC) == 0) {
                        continue;
                    }
                } else {
                    int flag = expose.require();
                    String[] keys = expose.value();

                    if (count == 0) {
                        if ((flag & Flag.Internal) != 0) {
                            continue;
                        }
                        if (keys.length != 0) {
                            if (get(keys[0]) != null) {
                                continue;
                            }

                            member = new Accessor<>(
                                expose, method, this
                            );

                            for (String key : keys) {
                                setWriter(
                                    grade, key, member
                                );
                            }
                            continue;
                        }
                    } else {
                        if ((flag & Flag.Readonly) != 0) {
                            continue;
                        }
                        if (keys.length != 0) {
                            if (set(keys[0]) != null) {
                                continue;
                            }

                            variable = true;
                            member = new Accessor<>(
                                expose, method, this
                            );

                            for (String alias : keys) {
                                if (!alias.isEmpty()) {
                                    setReader(
                                        alias, member
                                    );
                                }
                            }

                            if (nimble) {
                                if (member.index >= 0) {
                                    setReader(
                                        member.index, member
                                    );
                                }
                            }
                        }
                    }
                }

                String name = Find.name(method);
                if (name == null) {
                    continue;
                }

                if (count == 0) {
                    if (get(name) == null) {
                        setWriter(
                            grade, name, new Accessor<>(
                                expose, method, this
                            )
                        );
                    }
                } else {
                    if (set(name) == null) {
                        variable = true;
                        setReader(
                            name, member = new Accessor<>(
                                expose, method, this
                            )
                        );

                        if (nimble) {
                            if (member.index >= 0) {
                                setReader(
                                    member.index, member
                                );
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // Nothing
            }
    }

    protected void onConstructors(
        @NotNull Constructor<?>[] constructors
    ) {
        Constructor<?> $ = null,
            b = constructors[0];
        for (int i = 1; i < constructors.length; i++) {
            Constructor<?> c = constructors[i];
            if (b.getParameterCount() <=
                c.getParameterCount()) {
                $ = b;
                b = c;
            } else if ($ == null) {
                $ = c;
            } else {
                if ($.getParameterCount() <=
                    c.getParameterCount()) {
                    $ = c;
                }
            }
        }

        try {
            if (!b.isAccessible()) {
                b.setAccessible(true);
            }
            int count = b.getParameterCount();
            if (count == 0) {
                handle = LOOKUP.unreflectConstructor(b);
            } else {
                builder = (Constructor<T>) b;
                args = b.getParameterTypes();
                if ($ != null) {
                    int i = $.getParameterCount();
                    if (i + 2 == count && args[i] == int.class &&
                        "kotlin.jvm.internal.DefaultConstructorMarker".equals(args[i + 1].getName())) {
                        b = $;
                        count -= 2;
                        marker = true;
                    }
                }

                Type[] ts = b.getGenericParameterTypes();
                Annotation[][] as = b.getParameterAnnotations();

                int i = 0, j = as.length - count;
                Class<?> declaringClass = klass.getDeclaringClass();

                if (declaringClass != null &&
                    (klass.getModifiers() & Modifier.STATIC) == 0) {
                    if (declaringClass == args[0]) {
                        i++;
                        cxt = declaringClass;
                    }
                }

                for (; i < count; i++) {
                    int k = i + j;
                    Argument arg = new Argument(
                        i, ts[i], args[i], this, as[k]
                    );

                    Expose expose = arg
                        .getAnnotation(Expose.class);
                    if (expose == null) {
                        setParameter(
                            "arg" + k, arg
                        );
                    } else {
                        String[] v = expose.value();
                        for (String alias : v) {
                            setParameter(
                                alias, arg
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Nothing
        }
    }
}
