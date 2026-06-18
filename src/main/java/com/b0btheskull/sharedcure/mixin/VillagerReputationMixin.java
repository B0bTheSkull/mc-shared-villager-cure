package com.b0btheskull.sharedcure.mixin;

import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

/**
 * Makes the villager trading-reputation that drives price discounts SHARED across all players.
 *
 * Vanilla {@link Villager#getPlayerReputation(Player)} returns only the asking player's own
 * reputation with the villager, so the big discount you get from curing a zombie villager is
 * tied to your UUID alone. This injection returns the BEST reputation any player has earned with
 * that villager instead, so when one person cures a villager every player gets the discount.
 *
 * Taking the max (rather than summing) means a single player's grief penalty can never drag the
 * shared price up, and a single cure lifts the discount for everyone — never makes anyone's price
 * worse than vanilla would have given them.
 */
@Mixin(Villager.class)
public abstract class VillagerReputationMixin {

    @Inject(method = "getPlayerReputation", at = @At("RETURN"), cancellable = true)
    private void sharedcure$shareReputation(Player player, CallbackInfoReturnable<Integer> cir) {
        Villager self = (Villager) (Object) this;
        GossipContainer gossips = self.getGossips();

        int best = cir.getReturnValueI();
        for (UUID uuid : gossips.getGossipEntries().keySet()) {
            // Same predicate vanilla uses for getPlayerReputation: count every gossip type.
            int rep = gossips.getReputation(uuid, type -> true);
            if (rep > best) {
                best = rep;
            }
        }
        cir.setReturnValue(best);
    }
}
