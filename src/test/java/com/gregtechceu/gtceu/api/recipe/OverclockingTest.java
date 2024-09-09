package com.gregtechceu.gtceu.api.recipe;

import com.gregtechceu.gtceu.GTCEu;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.recipe.OverclockingLogic.*;

import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import org.jetbrains.annotations.NotNull;

@GameTestHolder(GTCEu.MOD_ID)
@PrefixGameTestTemplate(false)
public class OverclockingTest {

    @GameTest(template = "empty_1x1")
    public static void testULV(GameTestHelper helper) {
        final int recipeDuration = 32768;
        final int recipeTier = ULV;
        final long recipeVoltage = V[ULV];

        int machineTier = LV;

        OCResult oc = testOC(recipeDuration, recipeTier, recipeVoltage, machineTier, V[machineTier]);

        helper.assertTrue(oc.getEut() == recipeVoltage, "wrong eut");
    }

    @NotNull
    private static OCResult testOC(int recipeDuration, int recipeTier, long recipeVoltage, int machineTier, long maxVoltage) {
        int ocs = machineTier - recipeTier;
        if(recipeTier == ULV) ocs--;

        OCResult ocResult = new OCResult();

        if(ocs <= 0) {
            ocResult.init(recipeVoltage, recipeDuration, 0);
            return ocResult;
        }

        OCParams ocParams = new OCParams();
        ocParams.initialize(recipeVoltage, recipeDuration, ocs);

        OverclockingLogic.standardOverclockingLogic(ocParams, ocResult, maxVoltage, STD_DURATION_FACTOR, STD_VOLTAGE_FACTOR);
        return ocResult;
    }
}
