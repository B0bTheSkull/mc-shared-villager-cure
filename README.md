# Shared Villager Cure

A tiny **server-side** Fabric mod for Minecraft **26.2** that shares zombie-villager cure discounts across **all** players.

## What it does

In vanilla, when you cure a zombie villager you earn a large permanent trading discount — but that discount is tied to **your** player UUID alone. Other players on the server still pay full price at that villager.

This mod makes the cure discount **shared**: when anyone cures a villager, every player gets that villager's discounted prices.

## How it works

The cure "reputation" is stored as *gossip* on the villager entity itself (in the world save), keyed by the curing player's UUID. A Mixin into `Villager.getPlayerReputation(Player)` returns the **maximum** reputation any player has earned with that villager, instead of only the asking player's:

```java
@Inject(method = "getPlayerReputation", at = @At("RETURN"), cancellable = true)
```

Taking the *max* (rather than summing) means:

- One cure lifts the discount for **everyone**.
- A single player's grief penalty can never raise prices for others.
- No player ever pays **more** than vanilla would have charged them.

Because the data lives on the villager (not the player), the original curer does **not** need to be online for others to benefit. Standard vanilla gossip decay still applies, so the shared discount fades over time exactly like vanilla.

## Notes

- **Server-side only** — clients install nothing.
- Built against MC 26.2 / Fabric Loader 0.19.3 / Java 25, Mojang official mappings.
- No dependencies beyond Fabric Loader.

## Build

```sh
./gradlew build
# -> build/libs/shared-villager-cure-1.0.0.jar
```

Drop the jar in your server's `mods/` folder and restart.
