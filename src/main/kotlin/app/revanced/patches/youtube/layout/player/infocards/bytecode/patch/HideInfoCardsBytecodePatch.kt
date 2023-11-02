package app.revanced.patches.youtube.layout.player.infocards.bytecode.patch

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patches.youtube.layout.player.infocards.bytecode.fingerprints.InfocardsIncognitoFingerprint
import app.revanced.patches.youtube.layout.player.infocards.bytecode.fingerprints.InfocardsIncognitoParentFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import app.revanced.shared.extensions.exception
import app.revanced.shared.util.integrations.Constants.PLAYER_LAYOUT

@Name("hide-info-cards-bytecode-patch")
@YouTubeCompatibility
class HideInfoCardsBytecodePatch : BytecodePatch(
    listOf(InfocardsIncognitoParentFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        InfocardsIncognitoParentFingerprint.result?.classDef?.let { classDef ->
            InfocardsIncognitoFingerprint.also {
                it.resolve(context, classDef)
            }.result?.mutableMethod?.
            addInstructions(
                1, """
                    invoke-static {p4}, $PLAYER_LAYOUT->hideInfoCard(Z)Z
                    move-result p4
                    """
            ) ?: throw InfocardsIncognitoFingerprint.exception
        } ?: throw InfocardsIncognitoParentFingerprint.exception
    }
}