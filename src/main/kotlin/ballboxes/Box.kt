package ballboxes

import model.Info

class Box(
    val total: Int, val red: Int, val white: Int,
    val black: Int, val green: Int, val blue: Int
): Info {
    override fun toString(): String {
        return "Box(total=$total, red=$red, white=$white, black=$black, green=$green, blue=$blue)\n"
    }
}