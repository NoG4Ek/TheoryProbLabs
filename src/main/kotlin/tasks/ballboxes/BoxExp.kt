package tasks.ballboxes

import models.Experiment

class BoxExp(): Experiment {
    var list = mutableListOf<Color>()
    enum class Color {
        RED, WHITE, BLACK, GREEN, BLUE;
    }

    override fun toString(): String {
        return "Experiment(list=$list)\n"
    }
}