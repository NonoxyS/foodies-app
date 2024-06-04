package com.nonoxy.foodies_theme.ui.theme.entity

import android.graphics.Color
import android.os.Parcelable
import com.nonoxy.foodies_theme.ui.theme.util.toPx
import kotlinx.parcelize.Parcelize

/**
 * Композитная тень дизайн системы
 *
 * @param name - название тени
 * @param layers - список теней
 */
@Parcelize
data class CustomShadowParams(
    val name: String,
    val layers: List<Shadow>
) : Parcelable {
    companion object {

        const val SHADOW_COLOR = Color.BLACK

        fun shadow1(shadowColor: Int = SHADOW_COLOR): CustomShadowParams {
            return CustomShadowParams(
                name = "Shadow 1",
                listOf(
                    Shadow(
                        dX = 0.toPx,
                        dY = 1.toPx,
                        radius = 2.toPx,
                        color = shadowColor,
                        colorAlpha = 0.18f,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 1f),
                                GradientPointAndColorMultiplier(0.75f, 0.12f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    )
                )
            )
        }

        fun shadow2(shadowColor: Int = SHADOW_COLOR): CustomShadowParams {
            return CustomShadowParams(
                name = "Shadow For Top Bar",
                listOf(
                    Shadow(
                        dX = 8.toPx,
                        dY = 8.toPx,
                        radius = 8.toPx,
                        color = shadowColor,
                        colorAlpha = 0.12f,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 0.6f),
                                GradientPointAndColorMultiplier(0.75f, 0.10f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    )
                )
            )
        }

        fun shadow3(shadowColor: Int = SHADOW_COLOR): CustomShadowParams {
            return CustomShadowParams(
                name = "Shadow 3",
                listOf(
                    Shadow(
                        dX = 0.toPx,
                        dY = 4.toPx,
                        radius = 10.toPx,
                        color = shadowColor,
                        colorAlpha = 0.18f,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 0.65f),
                                GradientPointAndColorMultiplier(0.75f, 0.05f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    )
                )
            )
        }

        fun shadow4(shadowColor: Int = SHADOW_COLOR): CustomShadowParams {
            return CustomShadowParams(
                name = "Shadow 4",
                listOf(
                    Shadow(
                        dX = 0.toPx,
                        dY = 4.toPx,
                        radius = 8.toPx,
                        color = shadowColor,
                        colorAlpha = 0.27f,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 1f),
                                GradientPointAndColorMultiplier(0.8f, 0.15f),
                                GradientPointAndColorMultiplier(1f, 0f),
                            )
                        )
                    )
                )
            )
        }

        fun shadow5(shadowColor: Int = SHADOW_COLOR): CustomShadowParams {
            return CustomShadowParams(
                name = "Shadow 5",
                listOf(
                    Shadow(
                        dX = 0.toPx,
                        dY = 7.toPx,
                        radius = 12.toPx,
                        color = shadowColor,
                        colorAlpha = 0.22f,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 1f),
                                GradientPointAndColorMultiplier(0.84f, 0.05f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    )
                )
            )
        }

        fun shadow6(shadowColor: Int = SHADOW_COLOR): CustomShadowParams {
            return CustomShadowParams(
                name = "Shadow 6",
                listOf(
                    Shadow(
                        dX = 0.toPx,
                        dY = 3.toPx,
                        radius = 5.toPx,
                        color = shadowColor,
                        colorAlpha = 0.27f,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 1f),
                                GradientPointAndColorMultiplier(0.85f, 0.1f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    ),
                    Shadow(
                        dX = 0.toPx,
                        dY = 1.toPx,
                        radius = 14.toPx,
                        color = shadowColor,
                        colorAlpha = 0.15f,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 1f),
                                GradientPointAndColorMultiplier(0.85f, 0.1f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    )
                )
            )
        }

        fun shadow7(shadowColor: Int = SHADOW_COLOR): CustomShadowParams {
            return CustomShadowParams(
                name = "Shadow 7",
                listOf(
                    Shadow(
                        dX = 0.toPx,
                        dY = 11.toPx,
                        radius = 15.toPx,
                        colorAlpha = 0.2f,
                        color = shadowColor,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 0.1f),
                                GradientPointAndColorMultiplier(0.8f, 0.05f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    ),
                    Shadow(
                        dX = 0.toPx,
                        dY = 9.toPx,
                        radius = 35.toPx,
                        colorAlpha = 0.15f,
                        color = shadowColor,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 1f),
                                GradientPointAndColorMultiplier(0.8f, 0.05f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    ),
                    Shadow(
                        dX = 0.toPx,
                        dY = 24.toPx,
                        radius = 38.toPx,
                        colorAlpha = 0.08f,
                        color = SHADOW_COLOR,
                        linearGradientParams = GradientParams(
                            listOf(
                                GradientPointAndColorMultiplier(0f, 0.8f),
                                GradientPointAndColorMultiplier(0.46f, 0.3f),
                                GradientPointAndColorMultiplier(0.75f, 0.1f),
                                GradientPointAndColorMultiplier(1f, 0f)
                            )
                        )
                    )
                )
            )
        }

        fun noShadow(): CustomShadowParams =
            CustomShadowParams(
                name = "NoShadow",
                listOf(
                    Shadow(
                        dX = 0f,
                        dY = 0f,
                        radius = 0f,
                        color = Color.TRANSPARENT,
                        linearGradientParams = GradientParams.defaultLinearGradient(),
                        colorAlpha = 0f,
                    )
                )
            )
    }
}