package uk.co.callumbirks.platform

import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import uk.co.callumbirks.CobbleEggs.cobbleEggsResource

abstract class PlatformRegistry<R: Registry<T>, K : RegistryKey<R>, T> {
    /**
     * The vanilla [Registry]
     */
    abstract val registry: R

    /**
     * The vanilla [RegistryKey]
     */
    abstract val registryKey: K

    protected val queue = hashMapOf<Identifier, T>()

    /**
     * Creates a new entry in this registry.
     * @param E The type of the entry.
     * @param name The name of the entry ([Identifier.path])
     * @param entry The entry being added.
     * @return The entry created
     */
    open fun <E : T> create(name: String, entry: E): E {
        val identifier = cobbleEggsResource(name)
        this.queue[identifier] = entry
        return entry
    }

    /**
     * Handles the registration of this registry into the platform specific one.
     * @param consumer The consumer that will handle logic for registering the entries.
     */
    open fun register(consumer: (Identifier, T) -> Unit) {
        this.queue.forEach(consumer)
    }

    /**
     * Returns a collection of every entry in this registry.
     * @return The entries of this registry.
     */
    open fun all(): Collection<T> = this.queue.values.toList()
}