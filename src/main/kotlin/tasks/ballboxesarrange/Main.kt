package tasks.ballboxesarrange

import models.Property

fun main() {
    val property = Property("ballout.txt", 50)
    val builder = BallBoxes()
    builder.read()
    builder.buildHypo()
    val solver = Solver(builder.hypoArrange, builder.list, property)
    solver.run()
}