package net.deddybones.deddymod.util.forge;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mojang.serialization.Lifecycle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import net.minecraft.core.*;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;

public class RegistrySetBuilder {
    private final List<RegistrySetBuilder.RegistryStub<?>> entries = new ArrayList<>();

    @SuppressWarnings("unused")
    static <T> HolderGetter<T> wrapContextLookup(final HolderLookup.RegistryLookup<T> p_255625_) {
        return new RegistrySetBuilder.EmptyTagLookup<T>(p_255625_) {
            public @NotNull Optional<Holder.Reference<T>> get(@NotNull ResourceKey<T> p_255765_) {
                return p_255625_.get(p_255765_);
            }
        };
    }

    static <T> HolderLookup.RegistryLookup<T> lookupFromMap(final ResourceKey<? extends Registry<? extends T>> p_311196_, final Lifecycle p_311352_, final Map<ResourceKey<T>, Holder.Reference<T>> p_311458_) {
        return new HolderLookup.RegistryLookup<T>() {
            public @NotNull ResourceKey<? extends Registry<? extends T>> key() {
                return p_311196_;
            }

            public @NotNull Lifecycle registryLifecycle() {
                return p_311352_;
            }

            public @NotNull Optional<Holder.Reference<T>> get(@NotNull ResourceKey<T> p_310975_) {
                return Optional.ofNullable(p_311458_.get(p_310975_));
            }

            public @NotNull Stream<Holder.Reference<T>> listElements() {
                return p_311458_.values().stream();
            }

            public @NotNull Optional<HolderSet.Named<T>> get(@NotNull TagKey<T> p_311223_) {
                return Optional.empty();
            }

            public @NotNull Stream<HolderSet.Named<T>> listTags() {
                return Stream.empty();
            }
        };
    }

    public <T> RegistrySetBuilder add(ResourceKey<? extends Registry<T>> p_256446_, Lifecycle p_256394_, RegistrySetBuilder.RegistryBootstrap<T> p_256638_) {
        this.entries.add(new RegistrySetBuilder.RegistryStub<>(p_256446_, p_256394_, p_256638_));
        return this;
    }

    public <T> RegistrySetBuilder add(ResourceKey<? extends Registry<T>> p_256261_, RegistrySetBuilder.RegistryBootstrap<T> p_256010_) {
        return this.add(p_256261_, Lifecycle.stable(), p_256010_);
    }

    @SuppressWarnings("unused")
    public List<? extends ResourceKey<? extends Registry<?>>> getEntryKeys() {
        return this.entries.stream().map(RegistrySetBuilder.RegistryStub::key).toList();
    }

    private RegistrySetBuilder.BuildState createState(RegistryAccess p_256400_) {
        RegistrySetBuilder.BuildState registrysetbuilder$buildstate = RegistrySetBuilder.BuildState.create(p_256400_, this.entries.stream().map(RegistrySetBuilder.RegistryStub::key));
        this.entries.forEach((p_255629_) -> {
            p_255629_.apply(registrysetbuilder$buildstate);
        });
        return registrysetbuilder$buildstate;
    }

    private static HolderLookup.Provider buildProviderWithContext(RegistryAccess p_311176_, Stream<HolderLookup.RegistryLookup<?>> p_311668_) {
        Stream<HolderLookup.RegistryLookup<?>> stream = p_311176_.registries().map((p_258195_) -> {
            return p_258195_.value().asLookup();
        });
        return HolderLookup.Provider.create(Stream.concat(stream, p_311668_));
    }

    @SuppressWarnings("unused")
    public HolderLookup.Provider build(RegistryAccess p_256112_) {
        RegistrySetBuilder.BuildState registrysetbuilder$buildstate = this.createState(p_256112_);
        Stream<HolderLookup.RegistryLookup<?>> stream = this.entries.stream().map((p_308461_) -> {
            return p_308461_.collectRegisteredValues(registrysetbuilder$buildstate).buildAsLookup(registrysetbuilder$buildstate.owner);
        });
        HolderLookup.Provider holderlookup$provider = buildProviderWithContext(p_256112_, stream);
        registrysetbuilder$buildstate.reportNotCollectedHolders();
        registrysetbuilder$buildstate.reportUnclaimedRegisteredValues();
        registrysetbuilder$buildstate.throwOnError();
        return holderlookup$provider;
    }

    private HolderLookup.Provider createLazyFullPatchedRegistries(RegistryAccess p_312999_, HolderLookup.Provider p_309815_, Cloner.Factory p_311992_, Map<ResourceKey<? extends Registry<?>>, RegistrySetBuilder.RegistryContents<?>> p_309672_, HolderLookup.Provider p_312434_) {
        RegistrySetBuilder.CompositeOwner registrysetbuilder$compositeowner = new RegistrySetBuilder.CompositeOwner();
        MutableObject<HolderLookup.Provider> mutableobject = new MutableObject<>();
        List<HolderLookup.RegistryLookup<?>> list = p_309672_.keySet().stream().map((p_308443_) -> {
            return this.createLazyFullPatchedRegistries(registrysetbuilder$compositeowner, p_311992_, p_308443_, p_312434_, p_309815_, mutableobject);
        }).peek(registrysetbuilder$compositeowner::add).collect(Collectors.toUnmodifiableList());
        HolderLookup.Provider holderlookup$provider = buildProviderWithContext(p_312999_, list.stream());
        mutableobject.setValue(holderlookup$provider);
        return holderlookup$provider;
    }

    private <T> HolderLookup.RegistryLookup<T> createLazyFullPatchedRegistries(HolderOwner<T> p_312548_, Cloner.Factory p_312934_, ResourceKey<? extends Registry<? extends T>> p_313093_, HolderLookup.Provider p_311682_, HolderLookup.Provider p_313198_, MutableObject<HolderLookup.Provider> p_311605_) {
        Cloner<T> cloner = p_312934_.cloner(p_313093_);
        if (cloner == null) {
            throw new NullPointerException("No cloner for " + p_313093_.location());
        } else {
            Map<ResourceKey<T>, Holder.Reference<T>> map = new HashMap<>();
            HolderLookup.RegistryLookup<T> registrylookup = p_311682_.lookupOrThrow(p_313093_);
            registrylookup.listElements().forEach((p_308453_) -> {
                ResourceKey<T> resourcekey = p_308453_.key();
                LazyHolder<T> lazyholder = new LazyHolder<>(p_312548_, resourcekey);
                lazyholder.supplier = () -> {
                    return cloner.clone((T)p_308453_.value(), p_311682_, p_311605_.getValue());
                };
                map.put(resourcekey, lazyholder);
            });
            Lifecycle lifecycle;
            Optional<HolderLookup.RegistryLookup<T>> optional = p_313198_.lookup(p_313093_);
            if (optional.isPresent()) {
                HolderLookup.RegistryLookup<T> registrylookup1 = optional.get();
                registrylookup1.listElements().forEach((p_308430_) -> {
                    ResourceKey<T> resourcekey = p_308430_.key();
                    map.computeIfAbsent(resourcekey, (p_308437_) -> {
                        LazyHolder<T> lazyholder = new LazyHolder<>(p_312548_, resourcekey);
                        lazyholder.supplier = () -> {
                            return cloner.clone((T)p_308430_.value(), p_313198_, p_311605_.getValue());
                        };
                        return lazyholder;
                    });
                });
                lifecycle = registrylookup.registryLifecycle().add(registrylookup1.registryLifecycle());
            } else {
                lifecycle = registrylookup.registryLifecycle();
            }
            return lookupFromMap(p_313093_, lifecycle, map);
        }
    }

    public RegistrySetBuilder.PatchedRegistries buildPatch(RegistryAccess p_255676_, HolderLookup.Provider p_255900_, Cloner.Factory p_310265_) {
        RegistrySetBuilder.BuildState registrysetbuilder$buildstate = this.createState(p_255676_);
        Map<ResourceKey<? extends Registry<?>>, RegistrySetBuilder.RegistryContents<?>> map = new HashMap<>();
        this.entries.stream().map((p_308447_) -> {
            return p_308447_.collectRegisteredValues(registrysetbuilder$buildstate);
        }).forEach((p_272339_) -> {
            map.put(p_272339_.key, p_272339_);
        });
        Set<ResourceKey<? extends Registry<?>>> set = p_255676_.listRegistries().collect(Collectors.toUnmodifiableSet());
        p_255900_.listRegistries().filter((p_308455_) -> {
            return !set.contains(p_308455_);
        }).forEach((p_308463_) -> {
            map.putIfAbsent(p_308463_, new RegistrySetBuilder.RegistryContents<>(p_308463_, Lifecycle.stable(), Map.of()));
        });
        Stream<HolderLookup.RegistryLookup<?>> stream = map.values().stream().map((p_308445_) -> {
            return p_308445_.buildAsLookup(registrysetbuilder$buildstate.owner);
        });
        HolderLookup.Provider holderlookup$provider = buildProviderWithContext(p_255676_, stream);
        registrysetbuilder$buildstate.reportUnclaimedRegisteredValues();
        registrysetbuilder$buildstate.throwOnError();
        HolderLookup.Provider holderlookup$provider1 = this.createLazyFullPatchedRegistries(p_255676_, p_255900_, p_310265_, map, holderlookup$provider);
        return new RegistrySetBuilder.PatchedRegistries(holderlookup$provider1, holderlookup$provider);
    }

    static record BuildState(RegistrySetBuilder.CompositeOwner owner, RegistrySetBuilder.UniversalLookup lookup,
                             Map<ResourceLocation, HolderGetter<?>> registries,
                             Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues,
                             List<RuntimeException> errors) {
        public static RegistrySetBuilder.BuildState create(RegistryAccess p_255995_, Stream<ResourceKey<? extends Registry<?>>> p_256495_) {
            RegistrySetBuilder.CompositeOwner registrysetbuilder$compositeowner = new RegistrySetBuilder.CompositeOwner();
            List<RuntimeException> list = new ArrayList<>();
            RegistrySetBuilder.UniversalLookup registrysetbuilder$universallookup = new RegistrySetBuilder.UniversalLookup(registrysetbuilder$compositeowner);
            ImmutableMap.Builder<ResourceLocation, HolderGetter<?>> builder = ImmutableMap.builder();
            p_255995_.registries().forEach((p_258197_) -> {
                builder.put(p_258197_.key().location(), net.minecraftforge.common.ForgeHooks.wrapRegistryLookup(p_258197_.value().asLookup()));
            });
            p_256495_.forEach((p_256603_) -> {
                builder.put(p_256603_.location(), registrysetbuilder$universallookup);
            });
            return new RegistrySetBuilder.BuildState(registrysetbuilder$compositeowner, registrysetbuilder$universallookup, builder.build(), new HashMap<>(), list);
        }

        public <T> BootstapContext<T> bootstapContext() {
            return new BootstapContext<T>() {
                public Holder.@NotNull Reference<T> register(@NotNull ResourceKey<T> p_256176_, @NotNull T p_256422_, @NotNull Lifecycle p_255924_) {
                    RegistrySetBuilder.RegisteredValue<?> registeredvalue = BuildState.this.registeredValues.put(p_256176_, new RegistrySetBuilder.RegisteredValue<>(p_256422_, p_255924_));
                    if (registeredvalue != null) {
                        BuildState.this.errors.add(new IllegalStateException("Duplicate registration for " + p_256176_ + ", new=" + p_256422_ + ", old=" + registeredvalue.value));
                    }

                    return BuildState.this.lookup.getOrCreate(p_256176_);
                }

                public <S> @NotNull HolderGetter<S> lookup(@NotNull ResourceKey<? extends Registry<? extends S>> p_255961_) {
                    return (HolderGetter<S>) BuildState.this.registries.getOrDefault(p_255961_.location(), BuildState.this.lookup);
                }

                @Override
                public <S> @NotNull Optional<HolderLookup.RegistryLookup<S>> registryLookup(@NotNull ResourceKey<? extends Registry<? extends S>> registry) {
                    return Optional.ofNullable((HolderLookup.RegistryLookup<S>) BuildState.this.registries.get(registry.location()));
                }
            };
        }

        public void reportUnclaimedRegisteredValues() {
            this.registeredValues.forEach((p_256143_, p_256662_) -> {
                this.errors.add(new IllegalStateException("Orpaned value " + p_256662_.value + " for key " + p_256143_));
            });
        }

        public void reportNotCollectedHolders() {
            for (ResourceKey<Object> resourcekey : this.lookup.holders.keySet()) {
                this.errors.add(new IllegalStateException("Unreferenced key: " + resourcekey));
            }

        }

        public void throwOnError() {
            if (!this.errors.isEmpty()) {
                IllegalStateException illegalstateexception = new IllegalStateException("Errors during registry creation");

                for (RuntimeException runtimeexception : this.errors) {
                    illegalstateexception.addSuppressed(runtimeexception);
                }

                throw illegalstateexception;
            }
        }
    }

    static class CompositeOwner implements HolderOwner<Object> {
        private final Set<HolderOwner<?>> owners = Sets.newIdentityHashSet();

        public boolean canSerializeIn(@NotNull HolderOwner<Object> p_256333_) {
            return this.owners.contains(p_256333_);
        }

        public void add(HolderOwner<?> p_256361_) {
            this.owners.add(p_256361_);
        }

        public <T> HolderOwner<T> cast() {
            return (HolderOwner<T>) this;
        }
    }

    abstract static class EmptyTagLookup<T> implements HolderGetter<T> {
        protected final HolderOwner<T> owner;

        protected EmptyTagLookup(HolderOwner<T> p_256166_) {
            this.owner = p_256166_;
        }

        public @NotNull Optional<HolderSet.Named<T>> get(@NotNull TagKey<T> p_256664_) {
            return Optional.of(HolderSet.emptyNamed(this.owner, p_256664_));
        }
    }

    static class LazyHolder<T> extends Holder.Reference<T> {
        @Nullable
        Supplier<T> supplier;

        protected LazyHolder(HolderOwner<T> p_311720_, @Nullable ResourceKey<T> p_312254_) {
            super(Holder.Reference.Type.STAND_ALONE, p_311720_, p_312254_, (T) null);
        }

        public void bindValue(T p_309503_) {
            super.bindValue(p_309503_);
            this.supplier = null;
        }

        public T value() {
            if (this.supplier != null) {
                this.bindValue(this.supplier.get());
            }

            return super.value();
        }
    }

    public static record PatchedRegistries(HolderLookup.Provider full, HolderLookup.Provider patches) {
    }

    static record RegisteredValue<T>(T value, Lifecycle lifecycle) {
    }

    @FunctionalInterface
    public interface RegistryBootstrap<T> {
        void run(BootstapContext<T> p_255783_);
    }

    static record RegistryContents<T>(ResourceKey<? extends Registry<? extends T>> key, Lifecycle lifecycle,
                                      Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> values) {
        public HolderLookup.RegistryLookup<T> buildAsLookup(RegistrySetBuilder.CompositeOwner p_311874_) {
            Map<ResourceKey<T>, Holder.Reference<T>> map = this.values.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, (p_311794_) -> {
                RegistrySetBuilder.ValueAndHolder<T> valueandholder = p_311794_.getValue();
                Holder.Reference<T> reference = valueandholder.holder().orElseGet(() -> {
                    return Holder.Reference.createStandAlone(p_311874_.cast(), p_311794_.getKey());
                });
                reference.bindValue(valueandholder.value().value());
                return reference;
            }));
            HolderLookup.RegistryLookup<T> registrylookup = RegistrySetBuilder.lookupFromMap(this.key, this.lifecycle, map);
            p_311874_.add(registrylookup);
            return registrylookup;
        }
    }

    static record RegistryStub<T>(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle,
                                  RegistrySetBuilder.RegistryBootstrap<T> bootstrap) {
        void apply(RegistrySetBuilder.BuildState p_256272_) {
            this.bootstrap.run(p_256272_.bootstapContext());
        }

        public RegistrySetBuilder.RegistryContents<T> collectRegisteredValues(RegistrySetBuilder.BuildState p_256416_) {
            Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> map = new HashMap<>();
            Iterator<Map.Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>>> iterator = p_256416_.registeredValues.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> entry = iterator.next();
                ResourceKey<?> resourcekey = entry.getKey();
                if (resourcekey.isFor(this.key)) {
                    RegistrySetBuilder.RegisteredValue<T> registeredvalue = (RegistrySetBuilder.RegisteredValue<T>) entry.getValue();
                    Holder.Reference<T> reference = (Holder.Reference<T>) p_256416_.lookup.holders.remove(resourcekey);
                    map.put((ResourceKey<T>) resourcekey, new RegistrySetBuilder.ValueAndHolder<>(registeredvalue, Optional.ofNullable(reference)));
                    iterator.remove();
                }
            }

            return new RegistrySetBuilder.RegistryContents<>(this.key, this.lifecycle, map);
        }
    }

    static class UniversalLookup extends RegistrySetBuilder.EmptyTagLookup<Object> {
        final Map<ResourceKey<Object>, Holder.Reference<Object>> holders = new HashMap<>();

        public UniversalLookup(HolderOwner<Object> p_256629_) {
            super(p_256629_);
        }

        public Optional<Holder.Reference<Object>> get(ResourceKey<Object> p_256303_) {
            return Optional.of(this.getOrCreate(p_256303_));
        }

        <T> Holder.Reference<T> getOrCreate(ResourceKey<T> p_256298_) {
            return (Holder.Reference<T>) this.holders.computeIfAbsent((ResourceKey<Object>) p_256298_, (p_256154_) -> {
                return Holder.Reference.createStandAlone(this.owner, p_256154_);
            });
        }
    }

    static record ValueAndHolder<T>(RegistrySetBuilder.RegisteredValue<T> value, Optional<Holder.Reference<T>> holder) {
    }
}
