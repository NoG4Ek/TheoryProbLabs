package tasks.ballboxesarrange

import models.Info

class Box(
    val total: Int, val red: Int, val white: Int,
    val black: Int, val green: Int, val blue: Int,
    val yellow: Int
): Info {
    override fun toString(): String {
        return "Box(total=$total, red=$red, white=$white, black=$black, green=$green, blue=$blue, yellow=$yellow)\n"
    }
}